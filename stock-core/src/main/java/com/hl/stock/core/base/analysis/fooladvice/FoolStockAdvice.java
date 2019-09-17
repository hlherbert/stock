package com.hl.stock.core.base.analysis.fooladvice;

import com.hl.stock.core.base.analysis.advice.StockAdvice;

import java.util.Date;
import java.util.List;

/**
 * 傻瓜式推荐结果
 */
public class FoolStockAdvice {

    /**
     * 推荐的股票
     */
    private List<StockAdvice> advices;

    /**
     * 风险
     */
    private StockAdvice.Risk risk = StockAdvice.Risk.Low;
    /**
     * 建议内容:点评
     */
    private String message;

    /**
     * 选取股票的策略
     */
    private String strategy;

    /**
     * 推荐购买日期
     */
    private Date buyDate;

    /**
     * 推荐卖出日期
     */
    private Date sellDate;

    public List<StockAdvice> getAdvices() {
        return advices;
    }

    public void setAdvices(List<StockAdvice> advices) {
        this.advices = advices;
    }

    public StockAdvice.Risk getRisk() {
        return risk;
    }

    public void setRisk(StockAdvice.Risk risk) {
        this.risk = risk;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    public Date getSellDate() {
        return sellDate;
    }

    public void setSellDate(Date sellDate) {
        this.sellDate = sellDate;
    }

    /**
     * 追加消息
     *
     * @param msg 附加消息
     */
    public void appendMessage(String msg) {
        this.message = (message == null ? msg : message + "|" + msg);
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
}
