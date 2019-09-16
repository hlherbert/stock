package com.hl.stock.core.base.analysis.fooladvice;

import com.hl.stock.core.base.analysis.advice.StockAdvice;

import java.util.List;

/**
 * 过滤器链处理器
 */
public class FoolStockFilterProcessor {

    public static FoolStockAdvice process(List<FoolStockFilter> filters, FoolStockAdvice foolAdvice) {
        for (FoolStockFilter filter : filters) {
            //遇到中高风险，中断过滤器链
            if (foolAdvice.getRisk().compareTo(StockAdvice.Risk.Low) > 0) {
                break;
            }
            foolAdvice = filter.filter(foolAdvice);
        }
        return foolAdvice;
    }
}
