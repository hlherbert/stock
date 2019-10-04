package com.hl.stock.core.base.analysis.fooladvice;

import com.hl.stock.core.base.analysis.advice.StockAdvice;
import com.hl.stock.core.base.data.StockDao;
import com.hl.stock.core.base.model.StockData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 股票价位过滤器
 */
@Component
public class PriceFilter implements FoolStockFilter {

    @Autowired
    private StockDao stockDao;

    @Override
    public FoolStockAdvice filter(FoolStockAdvice foolAdvice) {
        //看股票当前价位？9~29元，有上涨空间；低于9元活力不足；高于30元风险大。
        List<StockAdvice> advices = foolAdvice.getAdvices();
        if (advices == null || advices.isEmpty()) {
            return foolAdvice;
        }
        for (StockAdvice advice : advices) {
            String code = advice.getCode();
            Date lastDataDate = stockDao.lastDateOfDataLimit(code, foolAdvice.getBuyDate());
            StockData data = stockDao.loadDataPoint(code, lastDataDate);
            if (data == null) {
                advice.riseRisk(StockAdvice.Risk.High);
                advice.appendMessage("查不到最近收盘价（风险高）.");
            }
            Double closePrice = data.getClosePrice();
            if (closePrice == null) {
                advice.riseRisk(StockAdvice.Risk.High);
                advice.appendMessage("查不到最近收盘价（风险高）.");
            }
            if (closePrice >= 9 && closePrice < 30) {
                advice.riseRisk(StockAdvice.Risk.Low);
                advice.appendMessage(String.format("价格%f在9~30之间（有上涨空间）.", closePrice));
            } else if (closePrice < 9) {
                advice.riseRisk(StockAdvice.Risk.Mid);
                advice.appendMessage(String.format("价格%f低于9（活力不足）.", closePrice));
            } else {
                advice.riseRisk(StockAdvice.Risk.High);
                advice.appendMessage(String.format("价格%f高于30（成本太大）.", closePrice));
            }
        }
        return foolAdvice;
    }


}
