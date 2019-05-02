package com.hl.stock.core.download;

import com.hl.stock.common.exception.AppException;
import com.hl.stock.common.exception.ErrorCode;
import com.hl.stock.core.emulation.StockErrorCode;
import com.hl.stock.core.model.StockData;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class StockDownloader {

    /**
     * 下载股票数据
     * @param code 编码
     * @param startDate 起始日期
     * @param endDate 结束日期
     * @return 股票数据
     */
    public StockData[] download(String code, Date startDate, Date endDate) throws AppException {
        StockErrorCode.NotImplemented.error();
        return null;
    }

    /**
     * 返回所有股票编码
     * @return 股票编码
     */
    public String[] downCodeList() throws AppException {
        StockErrorCode.NotImplemented.error();



        return null;
    }
}
