// 股票历史数据功能
import jQuery from "jquery";
import {
    StringUtil
} from './common/stringutil';
import {
    TaskService
} from "./common/taskservice";

// 常量定义，只在本文件内有效

// 主题
// default vintage dark macarons infographic shine roma

export class StockAnalysisFool {
    constructor() {
        
    }

    // HTML页面加载后初始化
    init(document) {
        if (document.querySelector('#doc-stock-analysis-fool') === null) {
            return;
        }

        // 推荐表格
        this.tableSuggest = document.querySelector('#table-suggest');

        // 傻瓜推荐结果
        this.txtAdviceRisk =  document.querySelector('#p-advice-risk');
        this.txtAdviceMessage =  document.querySelector('#p-advice-message');
        this.txtAdviceBuyDate =  document.querySelector('#p-advice-buydate');
        this.txtAdviceSellDate =  document.querySelector('#p-advice-selldate');

        // 股票代码
        this.stockmetaMap = this.queryStockMetaMap();

        this.onDocumentLoad();
    }

    // 推荐按钮按下
    onDocumentLoad() {
        // 查询推荐股票并显示在table里
        this.foolSuggestStocks();
    }

    // 启动推荐股票任务
    startSuggestStocksTask(buyDate, strategy) {
        let url = StringUtil.stringFormat('/stock/analysis/suggestStocksTask?buyDate={0}&strategy={1}', buyDate, strategy);
        let callback = this.updateSuggestTable;
        TaskService.startTask(this.progressSuggest, this.labelProgressSuggest, url, callback.bind(this));
    }

    // 傻瓜式推荐股票
    foolSuggestStocks() {
        let url = '/stock/analysis/foolSuggestStocks';
        let callback = this.foolUpdateSuggestTable.bind(this);
        jQuery.get(url)
            .done(function (advice) {
                callback(advice);
            })
            .fail(function () {
                callback(null);
            });
    }

    // 更新推荐表格
    updateSuggestTable(progressData) {
        let that = this;
        let table = this.tableSuggest;
        let advices = progressData.data;

        that.clearTable(table);

        // 插入标题
        let row = table.insertRow(0);
        row.innerHTML = "<th>代码</th><th>名称</th><th>风险</th><th>预期利润率</th>";

        if (advices === null || advices === "" || advices.length === 0) {
            return;
        }

        // 临时方便函数-插入一行
        let insertDataRow = function (stockAdvice) {
            if (stockAdvice === null || stockAdvice === undefined) {
                return;
            }
            let row = table.insertRow(table.rows.length);
            let name = that.findStockName(that.stockmetaMap, stockAdvice.code);
            row.innerHTML = StringUtil.stringFormat("<td>{0}</td><td>{1}</td><td>{2}</td><td>{3}</td>", stockAdvice.code, name, stockAdvice.risk, stockAdvice.profitRate);
        }

        // 插入数据
        for (let i = 0; i < advices.length; i++) {
            insertDataRow(advices[i]);
        }
    }

    // 更新推荐表格(傻瓜模式)
    foolUpdateSuggestTable(advice) {
        let that = this;
        let table = this.tableSuggest;
        let advices = advice.advices;
        
        // 插入推荐结果文本：
        that.txtAdviceRisk.textContent = StringUtil.riskToChinese(advice.risk);
        that.txtAdviceMessage.textContent = advice.message;
        that.txtAdviceBuyDate.textContent = StringUtil.formatStandardDate(advice.buyDate);
        that.txtAdviceSellDate.textContent = StringUtil.formatStandardDate(advice.sellDate);

        // 表格
        that.clearTable(table);
        // 插入标题
        let row = table.insertRow(0);
        row.innerHTML = "<th>代码</th><th>名称</th><th>风险</th><th>预期利润率</th>";

        if (advices === null || advices === "" || advices.length === 0) {
            return;
        }

        // 临时方便函数-插入一行
        let insertDataRow = function (stockAdvice) {
            if (stockAdvice === null || stockAdvice === undefined) {
                return;
            }
            let row = table.insertRow(table.rows.length);
            let name = that.findStockName(that.stockmetaMap, stockAdvice.code);
            row.innerHTML = StringUtil.stringFormat("<td>{0}</td><td>{1}</td><td>{2}</td><td>{3}</td>", stockAdvice.code, name, stockAdvice.risk, stockAdvice.profitRate);
        }

        // 插入数据
        for (let i = 0; i < advices.length; i++) {
            insertDataRow(advices[i]);
        }
    }
    
    // 清除表
    clearTable(table) {
        let nRows = table.rows.length;
        for (let i = 0; i < nRows; i++) {
            table.deleteRow(0);
        }
    }

    // 查股票元数据
    queryStockMetaMap() {
        let ret = null;
        jQuery.ajax({
            type: "get",
            url: "/stock/meta",
            data: null,
            async: false,
            success: function (data) {
                ret = data;
            }
        });

        let map = [];
        if (ret != null) {
            for (let i = 0; i < ret.length; i++) {
                let meta = ret[i];
                map[meta.code] = meta;
            }
        }
        return map;
    }

    findStockName(stockmetaMap, code) {
        return stockmetaMap[code].name;
    }
}