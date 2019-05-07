package com.hl.stock.core.base.download.model;

/**
 * 行情查询结果返回状态码
 */
public enum SouhuHistoryHqStatus {
    OK(0),  //成功
    NonExistent(2),
    BeginTimeInvalid(3);

    private int code;

    SouhuHistoryHqStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
