package com.hl.stock.core.base.download;

import com.hl.stock.core.base.config.StockConfig;
import com.hl.stock.core.base.data.StockDao;
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

        final Date historyStartDateFinal = stockConfig.getDefaultHistoryStartDate();
        final Date historyEndDate = new Date();

        // 多线程干活，提高性能。 同时并发下载多只股票的数据
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        int nStockMetas = stockMetas.size();
        CountDownLatch countDownLatch = new CountDownLatch(nStockMetas);

        for (StockMeta meta : stockMetas) {
            Future<?> future = fixedThreadPool.submit(() -> {
                        // 获取改股票最后一天数据时间
                        String code = meta.getCode();

                        Date lastDate = stockDao.lastDateOfData(code);
                        // 如果从数据库里没有查到该股票的最后一天数据，则起始时间设置为配置中的数据开始时间
                        if (lastDate == null) {
                            lastDate = historyStartDateFinal;
                        } else {
                            lastDate = DateTimeUtils.dateAfterDays(lastDate, 1);
                        }
                        // 下载从lastDate到histroyEndDate的日期的数据
                        try {
                            Thread.sleep(delayPerStock);
                            stockDownloadSaver.downloadSaveHistory(meta.getZone(), meta.getCode(), lastDate, historyEndDate);
                            StockComplementTask.this.incProgress(100.0 / (double) nStockMetas);
                        } catch (InterruptedException e) {
                            logger.error("thread interrupted.", e);
                        } finally {
                            countDownLatch.countDown();
                            logger.info("remain {} stocks to download.", countDownLatch.getCount());
                            if (countDownLatch.getCount() <= 0) {
                                StockComplementTask.this.finish();
                            }
                        }
                    }
            );
        }
    }
}
