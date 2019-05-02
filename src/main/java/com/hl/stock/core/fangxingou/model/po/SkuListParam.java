package com.hl.stock.core.fangxingou.model.po;

/**
 * sku列表参数
 */
public class SkuListParam {
    //product_id 	string 	*否 	3539925204033339668 	商品id，和接入方的out_product_id二选一
    private String productId;
    //out_product_id 	string 	*否 	123 	和product_id二选一
    private String outProductId;

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
