package com.hl.stock.core.base.data.mapper;

import com.hl.stock.core.base.model.StockData;
import com.hl.stock.core.common.exception.AppException;
import com.hl.stock.core.common.util.JsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StockDataMapperTest {
    @Autowired
    private StockDataMapper stockDataMapper;

    @Test
    public void getSeries() throws AppException {
        String code = "000001";
        List<StockData> series = stockDataMapper.getSeries(code);
        series.stream().map(data -> JsonUtils.toJson(data)).forEach(System.out::println);
    }

    @Test
    public void hasSeries() {
        String code1 = "000001";
        String code2 = "00000zz";
        boolean res = stockDataMapper.hasSeries(code1);
        System.out.println(res);

        res = stockDataMapper.hasSeries(code2);
        System.out.println(res);
    }
}