package com.hl.stock.core.server.controller.download;


import com.hl.stock.core.base.download.StockComplementTask;
import com.hl.stock.core.base.task.StockTaskProgress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 股票数据查询器
 */
@RestController
public class StockDownloadController {

    @Autowired
    private StockComplementTask stockComplementTask;

    /**
     * 提交补录任务
     *
     * @return 补录任务id
     */
    @PostMapping("/stock/download/complementTask")
    public String startComplementTask() {
        stockComplementTask.start();
        return stockComplementTask.getId();
    }


    /**
     * 查询补录任务进度
     *
     * @return 补录任务id
     */
    @GetMapping("/stock/download/complementTask")
    public StockTaskProgress<Void> getComplementTaskProgress() {
        return stockComplementTask.getProgressData();
    }

}
