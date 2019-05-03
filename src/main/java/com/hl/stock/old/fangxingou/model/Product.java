package com.hl.stock.old.fangxingou.model;

/**
 * 商品
 */
public class Product {

    //name	string	是	xxx补水液	商品名称
    private String name;

    //pic	string	是	img_url1|img_url2|img_url3	商品轮播图，每张图片用 | 分开，第一张图为主图
    //数量限制 : 最少1张、最多5张
    private String pic;

    //description	string	是	img_url1|img_url2|img_url3	商品描述，目前只支持图片
    //多张图片用 | 分开
    //不能用其他网站的文本粘贴，这样会出现css样式不兼容
    private String description;

    //out_product_id	string	是	234123	外部商品id,接入方的商品id (需为数字字符串, max = int64)
    private String outProductId;

    //market_price	string	是	12800	市场价，单位分
    private String marketPrice;

    //discount_price	string	否	11000	售卖价，单位分
    private String discountPrice;

    //cos_ratio	string	否	10	佣金分类10%，范围为0-99
    private String cosRatio;

    //first_cid	string	是	2	一级分类id
    private String firstCid;

    //second_cid	string	是	100	二级分类id
    private String secondCid;

    //third_cid	string	是	1000	三级分类id
    private String thirdCid;

    //pay_type	string	是	2	付款方式 (0货到付款, 1在线支付, 2两者都支持)
    private String payType;

    //spec_id	string	是	23	规格id, 要先创建商品通用规格, 如颜色-尺寸
    private String specId;

    //spec_pic	string	否	1234|img_url^1235|img_url	主规格id, 如颜色-尺寸, 颜色就是主规格, 颜色如黑,白,黄,它们的id|图片url
    private String specPic;

    //mobile	string	是	13122225555	客服号码
    private String mobile;

    //product_format	string	否	品牌|ss^货号|8888^上市年份季节|2018年秋季	属性名称|属性值之间用|分隔, 多组之间用^分开
    private String productFormat;

    //usp	string	否	补水保湿	商品卖点
    private String usp;

    //recommend_remark	string	否	这个商品很好啊	商家推荐语
    private String recommendRemark;

    //extra	string	否	123	额外信息
    private String extra;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOutProductId() {
        return outProductId;
    }

    public void setOutProductId(String outProductId) {
        this.outProductId = outProductId;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getCosRatio() {
        return cosRatio;
    }

    public void setCosRatio(String cosRatio) {
        this.cosRatio = cosRatio;
    }

    public String getFirstCid() {
        return firstCid;
    }

    public void setFirstCid(String firstCid) {
        this.firstCid = firstCid;
    }

    public String getSecondCid() {
        return secondCid;
    }

    public void setSecondCid(String secondCid) {
        this.secondCid = secondCid;
    }

    public String getThirdCid() {
        return thirdCid;
    }

    public void setThirdCid(String thirdCid) {
        this.thirdCid = thirdCid;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
    }

    public String getSpecPic() {
        return specPic;
    }

    public void setSpecPic(String specPic) {
        this.specPic = specPic;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProductFormat() {
        return productFormat;
    }

    public void setProductFormat(String productFormat) {
        this.productFormat = productFormat;
    }

    public String getUsp() {
        return usp;
    }

    public void setUsp(String usp) {
        this.usp = usp;
    }

    public String getRecommendRemark() {
        return recommendRemark;
    }

    public void setRecommendRemark(String recommendRemark) {
        this.recommendRemark = recommendRemark;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
