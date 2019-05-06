package com.hl.stock.core.base.model;

import java.util.Date;

/**
 * 股票天数据
 */
public class StockData {
    /**
     * 日期
     */
    private Date date;

    /**
     * 代码
     */
    private String code;

    /**
     * 开盘价
     */
    private Double openPrice;

    /**
     * 收盘价
     */
    private Double closePrice;

    /**
     * 涨跌额
     */
    private Double growPrice;

    /**
     * 涨跌幅
     */
    private Double growPercent;

    /**
     * 最低价
     */
    private Double lowPrice;

    /**
     * 最高价
     */
    private Double highPrice;

    /**
     * 成交量(手)
     */
    private Integer amount;

    /**
     * 成交金额(万)
     */
    private Double amountMoney;

    /**
     * 换手率
     */
    private Double exchangePercent;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(Double openPrice) {
        this.openPrice = openPrice;
    }

    public Double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(Double closePrice) {
        this.closePrice = closePrice;
    }

    public Double getGrowPrice() {
        return growPrice;
    }

    public void setGrowPrice(Double growPrice) {
        this.growPrice = growPrice;
    }

    public Double getGrowPercent() {
        return growPercent;
    }

    public void setGrowPercent(Double growPercent) {
        this.growPercent = growPercent;
    }

    public Double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(Double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public Double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(Double highPrice) {
        this.highPrice = highPrice;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getAmountMoney() {
        return amountMoney;
    }

    public void setAmountMoney(Double amountMoney) {
        this.amountMoney = amountMoney;
    }

    public Double getExchangePercent() {
        return exchangePercent;
    }

    public void setExchangePercent(Double exchangePercent) {
        this.exchangePercent = exchangePercent;
    }
}
