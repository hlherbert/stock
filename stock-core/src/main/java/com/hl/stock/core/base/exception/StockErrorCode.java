package com.hl.stock.core.base.exception;

import com.hl.stock.core.common.exception.AppException;
import com.hl.stock.core.common.exception.ErrorCode;
import com.hl.stock.core.common.util.SoundUtils;
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
    ExtractStockMetaFail(30001, "ExtractStockMetaFail"),        // 解析股票元数据失败

    DownloadStockDataFail(30002, "DownloadStockDataFail"),      // 下载股票数据失败

    ConvertStockDataFail(30003, "ConvertStockDataFail"),        // 转换股票数据失败

    ParseStockStartDateFail(30004, "ParseStockStartDateFail"),  // 解析股票下载起始时间参数失败

    DownloadStockCodeNotExists(30005, "DownloadStockCodeNotExists"),        // 下载股票代码不存在(股票退市)

    DownloadStockDataTimeout(30006, "DownloadStockDataTimeout"),            // 下载股票数据超时

    // data 40001-49999
    LoadStockDataFail(40001, "LoadStockDataFail"),        // 读取股票数据失败

    // query 50001-59999

    // emulation 60001-69999

    // analysis 70001-79999

    ;

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(StockErrorCode.class);

    /**
     * 错误码
     */
    protected final int code;

    /**
     * 错误信息
     */
    protected final String desc;

    StockErrorCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    protected ErrorCode toErrorCode() {
        return new ErrorCode(code, desc);
    }

    public void error() throws AppException {
        SoundUtils.beep();//声音告警
        toErrorCode().error();
    }

    public void error(Throwable cause) throws AppException {
        SoundUtils.beep();//声音告警
        toErrorCode().error(cause);
    }

    /**
     * 告警但不抛出异常
     */
    public void warn() {
        toErrorCode().warn();
    }

    /**
     * 告警但不抛出异常
     */
    public void warn(Throwable cause) {
        toErrorCode().warn(cause);
    }
}
