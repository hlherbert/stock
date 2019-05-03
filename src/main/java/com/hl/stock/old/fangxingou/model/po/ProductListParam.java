package com.hl.stock.old.fangxingou.model.po;

public class ProductListParam {
    private String page = "0";// 从0开始
    private String size = "10";// 每页条数
    private String status = "0"; //0-上架，1-下架

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
