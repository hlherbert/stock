package com.hl.stock.core.base.analysis.advice;

import com.hl.stock.core.base.analysis.stat.StockStat;
import com.hl.stock.core.base.analysis.stat.StockStatIndex;
import com.hl.stock.core.base.analysis.stat.StockStator;
import com.hl.stock.core.base.config.StockConfig;
import com.hl.stock.core.base.data.StockDao;
import com.hl.stock.core.base.i18n.StockMessage;
import com.hl.stock.core.base.model.StockData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class DefaultStockAdviceStrategy extends StockAdviceStrategy {
    /**
     * 溢价比高位阈值
     */
    private static final double PRICE_RATE_HIGH = 1;

    /**
     * 溢价比低位阈值
     */
    private static final double PRICE_RATE_LOW = 0.5;


    @Autowired
    private StockStator stockStator;

    @Autowired
    private StockDao stockDao;

    @Autowired
    private StockConfig stockConfig;

    public DefaultStockAdviceStrategy() {
        sellDaysFromBuy = StockAdviceStrategy.DEFAULT_SELL_DAYS_FROM_BUY;
        validProfitRate = StockAdviceStrategy.DEFAULT_VALID_PROFIT_RATE;
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
        Date startDate = stockConfig.getDefaultHistoryStartDate();
        Date endDate = new Date();
        // 先统计, 看溢价比
        List<StockData> datas = stockDao.loadData(code, startDate, endDate);
        StockStat stat = stockStator.stat(StockStatIndex.ClosePrice, datas);
        if (stat == null) {
            return StockAdvice.Unexceptable;
        }

        StockAdvice.Risk risk = StockAdvice.Risk.Mid;  //风险
        String msg = StockAdvice.Unexceptable.getMessage();         //建议

        try {
            // 股票平均价(支撑价)
            double supportPrice = stat.getAvgPrice();
            // 股票当前价位(最近收盘价)
            double curPrice = stat.getLatest().getClosePrice();
            double priceRate = curPrice / supportPrice;       //溢价比
            double profitRate = (supportPrice - curPrice) / curPrice; // 预期收益率

            if (priceRate > PRICE_RATE_HIGH) {
                // 溢价比大于阈值， 高风险
                risk = StockAdvice.Risk.High;
                msg = StockMessage.AdvicePriceRateHigh.toString();
            } else if (priceRate < PRICE_RATE_LOW) {
                // 溢价比低于阈值， 低风险，建议抄底
                risk = StockAdvice.Risk.Low;
                msg = StockMessage.AdvicePriceRateLow.toString();
            } else {
                // 中等风险，建议观望
                risk = StockAdvice.Risk.Mid;
                msg = StockMessage.AdvicePriceRateMid.toString();
            }
            return new StockAdvice(code, msg, priceRate, profitRate, risk, suggest(risk));
        } catch (Exception e) {
            // 遇到异常，不可预期
            return StockAdvice.Unexceptable;
        }
    }
}
