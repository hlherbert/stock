package com.hl.stock.core.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {
    public static final String yyyyMMdd = "yyyyMMdd";
    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    /**
     * 一天的毫秒数
     */
    public static final long ONE_DAY_MILLISECONDS = 1 * 24 * 60 * 60 * 1000;

    /**
     * 一年的天数
     */
    public static final int ONE_YEAR_DAYS = 365;

    public static Date fromString(String format, String str) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.parse(str);
    }

    public static String formatDate(String format, Date date) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static String standardDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(yyyy_MM_dd);
        return dateFormat.format(date);
    }

    /**
     * 计算baseDate + days的日期
     *
     * @param baseDate 基础时间
     * @param days     天数
     * @return baseDate + days后的日期
     */
    public static Date dateAfterDays(Date baseDate, int days) {
        return new Date(baseDate.getTime() + days * DateTimeUtils.ONE_DAY_MILLISECONDS);
    }

    /**
     * 计算lastDay - firstDay中间相差的天数
     *
     * @param firstDay 第一天
     * @param lastDay  最后一天
     * @return 天数差额
     */
    public static int daysBetween(Date firstDay, Date lastDay) {
        return (int) ((lastDay.getTime() - firstDay.getTime()) / ONE_DAY_MILLISECONDS);
    }
}
