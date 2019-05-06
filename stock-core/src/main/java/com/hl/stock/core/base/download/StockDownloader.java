package com.hl.stock.core.base.download;

import com.hl.stock.core.base.model.StockData;
import com.hl.stock.core.base.model.StockMeta;

import java.util.Date;
import java.util.List;

public interface StockDownloader {


    /**
     * 下载股票历史数据
     *
     * @param code      编码
     * @param startDate 起始日期
     * @param endDate   结束日期
     * @return 股票数据
     */
    List<StockData> downloadHistory(String code, Date startDate, Date endDate);

    /**
     * 返回所有股票编码
     *
     * @return 股票编码
     */
    List<StockMeta> downloadMeta();
}
