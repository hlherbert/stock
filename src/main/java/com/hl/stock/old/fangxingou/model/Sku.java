package com.hl.stock.old.fangxingou.model;

/**
 * sku
 */
public class Sku {
    //product_id 	string 	*否 	3539925204033339668 	商品id，和接入方的out_product_id二选一
    private String productId;

    //out_product_id 	string 	*否 	123 	和product_id二选一
    private String outProductId;

    //out_sku_id 	string 	是 	123 	业务方自己的sku_id,唯一 (需为数字字符串, max = int64)
    private String outSkuId;

    //spec_id 	string 	是 	121 	规格id
    private String specId;

    //spec_detail_ids 	string 	是 	100041|150041|160041 	子规格id,最多3级,如100041|150041|160041
    //    例如 女款|白色|XL
    private String specDetailIds;

    //    stock_num 	string 	是 	1000 	库存 (单位 分)
    private String stockNum;

    //    price 	string 	是 	12900 	售价 (单位 分)
    private String price;

    //    settlement_price 	string 	是 	9900 	结算价格 (单位 分)
    private String settlementPrice;

    //    code 	string 	否 	A0001 	商品编码(可以用out_sku_id)
    private String code;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getOutProductId() {
        return outProductId;
    }

    public void setOutProductId(String outProductId) {
        this.outProductId = outProductId;
    }

    public String getOutSkuId() {
        return outSkuId;
    }

    public void setOutSkuId(String outSkuId) {
        this.outSkuId = outSkuId;
    }

    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
    }

    public String getSpecDetailIds() {
        return specDetailIds;
    }

    public void setSpecDetailIds(String specDetailIds) {
        this.specDetailIds = specDetailIds;
    }

    public String getStockNum() {
        return stockNum;
    }

    public void setStockNum(String stockNum) {
        this.stockNum = stockNum;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSettlementPrice() {
        return settlementPrice;
    }

    public void setSettlementPrice(String settlementPrice) {
        this.settlementPrice = settlementPrice;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
