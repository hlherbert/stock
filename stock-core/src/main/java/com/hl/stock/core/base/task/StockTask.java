package com.hl.stock.core.base.task;

import java.util.Date;

/**
 * 任务
 */
public class StockTask {
    /**
     * 最大进度
     */
    public static final int MAX_PROGRESS = 100;

    private StockTaskType type;
    private String id;
    private String desc;
    private Date startTime;
    private Date endTime;
    private double progress;

    public StockTask(String id, StockTaskType type) {
        this.id = id;
        this.type = type;
    }

    public StockTask(String id, StockTaskType type, String desc) {
        this.id = id;
        this.type = type;
        this.desc = desc;
    }

    public StockTaskType getType() {
        return type;
    }

    public void setType(StockTaskType type) {
        this.type = type;
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
        startTime = new Date();
        progress = 0;
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
}
