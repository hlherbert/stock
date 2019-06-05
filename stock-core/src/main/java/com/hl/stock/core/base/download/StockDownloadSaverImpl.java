package com.hl.stock.core.base.download;

import com.hl.stock.core.base.config.StockConfig;
import com.hl.stock.core.base.data.StockDao;
import com.hl.stock.core.base.model.StockData;
import com.hl.stock.core.base.model.StockMeta;
import com.hl.stock.core.base.model.StockZone;
import com.hl.stock.core.common.perf.PerformanceMeasure;
import com.hl.stock.core.common.util.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class StockDownloadSaverImpl implements StockDownloadSaver {

    private static final Logger logger = LoggerFactory.getLogger(StockDownloadSaverImpl.class);

    @Autowired
    private StockDao stockDao;

    @Autowired
    private StockDownloader stockDownloader;

    @Autowired
    private StockConfig stockConfig;

    @Autowired
    private StockComplementTask stockComplementTask;

    /**
     * 日期属性格式
     */
    private DateFormat dateFormat = new SimpleDateFormat(DateTimeUtils.yyyy_MM_dd);

    /**
     * 重试间隔时间
     */
    @Value("${stock.task.StockDownloadTask.downloadAllStockHistoryData.delayPerStock}")
    private int delayPerStock;

    @PerformanceMeasure
    @Override
    public void downloadSaveMeta() {
        List<StockMeta> metas = stockDownloader.downloadMeta();
        if (metas == null || metas.isEmpty()) {
            // do nothing
            logger.warn("download all stock meta empty.");
            return;
        }
        stockDao.saveMetaBatch(metas);
    }


    @PerformanceMeasure
    @Override
    public void downloadSaveHistory(StockZone zone, String code, Date startDate, Date endDate) {
        List<StockData> data = stockDownloader.downloadHistory(zone, code, startDate, endDate);
        if (data == null || data.isEmpty()) {
            // do nothing
            logger.warn("download stock data empty. code:{}", code);
            return;
        }
        stockDao.saveDataBatch(data);
    }

    @Override
    public void downloadAllStockHistoryData() {
        // 下载元数据
        downloadSaveMeta();

        // 下载股票数据
        List<StockMeta> stockMetas = stockDao.loadMeta();
        final Date historyStartDateFinal = getDefaultHistoryStartDate();
        final Date historyEndDate = new Date();

        // 多线程干活，提高性能。 同时并发下载多只股票的数据
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<?>> futures = new ArrayList<Future<?>>();
        for (StockMeta meta : stockMetas) {
            Future<?> future = fixedThreadPool.submit(new Runnable() {
                @Override
                public void run() {
                    if (stockDao.hasData(meta.getCode())) {
                        // 如果已经下载过某只股票的数据了，就不要再下载了
                        logger.info("stock data already downloaded. code=" + meta.getCode());
                    } else {
                        try {
                            Thread.sleep(delayPerStock);
                            downloadSaveHistory(meta.getZone(), meta.getCode(), historyStartDateFinal, historyEndDate);
                        } catch (InterruptedException e) {
                            logger.error("thread interrupted.", e);
                        }
                    }
                }
            });

            futures.add(future);
        }
    }

    /**
     * 从配置中加载默认的下载开始时间
     *
     * @return 默认的下载开始时间
     */
    private Date getDefaultHistoryStartDate() {
        return stockConfig.getDefaultHistoryStartDate();
    }

    @Override
    public StockComplementTask startComplementTask() {
        stockComplementTask.start();
        return stockComplementTask;
    }
}
