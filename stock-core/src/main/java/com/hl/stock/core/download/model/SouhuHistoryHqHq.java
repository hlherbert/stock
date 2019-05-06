package com.hl.stock.core.download.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 历史行情具体数据
 */
public class SouhuHistoryHqHq extends ArrayList<String> {
    public static final int COL_SIZE = 10;
    public static final int COL_DATE = 0; // 日期
    public static final int COL_OPEN = 1; // 开盘价
    public static final int COL_CLOSE = 2; // 收盘价
    public static final int COL_GROW = 3; // 涨跌额
    public static final int COL_GROW_PERCENT = 4; // 涨跌幅 %
    public static final int COL_LOW = 5; // 最低
    public static final int COL_HIGH = 6; // 最高
    public static final int COL_AMOUNT = 7; // 成交量(手)
    public static final int COL_AMOUNT_MONEY = 8; // 成交金额(万)
    public static final int COL_EXCHANGE = 9; // 换手率 %

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static Double parseStockDouble(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        // 过滤无用字符
        str = str.replaceAll("[%|-]", "");
        if (str.isEmpty()) {
            return null;
        }
        return Double.parseDouble(str);
    }

    public Date getDate() {
        try {
            return dateFormat.parse(this.get(COL_DATE));
        } catch (ParseException e) {
            return null;
        }
    }

    public Double getOpen() {
        return parseStockDouble(this.get(COL_OPEN));
    }


    public Double getClose() {
        return parseStockDouble(this.get(COL_CLOSE));
    }


    public Double getGrow() {
        return parseStockDouble(this.get(COL_GROW));
    }

    public Double getGrowPercent() {
        return parseStockDouble(this.get(COL_GROW_PERCENT));
    }

    public Double getLow() {
        return parseStockDouble(this.get(COL_LOW));
    }

    public Double getHigh() {
        return parseStockDouble(this.get(COL_HIGH));
    }

    public Integer getAmount() {
        return Integer.parseInt(this.get(COL_AMOUNT));
    }

    public Double getAmountMoney() {
        return parseStockDouble(this.get(COL_AMOUNT_MONEY));
    }

    public Double getExchange() {
        return parseStockDouble(this.get(COL_EXCHANGE));
    }
}
