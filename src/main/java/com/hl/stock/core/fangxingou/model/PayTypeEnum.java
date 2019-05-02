package com.hl.stock.core.fangxingou.model;

/**
 * 支付方式
 */
public enum PayTypeEnum {
    /**
     * 0货到付款,
     */
    ARRIVE(0),
    /**
     * 1在线支付,
     */
    ONLINE(1),
    /**
     * 2两者都支持
     */
    BOTH(2);

    private int val;

    public int getVal() {
        return val;
    }

    public String getStrVal() {
        return String.valueOf(val);
    }

    PayTypeEnum(int val) {
        this.val = val;
    }
}
