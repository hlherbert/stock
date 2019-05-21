package com.hl.stock.core.base.config;

import com.hl.stock.core.base.exception.StockErrorCode;
import com.hl.stock.core.base.i18n.StockMessage;
import com.hl.stock.core.common.util.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;
import java.util.Date;

@Configuration
public class StockConfig {

    private static final Logger logger = LoggerFactory.getLogger(StockConfig.class);

    /**
     * 股票数据的起始时间
     */
    @Value("${stock.task.StockDownloadTask.downloadAllStockHistoryData.startDate}")
    private String historyStartDateStr;

    /**
     * 从配置中加载默认的股票开始时间
     *
     * @return 默认的下载开始时间
     */
    public Date getDefaultHistoryStartDate() {
        Date historyStartDate = null;
        try {
            historyStartDate = DateTimeUtils.fromString(DateTimeUtils.yyyy_MM_dd, historyStartDateStr);
        } catch (ParseException e) {
            StockErrorCode.ParseStockStartDateFail.error(e);
        }
        return historyStartDate;
    }

    private boolean enableDownHistory;

    private boolean enableComplementHistory;

    private boolean enableFindBestStrategy;

    public boolean isEnableDownHistory() {
        return enableDownHistory;
    }

    @Value("${stock.task.downloadAllStockHistoryData.autostart}")
    public void setEnableDownHistory(boolean enableDownHistory) {
        this.enableDownHistory = enableDownHistory;
        if (enableDownHistory) {
            logger.info(StockMessage.DownloadAllStockHistoryDataTaskEnabled.toString());
        } else {
            logger.info(StockMessage.DownloadAllStockHistoryDataTaskDisabled.toString());
        }
    }

    public boolean isEnableComplementHistory() {
        return enableComplementHistory;
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

    public boolean isEnableFindBestStrategy() {
        return enableFindBestStrategy;
    }

    @Value("${stock.task.findBestStrategy.autostart}")
    public void setEnableFindBestStrategy(boolean enableFindBestStrategy) {
        this.enableFindBestStrategy = enableFindBestStrategy;
        if (enableFindBestStrategy) {
            logger.info(StockMessage.FindBestStrategyTaskEnabled.toString());
        } else {
            logger.info(StockMessage.FindBestStrategyTaskDisabled.toString());
        }
    }
}
