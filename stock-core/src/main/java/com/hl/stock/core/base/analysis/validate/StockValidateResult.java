package com.hl.stock.core.base.analysis.validate;

/**
 * 策略有效性，验证结果
 */
public class StockValidateResult {
    /**
     * 参与验证总样本数
     */
    private long total;

    /**
     * 通过验证样本数
     */
    private long passed;

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

    public double passRate() {
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
