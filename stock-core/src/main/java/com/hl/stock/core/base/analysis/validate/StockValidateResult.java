package com.hl.stock.core.base.analysis.validate;

import java.util.Date;

/**
 * 策略有效性，验证结果
 */
public class StockValidateResult {
    /**
     * 策略名
     */
    private String strategy;
    /**
     * 时间
     */
    private Date date;
    /**
     * 参与验证总样本数
     */
    private long total;

    /**
     * 通过验证样本数
     */
    private long passed;

    /**
     * 平均收益率
     * 为一个验证交易周期的平均收益率。 VALID_TRANSACTION_CYCLE
     */
    private double profitRate;

    public StockValidateResult(String strategy, Date date) {
        this.date = date;
        this.strategy = strategy;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }


    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getPassed() {
        return passed;
    }

    public void setPassed(long passed) {
        this.passed = passed;
    }

    public long getFail() {
        return total - passed;
    }

    public double getPassRate() {
        return passed / (double) total;
    }

    public double getProfitRate() {
        return profitRate;
    }

    public void setProfitRate(double profitRate) {
        this.profitRate = profitRate;
    }

    /**
     * 将其他结果，合并到本结果对象
     */
    public void mergeResult(StockValidateResult result) {
        // 收益率变为加权的平均收益率，以total为权重
        this.profitRate = (this.total * this.profitRate + result.total * result.profitRate) / (this.total + result.total);
        if (Double.isNaN(this.profitRate)) {
            this.profitRate = 0;
        }
        this.total += result.total;
        this.passed += result.passed;
    }
}
