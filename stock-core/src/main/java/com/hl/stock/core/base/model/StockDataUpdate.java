package com.hl.stock.core.base.model;

import java.util.Date;

/**
 * 骨片更新日期信息
 */
public class StockDataUpdate {
    /**
     * 代码
     */
    private String code;

    /**
     * 更新日期
     */
    private Date updateDate;

    public StockDataUpdate(String code, Date updateDate) {
        this.code = code;
        this.updateDate = updateDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
