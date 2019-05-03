package com.hl.stock.old.fangxingou;

/**
 * 放心购open api
 */
public interface FangxingouApi {

    /**
     * 增加规格
     */
    String SPEC_ADD = "spec/add";

    /**
     * 查看规格详细
     */
    String SPEC_SPECDETAIL = "spec/specDetail";

    /**
     * 获取规格详细
     */
    String SPEC_LIST = "spec/list";

    /**
     * 删除规格
     */
    String SPEC_DEL = "/spec/del";

    /**
     * 获取商品分类
     */
    String PRODUCT_GET_GOODS_CATEGORY = "product/getGoodsCategory";

    /**
     * 添加商品
     */
    String PRODUCT_ADD = "product/add";

    /**
     * 删除商品
     */
    String PRODUCT_DEL = "product/del";

    /**
     * 获取商品列表
     */
    String PRODUCT_LIST = "product/list";

    /**
     * 获取商品详情
     */
    String PRODUCT_DETAIL = "product/detail";

    /**
     * 添加SKU, sku指一个product下，按照不同规格，可卖的最小规格，例如
     * product=苹果IPHONE 5
     * sku1=红色32GB，sku2=红色64GB,sku3=白色32GB,sku4=白色64GB
     * 每个sku下可以配置单独的价格和库存
     */
    String SKU_ADD = "sku/add";

    /**
     * 获取SKU列表
     */
    String SKU_LIST = "sku/list";
}
