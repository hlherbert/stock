package com.hl.stock.old.fangxingou.model;

import java.util.List;

/**
 * 具体规格
 */
public class Spec {
    private Long id; //当前节点的规格ID
    private Long specId; //祖先规格ID
    private String name; //规格名称。 颜色， 或者黑色
    private Long pid; //父节点ID
    private Integer isLeaf; //0-是叶子节点，1-不是
    private List<Spec> values; //子节点集，isLeaf=0时才有
    private Integer status; //状态

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSpecId() {
        return specId;
    }

    public void setSpecId(Long specId) {
        this.specId = specId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Integer getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(Integer isLeaf) {
        this.isLeaf = isLeaf;
    }

    public List<Spec> getValues() {
        return values;
    }

    public void setValues(List<Spec> values) {
        this.values = values;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
