package com.hl.stock.core.base.data;

import com.hl.stock.core.base.data.mapper.StockDataMapper;
import com.hl.stock.core.base.data.mapper.StockMetaMapper;
import com.hl.stock.core.base.model.StockData;
import com.hl.stock.core.base.model.StockMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Repository
public class StockDaoImpl implements StockDao {

    //沪深A股的代码模式， 00，60开头。 非创业板、B股、基金
    private static final String PATTERN_HU_SHEN_A = "((^00)|(^60)).*";

    //含有错误数据的股票代码 000900~000929
    private static final String PATTERN_BAD_DATA = "(^0009)[0-2].*";

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
    public StockData loadDataPoint(String code, Date date) {
        return stockDataMapper.getPoint(code, date);
    }

    @Override
    public List<StockMeta> loadMeta() {
        List<StockMeta> metas = stockMetaMapper.getAll();
        // 过滤掉 非创业板、B股、基金
        return metas.stream()
                .filter(meta -> (
                        Pattern.matches(PATTERN_HU_SHEN_A, meta.getCode())
                                && !Pattern.matches(PATTERN_BAD_DATA, meta.getCode())
                )).collect(Collectors.toList());
    }

    @Override
    public boolean hasData(String code) {
        return stockDataMapper.hasSeries(code);
    }

    @Override
    public Date firstDateOfData(String code) {
        return stockDataMapper.firstDateOfSeries(code);
    }

    @Override
    public Date lastDateOfData(String code) {
        return stockDataMapper.lastDateOfSeries(code);
    }

    @Override
    public Date lastDateOfDataLimit(String code, Date lastDateLimit) {
        return stockDataMapper.lastDateOfSeriesLimit(code, lastDateLimit);
    }

    @Override
    public void washData() {
        stockDataMapper.washData();
    }
}
