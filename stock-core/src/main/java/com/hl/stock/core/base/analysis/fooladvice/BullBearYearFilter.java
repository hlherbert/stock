package com.hl.stock.core.base.analysis.fooladvice;

import com.hl.stock.core.base.analysis.advice.StockAdvice;
import com.hl.stock.core.common.util.DateTimeUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 牛熊市过滤器
 */
public class BullBearYearFilter implements FoolStockFilter {

    /**
     * 牛市年份 - 人工判断
     */
    private Map<Integer, Boolean> bullYearMap = new HashMap<>();

    public BullBearYearFilter() {
        bullYearMap.put(2000, false);
        bullYearMap.put(2001, false);
        bullYearMap.put(2002, false);
        bullYearMap.put(2003, false);
        bullYearMap.put(2004, false);

        bullYearMap.put(2005, true);
        bullYearMap.put(2006, true);
        bullYearMap.put(2007, true);
        bullYearMap.put(2008, false);
        bullYearMap.put(2009, true);

        bullYearMap.put(2010, false);
        bullYearMap.put(2011, false);
        bullYearMap.put(2012, false);
        bullYearMap.put(2013, false);
        bullYearMap.put(2014, true);
        bullYearMap.put(2015, false);

        bullYearMap.put(2016, true);
        bullYearMap.put(2017, true);
        bullYearMap.put(2018, false);
        bullYearMap.put(2019, true);
    }

    @Override
    public FoolStockAdvice filter(FoolStockAdvice foolAdvice) {
        //        评估今年是牛市/熊市？ 熊市不推荐，牛市继续

        Date buyDate = foolAdvice.getBuyDate();
        //判断今年的行情
        int year = DateTimeUtils.yearOf(buyDate);
        Boolean isBull = isBullYear(buyDate);
        if (isBull == null) {
            //map中未配置今年牛熊市情况
            foolAdvice.riseRisk(StockAdvice.Risk.High);
            foolAdvice.appendMessage(String.format("未配置今年(%d)牛熊市信息.", year));
        } else if (isBull) {
            foolAdvice.riseRisk(StockAdvice.Risk.Low);
            foolAdvice.appendMessage(String.format("%d年是牛市（低风险）", year));
        } else {
            foolAdvice.riseRisk(StockAdvice.Risk.High);
            foolAdvice.appendMessage(String.format("%d年是熊市（高风险）", year));
        }
        return foolAdvice;
    }

    /**
     * 判断今年是否是牛市
     *
     * @param date 当前日期
     * @return true:是牛市; false:不是牛市
     */
    private Boolean isBullYear(Date date) {
        int year = DateTimeUtils.yearOf(date);
        return bullYearMap.get(year);
    }
}
