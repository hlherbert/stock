package com.hl.stock.core.base.analysis.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class StockStrategyFactory {
    @Autowired
    private StockStrategy priceRateStrategy;

    @Autowired
    private StockStrategy growSpeedStrategy;

    public StockStrategy getDefault() {
        return priceRateStrategy;
    }

    public List<StockStrategy> getAllStrategies() {
        return Arrays.asList(priceRateStrategy, growSpeedStrategy);
    }
}
