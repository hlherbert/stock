package com.hl.stock.core.base.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.UUID;

/**
 * 任务
 */
public abstract class StockTask<T> {
    private static Logger logger = LoggerFactory.getLogger(StockTask.class);
    /**
     * 最大进度
     */
    public static final int MAX_PROGRESS = 100;

    private String id;
    private String desc;
    private Date startTime;
    private Date endTime;
    private double progress;

    public StockTask() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getProgress() {
        return (int) progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void start() {
        if (progress > 0 && progress < MAX_PROGRESS) {
            logger.warn("task in progress. task id = {}", this.id);
            return;
        }
        startTime = new Date();
        progress = 0;
        doTask();
    }

    public void finish() {
        endTime = new Date();
        progress = MAX_PROGRESS;
    }

    public boolean finished() {
        return progress == MAX_PROGRESS;
    }

    public synchronized void incProgress(int n) {
        int newProgress = (int) (progress + n);
        if (newProgress > MAX_PROGRESS) {
            newProgress = MAX_PROGRESS;
        } else if (newProgress < 0) {
            newProgress = 0;
        }
        progress = newProgress;
    }

    public synchronized void incProgress(double n) {
        double newProgress = progress + n;
        if (newProgress > MAX_PROGRESS) {
            newProgress = MAX_PROGRESS;
        } else if (newProgress < 0) {
            newProgress = 0;
        }
        progress = newProgress;
    }

    public StockTaskProgress getProgressData() {
        return new StockTaskProgress((int) progress, getData());
    }

    /**
     * @return 获取当前数据
     */
    public abstract T getData();

    /**
     * 启动任务执行的具体操作
     */
    public abstract void doTask();
}
