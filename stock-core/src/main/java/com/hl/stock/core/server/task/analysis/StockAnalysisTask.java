package com.hl.stock.core.server.task.analysis;

import com.hl.stock.core.base.analysis.StockAnalysis;
import com.hl.stock.core.base.config.StockConfig;
import com.hl.stock.core.base.data.StockDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

/**
 * 股票分析任务集合
 */
@Component
public class StockAnalysisTask {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(StockAnalysisTask.class);

    @Autowired
    private StockAnalysis stockAnalysis;

    @Autowired
    private StockDao stockDao;

    @Autowired
    private StockConfig stockConfig;

    private boolean findBestStrategyTaskStart = false;


    /**
     * 下载任务
     */
    @Scheduled(fixedRateString = "${stock.task.default.fixedRate}",
            initialDelayString = "${stock.task.default.initialDelay}")
    public void findBestStrategy() throws ExecutionException, InterruptedException {
        if (!stockConfig.isEnableFindBestStrategy()) {
            return;
        }

        // 只运行一次，如果任务运行过了，就不再运行了。
        if (findBestStrategyTaskStart) {
            return;
        }
        findBestStrategyTaskStart = true;

        // 数据清洗
        stockDao.washData();

        stockAnalysis.findBestStrategy();
    }
}
