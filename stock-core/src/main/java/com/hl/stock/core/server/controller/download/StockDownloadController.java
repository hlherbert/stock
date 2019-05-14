package com.hl.stock.core.server.controller.download;


import com.hl.stock.core.base.download.StockDownloadSaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 股票数据查询器
 */
@RestController
public class StockDownloadController {

    @Autowired
    private StockDownloadSaver stockDownloadSaver;

    /**
     * 提交补录任务
     *
     * @return 补录任务id
     */
    @PostMapping("/stock/download/complement")
    public String complement() {
        String taskId = stockDownloadSaver.complementAllStockHistoryData();
        return taskId;
    }

}
