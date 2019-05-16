// 股票数据获取功能
import jQuery from "jquery";
import {StringUtil} from "./common/stringutil"

const MAX_PROGRESS = 100; //进度100%

export class StockData  {
    constructor() {
    }

    // HTML页面加载后初始化
    init(document) {
        //注意：代码中的回调函数都要用callback.bind(this), 避免回调里面的this不是类对象而是调用者
        if (document.querySelector('#doc-stock-data') === null) {
            return;
        }
        this.labelProgressComplement = document.querySelector('#progress-complement-val');
        this.progressComplement = document.querySelector('#progress-complement');
        this.btnComplement = document.querySelector('#btn-complement');
        this.btnComplement.addEventListener('click', this.onBtnComplementClick.bind(this));
    }

    // 补录按钮按下
    onBtnComplementClick(e) {
        // 阻止默认提交行为
        e.preventDefault();
        this.btnComplement.setAttribute('disabled', true);
        this.complement();
    }

    // 补录
    complement() {
        // 提交任务
        let url = "/stock/download/complement";
        let that = this; //避免回调里this被错误指向调用者
        jQuery.post(url).done(function (taskId) {
            let intervalId = window.setInterval(function () {
                let urlTaskProgress = StringUtil.stringFormat("/stock/task/progress?taskId={0}", taskId);
                jQuery.get(urlTaskProgress)
                    .done(function (progress) {
                        that.updateComplementProgress(progress);
                        if (progress === MAX_PROGRESS) {
                            this.btnComplement.removeAttribute('disabled');
                            that.clearInterval(intervalId);
                        }
                    });
            }, 1000);
        });
    }

    // 更新补录进度条
    updateComplementProgress(progress) {
        this.progressComplement.value = progress;
        this.labelProgressComplement.textContent = progress + "%";
        if (progress === MAX_PROGRESS) {
            this.labelProgressComplement.textContent = "完成";
        }
    }
}