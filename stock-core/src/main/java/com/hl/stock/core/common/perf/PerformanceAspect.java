package com.hl.stock.core.common.perf;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by caiqingliang on 2016/8/7.
 * 切面，计算函数耗时，按函数总耗时排序输出（总耗时、平均单次耗时、调用次数）
 *
 * 不测量性能，提高程序速度
 */
//@Aspect
//@Component
public class PerformanceAspect {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceAspect.class);

    private Map<String, MyProfile> map = new ConcurrentHashMap<>();

    @Around("@annotation(performanceMeasure)")
    public Object logTime(ProceedingJoinPoint pointcut, PerformanceMeasure performanceMeasure) throws Throwable {
        long pre = System.nanoTime();
        Object rs = pointcut.proceed();//此处注意要返回
        String sig = pointcut.getSignature().getName();
        long time = (System.nanoTime() - pre) / 1000000;
        MyProfile profile = map.get(sig);
        if (profile == null) {
            profile = new MyProfile();
            map.put(sig, profile);
        }
        profile.addTime(time);
        return rs;
    }

    @PreDestroy
    public void print() {
        map.entrySet().stream().sorted((o1, o2) -> o1.getValue().compareTo(o2.getValue()))
                .forEach(entry -> {
                    logger.info(entry.getKey() + "\t" + entry.getValue());
                });
    }

    /**
     * 输出的数据结构，包含总耗时、平均单次耗时、调用次数
     * 大小根据总耗时比较
     */
    public static class MyProfile implements Comparable {
        AtomicLong totalCost = new AtomicLong(0);
        AtomicLong count = new AtomicLong(0);

        public void addTime(long time) {
            //times.add(time);
            totalCost.addAndGet(time);
            count.incrementAndGet();
        }

        public long getAvg() {
            return totalCost.get() / count.get();
        }

        public long getTotalCost() {
            return totalCost.get();
        }

        @Override
        public String toString() {
            return "total-cost " + getTotalCost() + "ms\tavg-cost " + getAvg() + " ms\tcalled " + count + " times";
        }

        @Override
        public int compareTo(Object o) {
            return Long.compare(this.getTotalCost(), ((MyProfile) o).getTotalCost());
        }
    }
}