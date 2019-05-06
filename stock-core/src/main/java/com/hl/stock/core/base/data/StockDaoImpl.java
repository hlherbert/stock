package com.hl.stock.core.base.data;

import com.hl.stock.core.base.data.mapper.StockMetaMapper;
import com.hl.stock.core.base.exception.StockErrorCode;
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

    @Override
    public void saveData(StockData stockData) {
        StockErrorCode.NotImplemented.error();
    }

    @Override
    public void saveDataBatch(List<StockData> stockData) {
        StockErrorCode.NotImplemented.error();
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
        StockErrorCode.NotImplemented.error();
        return null;
    }

    @Override
    public List<StockMeta> loadMeta() {
        return stockMetaMapper.getAll();
    }
}
