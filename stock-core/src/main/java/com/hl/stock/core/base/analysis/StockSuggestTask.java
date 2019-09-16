package com.hl.stock.core.base.analysis;

import com.hl.stock.core.base.analysis.advice.StockAdvice;
import com.hl.stock.core.base.analysis.advice.StockAdvisor;
import com.hl.stock.core.base.analysis.strategy.StockStrategy;
import com.hl.stock.core.base.data.StockDao;
import com.hl.stock.core.base.model.StockMeta;
import com.hl.stock.core.base.task.StockTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * 股票推荐异步任务
 */
@Component
public class StockSuggestTask extends StockTask<List<StockAdvice>> {

    private static Logger logger = LoggerFactory.getLogger(StockSuggestTask.class);

    @Autowired
    private StockDao stockDao;

    private List<StockAdvice> data;

    private StockStrategy strategy;
    private Date buyDate;

    public void init(Date buyDate, StockStrategy strategy) {
        this.strategy = strategy;
        this.buyDate = buyDate;
    }

    @Override
    public List<StockAdvice> getData() {
        return data;
    }

    @Override
    public void doTask() {
        // 获取所有股票信息
        List<StockMeta> metas = stockDao.loadMeta();
        int nStockMetas = metas.size();
        logger.info("metas size:{}", metas.size());
        CountDownLatch countDownLatch = new CountDownLatch(nStockMetas);

        List<StockAdvice> advices = new ArrayList<>();
        for (StockMeta meta : metas) {
            CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
                StockAdvice advice = strategy.advice(meta.getCode(), buyDate);
                if (advice.isSuggest()) {
                    advices.add(advice);
                }
                logger.info("advice code: {}", advice.getCode());
                this.incProgress(100.0 / (double) nStockMetas);
                countDownLatch.countDown();

                // 当所有任务完成
                if (countDownLatch.getCount() <= 0) {
                    logger.info("advices size:{}", advices.size());
                    // 按照利润率从高到低排序
                    advices.sort((o1, o2) -> Double.compare(o2.getProfitRate(), o1.getProfitRate()));

                    // 记录结果, 只推荐前N只股票
                    data = advices.subList(0, Math.min(StockAdvisor.N_SUGGEST_STOCK, advices.size()));
                    ;
                    this.finish();
                }

                return null;
            });
        }


    }
}
