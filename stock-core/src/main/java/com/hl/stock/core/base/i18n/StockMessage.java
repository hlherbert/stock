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
    FindBestStrategyTaskEnabled("任务已启用：自动寻找最优策略"),
    FindBestStrategyTaskDisabled("任务已禁用：自动寻找最优策略"),

    AdviceUnexceptable("信息不全，无法给出建议"),
    AdvicePriceRateHigh("溢价比高于阈值, 高风险."),
    AdvicePriceRateLow("溢价比低于阈值, 低风险，建议抄底."),
    AdvicePriceRateMid("溢价比在正常范围内, 建议观望."),

    AdviceGrowSpeedHigh("增长速度低于阈值, 高风险."),
    AdviceGrowSpeedLow("增长速度高于阈值, 低风险，建议抄底."),
    AdviceGrowSpeedMid("增长速度在正常范围内, 建议观望."),

    AdviceWisdomOfCrowdHigh("朝阳群众认为, 高风险."),
    AdviceWisdomOfCrowdLow("朝阳群众认为, 低风险，建议抄底."),
    AdviceWisdomOfCrowdMid("朝阳群众认为, 建议观望."),

    AdviceSTHigh("该股为ST股, 高风险."),

    StrategyPriceRate("溢价比策略. 溢价比=当前价格/支撑价格. 年化利润率=溢价比/时间差*365天." +
            "溢价比<0.5为低风险, >1为高风险."),
    StrategyGrowSpeed("增长速度策略. 增长速度=(当前价格-首日价格)/首日价格/时间差*365天. 利润率=增长速度." +
            "增长速度>0.1为低风险, <-0.1为高风险."),
    StrategyWisdomOfCrowd("群众指挥策略. 综合多种策略.")
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
