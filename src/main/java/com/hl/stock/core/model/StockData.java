package com.hl.stock.core.model;

import java.util.Date;

/**
 * 股票天数据
 */
public class StockData {
    /** 日期 */
    private Date date;

    /** 代码 */
    private String code;

    /** 开盘价 */
    private double openPrice;

    /** 收盘价 */
    private double closePrice;

    /** 涨跌额 */
    private double growPrice;

    /** 涨跌幅 */
    private double growPercent;

    /** 最低价 */
    private double lowPrice;

    /** 最高价 */
    private double highPrice;

    /** 成交量(手) */
    private int amount;

    /** 成交金额(万) */
    private double amountMoney;

    /** 换手率 */
    private double exchangePercent;

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

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    public double getGrowPrice() {
        return growPrice;
    }

    public void setGrowPrice(double growPrice) {
        this.growPrice = growPrice;
    }

    public double getGrowPercent() {
        return growPercent;
    }

    public void setGrowPercent(double growPercent) {
        this.growPercent = growPercent;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getAmountMoney() {
        return amountMoney;
    }

    public void setAmountMoney(double amountMoney) {
        this.amountMoney = amountMoney;
    }

    public double getExchangePercent() {
        return exchangePercent;
    }

    public void setExchangePercent(double exchangePercent) {
        this.exchangePercent = exchangePercent;
    }
}
