package com.hl.stock.core.base.download;

import com.hl.stock.core.base.config.StockConfig;
import com.hl.stock.core.base.data.StockDao;
import com.hl.stock.core.base.data.StockUpdateDao;
import com.hl.stock.core.base.model.StockMeta;
import com.hl.stock.core.base.task.StockTask;
import com.hl.stock.core.common.util.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 数据补录任务
 */
@Component
public class StockComplementTask extends StockTask<Void> {
    private static final Logger logger = LoggerFactory.getLogger(StockComplementTask.class);

    @Autowired
    private StockDao stockDao;

    @Autowired
    private StockUpdateDao stockUpdateDao;

    @Autowired
    private StockDownloadSaver stockDownloadSaver;

    @Autowired
    private StockConfig stockConfig;

    /**
     * 重试间隔时间
     */
    @Value("${stock.task.StockDownloadTask.downloadAllStockHistoryData.delayPerStock}")
    private int delayPerStock;

    @Override
    public Void getData() {
        return null;
    }

    @Override
    public void doTask() {
        // 数据清洗
        stockDao.washData();

        // 下载元数据
        stockDownloadSaver.downloadSaveMeta();

        // 下载股票数据
        List<StockMeta> stockMetas = stockDao.loadMeta();

        // 数据更新日期
        Map<String, Date> stockUpdateDates = stockUpdateDao.loadDataUpdate();
        logger.error("[d] stockUpdateDates={}", stockUpdateDates);

        final Date historyStartDateFinal = stockConfig.getDefaultHistoryStartDate();
        final Date historyEndDate = DateTimeUtils.todayDate();

        // 多线程干活，提高性能。 同时并发下载多只股票的数据
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        int nStockMetas = stockMetas.size();
        final double progressIncrement = 100.0 / (double) nStockMetas;
        CountDownLatch countDownLatch = new CountDownLatch(nStockMetas);

        for (StockMeta meta : stockMetas) {
            Future<?> future = fixedThreadPool.submit(() -> {
                        // 获取改股票最后一天数据时间
                        String code = meta.getCode();
                        Date updateDate = stockUpdateDates.get(code);
                        if (updateDate != null && !updateDate.before(historyEndDate)) {
                            logger.info("code={}, stock data is updated, need not download.");
                            incProgress(countDownLatch, progressIncrement);
                        } else {
                            Date lastDate = stockDao.lastDateOfData(code);
                            // 如果从数据库里没有查到该股票的最后一天数据，则起始时间设置为配置中的数据开始时间
                            if (lastDate == null) {
                                lastDate = historyStartDateFinal;
                            } else {
                                lastDate = DateTimeUtils.dateAfterDays(lastDate, 1);
                            }

                            // 下载从lastDate到histroyEndDate的日期的数据
                            try {
                                if (lastDate.after(historyEndDate)) {
                                    // do nothing
                                } else {
                                    Thread.sleep(delayPerStock);
                                    stockDownloadSaver.downloadSaveHistory(meta.getZone(), meta.getCode(), lastDate, historyEndDate);

                                    // 保存数据更新日期为今天
                                    stockUpdateDao.updateStockData(code, DateTimeUtils.todayDate());
                                }
                            } catch (InterruptedException e) {
                                logger.error("thread interrupted.", e);
                            } finally {
                                // finally 进度计数
                                incProgress(countDownLatch, progressIncrement);
                            }
                        }
                    }
            );
        }
    }

    /**
     * 更新进度
     *
     * @param remainStocks      进度计数器
     * @param progressIncrement 进度增量
     */
    private void incProgress(CountDownLatch remainStocks, double progressIncrement) {
        remainStocks.countDown();
        logger.info("remain {} stocks to download.", remainStocks.getCount());
        StockComplementTask.this.incProgress(progressIncrement);
        if (remainStocks.getCount() <= 0) {
            StockComplementTask.this.finish();
        }
    }
}
