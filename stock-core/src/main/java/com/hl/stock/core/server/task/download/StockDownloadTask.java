package com.hl.stock.core.server.task.download;

import com.hl.stock.core.base.config.StockConfig;
import com.hl.stock.core.base.download.StockDownloadSaver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

/**
 * 下载股票数据的定时任务
 * 通过配置application.properties来配置任务是否启动时自动运行
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
    private StockConfig stockConfig;

    private boolean historyTaskStart = false;

    private boolean historyComplementTaskStart = false;

    /**
     * 下载任务
     */
    @Scheduled(fixedRateString = "${stock.task.default.fixedRate}",
            initialDelayString = "${stock.task.default.initialDelay}")
    public void downloadAllStockHistoryData() throws ExecutionException, InterruptedException {
        if (!stockConfig.isEnableDownHistory()) {
            return;
        }

        // 只运行一次，如果任务运行过了，就不再运行了。
        if (historyTaskStart) {
            return;
        }
        historyTaskStart = true;
        stockDownloadSaver.downloadAllStockHistoryData();
    }

    /**
     * 补录任务
     */
    @Scheduled(fixedRateString = "${stock.task.default.fixedRate}",
            initialDelayString = "${stock.task.default.initialDelay}")
    public void complementAllStockHistoryData() {
        if (!stockConfig.isEnableComplementHistory()) {
            return;
        }

        // 只运行一次，如果任务运行过了，就不再运行了。
        if (historyComplementTaskStart) {
            return;
        }
        historyComplementTaskStart = true;
        stockDownloadSaver.startComplementTask();
    }
}
