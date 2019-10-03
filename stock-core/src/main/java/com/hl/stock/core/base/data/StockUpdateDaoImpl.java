package com.hl.stock.core.base.data;

import com.hl.stock.core.base.data.mapper.StockDataUpdateMapper;
import com.hl.stock.core.base.model.StockDataUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class StockUpdateDaoImpl implements StockUpdateDao {

    private static Logger logger = LoggerFactory.getLogger(StockUpdateDaoImpl.class);

    @Autowired
    private StockDataUpdateMapper stockDataUpdateMapper;

    private void updateStockData(StockDataUpdate stockDataUpdate) {
        stockDataUpdateMapper.insert(stockDataUpdate);
        stockDataUpdateMapper.update(stockDataUpdate);
    }

    @Override
    public void updateStockData(String code, Date updateDate) {
        updateStockData(new StockDataUpdate(code, updateDate));
    }

    @Override
    public Map<String, Date> loadDataUpdate() {
        List<StockDataUpdate> list = stockDataUpdateMapper.getAll();
        return list.stream().collect(Collectors.toMap(x -> x.getCode(), x -> x.getUpdateDate()));
    }
}
