package com.hl.stock.core.base.analysis.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public StockStrategy getStrategy(String name) {
        final Map<String, StockStrategy> strategyMap = Arrays.asList(priceRateStrategy, growSpeedStrategy).stream().collect(Collectors.toMap(
                strategy -> strategy.name(), strategy -> strategy
        ));
        return strategyMap.get(name);
    }
}
