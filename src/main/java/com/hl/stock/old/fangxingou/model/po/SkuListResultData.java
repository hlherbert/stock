package com.hl.stock.old.fangxingou.model.po;

public class SkuListResultData {
    // "id": 12765412,     // 即为sku_id
    private Long id;
    //"open_user_id": 353992520403334****,    // 即为app_key
    private Long openUserId;
    //"out_sku_id": 123,
    private Long outSkuId;
    //"product_id": 3276192774554309600,
    private Long productId;
    //"spec_detail_id1": 32,
    private Long specDetailId1;
    // "spec_detail_id2": 36,
    private Long specDetailId2;
    // "spec_detail_id3": 0,
    private Long specDetailId3;
    //"spec_detail_name1": "21#白皙色",
    private String specDetailName1;
    // "spec_detail_name2": "15g+15g",
    private String specDetailName2;
    // "spec_detail_name3": "",
    private String specDetailName3;
    //"stock_num": 300,
    private Long stockNum;
    //"price": 0,
    private Long price;
    //"settlement_price": 2100,
    private Long settlementPrice;
    // "spec_id": 8,
    private Long specId;
    // "code": "A0001"
    private String code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOpenUserId() {
        return openUserId;
    }

    public void setOpenUserId(Long openUserId) {
        this.openUserId = openUserId;
    }

    public Long getOutSkuId() {
        return outSkuId;
    }

    public void setOutSkuId(Long outSkuId) {
        this.outSkuId = outSkuId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getSpecDetailId1() {
        return specDetailId1;
    }

    public void setSpecDetailId1(Long specDetailId1) {
        this.specDetailId1 = specDetailId1;
    }

    public Long getSpecDetailId2() {
        return specDetailId2;
    }

    public void setSpecDetailId2(Long specDetailId2) {
        this.specDetailId2 = specDetailId2;
    }

    public Long getSpecDetailId3() {
        return specDetailId3;
    }

    public void setSpecDetailId3(Long specDetailId3) {
        this.specDetailId3 = specDetailId3;
    }

    public String getSpecDetailName1() {
        return specDetailName1;
    }

    public void setSpecDetailName1(String specDetailName1) {
        this.specDetailName1 = specDetailName1;
    }

    public String getSpecDetailName2() {
        return specDetailName2;
    }

    public void setSpecDetailName2(String specDetailName2) {
        this.specDetailName2 = specDetailName2;
    }

    public String getSpecDetailName3() {
        return specDetailName3;
    }

    public void setSpecDetailName3(String specDetailName3) {
        this.specDetailName3 = specDetailName3;
    }

    public Long getStockNum() {
        return stockNum;
    }

    public void setStockNum(Long stockNum) {
        this.stockNum = stockNum;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getSettlementPrice() {
        return settlementPrice;
    }

    public void setSettlementPrice(Long settlementPrice) {
        this.settlementPrice = settlementPrice;
    }

    public Long getSpecId() {
        return specId;
    }

    public void setSpecId(Long specId) {
        this.specId = specId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
