package com.hl.stock.core.base.download;

import com.hl.stock.core.base.model.StockZone;

import java.util.Date;

/**
 * 股票数据下载保存器
 * 一边下载数据，一边保存到数据库
 */
public interface StockDownloadSaver {

    /**
     * 下载并保存股票历史数据
     *
     * @param zone      交易所
     * @param code      编码
     * @param startDate 起始日期
     * @param endDate   结束日期
     * @return 股票数据
     */
    void downloadSaveHistory(StockZone zone, String code, Date startDate, Date endDate);

    /**
     * 下载并保存所有股票编码
     *
     * @return 股票编码
     */
    void downloadSaveMeta();
}
