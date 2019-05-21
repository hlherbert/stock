package com.hl.stock.core.base.analysis.validate;

import com.hl.stock.core.base.analysis.advice.strategy.StockStrategy;
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

    /**
     * 验证交易周期T，D日卖出后，D+T卖出 30天
     */
    private static final int VALID_TRANSACTION_CYCLE = 30;

    /**
     * 验证利润率 阈值  (保证年利率10%，摊下来一个交易周期的利润率)
     */
    private static final double VALID_PROFIT_RATE_THRESHOLD = 0.1 / 365 * VALID_TRANSACTION_CYCLE;

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
    public Boolean validateStrategy(String code, Date buyDate, StockStrategy strategy) {
        Date endDate = DateTimeUtils.dateAfterDays(buyDate, VALID_TRANSACTION_CYCLE + 1);
        List<StockData> datas = stockDao.loadData(code, buyDate, endDate);

        // 无数据，算无效样本，不计入结果
        if (datas == null || datas.isEmpty()) {
            return null;
        }
        double buyPrice = datas.get(0).getClosePrice(); //买入价
        double profitRateThreshold = VALID_PROFIT_RATE_THRESHOLD;
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
    public StockValidateResult validateStrategy(List<String> codes, Date buyDate, StockStrategy strategy) {
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
