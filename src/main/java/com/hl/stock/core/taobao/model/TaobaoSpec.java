package com.hl.stock.core.taobao.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 淘宝商品规格
 */
public class TaobaoSpec {

    /**
     * 子规格
     */
    List<TaobaoSpec> childSpecs = new ArrayList<>();
    /**
     * spec ID
     */
    private String id;
    /**
     * 规格名称
     */
    private String name;
    /**
     * 规格对应的图片
     */
    private String img;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<TaobaoSpec> getChildSpecs() {
        return childSpecs;
    }

    public void setChildSpecs(List<TaobaoSpec> childSpecs) {
        this.childSpecs = childSpecs;
    }
}
