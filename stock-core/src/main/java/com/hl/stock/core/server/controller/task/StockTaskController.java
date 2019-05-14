package com.hl.stock.core.server.controller.task;


import com.hl.stock.core.base.task.StockTaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务进度查询器
 */
@RestController
public class StockTaskController {

    @Autowired
    private StockTaskManager stockTaskManager;

    /**
     * 查询任务的进度
     *
     * @return 0-100 100表示100%完成
     */
    @GetMapping("/stock/task/progress")
    public int getTaskProgress(@RequestParam("taskId") String taskId) {
        return stockTaskManager.getTaskProgress(taskId);
    }

}
