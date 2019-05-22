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

    /**
     * 将其他结果，合并到本结果对象
     */
    public void mergeResult(StockValidateResult result) {
        this.total += result.total;
        this.passed += result.passed;
    }
}
