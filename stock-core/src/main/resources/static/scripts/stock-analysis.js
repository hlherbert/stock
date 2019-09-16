// 股票历史数据功能
import jQuery from "jquery";
import echarts from 'echarts';
import {
    StringUtil
} from './common/stringutil';
import {
    TaskService
} from "./common/taskservice";

// 常量定义，只在本文件内有效

// 主题
// default vintage dark macarons infographic shine roma
const chartTheme = "default";

const STRATEGY_PRICE_RATE = "PriceRate";
const STRATEGY_GROW_SPEED = "GrowSpeed";
const STRATEGY_WISDOM_OF_CROWD = "WisdomOfCrowd";

const MARKLINE = {
    data: [{
        type: 'average',
        name: '平均',
        label: {
            formatter: "{b}:{c}"
        }
    }]
}

export class StockAnalysis {
    constructor() {
        // 数据意义：日期、priceRate策略正确率,  growSpeed策略正确率
        this.data0 = [
            ['2013/1/21', 0.1, 0.4, 0.5],
            ['2013/1/22', 0.2, 0.5, 0.6],
            ['2013/1/23', 0.3, 0.6, 0.7]
        ];

        this.option0 = {
            title: {
                text: '正确率'
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross'
                }
            },
            toolbox: {
                feature: {
                    mark: {},
                    dataView: {
                        readonly: true
                    },
                    restore: {},
                    saveAsImage: {}
                }
            },
            dataZoom: {
                start: 0,
                end: 100
            },
            legend: {},
            xAxis: [{
                name: '时间',
                type: 'category',
                splitNumber: 20,
                min: 'dataMin',
                max: 'dataMax'
            }],
            yAxis: [{
                name: '正确率',
                type: 'value',
                axisLabel: {
                    formatter: function (value) {
                        return (value * 100) + ' %';
                    }
                }
            }],
            series: [{
                    type: 'line',
                    markLine: MARKLINE
                },
                {
                    type: 'line',
                    markLine: MARKLINE
                },
                {
                    type: 'line',
                    markLine: MARKLINE
                }
            ],
            dataset: {
                dimensions: ['date', 'priceRate', 'growSpeed', 'wisdomOfCrowd'],
                source: this.data0
            }
        };

        this.tableStat;
        this.strategyChart;
        this.btnStrategyValidate;
        this.btnSuggest;
        this.spanSuggestWait;
        this.stockmetaMap;
    }

    // HTML页面加载后初始化
    init(document) {
        if (document.querySelector('#doc-stock-analysis') === null) {
            return;
        }

        // 推荐表格
        this.tableSuggest = document.querySelector('#table-suggest');
        this.selStrategy = document.querySelector('#sel-strategy');

        // 策略验证按钮
        this.btnStrategyValidate = document.querySelector('#btn-strategy-validate');
        this.btnStrategyValidate.addEventListener('click', this.onBtnStrategyValidateClick.bind(this));

        // 推荐按钮
        this.btnSuggest = document.querySelector('#btn-suggest');
        this.btnSuggest.addEventListener('click', this.onBtnSuggestClick.bind(this));

        // 推荐进度
        this.progressSuggest = document.querySelector('#progress-suggest');
        this.labelProgressSuggest = document.querySelector('#progress-suggest-val');

        // 傻瓜推荐结果
        this.txtAdviceRisk =  document.querySelector('p-advice-risk');
        this.txtAdviceMessage =  document.querySelector('p-advice-message');
        this.txtAdviceBuyDate =  document.querySelector('p-advice-buydate');
        this.txtAdviceSellDate =  document.querySelector('p-advice-selldate');

        this.strategyChart = echarts.init(document.querySelector('#chart-strategy'), chartTheme);
        this.initChart(this.strategyChart, this.option0);

        this.stockmetaMap = this.queryStockMetaMap();
    }

    // 显示标题，图例和空的坐标轴
    initChart(chart, option) {
        chart.setOption(option);
    }

    // 策略验证按钮按下
    onBtnStrategyValidateClick(e) {
        // 阻止默认提交行为
        e.preventDefault();

        // 查询股票代码的历史并显示在echart里
        this.updateStrategyValidateChart(this.strategyChart);
    }

    // 查询策略验证结果数据
    queryAllStrategyValidateResults(callback) {
        let url = "/stock/analysis/validateResults";
        jQuery.get(url)
            .done(function (validateResults) {
                callback(validateResults);
            })
            .fail(function () {
                callback(null);
            });
    }

    // 更新图表数据
    updateStrategyValidateChart(chart) {
        let callback = function (validateResults) {
            if (validateResults === null) {
                return;
            }

            let optionNew = chart.getOption(); //this.option0; 避免每次把markline覆盖了

            // 填入数据
            let dataNew = this.extractStrategyValidateResults(validateResults);
            optionNew.dataset.source = dataNew.data;

            //chart.setOption(optionNew);
            // 局部刷新，性能更高
            chart.setOption({
                dataset: {
                    source: dataNew.data
                }
            });
        }

        this.queryAllStrategyValidateResults(callback.bind(this));
    }

    extractStrategyValidateResults(validateResults) {
        let results = [];

        // 转换为 map<date,obj>
        for (let i = 0; i < validateResults.length; i++) {
            let row = validateResults[i];
            let dateObj = new Date();
            dateObj.setTime(Date.parse(row.date));

            let newRow = {};
            let date = StringUtil.dateFormat(dateObj, "yyyy/MM/dd");

            if (results[date] != null) {
                newRow = results[date];
            }

            newRow.date = date;
            let passRate = row.passRate;
            let strategy = row.strategy;
            if (strategy === STRATEGY_PRICE_RATE) {
                newRow.priceRate = passRate;
            } else if (strategy === STRATEGY_GROW_SPEED) {
                newRow.growSpeed = passRate;
            } else if (strategy === STRATEGY_WISDOM_OF_CROWD) {
                newRow.wisdomOfCrowd = passRate;
            }
            results[date] = newRow;
        }

        // map中的值写入数组
        let data = [];
        for (let k in results) {
            data.push(results[k]);
        }

        return {
            data: data
        };
    }


    // 推荐按钮按下
    onBtnSuggestClick(e) {
        // 阻止默认提交行为
        e.preventDefault();

        let strategy = this.selStrategy.value; //策略

        // 购买日期设置为今天
        let buyDate = StringUtil.dateFormat(new Date(), "yyyyMMdd");

        // 查询推荐股票并显示在table里
        if (strategy == "Fool") {
            this.foolSuggestStocks();
        } else {
            this.startSuggestStocksTask(buyDate, strategy);
        }
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

        // 插入推荐结果
        that.txtAdviceRisk.value = advice.risk;
        that.txtAdviceMessage = advice.message;
        that.txtAdviceBuyDate = advice.buyDate;
        that.txtAdviceSellDate = advice.sellDate;
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