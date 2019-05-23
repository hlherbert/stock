package com.hl.stock.core.base.analysis.strategy;

import com.hl.stock.core.base.analysis.advice.StockAdvice;
import com.hl.stock.core.base.data.StockDao;
import com.hl.stock.core.base.i18n.StockMessage;
import com.hl.stock.core.base.model.StockMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * 股票推荐时，对结果过滤
 * 过滤掉ST股
 */
@Component
public class StockAdviceFilter {
    private static final Logger logger = LoggerFactory.getLogger(StockAdviceFilter.class);

    /**
     * ST股票的利润率
     */
    private static final double ST_STOCK_PROFIT = -1;

    private StockDao stockDao;

    /**
     * 股票编码map
     */
    private Map<String, StockMeta> stockMetas;

    /**
     * 股票是否为ST股
     *
     * @param code
     * @return
     */
    private boolean isST(String code) {
        return stockMetas.get(code).getName().startsWith("*ST");
    }

    @Autowired
    public void setStockDao(StockDao stockDao) {
        this.stockDao = stockDao;
        stockMetas = stockDao.loadMeta().stream().collect(Collectors.toMap(meta -> meta.getCode(), meta -> meta));
    }

    public StockAdvice filterAdvice(StockAdvice advice) {
        if (advice == null) {
            return null;
        }
        String code = advice.getCode();
        if (isST(code)) {
            // 过滤ST股
            advice.setProfitRate(ST_STOCK_PROFIT);
            advice.setRisk(StockAdvice.Risk.High);
            advice.setSuggest(false);
            advice.setMessage(StockMessage.AdviceSTHigh.toString());
        }

        return advice;
    }

}