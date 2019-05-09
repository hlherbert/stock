package com.hl.stock.core.base.data;

import com.hl.stock.core.base.data.mapper.StockDataMapper;
import com.hl.stock.core.base.data.mapper.StockMetaMapper;
import com.hl.stock.core.base.model.StockData;
import com.hl.stock.core.base.model.StockMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class StockDaoImpl implements StockDao {

    @Autowired
    private StockMetaMapper stockMetaMapper;

    @Autowired
    private StockDataMapper stockDataMapper;

    @Override
    public void saveData(StockData stockData) {
        stockDataMapper.insert(stockData);
    }

    @Override
    public void saveDataBatch(List<StockData> stockDatas) {
        stockDataMapper.insertBatch(stockDatas);
    }

    @Override
    public void saveMeta(StockMeta stockMeta) {
        stockMetaMapper.insert(stockMeta);
    }

    @Override
    public void saveMetaBatch(List<StockMeta> stockMetas) {
        stockMetaMapper.insertBatch(stockMetas);
    }

    @Override
    public List<StockData> loadData(String code, Date startDate, Date endDate) {
        return stockDataMapper.getSeriesByTime(code, startDate, endDate);
    }

    @Override
    public List<StockMeta> loadMeta() {
        return stockMetaMapper.getAll();
    }

    @Override
    public boolean hasData(String code) {
        return stockDataMapper.hasSeries(code);
    }

    @Override
    public Date lastDateOfData(String code) {
        return stockDataMapper.lastDateOfSeries(code);
    }
}
