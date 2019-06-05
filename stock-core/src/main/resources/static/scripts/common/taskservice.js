import jQuery from "jquery";

const MAX_PROGRESS = 100; //进度100%

export class TaskService {
    /**
     * 启动异步任务，并更新任务条
     * @param {*} taskBar 任务条<progress>控件，具有value属性表示进度值
     * @param {*} progressLabel 标注进度值的<label>控件, 具有textContent属性
     * @param {*} taskurl 任务url, 启动用post, 查询进度用get
     * @param {*} successCallback 任务完成后的回调函数 f(progressData)
     * @param {*} interval 更新进度条的时间间隔(ms)
     */
    static startTask(taskBar, progressLabel, taskurl, successCallback, interval = 1000) {
        // 提交任务
        jQuery.post(taskurl).done(function () {
            let intervalId = window.setInterval(function () {
                jQuery.get(taskurl)
                    .done(function (progressData) {
                        let progress = progressData.progress;
                        taskBar.value = progress;
                        if (progressLabel) {
                            progressLabel.textContent = progress + "%";
                        }

                        if (progress === MAX_PROGRESS) {
                            if (successCallback) {
                                successCallback(progressData);
                            }
                            window.clearInterval(intervalId);
                        }
                    });
            }, interval);
        });
    }

}