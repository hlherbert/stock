package com.hl.stock.core.download.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 搜狐股票 历史行情 API的返回数据结构
 * http://q.stock.sohu.com/hisHq?code=zs_000001&start=20000504&end=20151215&stat=0&order=D&period=d
 */
public class SouhuHistoryHq {

    @SerializedName("status")
    private int status;

    @SerializedName("code")
    private String code;

    @SerializedName("stat")
    private SouhuHistoryHqStat stat;

    @SerializedName("hq")
    private List<SouhuHistoryHqHq> hq;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SouhuHistoryHqStat getStat() {
        return stat;
    }

    public void setStat(SouhuHistoryHqStat stat) {
        this.stat = stat;
    }

    public List<SouhuHistoryHqHq> getHq() {
        return hq;
    }

    public void setHq(List<SouhuHistoryHqHq> hq) {
        this.hq = hq;
    }
}
