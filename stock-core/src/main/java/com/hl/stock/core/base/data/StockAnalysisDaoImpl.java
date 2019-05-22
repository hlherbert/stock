package com.hl.stock.core.base.data;

import com.hl.stock.core.base.analysis.validate.StockValidateResult;
import com.hl.stock.core.base.data.mapper.StockValidateResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class StockAnalysisDaoImpl implements StockAnalysisDao {
    @Autowired
    private StockValidateResultMapper stockValidateResultMapper;

    @Override
    public void saveValidateResult(StockValidateResult stockValidateResult) {
        stockValidateResultMapper.insert(stockValidateResult);
    }

    @Override
    public void saveValidateResultBatch(List<StockValidateResult> stockValidateResults) {
        stockValidateResultMapper.insertBatch(stockValidateResults);
    }

    @Override
    public List<StockValidateResult> loadValidateResult(String strategy) {
        return stockValidateResultMapper.getSeries(strategy);
    }

    @Override
    public List<StockValidateResult> loadAllValidateResult() {
        return stockValidateResultMapper.getAll();
    }

    @Override
    public StockValidateResult loadValidateResultPoint(String strategy, Date date) {
        return stockValidateResultMapper.getPoint(strategy, date);
    }

    @Override
    public boolean hasValidateResult(String strategy) {
        return stockValidateResultMapper.hasSeries(strategy);
    }
}
