package com.hl.stock.core.base.analysis.advice;

import com.hl.stock.core.base.analysis.strategy.StockStrategy;
import com.hl.stock.core.base.data.StockDao;
import com.hl.stock.core.base.model.StockMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 买股建议器
 */
@Component
public class StockAdvisor {

    private static Logger logger = LoggerFactory.getLogger(StockAdvisor.class);

    /**
     * 推荐股票个数
     */
    public static final int N_SUGGEST_STOCK = 10;

    @Autowired
    StockDao stockDao;

    /**
     * 推荐可以购买的股票
     *
     * @param buyDate  买入日期
     * @param strategy 选股策略
     * @return 推荐股票清单，按照利润率从高到底排序
     */
    public List<StockAdvice> suggestStocks(Date buyDate, StockStrategy strategy) {
        // 获取所有股票信息
        List<StockMeta> metas = stockDao.loadMeta();
        logger.info("metas size:{}", metas.size());

//        List<StockAdvice> advices = new ArrayList<>();
//        for (StockMeta meta:metas) {
//            StockAdvice advice = strategy.advice(meta.getCode(), buyDate);
//            if (advice.isSuggest()){
//                advices.add(advice);
//            }
//            logger.info("advice code: {}",advice.getCode());
//        }

        List<StockAdvice> advices = metas.parallelStream()
                .map(meta -> strategy.advice(meta.getCode(), buyDate))
                .filter(advice -> advice.isSuggest()).collect(Collectors.toList());

        logger.info("advices size:{}", advices.size());
        // 按照利润率从高到低排序
        advices.sort(Comparator.comparing(StockAdvice::getProfitRate).reversed());

        // 只推荐前 N_SUGGEST_STOCK只股票
        return advices.subList(0, Math.min(N_SUGGEST_STOCK, advices.size()));
        //return advices;
    }


}
