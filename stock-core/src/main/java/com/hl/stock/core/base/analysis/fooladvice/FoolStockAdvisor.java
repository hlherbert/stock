package com.hl.stock.core.base.analysis.fooladvice;

import com.hl.stock.core.base.analysis.advice.StockAdvisor;
import com.hl.stock.core.common.util.DateTimeUtils;
import com.hl.stock.core.common.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 傻瓜式推荐器
 */
@Component
public class FoolStockAdvisor {

    private static Logger logger = LoggerFactory.getLogger(FoolStockAdvisor.class);

    @Autowired
    private StockAdvisor stockAdvisor;

    private PriceFilter priceFilter;

    private List<FoolStockFilter> foolStockFilterList;

    @Autowired
    public FoolStockAdvisor(PriceFilter priceFilter) {
        this.foolStockFilterList = Arrays.asList(
                new BullBearYearFilter(),
                new MonthChooseFilter(),
                priceFilter
        );
    }

    /**
     * 傻瓜式推荐股票
     *
     * @return 推荐股票清单，购买日期
     */
    public FoolStockAdvice foolSuggestStocks() {
        logger.info("foolSuggestStocks begin.");
        // 明天购买
        Date buyDate = DateTimeUtils.dateAfterDays(new Date(), 1);
        // 一月后卖
        Date sellDate = DateTimeUtils.dateAfterDays(buyDate, DateTimeUtils.ONE_MONTH_DAYS);

//        评估今年是牛市/熊市？ 熊市不推荐，牛市继续

//        看当前月份？1，2，11大赚-用growspeed；3，4，7小赚-用priceRate；5，6，8，12有风险-growSpeed；9大亏，不推荐。

//        看股票当前价位？9~29元，有上涨空间；低于9元活力不足；高于30元风险大。

        FoolStockAdvice advice = new FoolStockAdvice();
        advice.setAdvices(null);
        advice.setBuyDate(buyDate);
        advice.setSellDate(sellDate);
        advice = FoolStockFilterProcessor.process(foolStockFilterList, advice);
        logger.info("foolSuggestStocks end. advise={}", JsonUtils.toPrettyJson(advice));
        return advice;
    }
}
