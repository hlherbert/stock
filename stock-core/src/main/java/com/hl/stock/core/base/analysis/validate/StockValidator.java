package com.hl.stock.core.base.analysis.validate;

import com.hl.stock.core.base.analysis.strategy.StockStrategy;

import java.util.Date;
import java.util.List;

public interface StockValidator {
    StockValidateResult validateStrategy(List<String> codes, Date buyDate, StockStrategy strategy);
}
