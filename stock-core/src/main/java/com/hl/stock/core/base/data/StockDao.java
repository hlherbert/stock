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
     * 读取股票数据中，符合条件的一个值
     *
     * @param code
     * @param date
     * @return code=code & date=date的数据点
     */
    StockData loadDataPoint(String code, Date date);

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


    /**
     * 某只股票数据中的第一天
     *
     * @param code 股票代码
     * @return 该股票历史数据中的最早日期. 如果未查到该股票数据，则返回null
     */
    Date firstDateOfData(String code);

    /**
     * 某只股票数据中的最后一天
     *
     * @param code 股票代码
     * @return 该股票历史数据中的最后日期. 如果未查到该股票数据，则返回null
     */
    Date lastDateOfData(String code);

    /**
     * 某只股票数据中的最后一天， 且这一天早于或等于lastDateLimit
     *
     * @param code          股票代码
     * @param lastDateLimit 股票数据最后一天的最大值
     * @return 该股票历史数据中，小于等于lastDateLimit的最后日期. 如果未查到该股票数据，则返回null
     */
    Date lastDateOfDataLimit(String code, Date lastDateLimit);

    /**
     * 清洗数据
     * 将非法的股票数据清洗掉，例如时间为未来时间的数据。
     */
    void washData();
}
