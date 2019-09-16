package com.hl.stock.core.base.analysis.fooladvice;

import com.hl.stock.core.common.util.JsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FoolStockAdvisorTest {

    @Autowired
    FoolStockAdvisor foolStockAdvisor;

    @Test
    public void test() {
        FoolStockAdvice advice = foolStockAdvisor.foolSuggestStocks();
        System.out.println(JsonUtils.toPrettyJson(advice));
    }
}