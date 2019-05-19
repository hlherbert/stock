// 股票统计功能
import jQuery from "jquery";
import {
    StringUtil
} from './common/stringutil'

const STYLECLASS_HIGH_RISK = "high-risk";
const STYLECLASS_LOW_RISK = "low-risk";
const STYLECLASS_MID_RISK = "mid-risk";

export class StockStat {

    constructor() {
        this.tableStat;
        this.selIndex;
        this.inputCode;
        this.inputStart;
        this.inputEnd;
        this.chartHistory;
        this.pAdvice;
    }

    // HTML页面加载后初始化
    init(document, chartHistory) {
        //注意：代码中的回调函数都要用callback.bind(this), 避免回调里面的this不是类对象而是调用者

        if (document.querySelector('#doc-stock-history') === null) {
            return;
        }
        this.tableStat = document.querySelector('#table-stat');
        this.selIndex = document.querySelector('#sel-index');
        this.selIndex.addEventListener('change', this.onIndexChange.bind(this));
        this.inputCode = document.querySelector('#input-code');
        this.inputStart = document.querySelector('#input-start');
        this.inputEnd = document.querySelector('#input-end');
        this.pAdvice = document.querySelector('#p-advice');

        // 查询按钮
        this.btnQuery = document.querySelector('#btn-query');
        this.btnQuery.addEventListener('click', this.onBtnQueryClick.bind(this));

        //图表
        this.chartHistory = chartHistory;
    }

    // 查询按钮按下
    onBtnQueryClick(e) {
        // 阻止默认提交行为
        e.preventDefault();

        // 查询股票统计信息并显示在图表里
        this.updateCurStatTable();

        // 给出建议
        this.updateCurAdvice();
    }

    // 统计指标改变
    onIndexChange() {
        this.updateCurStatTable();
    }

    // 查询历史数据
    queryStockStat(index, code, start, end, callback) {
        //let url = "/stock/data?code=603999&start=20100101&end=20190508";
        let url = StringUtil.stringFormat('/stock/analysis/stat?index={0}&code={1}&start={2}&end={3}', index, code, start, end);
        jQuery.get(url)
            .done(function (stockstat) {
                callback(stockstat);
            })
            .fail(function () {
                callback(null);
            });
    }

    queryAdvice(code, buyDate, callback) {
        let url = StringUtil.stringFormat('/stock/analysis/advice?code={0}&buyDate={1}', code, buyDate);
        jQuery.get(url)
            .done(function (advice) {
                callback(advice);
            })
            .fail(function () {
                callback(null);
            });
    }

    // 更新统计表
    updateCurStatTable() {
        let index = this.selIndex.value; //指标
        let code = this.inputCode.value; //当前股票编码
        let startdate = this.inputStart.value;
        let enddate = this.inputEnd.value;
        this.updateStatTable(this.tableStat, index, code, startdate, enddate);
    }

    // 更新统计表
    updateStatTable(table, index, code, startdate, enddate) {
        let callback = function (stockstat) {
            this.clearTable(table);

            // 插入标题
            let row = table.insertRow(0);
            row.innerHTML = "<th>统计项</th><th>日期</th><th>开盘价</th><th>收盘价</th>";

            if (stockstat === null || stockstat === "") {
                return;
            }

            // 临时方便函数
            let insertDataRow = function (title, stockdata) {
                let row = table.insertRow(table.rows.length);
                if (stockdata === null || stockdata === undefined) {
                    row.innerHTML = StringUtil.stringFormat("<th>{0}</th><td>-</td><td>-</td><td>-</td>", title);
                } else {
                    let date = StringUtil.formatStandardDate(stockdata.date, "yyyy/MM/dd");
                    row.innerHTML = StringUtil.stringFormat("<th>{0}</th><td>{1}</td><td>{2}</td><td>{3}</td>", title, date, stockdata.openPrice, stockdata.closePrice);
                }
            }

            // 插入数据
            insertDataRow('最早', stockstat.earliest);
            insertDataRow('最近', stockstat.latest);
            insertDataRow('最低', stockstat.low);
            insertDataRow('最高', stockstat.high);
            row = table.insertRow(table.rows.length);
            row.innerHTML = StringUtil.stringFormat('<th>均价</th><td>{0}</td><td colspan="2">{1}</td>', "-", (stockstat.avgPrice !== null) ? stockstat.avgPrice : "-");

            // 更新echart里的平均线
            this.updateChartAvgLine(this.chartHistory, stockstat.avgPrice);
        }

        this.queryStockStat(index, code, startdate, enddate, callback.bind(this));
    }

    // 清除表
    clearTable(table) {
        let nRows = table.rows.length;
        for (let i = 0; i < nRows; i++) {
            table.deleteRow(0);
        }
    }

    // 更新显示图标的平均线
    updateChartAvgLine(chart, avgPrice) {
        if (avgPrice === null) {
            return;
        }
        let option = chart.getOption();
        const iDayK = 0;
        const jSupportPrice = 0;
        let markLine = option.series[iDayK].markLine;
        markLine.data[jSupportPrice].yAxis = avgPrice;

        // 修改markline的值
        chart.setOption(option);
    }

    // 更新建议
    updateCurAdvice() {
        // 更新建议
        let code = this.inputCode.value; //当前股票编码
        let buydate = this.inputEnd.value;

        this.updateAdvice(this.pAdvice, code, buyDate);
    }

    // 更新建议文字
    updateAdvice(adviceElement, code, buyDate) {
        let callback = function (advice) {
            if (advice === null) {
                return;
            }

            adviceElement.textContent = advice.message;
            let risk = advice.risk;
            if (risk === "High") {
                adviceElement.className = STYLECLASS_HIGH_RISK;
            } else if (risk === "Low") {
                adviceElement.className = STYLECLASS_LOW_RISK;
            } else if (risk === "Mid") {
                adviceElement.className = STYLECLASS_MID_RISK;
            }
        }
        queryAdvice(code, buyDate, callback.bind(this));
    }
}