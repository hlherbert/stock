package com.hl.stock.core.base.analysis.validate;

import com.hl.stock.core.base.analysis.strategy.StockStrategy;
import com.hl.stock.core.base.data.StockDao;
import com.hl.stock.core.base.model.StockData;
import com.hl.stock.core.common.util.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

// finished
@Component
public class StockValidatorV1 implements StockValidator {

    /**
     * 验证交易周期T，D日卖出后，D+T卖出
     */
    private static final int VALID_TRANSACTION_CYCLE = 30;

    /**
     * 验证利润率 阈值  (保证年利率10%，摊下来一个交易周期的利润率)
     */
    private static final double VALID_PROFIT_RATE_THRESHOLD = 0.1 / 365 * VALID_TRANSACTION_CYCLE;

    private static final Logger logger = LoggerFactory.getLogger(StockValidatorV1.class);

    @Autowired
    private StockDao stockDao;

    /**
     * 验证策略的有效性
     * 验证方式：
     * 在buyDate买入该股票， [buyDate, buyData+T日] 这段时间，是否有某一天的利润率能够高于阈值 profitRateThreshold
     *
     * @param code     待验证股票样本
     * @param buyDate  验证的购买日期
     * @param strategy 推荐策略
     * @return 是否通过验证:  true: pass;  false: fail;  无效样本: null
     */
    public ValidateResult validateStrategy(String code, Date buyDate, StockStrategy strategy) {
        Date endDate = DateTimeUtils.dateAfterDays(buyDate, VALID_TRANSACTION_CYCLE + 1);
        List<StockData> datas = stockDao.loadData(code, buyDate, endDate);

        // 无数据，算无效样本，不计入结果
        if (datas == null || datas.isEmpty()) {
            return null;
        }
        double buyPrice = datas.get(0).getClosePrice(); //买入价
        double profitRateThreshold = VALID_PROFIT_RATE_THRESHOLD;
        Optional<Double> maxPrice = datas.parallelStream().map(data -> data.getClosePrice()).max(Double::compare);

        if (maxPrice.isPresent()) {
            double profitRate = maxPrice.get() / buyPrice - 1;
            return new ValidateResult(profitRate > profitRateThreshold, profitRate);
        } else {
            //无数据，算无效样本，不计入结果
            return null;
        }

        //return datas.parallelStream().anyMatch(data -> data.getClosePrice() / buyPrice > (profitRateThreshold + 1));
    }

    /**
     * 验证策略的有效性
     *
     * @param codes    策略选出来的股票样本
     * @param buyDate  验证的购买日期
     * @param strategy 推荐策略
     * @return
     */
    @Override
    public StockValidateResult validateStrategy(List<String> codes, Date buyDate, StockStrategy strategy) {
        StockValidateResult result = new StockValidateResult(strategy.name(), buyDate);
        AtomicInteger nPass = new AtomicInteger(0);
        AtomicInteger nTotal = new AtomicInteger(0);
        double totalProfitRate = 0;
        ConcurrentLinkedQueue<Double> profitRateQueue = new ConcurrentLinkedQueue<>();

        // 内部由于涉及到DB IO + CPU，采用多线程提高资源利用率
        codes.parallelStream().forEach(code -> {
            //Boolean pass = validateStrategy(code, buyDate, strategy);
            ValidateResult res = validateStrategy(code, buyDate, strategy);

            if (res == null) {
                // 无效样本不纳入统计
                return;
            }
            if (res.passed) {
                nPass.incrementAndGet();
            }
            nTotal.incrementAndGet();
            //logger.info("### Validate Strategy: {}, buyDate: {}, code: {}, PassRate: {}", strategy.desc(), buyDate, code, nPass/nTotal);
            profitRateQueue.add(res.profitRate);
        });

        for (Double p : profitRateQueue) {
            totalProfitRate += p;
        }
        result.setPassed(nPass.get());
        result.setTotal(nTotal.get());
        result.setProfitRate((result.getTotal() > 0) ? totalProfitRate / result.getTotal() : 0);
        return result;
    }

    /**
     * 单个股票的中间结果
     */
    private class ValidateResult {
        //是否通过
        boolean passed;

        //利润率
        double profitRate;

        ValidateResult(boolean passed, double profitRate) {
            this.passed = passed;
            this.profitRate = profitRate;
        }
    }


}
