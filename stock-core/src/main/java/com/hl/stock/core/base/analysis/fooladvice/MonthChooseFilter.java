package com.hl.stock.core.base.analysis.fooladvice;

import com.hl.stock.core.base.analysis.advice.StockAdvice;
import com.hl.stock.core.base.analysis.advice.StockAdvisor;
import com.hl.stock.core.base.analysis.strategy.GrowSpeedStrategy;
import com.hl.stock.core.base.analysis.strategy.PriceRateStrategy;
import com.hl.stock.core.base.analysis.strategy.StockStrategyFactory;
import com.hl.stock.core.common.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 根据月份选策略过滤器
 */
@Component
public class MonthChooseFilter implements FoolStockFilter {

    @Autowired
    private StockAdvisor stockAdvisor;

    @Autowired
    private StockStrategyFactory factory;

    @Override
    public FoolStockAdvice filter(FoolStockAdvice foolAdvice) {
        // 看当前月份？1，2，11大赚-用growspeed；3，4，7小赚-用priceRate；5，6，8，12有风险-growSpeed；9大亏，不推荐。
        final int month = DateTimeUtils.monthOf(foolAdvice.getBuyDate());
        if (month == 1 || month == 2 || month == 11) {
            foolAdvice.setStrategy(new GrowSpeedStrategy().name());
            foolAdvice.riseRisk(StockAdvice.Risk.Low);
            foolAdvice.appendMessage(String.format("%d月大赚（低风险）.", month));
        } else if (month == 3 || month == 4 || month == 7) {
            foolAdvice.setStrategy(new PriceRateStrategy().name());
            foolAdvice.riseRisk(StockAdvice.Risk.Low);
            foolAdvice.appendMessage(String.format("%d月小赚（低风险）.", month));
        } else if (month == 5 || month == 6 || month == 8 || month == 12) {
            foolAdvice.setStrategy(new PriceRateStrategy().name());
            foolAdvice.riseRisk(StockAdvice.Risk.Mid);
            foolAdvice.appendMessage(String.format("%d月中风险.建议观望.", month));
        } else {
            foolAdvice.riseRisk(StockAdvice.Risk.High);
            foolAdvice.appendMessage(String.format("%d月高风险.", month));
        }

        // 低风险则进行股票推荐
        if (foolAdvice.getRisk() == StockAdvice.Risk.Low) {
            List<StockAdvice> advices = stockAdvisor.suggestStocks(foolAdvice.getBuyDate(),
                    factory.getStrategy(foolAdvice.getStrategy()));
            foolAdvice.setAdvices(advices);
        }
        return foolAdvice;
    }


}
