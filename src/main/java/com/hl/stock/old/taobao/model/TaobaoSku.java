package com.hl.stock.old.taobao.model;

public class TaobaoSku {
    // 规格ID spec ids   ;20549:59280855;1627207:28334;
    private String specIds;

    // 价格 666.00
    private String price;

    // 库存 2
    private String stock;

    // sku id
    private String skuId;

    // 是否卖光了
    private Boolean oversold;

    public String getSpecIds() {
        return specIds;
    }

    public void setSpecIds(String specIds) {
        this.specIds = specIds;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public Boolean getOversold() {
        return oversold;
    }

    public void setOversold(Boolean oversold) {
        this.oversold = oversold;
    }
}
