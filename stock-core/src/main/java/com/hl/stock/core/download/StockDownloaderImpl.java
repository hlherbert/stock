package com.hl.stock.core.download;

import com.hl.stock.core.model.StockData;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class StockDownloaderImpl implements StockDownloader {

    private StockDownloaderCodeHelper stockDownloaderCodeHelper = StockDownloaderCodeHelper.getInstance();

    private StockDownloaderDataHelper stockDownloaderDataHelper = StockDownloaderDataHelper.getInstance();

    /**
     * 下载股票数据
     *
     * @param code      编码
     * @param startDate 起始日期
     * @param endDate   结束日期
     * @return 股票数据
     */
    @Override
    public List<StockData> downloadHistory(String code, Date startDate, Date endDate) {
        List<StockData> data = stockDownloaderDataHelper.downSouhuHistoryStockData(code, startDate, endDate);
        return data;
    }

    /**
     * 返回所有股票编码
     *
     * @return 股票编码
     */
    @Override
    public List<String> downCodeList() {
        return stockDownloaderCodeHelper.loadAllCodes();
    }
}
