package com.hl.stock.core.base.analysis.advice;

import com.hl.stock.core.base.i18n.StockMessage;

/**
 * 股票推荐
 */
public class StockAdvice {

    public static final StockAdvice Unexceptable = new StockAdvice(
            null, StockMessage.AdviceUnexceptable.toString(), 0, Risk.High, false
    );
    /**
     * 推荐股票的编码
     */
    private String code;
    /**
     * 建议内容
     */
    private String message;
    /**
     * 预期利润率（年化) = (卖出价格-当前价格)/当前价格/(卖出时间-买入时间)*365
     */
    private double profitRate;
    /**
     * 风险
     */
    private Risk risk = Risk.Mid;
    /**
     * 是否建议购买
     */
    private boolean suggest = false;

    public StockAdvice() {
    }


    public StockAdvice(String code, String message, double profitRate, Risk risk, boolean suggest) {
        this.code = code;
        this.message = message;
        this.profitRate = profitRate;
        this.risk = risk;
        this.suggest = suggest;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getProfitRate() {
        return profitRate;
    }

    public void setProfitRate(double profitRate) {
        this.profitRate = profitRate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Risk getRisk() {
        return risk;
    }

    public void setRisk(Risk risk) {
        this.risk = risk;
    }

    public boolean isSuggest() {
        return suggest;
    }

    public void setSuggest(boolean suggest) {
        this.suggest = suggest;
    }

    /**
     * 风险
     */
    public enum Risk {
        Low,
        Mid,
        High
    }
}
