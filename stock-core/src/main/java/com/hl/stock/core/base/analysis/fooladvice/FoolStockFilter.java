package com.hl.stock.core.base.analysis.fooladvice;

/**
 * 过滤器
 */
public interface FoolStockFilter {
    FoolStockAdvice filter(FoolStockAdvice foolAdvice);
}
