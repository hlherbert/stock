package com.hl.stock.core.fangxingou.model.po;

public class SpecAddParam {

    /**
     * 规格名称，如果不填，则规格名为子规格名用 "-" 自动生成
     */
    private String name;

    /**
     * 店铺通用规格，能被同类商品通用
     * 多规格用^分隔，父规格与子规格用|分隔，子规格用,分隔 颜色|黑色,白色,黄色^尺码|S,M,L
     */
    private String specs;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

}