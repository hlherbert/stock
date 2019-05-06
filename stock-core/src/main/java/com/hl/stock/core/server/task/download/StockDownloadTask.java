package com.hl.stock.core.server.task.download;

import com.hl.stock.core.base.download.StockDownloadSaver;
import com.hl.stock.core.common.util.PerformanceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

    private boolean metaTaskStart = false;


    /**
     * 启动3秒后执行任务
     */
    @Scheduled(fixedRateString = "${stock.task.StockDownloadTask.downloadStockMetas.fixedRate}",
            initialDelayString = "${stock.task.StockDownloadTask.downloadStockMetas.initialDelay}")
    public void downloadStockMetas() {
        // 只运行一次，如果任务运行过了，就不再运行了。
        if (metaTaskStart) {
            return;
        }
        metaTaskStart = true;
        PerformanceUtils.logTimeStart("downloadStockMetas");
        stockDownloadSaver.downloadSaveMeta();
        PerformanceUtils.logTimeEnd("downloadStockMetas");
    }
}
