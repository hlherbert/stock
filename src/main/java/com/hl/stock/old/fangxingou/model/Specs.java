package com.hl.stock.old.fangxingou.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 复合规格
 */
public class Specs {
    private Long id;
    private String name;

    @SerializedName("Specs")
    private List<Spec> specs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Spec> getSpecs() {
        return specs;
    }

    public void setSpecs(List<Spec> specs) {
        this.specs = specs;
    }
}
