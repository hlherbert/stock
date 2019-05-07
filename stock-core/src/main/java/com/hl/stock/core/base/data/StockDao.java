package com.hl.stock.core.base.data;

import com.hl.stock.core.base.model.StockData;
import com.hl.stock.core.base.model.StockMeta;

import java.util.Date;
import java.util.List;

/**
 * 股票数据存储操作对象
 */
public interface StockDao {
    /**
     * 保存股票数据
     *
     * @param stockData
     */
    void saveData(StockData stockData);

    /**
     * 批量保存股票数据
     *
     * @param stockDatas
     */
    void saveDataBatch(List<StockData> stockDatas);


    /**
     * 保存股票元数据
     *
     * @param stockMeta
     */
    void saveMeta(StockMeta stockMeta);

    /**
     * 批量保存股票元数据
     *
     * @param stockMetas
     */
    void saveMetaBatch(List<StockMeta> stockMetas);

    /**
     * 读取股票数据
     *
     * @param code
     * @param startDate
     * @param endDate
     * @return
     */
    List<StockData> loadData(String code, Date startDate, Date endDate);

    /**
     * 读取股票元数据
     *
     * @return
     */
    List<StockMeta> loadMeta();


    /**
     * 数据库中是否有股票数据
     *
     * @param code 股票代码
     * @return true:有。false:没有
     */
    boolean hasData(String code);
}
