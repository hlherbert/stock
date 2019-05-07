package com.hl.stock.core.server.task.download;

import com.hl.stock.core.base.data.StockDao;
import com.hl.stock.core.base.download.StockDownloadSaver;
import com.hl.stock.core.base.exception.StockErrorCode;
import com.hl.stock.core.base.model.StockMeta;
import com.hl.stock.core.base.model.StockZone;
import com.hl.stock.core.common.util.DateTimeUtils;
import com.hl.stock.core.common.util.PerformanceUtils;
import com.hl.stock.core.common.util.SoundUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 下载股票数据的定时任务
 */
@Component
public class StockDownloadTask {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(StockDownloadTask.class);

    @Autowired
    private StockDownloadSaver stockDownloadSaver;

    @Autowired
    private StockDao stockDao;

    /**
     * 下载数据的起始时间
     */
    @Value("${stock.task.StockDownloadTask.downloadAllStockHistoryData.startDate}")
    private String historyStartDateStr;

    /**
     * 日期属性格式
     */
    private DateFormat dateFormat = new SimpleDateFormat(DateTimeUtils.yyyy_MM_dd);

    private boolean historyTaskStart = false;

    /**
     * 下载股票全部元数据
     */
    private void downloadAllStockMetas() {
        PerformanceUtils.logTimeStart("downloadAllStockMetas");
        stockDownloadSaver.downloadSaveMeta();
        PerformanceUtils.logTimeEnd("downloadAllStockMetas");
    }

    /**
     * 下载某只股票历史数据
     */
    private void downloadStockHistoryData(StockZone zone, String code, Date startDate, Date endDate) {
        stockDownloadSaver.downloadSaveHistory(zone, code, startDate, endDate);
    }

    /**
     * 启动3秒后执行任务
     */
    @Scheduled(fixedRateString = "${stock.task.StockDownloadTask.downloadAllStockHistoryData.fixedRate}",
            initialDelayString = "${stock.task.StockDownloadTask.downloadAllStockHistoryData.initialDelay}")
    public void downloadAllStockHistoryData() throws ExecutionException, InterruptedException {
        // 只运行一次，如果任务运行过了，就不再运行了。
        if (historyTaskStart) {
            return;
        }
        historyTaskStart = true;

        // 发声通知
        SoundUtils.beep();

        // 下载元数据
        PerformanceUtils.logTimeStart("downloadStockMetas");
        downloadAllStockMetas();
        PerformanceUtils.logTimeEnd("downloadStockMetas");

        // 下载股票数据
        PerformanceUtils.logTimeStart("downloadStockHistoryData");
        List<StockMeta> stockMetas = stockDao.loadMeta();
        Date historyStartDate = null;
        try {
            historyStartDate = dateFormat.parse(historyStartDateStr);
        } catch (ParseException e) {
            StockErrorCode.ParseStockStartDateFail.error(e);
        }
        final Date historyStartDateFinal = historyStartDate;
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
                        downloadStockHistoryData(meta.getZone(), meta.getCode(), historyStartDateFinal, historyEndDate);
                        PerformanceUtils.logTimeEnd("down stock data code=" + meta.getCode());
                    }
                }
            });
            futures.add(future);
        }

        // 阻塞到所有任务完成
        for (Future<?> future : futures) {
            future.get();
        }

        PerformanceUtils.logTimeEnd("downloadStockHistoryData");
    }
}
