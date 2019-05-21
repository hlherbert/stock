package com.hl.stock.core.base.data;

import com.hl.stock.core.base.analysis.validate.StockValidateResult;

import java.util.Date;
import java.util.List;

/**
 * 股票策略验证结果数据存储操作对象
 */
public interface StockAnalysisDao {
    /**
     * 保存股票策略验证结果
     *
     * @param stockValidateResult
     */
    void saveValidateResult(StockValidateResult stockValidateResult);

    /**
     * 批量保存股票策略验证结果
     *
     * @param stockValidateResults
     */
    void saveValidateResultBatch(List<StockValidateResult> stockValidateResults);

    /**
     * 读取股票策略验证结果
     *
     * @param strategy
     * @return
     */
    List<StockValidateResult> loadValidateResult(String strategy);

    /**
     * 读取股票策略验证结果 一个点
     *
     * @param strategy
     * @param date
     * @return code=code & date=date的数据点
     */
    StockValidateResult loadValidateResultPoint(String strategy, Date date);

    /**
     * 数据库中是否有股票策略验证结果
     *
     * @param strategy
     * @return true:有。false:没有
     */
    boolean hasValidateResult(String strategy);

}
