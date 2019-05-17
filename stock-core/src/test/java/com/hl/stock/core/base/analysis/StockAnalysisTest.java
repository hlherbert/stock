package com.hl.stock.core.base.analysis;

import com.hl.stock.core.base.analysis.advice.StockAdvice;
import com.hl.stock.core.base.analysis.stat.StockStat;
import com.hl.stock.core.base.analysis.stat.StockStatIndex;
import com.hl.stock.core.base.data.StockDao;
import com.hl.stock.core.common.util.DateTimeUtils;
import com.hl.stock.core.common.util.JsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StockAnalysisTest {

    @Autowired
    private StockAnalysis stockAnalysis;

    @Autowired
    private StockDao stockDao;

    @Test
    public void stat() throws ParseException {
        String code = "000002"; //万科A
        StockStat stat = stockAnalysis.stat(StockStatIndex.OpenPrice,
                code, DateTimeUtils.fromString(DateTimeUtils.yyyyMMdd, "20000101"), new Date());
        System.out.println(JsonUtils.toPrettyJson(stat));
    }

    @Test
    public void giveAdvice() {

    }

    @Test
    public void suggestStocks() {
        List<StockAdvice> stocksAdvices = stockAnalysis.suggestStocks(new Date());
        System.out.println(JsonUtils.toPrettyJson(stocksAdvices));
    }
}