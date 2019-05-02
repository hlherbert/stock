package com.hl.stock.core.taobao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hl.stock.core.taobao.model.TaobaoItem;
import com.hl.stock.core.taobao.model.TaobaoSku;
import com.hl.stock.common.util.CharsetConstant;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Map;

/**
 * 淘宝商品检查器
 * 检查是否符合可上传条件
 */
public class TaobaoItemChecker {
    protected static TaobaoItemChecker instance = new TaobaoItemChecker();
    private static Logger logger = LoggerFactory.getLogger(TaobaoItemChecker.class);

    protected TaobaoItemChecker() {
    }

    public static TaobaoItemChecker getInstance() {
        return instance;
    }

    /**
     * 返回组装的错误信息
     *
     * @param msg  错误信息
     * @param item 淘宝产品
     * @return
     */
    private String err(String msg, TaobaoItem item) {
        return MessageFormat.format("invalid item {0}: ", item.getId()) + msg;
    }

    /**
     * 检查项目是否符合可上传条件
     *
     * @param item
     * @return
     */
    public boolean checkItemValid(TaobaoItem item) {
        // sku中descIds超过3级的，无法上传SKU
        Map<String, TaobaoSku> skuMap = item.getSkuMap();
        if (skuMap != null) {
            for (TaobaoSku sku : skuMap.values()) {
                char[] chars = sku.getSpecIds().toCharArray();
                int nSpecIds = 0;
                for (char c : chars) {
                    if (c == ':') {
                        nSpecIds++;
                    }
                }
                if (nSpecIds > 3) {
                    logger.warn(err("sku specIds > 3.  specIds=" + sku.getSpecIds(), item));
                    return false;
                }
            }
        }

        // 标题长度大于30个中文的，无法上传
        try {
            int titleLen = item.getTitle().getBytes(CharsetConstant.GB2312).length;
            if (titleLen > 60) {
                logger.warn(err("title length > 60", item));
                return false;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        // 缺少轮播图或详情图的，禁止上传
        if (item.getPics() == null || item.getPics().isEmpty()) {
            logger.warn(err("pics is null", item));
            return false;
        }
        if (item.getDetailImgs() == null || item.getDetailImgs().isEmpty()) {
            logger.warn(err("detailImages is null", item));
            return false;
        }

        return true;
    }
}
