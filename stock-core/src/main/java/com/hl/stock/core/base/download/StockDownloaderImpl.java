package com.hl.stock.core.base.download;

import com.hl.stock.core.base.model.StockData;
import com.hl.stock.core.base.model.StockMeta;
import com.hl.stock.core.base.model.StockZone;
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
     * @param zone      交易所
     * @param code      编码
     * @param startDate 起始日期
     * @param endDate   结束日期
     * @return 股票数据
     */
    @Override
    public List<StockData> downloadHistory(StockZone zone, String code, Date startDate, Date endDate) {
        List<StockData> data = stockDownloaderDataHelper.downSouhuHistoryStockData(zone, code, startDate, endDate);
        return data;
    }

    /**
     * 返回所有股票元数据
     *
     * @return 股票元数据
     */
    @Override
    public List<StockMeta> downloadMeta() {
        return stockDownloaderCodeHelper.loadAllMeta();
    }
}
