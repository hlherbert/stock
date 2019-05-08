package com.hl.stock.core.base.i18n;

import java.util.Locale;

/**
 * 国际化文字
 */
public enum StockMessage {
    DownloadAllStockMeta("下载所有股票代码"),
    DownloadAllStockHistoryData("下载所有股票历史数据"),

    ;


    private final static String LANG_ZH = "zh";
    private String cn;
    private String en;

    StockMessage(String cn) {
        this.cn = cn;
        this.en = cn;
    }

    StockMessage(String cn, String en) {
        this.cn = cn;
        this.en = en;
    }

    public String getCn() {
        return cn;
    }

    public String getEn() {
        return en;
    }


    @Override
    public String toString() {
        if (LANG_ZH.equals(Locale.getDefault().getLanguage())) {
            return cn;
        } else {
            return en;
        }
    }

}
