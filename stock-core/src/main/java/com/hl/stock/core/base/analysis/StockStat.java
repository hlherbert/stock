package com.hl.stock.core.base.analysis;

import com.hl.stock.core.base.model.StockData;

/**
 * 股票数据统计结果
 */
public class StockStat {
    private StockStatIndex index;
    /**
     * 最高点
     */
    private StockData high;
    /**
     * 最高点
     */
    private StockData low;
    /**
     * 最早点（离当前时间最远的点）
     */
    private StockData earliest;
    /**
     * 最晚点（离当前时间最近的点）
     */
    private StockData latest;
    /**
     * 均价 = sum(成交金额)/sum(成交量)
     */
    private double avgPrice;

    public StockStatIndex getIndex() {
        return index;
    }

    public void setIndex(StockStatIndex index) {
        this.index = index;
    }

    public StockData getHigh() {
        return high;
    }

    public void setHigh(StockData high) {
        this.high = high;
    }

    public StockData getLow() {
        return low;
    }

    public void setLow(StockData low) {
        this.low = low;
    }

    public StockData getEarliest() {
        return earliest;
    }

    public void setEarliest(StockData earliest) {
        this.earliest = earliest;
    }

    public StockData getLatest() {
        return latest;
    }

    public void setLatest(StockData latest) {
        this.latest = latest;
    }

    public double getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(double avgPrice) {
        this.avgPrice = avgPrice;
    }
}
