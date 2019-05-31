package com.hl.stock.core.base.analysis.emulate;

import com.hl.stock.core.base.analysis.advice.StockAdvice;
import com.hl.stock.core.base.analysis.advice.StockAdvisor;
import com.hl.stock.core.base.analysis.strategy.StockStrategy;
import com.hl.stock.core.base.analysis.strategy.StockStrategyFactory;
import com.hl.stock.core.base.analysis.validate.StockValidateResult;
import com.hl.stock.core.base.analysis.validate.StockValidator;
import com.hl.stock.core.base.config.StockConfig;
import com.hl.stock.core.base.data.StockAnalysisDao;
import com.hl.stock.core.common.util.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 股票策略选择模拟器
 */
@Component
public class StockStrategyEmulator {

    /**
     * 模拟交易的时间间隔
     */
    private static final int EXPERIMENT_DAY_STEP = 90;

    private static final Logger logger = LoggerFactory.getLogger(StockStrategyEmulator.class);

    @Autowired
    private StockAdvisor stockAdvisor;

    @Autowired
    private StockValidator stockValidator;

    @Autowired
    private StockConfig stockConfig;

    @Autowired
    private StockStrategyFactory stockStrategyFactory;

    @Autowired
    private StockAnalysisDao stockAnalysisDao;

    /**
     * 生成模拟交易的日期
     *
     * @return 模拟交易日期集合
     */
    private List<Date> genExperimentDays() {
        // 从最早历史时间到今天，每隔EXPERIMENT_DAY_STEP一个点
        Date startDate = stockConfig.getDefaultHistoryStartDate();
        Date endDate = new Date();
        List<Date> experimentDays = new ArrayList<>();
        Date d = startDate;
        while (d.before(endDate)) {
            experimentDays.add(d);
            d = DateTimeUtils.dateAfterDays(d, EXPERIMENT_DAY_STEP);
        }
        return experimentDays;
    }


    /**
     * 记录验证结果到数据库中
     *
     * @param validateResult 验证结果
     */
    private void recordValidateResult(StockValidateResult validateResult) {
        stockAnalysisDao.saveValidateResult(validateResult);
    }

    /**
     * 用指定选股策略模拟交易和验证策略有效性
     *
     * @param strategy 选股策略
     * @return 策略有效性
     */
    public StockValidateResult emulateAndValidate(StockStrategy strategy) {
        StockValidateResult totalValidateResult = new StockValidateResult(strategy.name(), new Date());

        // 时间范围： 生成模拟交易的日期t0,t1,t2,...ti..., 假装在这些日期进行股票交易
        List<Date> experimentDays = genExperimentDays(); //待生成

        Queue<StockValidateResult> validateResults = new ConcurrentLinkedQueue<>();
        AtomicInteger remainDays = new AtomicInteger(experimentDays.size());

        // 按时间顺序进行，如果没处理完，下次可以补录
        for (Date buyDate : experimentDays) {
            remainDays.decrementAndGet();

            StockValidateResult validateResult = stockAnalysisDao.loadValidateResultPoint(strategy.name(), buyDate);
            if (validateResult != null) {
                // 如果已经有数据了，则无需计算该点
                logger.info("validate point has exists, date={}", DateTimeUtils.standardDate(buyDate));
            } else {
                // 空间范围： 在模拟交易日ti, 从所有股票中找出推荐股集合Si
                List<StockAdvice> advices = stockAdvisor.suggestStocks(buyDate, strategy);
                List<String> codes = advices.stream().map(StockAdvice::getCode).collect(Collectors.toList());

                // 验证：在ti买入，ti+T后卖出，收益率>阈值
                validateResult = stockValidator.validateStrategy(codes, buyDate, strategy);

                // 记录到数据库
                // [debug] logger.debug("total: {}, pr: {}", validateResult.getTotal(),validateResult.getPassRate());
                recordValidateResult(validateResult);
            }
            // 记录有效验证结果
            if (validateResult.getTotal() > 0) {
                validateResults.add(validateResult);
                logger.info("## Valid Strategy: {}, buyDate: {}, PassRate: {}, Remain: {}", strategy.name(),
                        DateTimeUtils.standardDate(buyDate), validateResult.getPassRate(), remainDays.get());
            }
        }

        // 合并验证结果
        validateResults.stream().forEach(totalValidateResult::mergeResult);
        return totalValidateResult;
    }


    /**
     * 找到最优策略
     *
     * @return 最优策略
     */
    public StockStrategy findBestStrategy() {
        List<StockStrategy> strategies = stockStrategyFactory.getAllStrategies();
        StockStrategy bestStrategy = null;
        double bestPassRate = 0;
        for (StockStrategy strategy : strategies) {
            StockValidateResult validResult = emulateAndValidate(strategy);
            double strategyPassRate = validResult.getPassRate();
            logger.info("Strategy: {}, PassRate: {}", strategy.name(), strategyPassRate);
            if (strategyPassRate > bestPassRate) {
                bestStrategy = strategy;
                bestPassRate = strategyPassRate;
            }
        }

        return bestStrategy;
    }
}
