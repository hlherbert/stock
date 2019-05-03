package com.hl.stock.core.exception;

import com.hl.stock.common.exception.AppException;
import com.hl.stock.common.exception.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通用错误码
 */
public enum StockErrorCode {
    // common 10001-19999
    Unkown(10001, "Unkown"),
    NotImplemented(10002, "NotImplemented"),

    // model 20001-29999

    // downloadHistory 30001-39999
    ExtractStockCodeFail(30001, "ExtractCodeFail"),

    DownloadStockDataFail(30002, "DownloadStockDataFail"),

    ConvertStockDataFail(30003, "ConvertStockDataFail");

    // data 40001-49999

    // query 50001-59999

    // emulation 60001-69999

    // analysis 70001-79999

    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(StockErrorCode.class);

    /** 错误码 */
    protected final int code;

    /** 错误信息 */
    protected final String desc;

    StockErrorCode(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    protected ErrorCode toErrorCode() {
        return new ErrorCode(code, desc);
    }

    public void error() throws AppException {
        toErrorCode().error();
    }

    public void error(Throwable cause) throws AppException {
        toErrorCode().error(cause);
    }
}
