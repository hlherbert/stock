package com.hl.stock.core.storage;

import com.hl.stock.core.fangxingou.model.SpecIndex;
import com.hl.stock.core.taobao.model.TaobaoItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 产品存储
 */
public class ProductRepository {

    private static ProductRepository instance;
    static {
        instance = new ProductRepository();
    }

    protected ProductRepository() {
    }

    public static ProductRepository getInstance() {
        return instance;
    }
    /**
     * 爬虫爬下来商品
     */
    private List<TaobaoItem> items = new ArrayList<>();

    /**
     * 放心购规格列表
     */
    private List<SpecIndex> specIndexList = new ArrayList<>();

    /**
     * 放心购规格名（一级规格）
     */
    private Map<String, SpecIndex> specIndexMap = new HashMap<>();

    public static void setInstance(ProductRepository instance) {
        ProductRepository.instance = instance;
    }

    public List<TaobaoItem> getItems() {
        return items;
    }

    public void setItems(List<TaobaoItem> items) {
        this.items = items;
    }

    public List<SpecIndex> getSpecIndexList() {
        return specIndexList;
    }

    public void setSpecIndexList(List<SpecIndex> specIndexList) {
        if (specIndexList == null) {
            this.specIndexList.clear();
            return;
        }

        this.specIndexList = specIndexList;
        for (SpecIndex specIndex : specIndexList) {
            specIndexMap.put(specIndex.getName(), specIndex);
        }
    }


    /**
     * 判断规格名是否已经存在
     *
     * @param specName 规格名
     * @return
     */
    public boolean isSpecNameExist(String specName) {
        return specIndexMap.get(specName) != null;
    }

    /**
     * 获取规格索引
     *
     * @param specName 规格名
     * @return
     */
    public SpecIndex getSpecIndex(String specName) {
        return specIndexMap.get(specName);
    }

}
