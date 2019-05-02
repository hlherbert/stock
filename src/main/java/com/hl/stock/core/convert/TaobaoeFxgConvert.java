package com.hl.stock.core.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hl.stock.core.conf.SellerProperties;
import com.hl.stock.core.fangxingou.model.*;
import com.hl.stock.core.taobao.model.TaobaoItem;
import com.hl.stock.core.taobao.model.TaobaoSku;
import com.hl.stock.core.taobao.model.TaobaoSpec;

import java.util.*;

/**
 * 淘宝转放心购
 */
public class TaobaoeFxgConvert {
    private static Logger logger = LoggerFactory.getLogger(TaobaoeFxgConvert.class);
    /**
     * 商品轮播图数量限制
     */
    private static final int PRODUCT_PIC_LIMIT = ProductConstant.PRODUCT_PIC_LIMIT;

    /**
     * 卖家信息配置
     */
    private static SellerProperties sellerProperties = SellerProperties.getInstance();

    private static Random random = new Random();

    /**
     * 淘宝规格转放心购规格
     *
     * @param taobaoItem 淘宝商品信息
     * @return 放心购的商品信息格式 颜色|黑色,白色,黄色^尺码|S,M,L
     * 多规格用^分隔，父规格与子规格用|分隔，子规格用,分隔
     */
    public static String taobao2FxgSpecs(TaobaoItem taobaoItem) {
        List<TaobaoSpec> specs = taobaoItem.getSpecs();
        if (specs == null || specs.isEmpty()) {
            return SpecConstant.DEFAULT_SPEC;
        }
        List<String> specStrs = new ArrayList<>();
        for (TaobaoSpec spec : specs) {
            List<TaobaoSpec> children = spec.getChildSpecs();
            List<String> childrenSpecNames = new ArrayList<>();
            for (TaobaoSpec child : children) {
                childrenSpecNames.add(child.getName());
            }
            String specStr = spec.getName() + "|" + String.join(",", childrenSpecNames);
            specStrs.add(specStr);
        }
        return String.join("^", specStrs);
    }

    /**
     * 淘宝商品转为放心购商品
     *
     * @param item  taobao商品
     * @param specs 生成的放心购商品规格
     * @return 放心购商品
     */
    public static Product taobao2FxgProduct(TaobaoItem item, Specs specs) {
        Product product = new Product();
        product.setName(item.getTitle());

        // 最多5张
        List<String> picsLimit = item.getPics().subList(0, PRODUCT_PIC_LIMIT);
        String pics = String.join("|", picsLimit);

        String descImgs = String.join("|", item.getDetailImgs());

        product.setPic(pics);
        product.setDescription(descImgs);
        product.setOutProductId(item.getId());

        if (item.getPrice() != null) {
            product.setMarketPrice(String.valueOf((long) (item.getPrice() * 100))); //rmb --> fen
        }
        if (item.getPricePromote() != null) {
            product.setDiscountPrice(String.valueOf((long) (item.getPricePromote() * 100)));
        }
        product.setCosRatio("0"); //佣金率 TODO:佣金先填0

        //TODO: 现在不知道原商品分类，设置为其他
        product.setFirstCid(CategoryEnum.OTHER_CID1.getStrId());
        product.setSecondCid(CategoryEnum.OTHER_CID2.getStrId());
        product.setThirdCid(CategoryEnum.OTHER_CID3.getStrId());

        // 支付方式，只支持在线支付
        product.setPayType(PayTypeEnum.ONLINE.getStrVal());
        product.setSpecId(String.valueOf(specs.getId()));

        // 将规格设置到放心购产品里面，并解析出主规格和图片
        extractTaobaoSpecs2FxgProduct(item, specs, product);

        // 移动电话
        product.setMobile(sellerProperties.getMobile());

        //读取属性
        Map<String, String> basicInfoMap = item.getBasicInfoMap();
        // 设置品牌
        basicInfoMap.put("品牌", sellerProperties.getBrand());

        List<String> props = new ArrayList<>();
        for (Map.Entry<String, String> entry : basicInfoMap.entrySet()) {
            String propName = entry.getKey();
            String propValue = entry.getValue();
            String prop = propName + "|" + propValue;
            props.add(prop);
        }
        product.setProductFormat(String.join("^", props));
        product.setUsp(item.getHotDesc()); // 商品卖点=热门描述

        product.setRecommendRemark(getRandomRecommendRemark());
        product.setExtra(sellerProperties.getExtra());

        return product;
    }

    /**
     * 将规格设置到放心购产品里面，并解析出主规格和图片
     *
     * @param item    淘宝产品
     * @param specs   放心购规格，应该是通过新增规格API返回的规格对象
     * @param product 放心购产品
     */
    private static void extractTaobaoSpecs2FxgProduct(TaobaoItem item, Specs specs, Product product) {
        //寻找specs中的主规格
        Spec mainSpec = null;
        TaobaoSpec taobaoMainSpec = item.getMainSpec();
        if (taobaoMainSpec != null) {
            String taobaoMainSpecName = taobaoMainSpec.getName();
            List<Spec> childSpecs = specs.getSpecs();
            for (Spec childSpec : childSpecs) {
                // 比较规格名称是否和主规格一致
                if (taobaoMainSpecName.equals(childSpec.getName())) {
                    mainSpec = childSpec;
                    break;
                }
            }
        }

        if (mainSpec != null) {
            // 如果有主规格，则设置图片
            List<TaobaoSpec> taobaoSubSpecs = taobaoMainSpec.getChildSpecs();
            HashMap<String, TaobaoSpec> taobaoSubSpecMap = new HashMap<>();
            for (TaobaoSpec taobaoSubSpec : taobaoSubSpecs) {
                taobaoSubSpecMap.put(taobaoSubSpec.getName(), taobaoSubSpec);
            }

            List<Spec> subSpecs = mainSpec.getValues();//红 黄 蓝 等
            List<String> specPicStrs = new ArrayList<>();
            for (Spec subSpec : subSpecs) {
                String subSpecName = subSpec.getName();
                TaobaoSpec taobaoSubSpec = taobaoSubSpecMap.get(subSpecName);
                String subSpecId = String.valueOf(subSpec.getId());
                String subSpecImg = taobaoSubSpec.getImg();

                // 有可能朱规格没有图片
                if (subSpecImg == null) {
                    continue;
                }

                String specPicStr = subSpecId + "|" + subSpecImg;
                specPicStrs.add(specPicStr);
            }
            if (specPicStrs != null && !specPicStrs.isEmpty()) {
                String specPic = String.join("^", specPicStrs);
                product.setSpecPic(specPic);
            }
        }
    }

    /**
     * 淘宝产品中找到sku，并转换为fxg的sku
     * @param item 淘宝产品
     * @param product 淘宝产品转换的放心购产品
     * @param specs 产品规格
     * @return
     */
    public static List<Sku> taobao2FxgSku(TaobaoItem item, Product product, Specs specs) {
        List<Sku> skuList = new ArrayList<Sku>();

        Map<String, TaobaoSpec> taobaoSpecMapByName = new HashMap<>(); //name, taobaoSpec
        Map<String, TaobaoSpec> taobaoSpecMapById = new HashMap<>(); //id, taobaoSpec
        Map<String, Spec> specMapByName = new HashMap<>(); //name, spec
        Map<TaobaoSpec, Spec> taobaoSpec2SpecMap = new HashMap<>(); // taobaoSpec, spec

        for (TaobaoSpec tbspec : item.getSpecs()) {
            // 第一级不加入
//            taobaoSpecMapByName.put(tbspec.getName(), tbspec);
//            if (tbspec.getId() != null) {
//                taobaoSpecMapById.put(tbspec.getId(), tbspec);
//            }
            if (tbspec.getChildSpecs() == null) {
                continue;
            }
            for (TaobaoSpec childTbSpec : tbspec.getChildSpecs()) {
                taobaoSpecMapByName.put(childTbSpec.getName(), childTbSpec);
                if (childTbSpec.getId() != null) {
                    taobaoSpecMapById.put(childTbSpec.getId(), childTbSpec);
                }
            }
        }
        for (Spec spec : specs.getSpecs()) {
            //第一级不加入
            //specMapByName.put(spec.getName(), spec);
            if (spec.getValues() == null) {
                continue;
            }
            for (Spec childSpec : spec.getValues()) {
                specMapByName.put(childSpec.getName(), childSpec);
            }
        }

        for (TaobaoSpec tbSpec : taobaoSpecMapById.values()) {
            String name = tbSpec.getName();
            Spec spec = specMapByName.get(name);
            if (spec != null) {
                taobaoSpec2SpecMap.put(tbSpec, spec);
            }
        }

        // 产品只有唯一一种SKU时
        if (item.getSkuMap() == null || item.getSkuMap().size() == 0) {
            //添加一个默认SKU
            Sku sku = new Sku();

            sku.setOutProductId(product.getOutProductId());
            sku.setOutSkuId(product.getOutProductId()); //使用out_productid作为sku_id
            sku.setSpecId(product.getSpecId()); //父规格ID
            sku.setSpecDetailIds(String.valueOf(specs.getSpecs().get(0).getId()));//需要转换为 子规格ID100041|150041|160041
            sku.setStockNum(sellerProperties.getStock());//使用商家预设的库存
            sku.setPrice(product.getMarketPrice());
            sku.setSettlementPrice(product.getDiscountPrice());
            sku.setCode(product.getOutProductId());

            skuList.add(sku);
            return skuList;
        }

        // 产品有多个SKU
        for (TaobaoSku tbSku : item.getSkuMap().values()) {
            String tbSpecIds = tbSku.getSpecIds();
            //淘宝规格ID ;20549:59280855;1627207:28334;
            //需要转换为放心购 子规格ID 100041|150041|160041
            String[] tbSpecIdArray = tbSpecIds.split(";");
            List<String> specIdList = new ArrayList<>();
            boolean specError = false; //解析spec是否遇到错误
            for (String tbSpecId : tbSpecIdArray) {
                if (tbSpecId == null || tbSpecId.isEmpty()) {
                    continue;
                }
                //找到tbSpecId对应的tbSpec
                TaobaoSpec tbSpec = taobaoSpecMapById.get(tbSpecId);
                if (tbSpec == null) {
                    continue;
                }
                //tbSpec找到对应spec
                Spec spec = taobaoSpec2SpecMap.get(tbSpec);
                if (spec == null) {
                    logger.error("cannot find spec for tbspec:" + tbSpecId);
                    specError = true;
                    break;
                }
                specIdList.add(String.valueOf(spec.getId()));
            }

            if (specError) {
                continue;
            }
            String specDetailIds = String.join("|", specIdList);

            Sku sku = new Sku();
            sku.setOutProductId(product.getOutProductId());
            sku.setOutSkuId(tbSku.getSkuId());
            sku.setSpecId(product.getSpecId()); //父规格ID
            sku.setSpecDetailIds(specDetailIds);//需要转换为 子规格ID 100041|150041|160041
            sku.setStockNum(sellerProperties.getStock());//使用商家预设的库存
            long tbSkuPrice = (long) (Double.valueOf(tbSku.getPrice()) * 100); //单位分,整数
            String strPrice = String.valueOf(tbSkuPrice);
            sku.setPrice(strPrice);
            sku.setSettlementPrice(strPrice);
            sku.setCode(product.getOutProductId());

            skuList.add(sku);
        }

        return skuList;
    }

    /**
     * 获取一个随机推荐语
     *
     * @return 一个随机推荐语
     */
    private static String getRandomRecommendRemark() {
        List<String> remarks = sellerProperties.getRecommendRemarks();
        int i = random.nextInt(remarks.size());
        return remarks.get(i);
    }
}
