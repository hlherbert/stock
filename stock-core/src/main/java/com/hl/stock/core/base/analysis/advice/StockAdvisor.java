package com.hl.stock.core.base.analysis.advice;

import com.hl.stock.core.base.data.StockDao;
import com.hl.stock.core.base.model.StockMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 买股建议器
 */
@Component
public class StockAdvisor {

    @Autowired
    StockDao stockDao;

    /**
     * 推荐可以购买的股票
     *
     * @param buyDate  买入日期
     * @param strategy 选股策略
     * @return 推荐股票清单，按照利润率从高到底排序
     */
    public List<StockAdvice> suggestStocks(Date buyDate, StockAdviceStrategy strategy) {
        // 获取所有股票信息
        List<StockMeta> metas = stockDao.loadMeta();

        List<StockAdvice> advices = new ArrayList<>();
        for (StockMeta meta : metas) {
            StockAdvice advice = strategy.advice(meta.getCode(), buyDate);

            if (advice.isSuggest()) {
                advices.add(advice);
                System.err.println("[TEST-HL] advice:" + meta.getCode() + ": " + meta.getName());
            } else {
                System.err.println("[TEST-HL] not advice:" + meta.getCode() + ": " + meta.getName());
            }
        }

        // 按照利润率从高到低排序
        advices.sort((o1, o2) -> Double.compare(o1.getProfitRate(), o2.getProfitRate()));

        return advices;
    }
}
