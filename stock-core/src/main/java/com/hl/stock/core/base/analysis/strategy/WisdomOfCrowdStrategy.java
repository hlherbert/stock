package com.hl.stock.core.base.analysis.strategy;

import com.hl.stock.core.base.analysis.advice.StockAdvice;
import com.hl.stock.core.base.i18n.StockMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 群众智慧策略，综合多种策略
 */
@Component
public class WisdomOfCrowdStrategy implements StockStrategy {

    @Autowired
    private transient StockAdviceFilter stockAdviceFilter;

    @Autowired
    private transient StockStrategyFactory stockStrategyFactory;

    public WisdomOfCrowdStrategy() {
    }

    /**
     * 是否建议购买
     *
     * @param risk 风险
     * @return true:建议买; false:不建议买
     */
    private static boolean suggest(StockAdvice.Risk risk) {
        return risk == StockAdvice.Risk.Low;
    }

    @Override
    public StockAdvice advice(String code, Date buyDate) {
        // 除自身外的其他策略
        List<StockStrategy> crowdStrategies = stockStrategyFactory.getAllStrategies().stream().filter(strategy -> !strategy.name().equals(this.name())).collect(Collectors.toList());

        // 群众智慧，使用各个子策略的最高风险作为预期风险，各个子策略的利润率均值作为预期利润率
        StockAdvice.Risk risk = StockAdvice.Risk.Low;
        double sumProfitRate = 0;
        for (StockStrategy strategy : crowdStrategies) {
            StockAdvice advice = strategy.advice(code, buyDate);
            if (advice.getRisk().getValue() > risk.getValue()) {
                risk = advice.getRisk();
            }
            sumProfitRate += advice.getProfitRate();
        }
        // 平均利润率
        double profitRate = sumProfitRate / crowdStrategies.size();

        String msg; //建议
        if (risk == StockAdvice.Risk.High) {
            msg = StockMessage.AdviceWisdomOfCrowdHigh.toString();
        } else if (risk == StockAdvice.Risk.Low) {
            msg = StockMessage.AdviceWisdomOfCrowdLow.toString();
        } else {
            // 中等风险，建议观望
            msg = StockMessage.AdviceWisdomOfCrowdMid.toString();
        }
        msg = desc() + " | " + msg;
        return stockAdviceFilter.filterAdvice(new StockAdvice(code, msg, profitRate, risk, suggest(risk)));
    }

    @Override
    public String desc() {
        return StockMessage.StrategyWisdomOfCrowd.toString();
    }

    @Override
    public String name() {
        return "WisdomOfCrowd";
    }
}
