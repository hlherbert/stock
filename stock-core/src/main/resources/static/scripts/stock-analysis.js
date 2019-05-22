// 股票历史数据功能
import jQuery from "jquery";
import echarts from 'echarts';
import {
    StringUtil
} from './common/stringutil';

// 常量定义，只在本文件内有效

// 主题
// default vintage dark macarons infographic shine roma
const chartTheme = "default";

const STRATEGY_PRICE_RATE= "PriceRate";
const STRATEGY_GROW_SPEED= "GrowSpeed";

export class StockAnalysis {
    constructor() {
        // 数据意义：日期、priceRate策略正确率,  growSpeed策略正确率
        this.data0 = [
            ['2013/1/21', 0.1, 0.4],
            ['2013/1/22', 0.2, 0.5],
            ['2013/1/23', 0.3, 0.6]
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
                    dataView: {readonly:true},
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
                    formatter: function(value) {
                        return (value*100) + ' %';
                    }
                }
            }],
            series: [{
                    type: 'line',
                    markLine: {
                        // symbol: "none", //去掉警戒线最后面的箭头
                        // label: {
                        //     position: "start" //将警示值放在哪个位置，三个值“start”,"middle","end"  开始  中点 结束
                        // },
                        data: [
                            {
                                type: 'average',
                                name: '平均',
                                // silent: true,
                                // lineStyle: {
                                //     type: "solid",
                                //     //color: "blue",
                                //     width: 1
                                // },
                                label: {
                                    formatter:"{b}:{c}"
                                }
                            }
                        ]
                    }
                },
                {
                    type: 'line',
                    markLine: {
                        // symbol: "none", //去掉警戒线最后面的箭头
                        // label: {
                        //     position: "start" //将警示值放在哪个位置，三个值“start”,"middle","end"  开始  中点 结束
                        // },
                        data: [
                            {
                                type: 'average',
                                name: '平均',
                                // silent: true,
                                // lineStyle: {
                                //     type: "solid",
                                //     //color: "blue",
                                //     width: 1
                                // },
                                label: {
                                    formatter:"{b}:{c}"
                                }
                            }
                        ]
                    }
                }
            ],
            dataset: {
                dimensions: ['date', 'priceRate', 'growSpeed'],
                source: this.data0
            }
        };

        this.chart;
    }

    // HTML页面加载后初始化
    init(document) {
        if (document.querySelector('#doc-stock-analysis') === null) {
            return;
        }

        // 查询按钮
        this.btnStrategyValidate = document.querySelector('#btn-strategy-validate');
        this.btnStrategyValidate.addEventListener('click', this.onBtnStrategyValidateClick.bind(this));

        this.strategyChart = echarts.init(document.querySelector('#chart-strategy'), chartTheme);
        this.initChart(this.strategyChart, this.option0);
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
            chart.setOption({dataset:{
                source:dataNew.data
            }});
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

}