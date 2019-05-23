package com.hl.stock.core.base.analysis.strategy;

import com.hl.stock.core.base.analysis.advice.StockAdvice;
import com.hl.stock.core.base.analysis.stat.StockStator;
import com.hl.stock.core.base.config.StockConfig;
import com.hl.stock.core.base.data.StockDao;
import com.hl.stock.core.base.exception.StockErrorCode;
import com.hl.stock.core.base.i18n.StockMessage;
import com.hl.stock.core.base.model.StockData;
import com.hl.stock.core.common.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 增长速度策略
 */
@Component
public class GrowSpeedStrategy implements StockStrategy {
    /**
     * 速率高位阈值
     */
    private static final double GROW_SPEED_RATE_HIGH = 0.1;

    /**
     * 速率低位阈值
     */
    private static final double GROW_SPEED_RATE_LOW = -0.1;


    @Autowired
    private StockStator stockStator;

    @Autowired
    private StockDao stockDao;

    @Autowired
    private StockConfig stockConfig;

    @Autowired
    private StockAdviceFilter stockAdviceFilter;

    public GrowSpeedStrategy() {
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
        Date firstDay = null;
        try {
            firstDay = stockDao.firstDateOfData(code);
        } catch (Exception e) {
            StockErrorCode.LoadStockDataFail.error();
        }
        if (firstDay == null) {
            // 无数据
            return StockAdvice.Unexceptable;
        }
        Date lastDay = stockDao.lastDateOfDataLimit(code, buyDate);
        if (lastDay == null) {
            // 无数据
            return StockAdvice.Unexceptable;
        }

        // 先统计, 增长速率
        StockData dataFirst = stockDao.loadDataPoint(code, firstDay);
        if (dataFirst == null) {
            // 无数据
            return StockAdvice.Unexceptable;
        }

        StockData dataLast = stockDao.loadDataPoint(code, lastDay);
        if (dataLast == null) {
            // 无数据
            return StockAdvice.Unexceptable;
        }

        double priceFirst = dataFirst.getClosePrice();
        double priceLast = dataLast.getClosePrice();

        // 增长速度（每年）
        double growSpeed = (priceLast / priceFirst - 1) / DateTimeUtils.daysBetween(firstDay, lastDay) * DateTimeUtils.ONE_YEAR_DAYS;

        // 预期利润率（年化）
        double profitRate = growSpeed;

        StockAdvice.Risk risk; //风险
        String msg; //建议
        try {
            if (growSpeed < GROW_SPEED_RATE_LOW) {
                risk = StockAdvice.Risk.High;
                msg = StockMessage.AdviceGrowSpeedHigh.toString();
            } else if (growSpeed > GROW_SPEED_RATE_HIGH) {
                risk = StockAdvice.Risk.Low;
                msg = StockMessage.AdviceGrowSpeedLow.toString();
            } else {
                // 中等风险，建议观望
                risk = StockAdvice.Risk.Mid;
                msg = StockMessage.AdviceGrowSpeedMid.toString();
            }
            msg = desc() + " | " + msg;
            return stockAdviceFilter.filterAdvice(new StockAdvice(code, msg, profitRate, risk, suggest(risk)));
        } catch (Exception e) {
            // 遇到异常，不可预期
            return StockAdvice.Unexceptable;
        }
    }

    @Override
    public String desc() {
        return StockMessage.StrategyGrowSpeed.toString();
    }

    @Override
    public String name() {
        return "GrowSpeed";
    }
}
