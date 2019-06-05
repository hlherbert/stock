package com.hl.stock.core.base.task;

public class StockTaskProgress<T> {
    private int progress;
    private T data;

    public StockTaskProgress(int progress, T data) {
        this.progress = progress;
        this.data = data;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}
