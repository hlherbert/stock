package com.hl.stock.core.base.i18n;

import java.util.Locale;

/**
 * 国际化文字
 */
public enum StockMessage {
    DownloadAllStockMeta("下载所有股票代码"),
    DownloadAllStockHistoryData("下载所有股票历史数据"),
    ComplementStockHistoryData("补录股票历史数据"),
    ComplementAllStockHistoryData("补录所有股票历史数据"),

    DownloadAllStockHistoryDataTaskEnabled("任务已启用：自动下载所有股票历史数据"),
    DownloadAllStockHistoryDataTaskDisabled("任务已禁用：自动下载所有股票历史数据"),
    ComplementAllStockHistoryDataTaskEnabled("任务已启用：自动补录所有股票历史数据"),
    ComplementAllStockHistoryDataTaskDisabled("任务已禁用：自动补录所有股票历史数据"),

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
