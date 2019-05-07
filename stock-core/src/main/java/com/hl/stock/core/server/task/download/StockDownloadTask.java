package com.hl.stock.core.server.task.download;

import com.hl.stock.core.base.data.StockDao;
import com.hl.stock.core.base.download.StockDownloadSaver;
import com.hl.stock.core.base.exception.StockErrorCode;
import com.hl.stock.core.base.model.StockMeta;
import com.hl.stock.core.base.model.StockZone;
import com.hl.stock.core.common.util.DateTimeUtils;
import com.hl.stock.core.common.util.PerformanceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    public void downloadAllStockHistoryData() {
        // 只运行一次，如果任务运行过了，就不再运行了。
        if (historyTaskStart) {
            return;
        }
        historyTaskStart = true;

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
        Date historyEndDate = new Date();
        for (StockMeta meta : stockMetas) {
            downloadStockHistoryData(meta.getZone(), meta.getCode(), historyStartDate, historyEndDate);
            PerformanceUtils.logTimeEnd("down stock data code=" + meta.getCode());
        }
        PerformanceUtils.logTimeEnd("downloadStockHistoryData");
    }
}
