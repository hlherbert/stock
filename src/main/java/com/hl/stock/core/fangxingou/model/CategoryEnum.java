package com.hl.stock.core.fangxingou.model;

/**
 * 商品分类
 */
public enum CategoryEnum {
    /**
     * 其他 - 1级
     */
    OTHER_CID1(1307L, "其他"),
    /**
     * 其他 - 2级
     */
    OTHER_CID2(1408L, "其他"),
    /**
     * 其他 - 3级
     */
    OTHER_CID3(2653L, "其他");

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public String getStrId() {
        return String.valueOf(id);
    }

    public String getName() {
        return name;
    }

    CategoryEnum(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
