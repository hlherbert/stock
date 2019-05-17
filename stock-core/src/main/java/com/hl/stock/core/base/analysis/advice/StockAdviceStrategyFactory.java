package com.hl.stock.core.base.analysis.advice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StockAdviceStrategyFactory {

    @Autowired
    private DefaultStockAdviceStrategy defaultStockAdviceStrategy;

    public StockAdviceStrategy createDefault() {
        return defaultStockAdviceStrategy;
    }
}
