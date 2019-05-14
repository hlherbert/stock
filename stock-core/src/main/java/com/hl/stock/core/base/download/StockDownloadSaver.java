package com.hl.stock.core.base.download;

import com.hl.stock.core.base.model.StockZone;

import java.util.Date;

/**
 * 股票数据下载保存器
 * 一边下载数据，一边保存到数据库
 */
public interface StockDownloadSaver {

    /**
     * 下载并保存所有股票编码
     */
    void downloadSaveMeta();

    /**
     * 下载并保存股票历史数据
     *
     * @param zone      交易所
     * @param code      编码
     * @param startDate 起始日期
     * @param endDate   结束日期
     */
    void downloadSaveHistory(StockZone zone, String code, Date startDate, Date endDate);

    /**
     * 下载所有股票历史数据
     */
    void downloadAllStockHistoryData();

    /**
     * 补录所有股票历史数据（从该股票已有的最后一天数据时间到今天）
     * @return 任务id
     */
    String complementAllStockHistoryData();
}
