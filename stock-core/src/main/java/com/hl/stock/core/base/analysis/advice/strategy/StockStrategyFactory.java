package com.hl.stock.core.base.analysis.advice.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class StockStrategyFactory {

    @Autowired
    private StockStrategy defaultStockStrategy;

    public StockStrategy createDefault() {
        return defaultStockStrategy;
    }

    @Bean
    public StockStrategy getDefaultStockStrategy() {
        return new DefaultStockStrategy();
    }
}
