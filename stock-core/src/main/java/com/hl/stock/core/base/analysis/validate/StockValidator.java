package com.hl.stock.core.base.analysis.validate;

import com.hl.stock.core.base.analysis.advice.StockAdviceStrategy;
import com.hl.stock.core.base.data.StockDao;
import com.hl.stock.core.base.model.StockData;
import com.hl.stock.core.common.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

// finished
@Component
public class StockValidator {

    @Autowired
    private StockDao stockDao;

    /**
     * 验证策略的有效性
     * 验证方式：
     * 在buyDate买入该股票， [buyDate, buyData+strategy.sellDaysFromBuy日] 这段时间，是否有某一天的利润率能够高于阈值 strategy.validProfitRate
     *
     * @param code     待验证股票样本
     * @param buyDate  验证的购买日期
     * @param strategy 推荐策略
     * @return 是否通过验证:  true: pass;  false: fail;  无效样本: null
     */
    public Boolean validateStrategy(String code, Date buyDate, StockAdviceStrategy strategy) {
        Date endDate = DateTimeUtils.dateAfterDays(buyDate, strategy.getSellDaysFromBuy() + 1);
        List<StockData> datas = stockDao.loadData(code, buyDate, endDate);

        // 无数据，算无效样本，不计入结果
        if (datas == null || datas.isEmpty()) {
            return null;
        }
        double buyPrice = datas.get(0).getClosePrice(); //买入价
        double profitRateThreshold = strategy.getValidProfitRate();
        return datas.parallelStream().anyMatch(data -> data.getClosePrice() / buyPrice > (profitRateThreshold + 1));
    }


    /**
     * 验证策略的有效性
     *
     * @param codes    策略选出来的股票样本
     * @param buyDate  验证的购买日期
     * @param strategy 推荐策略
     * @return
     */
    public StockValidateResult validateStrategy(List<String> codes, Date buyDate, StockAdviceStrategy strategy) {
        StockValidateResult result = new StockValidateResult();
        int nPass = 0;
        int nTotal = 0;
        for (String code : codes) {
            Boolean pass = validateStrategy(code, buyDate, strategy);
            if (pass == null) {
                // 无效样本不纳入统计
                continue;
            }
            if (pass) {
                nPass++;
            }
            nTotal++;
        }
        result.setPassed(nPass);
        result.setTotal(nTotal);
        return result;
    }


}
