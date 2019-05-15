package com.hl.stock.core.base.analysis;

import com.hl.stock.core.base.data.StockDao;
import com.hl.stock.core.base.model.StockData;
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
        List<StockData> datas = stockDao.loadData(code, DateTimeUtils.fromString(DateTimeUtils.yyyyMMdd, "20000101"), new Date());
        StockStat stat = stockAnalysis.stat(datas, StockStatIndex.OpenPrice);
        System.out.println(JsonUtils.toPrettyJson(stat));
    }
}