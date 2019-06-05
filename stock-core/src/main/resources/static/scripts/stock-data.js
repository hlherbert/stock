// 股票数据获取功能
import {TaskService} from "./common/taskservice"

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
        let url = "/stock/download/complementTask";
        TaskService.startTask(this.progressComplement, this.labelProgressComplement, url, null);
    }
}