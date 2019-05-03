package com.hl.stock.old.fangxingou.model.po;

import com.hl.stock.common.util.JsonUtils;

//返回结果
public class Result<T> {
    protected T data; //数据
    protected int errNo;//错误码 0
    protected String message;//消息 success

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getErrNo() {
        return errNo;
    }

    public void setErrNo(int errNo) {
        this.errNo = errNo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
