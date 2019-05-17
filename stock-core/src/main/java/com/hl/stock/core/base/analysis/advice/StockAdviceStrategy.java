package com.hl.stock.core.base.analysis.advice;

import java.util.Date;

/**
 * 选股策略
 */
public abstract class StockAdviceStrategy {

    /**
     * 交易周期T，D日卖出后，D+T卖出 30天
     */
    protected static final int DEFAULT_SELL_DAYS_FROM_BUY = 30;

    /**
     * 默认的验证通过利润率阈值  (保证年利率10%，摊下来一个交易周期的利润率)
     */
    protected static final double DEFAULT_VALID_PROFIT_RATE = 0.1 / 365 * DEFAULT_SELL_DAYS_FROM_BUY;

    /**
     * D日购买后，D+sellDaysFromBuy日卖出(或这段时间内卖出)。
     */
    protected int sellDaysFromBuy = DEFAULT_SELL_DAYS_FROM_BUY;

    /**
     * 验证通过的利润率阈值
     */
    protected double validProfitRate = DEFAULT_VALID_PROFIT_RATE;

    public int getSellDaysFromBuy() {
        return sellDaysFromBuy;
    }

    public void setSellDaysFromBuy(int sellDaysFromBuy) {
        this.sellDaysFromBuy = sellDaysFromBuy;
    }

    public double getValidProfitRate() {
        return validProfitRate;
    }

    public void setValidProfitRate(double validProfitRate) {
        this.validProfitRate = validProfitRate;
    }

    /**
     * 是否建议在buyDate购买某股票
     *
     * @param code    股票code
     * @param buyDate 购买日期
     * @return 股票的建议。如果推荐，ret.suggest = true;  如果不推荐，ret.sugget=false
     */
    abstract public StockAdvice advice(String code, Date buyDate);
}
