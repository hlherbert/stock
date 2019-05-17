package com.hl.stock.core.base.analysis;

import com.hl.stock.core.base.analysis.advice.StockAdvice;
import com.hl.stock.core.base.analysis.advice.StockAdviceStrategy;
import com.hl.stock.core.base.analysis.advice.StockAdviceStrategyFactory;
import com.hl.stock.core.base.analysis.advice.StockAdvisor;
import com.hl.stock.core.base.analysis.validate.StockValidateResult;
import com.hl.stock.core.base.analysis.validate.StockValidator;
import com.hl.stock.core.base.config.StockConfig;
import com.hl.stock.core.common.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 股票策略选择模拟器
 */
@Component
public class StockStrategyEmulator {

    /**
     * 模拟交易的时间间隔
     */
    private static final int EXPERIMENT_DAY_STEP = 10;
    @Autowired
    private StockAdvisor stockAdvisor;
    @Autowired
    private StockValidator stockValidator;
    @Autowired
    private StockConfig stockConfig;
    @Autowired
    private StockAdviceStrategyFactory stockAdviceStrategyFactory;

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
        for (Date d = startDate; d.before(endDate); d = DateTimeUtils.dateAfterDays(d, EXPERIMENT_DAY_STEP)) {
            experimentDays.add(d);
        }
        return experimentDays;
    }

    /**
     * 用指定选股策略模拟交易和验证策略有效性
     *
     * @param strategy 选股策略
     * @return 策略有效性
     */
    public StockValidateResult emulateAndValidate(StockAdviceStrategy strategy) {
        StockValidateResult totalValidateResult = new StockValidateResult();

        // 时间范围： 生成模拟交易的日期, 假装在这些日期进行股票交易
        List<Date> experimentDays = genExperimentDays(); //待生成

        for (Date buyDate : experimentDays) {
            // 空间范围： 从所有股票中找出推荐股
            List<StockAdvice> advices = stockAdvisor.suggestStocks(buyDate, strategy);
            List<String> codes = advices.stream().map(advice -> advice.getCode()).collect(Collectors.toList());
            StockValidateResult validateResult = stockValidator.validateStrategy(codes, buyDate, strategy);
            totalValidateResult.mergeResult(validateResult);
        }

        return totalValidateResult;
    }

    /**
     * 找到最优策略
     *
     * @return 最优策略
     */
    public StockAdviceStrategy findBestStrategy() {
        return stockAdviceStrategyFactory.createDefault();
        // TODO: 现在暂时返回默认策略。下一步使用模拟的方式找到最优策略
    }
}
