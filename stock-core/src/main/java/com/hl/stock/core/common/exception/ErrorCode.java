package com.hl.stock.core.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * 通用错误码
 */
public class ErrorCode {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(ErrorCode.class);

    /**
     * 错误码
     */
    protected final int code;

    /**
     * 错误信息
     */
    protected final String desc;

    public ErrorCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getMsg() {
        return MessageFormat.format("{0,number,#}: {1}", code, desc);
    }

    public void error() throws AppException {
        logger.error(this.getMsg());
        throw new AppException(this);
    }

    public void error(Throwable cause) throws AppException {
        logger.error(this.getMsg(), cause);
        throw new AppException(this, cause);
    }
}
