package com.hl.stock.core.server.controller.analysis;


import com.hl.stock.core.base.analysis.StockAnalysis;
import com.hl.stock.core.base.analysis.StockSuggestTask;
import com.hl.stock.core.base.analysis.advice.StockAdvice;
import com.hl.stock.core.base.analysis.fooladvice.FoolStockAdvice;
import com.hl.stock.core.base.analysis.stat.StockStat;
import com.hl.stock.core.base.analysis.stat.StockStatIndex;
import com.hl.stock.core.base.analysis.strategy.StockStrategy;
import com.hl.stock.core.base.analysis.strategy.StockStrategyFactory;
import com.hl.stock.core.base.analysis.validate.StockValidateResult;
import com.hl.stock.core.base.task.StockTaskProgress;
import com.hl.stock.core.common.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * 股票数据分析器
 */
@RestController
public class StockAnalysisController {

    @Autowired
    private StockAnalysis stockAnalysis;

    @Autowired
    private StockStrategyFactory stockStrategyFactory;

    @Autowired
    private StockSuggestTask stockSuggestTask;

    /**
     * 统计股票数据
     *
     * @param index 统计指标
     * @param code  编码
     * @param start 起始时间
     * @param end   终止时间
     * @return 统计结果
     * @throws ParseException
     */
    @GetMapping("/stock/analysis/stat")
    public StockStat stat(
            @RequestParam("index") StockStatIndex index,
            @RequestParam("code") String code,
            @RequestParam("start") String start,
            @RequestParam("end") String end) throws ParseException {
        Date startDate = DateTimeUtils.fromString(DateTimeUtils.yyyyMMdd, start);
        Date endDate = DateTimeUtils.fromString(DateTimeUtils.yyyyMMdd, end);
        return stockAnalysis.stat(index, code, startDate, endDate);
    }

    /**
     * 对单只股票进行推荐评价
     *
     * @param code    代码
     * @param buyDate 购买日期
     * @return 对在指定购买日期是否应该购买该股票进行评估
     * @throws ParseException
     */
    @GetMapping("/stock/analysis/advice")
    public StockAdvice advice(@RequestParam("code") String code, @RequestParam("buyDate") String buyDate) throws ParseException {
        Date buyDateV = DateTimeUtils.fromString(DateTimeUtils.yyyyMMdd, buyDate);
        return stockAnalysis.advice(code, buyDateV);
    }

    /**
     * 查询所有策略的验证结果
     *
     * @return 所有策略的验证结果
     */
    @GetMapping("/stock/analysis/validateResults")
    public List<StockValidateResult> loadAllStrategyValidateResult() {
        return stockAnalysis.loadAllStrategyValidateResult();
    }

    /**
     * 使用指定策略，推荐可以购买的股票
     *
     * @param buyDate      买入日期
     * @param strategyName 策略名称
     * @return 推荐股票清单，按照利润率从高到底排序
     */
    @GetMapping("/stock/analysis/suggestStocks")
    public List<StockAdvice> suggestStocks(@RequestParam("buyDate") String buyDate, @RequestParam("strategy") String strategyName) throws ParseException {
        Date buyDateV = DateTimeUtils.fromString(DateTimeUtils.yyyyMMdd, buyDate);
        StockStrategy strategy = stockStrategyFactory.getStrategy(strategyName);
        return stockAnalysis.suggestStocks(buyDateV, strategy);
    }

    /**
     * 启动推荐股票异步任务：使用指定策略，推荐可以购买的股票
     *
     * @param buyDate      买入日期
     * @param strategyName 策略名称
     * @return 推荐股票清单，按照利润率从高到底排序
     */
    @PostMapping("/stock/analysis/suggestStocksTask")
    public void startSuggestStocksTask(@RequestParam("buyDate") String buyDate, @RequestParam("strategy") String strategyName) throws ParseException {
        Date buyDateV = DateTimeUtils.fromString(DateTimeUtils.yyyyMMdd, buyDate);
        StockStrategy strategy = stockStrategyFactory.getStrategy(strategyName);
        stockSuggestTask.init(buyDateV, strategy);
        stockSuggestTask.start();
    }

    /**
     * 获取推荐股票任务的进度
     *
     * @return
     */
    @GetMapping("/stock/analysis/suggestStocksTask")
    public StockTaskProgress<List<StockAdvice>> getSuggestStocksTaskProgress() {
        return stockSuggestTask.getProgressData();
    }


    /**
     * 傻瓜式推荐股票
     *
     * @return 推荐股票清单，和建议购买日期
     */
    @GetMapping("/stock/analysis/foolSuggestStocks")
    public FoolStockAdvice foolSuggestStocks() {
        return stockAnalysis.foolSuggestStocks();
    }
}
