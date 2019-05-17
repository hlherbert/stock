package com.hl.stock.core.base.config;

import com.hl.stock.core.base.exception.StockErrorCode;
import com.hl.stock.core.common.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;
import java.util.Date;

@Configuration
public class StockConfig {


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
}
