package com.hl.stock.core.server.controller.analysis;


import com.hl.stock.core.base.analysis.StockAnalysis;
import com.hl.stock.core.base.analysis.StockStat;
import com.hl.stock.core.base.analysis.StockStatIndex;
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
     * 提交补录任务
     *
     * @return 补录任务id
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
