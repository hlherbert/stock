package com.hl.stock.core.base.model;

/**
 * 股票元数据
 */
public class StockMeta {
    /**
     * 代码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 交易所
     */
    private StockZone zone;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StockZone getZone() {
        return zone;
    }

    public void setZone(StockZone zone) {
        this.zone = zone;
    }
}
