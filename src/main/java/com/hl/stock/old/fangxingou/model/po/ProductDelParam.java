package com.hl.stock.old.fangxingou.model.po;

public class ProductDelParam {
    private String productId;// 商品id，和接入方的out_product_id二选一
    private String outProductId;// 接入方商品ID(例如淘宝的商品id)

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
}
