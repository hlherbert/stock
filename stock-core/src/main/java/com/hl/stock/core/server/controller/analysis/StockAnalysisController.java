package com.hl.stock.core.server.controller.analysis;


import com.hl.stock.core.base.analysis.StockAnalysis;
import com.hl.stock.core.base.analysis.stat.StockStat;
import com.hl.stock.core.base.analysis.stat.StockStatIndex;
import com.hl.stock.core.common.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Date;

/**
 * 股票数据分析器
 */
@RestController
public class StockAnalysisController {

    @Autowired
    private StockAnalysis stockAnalysis;

    /**
     * 统计股票数据
     * @param index 统计指标
     * @param code 编码
     * @param start 起始时间
     * @param end 终止时间
     * @return 统计结果
     * @throws ParseException
     */
    @GetMapping("/stock/analysis/stat")
    public StockStat stat(
            @RequestParam("index") StockStatIndex index,
            @RequestParam("code") String code,
            @RequestParam("start") String start,
            @RequestParam("end") String end) throws ParseException {
        Date startDate = DateTimeUtils.fromString(DateTimeUtils.yyyyMMdd, start);
        Date endDate = DateTimeUtils.fromString(DateTimeUtils.yyyyMMdd, end);
        return stockAnalysis.stat(index, code, startDate, endDate);
    }

}
