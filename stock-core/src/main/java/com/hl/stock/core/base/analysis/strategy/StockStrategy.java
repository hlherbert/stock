package com.hl.stock.core.base.analysis.strategy;

import com.hl.stock.core.base.analysis.advice.StockAdvice;

import java.util.Date;

/**
 * 选股策略
 */
public abstract class StockStrategy {
    /**
     * 是否建议在buyDate购买某股票
     * 最重要的指标是预期利润率、风险、是否推荐
     * @param code    股票code
     * @param buyDate 购买日期
     * @return 股票的建议。如果推荐，ret.suggest = true;  如果不推荐，ret.sugget=false
     */
    abstract public StockAdvice advice(String code, Date buyDate);

    /**
     * 策略描述
     *
     * @return 策略描述
     */
    abstract public String desc();
}
