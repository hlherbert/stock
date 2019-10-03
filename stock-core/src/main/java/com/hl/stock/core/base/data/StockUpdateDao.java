package com.hl.stock.core.base.data;

import java.util.Date;
import java.util.Map;

/**
 * 股票数据更新存取对象
 */
public interface StockUpdateDao {
    /**
     * 记录股票数据最后更新时间
     *
     * @param code
     * @param updateDate
     */
    void updateStockData(String code, Date updateDate);

    /**
     * 读取股票数据最后更新时间
     */
    Map<String, Date> loadDataUpdate();

}
