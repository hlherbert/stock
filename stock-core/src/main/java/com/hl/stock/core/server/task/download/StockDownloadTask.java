package com.hl.stock.core.server.task.download;

import com.hl.stock.core.base.download.StockDownloadSaver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

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

    private boolean historyTaskStart = true;

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
        stockDownloadSaver.downloadAllStockHistoryData();
    }
}
