package com.hl.stock.core.server.task.download;

import com.hl.stock.core.base.download.StockDownloadSaver;
import com.hl.stock.core.base.i18n.StockMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private boolean historyTaskStart = false;

    private boolean historyComplementTaskStart = false;

    private boolean enableDownHistory;

    private boolean enableComplementHistory;

    @Value("${stock.task.downloadAllStockHistoryData.autostart}")
    public void setEnableDownHistory(boolean enableDownHistory) {
        this.enableDownHistory = enableDownHistory;
        if (enableDownHistory) {
            logger.info(StockMessage.DownloadAllStockHistoryDataTaskEnabled.toString());
        } else {
            logger.info(StockMessage.DownloadAllStockHistoryDataTaskDisabled.toString());
        }
    }

    @Value("${stock.task.complementAllStockHistoryData.autostart}")
    public void setEnableComplementHistory(boolean enableComplementHistory) {
        this.enableComplementHistory = enableComplementHistory;
        if (enableComplementHistory) {
            logger.info(StockMessage.ComplementAllStockHistoryDataTaskEnabled.toString());
        } else {
            logger.info(StockMessage.ComplementAllStockHistoryDataTaskDisabled.toString());
        }
    }

    /**
     * 下载任务
     */
    @Scheduled(fixedRateString = "${stock.task.StockDownloadTask.downloadAllStockHistoryData.fixedRate}",
            initialDelayString = "${stock.task.StockDownloadTask.downloadAllStockHistoryData.initialDelay}")
    public void downloadAllStockHistoryData() throws ExecutionException, InterruptedException {
        if (!enableDownHistory) {
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
    @Scheduled(fixedRateString = "${stock.task.StockDownloadTask.downloadAllStockHistoryData.fixedRate}",
            initialDelayString = "${stock.task.StockDownloadTask.downloadAllStockHistoryData.initialDelay}")
    public void complementAllStockHistoryData() {
        if (!enableComplementHistory) {
            return;
        }

        // 只运行一次，如果任务运行过了，就不再运行了。
        if (historyComplementTaskStart) {
            return;
        }
        historyComplementTaskStart = true;
        stockDownloadSaver.complementAllStockHistoryData();
    }
}
