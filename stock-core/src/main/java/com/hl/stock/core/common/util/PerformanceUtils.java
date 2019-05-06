package com.hl.stock.core.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.util.Date;

/**
 * 性能工具类
 */
public class PerformanceUtils {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(PerformanceUtils.class);

    /**
     * 2017-01-01 12:03:04
     */
    private static final DateFormat dateFormat = DateFormat.getDateTimeInstance();

    /**
     * 记录起始时间
     *
     * @param event 事件名称
     */
    public static void logTimeStart(String event) {
        logger.info("Start event:{}, time:{})", event, dateFormat.format(new Date()));
    }

    /**
     * 记录结束时间
     *
     * @param event 事件名称
     */
    public static void logTimeEnd(String event) {
        logger.info("End event:{}, time:{})", event, dateFormat.format(new Date()));
    }
}
