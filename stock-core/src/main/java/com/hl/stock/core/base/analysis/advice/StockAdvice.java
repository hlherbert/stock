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
     * 建议内容:点评
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
        // 保留2位小数
        return (double) Math.round(profitRate * 100) / 100;
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
     * 追加消息
     *
     * @param msg 附加消息
     */
    public void appendMessage(String msg) {
        this.message = message + "|" + msg;
    }

    /**
     * 提高风险
     *
     * @param risk 更高的风险
     */
    public void riseRisk(StockAdvice.Risk risk) {
        if (this.risk.getValue() < risk.getValue()) {
            this.risk = risk;
        }
    }

    /**
     * 风险
     */
    public enum Risk {
        Low(0, "低"),
        Mid(1, "中"),
        High(2, "高");

        private int value;
        private String txt;

        Risk(int value, String txt) {
            this.value = value;
            this.txt = txt;
        }

        public int getValue() {
            return this.value;
        }

        public String getTxt() {
            return txt;
        }

        @Override
        public String toString() {
            return txt;
        }
    }
}
