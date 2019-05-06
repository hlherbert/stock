package com.hl.stock.core.server.task.download;

import com.hl.stock.core.base.download.StockDownloadSaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 下载股票数据的定时任务
 */
@Component
public class StockDownloadTask {

    @Autowired
    private StockDownloadSaver stockDownloadSaver;

    private boolean metaTaskStart = false;


    /**
     * 启动3秒后执行任务
     */
    @Scheduled(fixedRateString = "${stock.task.StockDownloadTask.downloadStockMetas.fixedRate}",
            initialDelayString = "${stock.task.StockDownloadTask.downloadStockMetas.initialDelay}")
    public void downloadStockMetas() {
        if (metaTaskStart) {
            return;
        }
        metaTaskStart = true;
        System.out.println("start downloadStockMetas: " + new Date());
        stockDownloadSaver.downloadSaveMeta();
        System.out.println("finish downloadStockMetas: " + new Date());

    }
}
