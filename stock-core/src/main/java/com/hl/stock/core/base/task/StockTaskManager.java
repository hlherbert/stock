package com.hl.stock.core.base.task;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 任务管理器
 */
@Component
public class StockTaskManager {
    private Map<String, StockTask> tasks = new ConcurrentHashMap<>();
    private Map<StockTaskType, Queue<StockTask>> taskTypeQueueMap = new ConcurrentHashMap<>();

    private Queue<StockTask> createTaskQueue(StockTaskType type) {
        ConcurrentLinkedQueue<StockTask> queue = new ConcurrentLinkedQueue<>();
        taskTypeQueueMap.put(type, queue);
        return queue;
    }

    private Queue<StockTask> getTaskQueue(StockTaskType type) {
        return taskTypeQueueMap.get(type);
    }

    public StockTask createTask(StockTaskType type) {
        String id = UUID.randomUUID().toString();
        StockTask task = new StockTask(id, type);
        tasks.put(task.getId(), task);

        Queue<StockTask> queue = getTaskQueue(type);
        if (queue == null) {
            queue = createTaskQueue(type);
        }
        queue.add(task);
        return task;
    }


    public StockTask getTask(String id) {
        return tasks.get(id);
    }

    public StockTask getFirstTaskByType(StockTaskType type) {
        Queue<StockTask> queue = getTaskQueue(type);
        if (queue == null) {
            return null;
        }
        return queue.peek();
    }

    public StockTask removeTask(String id) {
        StockTask task = tasks.remove(id);
        if (task == null) {
            return task;
        }
        Queue<StockTask> queue = getTaskQueue(task.getType());
        queue.remove(task);
        return task;
    }

    public int getTaskProgress(String id) {
        StockTask task = tasks.get(id);
        if (task == null) {
            return 0;
        } else {
            return task.getProgress();
        }
    }
}
