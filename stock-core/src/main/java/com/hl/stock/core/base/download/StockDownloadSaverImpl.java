package com.hl.stock.core.base.download;

import com.hl.stock.core.base.data.StockDao;
import com.hl.stock.core.base.exception.StockErrorCode;
import com.hl.stock.core.base.i18n.StockMessage;
import com.hl.stock.core.base.model.StockData;
import com.hl.stock.core.base.model.StockMeta;
import com.hl.stock.core.base.model.StockZone;
import com.hl.stock.core.common.util.DateTimeUtils;
import com.hl.stock.core.common.util.PerformanceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
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

    /**
     * 下载数据的起始时间
     */
    @Value("${stock.task.StockDownloadTask.downloadAllStockHistoryData.startDate}")
    private String historyStartDateStr;

    /**
     * 日期属性格式
     */
    private DateFormat dateFormat = new SimpleDateFormat(DateTimeUtils.yyyy_MM_dd);

    /**
     * 重试间隔时间
     */
    @Value("${stock.task.StockDownloadTask.downloadAllStockHistoryData.delayPerStock}")
    private int delayPerStock;

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
        PerformanceUtils.logTimeStart(StockMessage.DownloadAllStockMeta.toString());
        downloadSaveMeta();
        PerformanceUtils.logTimeEnd(StockMessage.DownloadAllStockMeta.toString());

        // 下载股票数据
        PerformanceUtils.logTimeStart(StockMessage.DownloadAllStockHistoryData.toString());
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
                            PerformanceUtils.logTimeEnd("down stock data code=" + meta.getCode());
                        } catch (InterruptedException e) {
                            logger.error("thread interrupted.", e);
                        }
                    }
                }
            });

            futures.add(future);
        }

        PerformanceUtils.logTimeEnd(StockMessage.DownloadAllStockHistoryData.toString());
    }

    /**
     * 从配置中加载默认的下载开始时间
     *
     * @return 默认的下载开始时间
     */
    private Date getDefaultHistoryStartDate() {
        Date historyStartDate = null;
        try {
            historyStartDate = dateFormat.parse(historyStartDateStr);
        } catch (ParseException e) {
            StockErrorCode.ParseStockStartDateFail.error(e);
        }
        return historyStartDate;
    }

    @Override
    public void complementAllStockHistoryData() {
        // 下载元数据
        PerformanceUtils.logTimeStart(StockMessage.DownloadAllStockMeta.toString());
        downloadSaveMeta();
        PerformanceUtils.logTimeEnd(StockMessage.DownloadAllStockMeta.toString());

        // 下载股票数据
        PerformanceUtils.logTimeStart(StockMessage.ComplementAllStockHistoryData.toString());
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
                    // 获取改股票最后一天数据时间
                    String code = meta.getCode();
                    Date lastDate = stockDao.lastDateOfData(code);

                    // 如果从数据库里没有查到该股票的最后一天数据，则起始时间设置为配置中的数据开始时间
                    if (lastDate == null) {
                        lastDate = historyStartDateFinal;
                    }

                    // 下载从lastDate到histroyEndDate的日期的数据
                    try {
                        Thread.sleep(delayPerStock);
                        downloadSaveHistory(meta.getZone(), meta.getCode(), lastDate, historyEndDate);
                        PerformanceUtils.logTimeEnd(MessageFormat.format("{0}. code={1}， start={2}, end={3}",
                                StockMessage.ComplementStockHistoryData, code, lastDate, historyEndDate));
                    } catch (InterruptedException e) {
                        logger.error("thread interrupted.", e);
                    }
                }
            });

            futures.add(future);
        }

        PerformanceUtils.logTimeEnd(StockMessage.ComplementAllStockHistoryData.toString());
    }
}
