package com.hl.stock.core.server.controller.data;

import com.hl.stock.core.base.data.StockDao;
import com.hl.stock.core.base.model.StockData;
import com.hl.stock.core.common.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 股票数据查询器
 */
@RestController
public class StockDaoController {

    @Autowired
    private StockDao stockDao;

    @GetMapping("/stock/data")
    public List<StockData> loadData(@RequestParam("code") String code,
                                    @RequestParam("start") String start,
                                    @RequestParam("end") String end) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(DateTimeUtils.yyyyMMdd);
        Date startDate = dateFormat.parse(start);
        Date endDate = dateFormat.parse(end);

        return stockDao.loadData(code, startDate, endDate);
    }
}
