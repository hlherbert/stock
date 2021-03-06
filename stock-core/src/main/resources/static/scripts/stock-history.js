// 股票历史数据功能
import jQuery from "jquery";
import echarts from 'echarts';
import {
    StringUtil
} from './common/stringutil';

// 股票统计作为StockHistory的子模块载入
import {StockStat} from './stock-stat';

// 常量定义，只在本文件内有效
const iDayK = 0;
const iMA5 = 1;
const iMA10 = 2;
const iMA20 = 3;
const iMA30 = 4;
const iVolume = 5;

const xDayK = 0;
const xVolume = 1;

const jOpenPrice = 1;
const jClosePrice = 2;
const jLowPrice = 3;
const jHighPrice = 4;

// 主题
// default vintage dark macarons infographic shine roma
const chartTheme = "default";

export class StockHistory {
    constructor() {
        // 数据意义：开盘(open)，收盘(close)，最低(lowest)，最高(highest)
        this.data0 = this.splitData([
            ['2013/1/24', 2320.26, 2320.26, 2287.3, 2362.94],
            ['2013/1/25', 2300, 2291.3, 2288.26, 2308.38],
            ['2013/1/28', 2295.35, 2346.5, 2295.35, 2346.92],
            ['2013/1/29', 2347.22, 2358.98, 2337.35, 2363.8],
            ['2013/1/30', 2360.75, 2382.48, 2347.89, 2383.76],
            ['2013/1/31', 2383.43, 2385.42, 2371.23, 2391.82],
            ['2013/2/1', 2377.41, 2419.02, 2369.57, 2421.15],
            ['2013/2/4', 2425.92, 2428.15, 2417.58, 2440.38],
            ['2013/2/5', 2411, 2433.13, 2403.3, 2437.42],
            ['2013/2/6', 2432.68, 2434.48, 2427.7, 2441.73],
            ['2013/2/7', 2430.69, 2418.53, 2394.22, 2433.89],
            ['2013/2/8', 2416.62, 2432.4, 2414.4, 2443.03],
            ['2013/2/18', 2441.91, 2421.56, 2415.43, 2444.8],
            ['2013/2/19', 2420.26, 2382.91, 2373.53, 2427.07],
            ['2013/2/20', 2383.49, 2397.18, 2370.61, 2397.94],
            ['2013/2/21', 2378.82, 2325.95, 2309.17, 2378.82],
            ['2013/2/22', 2322.94, 2314.16, 2308.76, 2330.88],
            ['2013/2/25', 2320.62, 2325.82, 2315.01, 2338.78],
            ['2013/2/26', 2313.74, 2293.34, 2289.89, 2340.71],
            ['2013/2/27', 2297.77, 2313.22, 2292.03, 2324.63],
            ['2013/2/28', 2322.32, 2365.59, 2308.92, 2366.16],
            ['2013/3/1', 2364.54, 2359.51, 2330.86, 2369.65],
            ['2013/3/4', 2332.08, 2273.4, 2259.25, 2333.54],
            ['2013/3/5', 2274.81, 2326.31, 2270.1, 2328.14],
            ['2013/3/6', 2333.61, 2347.18, 2321.6, 2351.44],
            ['2013/3/7', 2340.44, 2324.29, 2304.27, 2352.02],
            ['2013/3/8', 2326.42, 2318.61, 2314.59, 2333.67],
            ['2013/3/11', 2314.68, 2310.59, 2296.58, 2320.96],
            ['2013/3/12', 2309.16, 2286.6, 2264.83, 2333.29],
            ['2013/3/13', 2282.17, 2263.97, 2253.25, 2286.33],
            ['2013/3/14', 2255.77, 2270.28, 2253.31, 2276.22],
            ['2013/3/15', 2269.31, 2278.4, 2250, 2312.08],
            ['2013/3/18', 2267.29, 2240.02, 2239.21, 2276.05],
            ['2013/3/19', 2244.26, 2257.43, 2232.02, 2261.31],
            ['2013/3/20', 2257.74, 2317.37, 2257.42, 2317.86],
            ['2013/3/21', 2318.21, 2324.24, 2311.6, 2330.81],
            ['2013/3/22', 2321.4, 2328.28, 2314.97, 2332],
            ['2013/3/25', 2334.74, 2326.72, 2319.91, 2344.89],
            ['2013/3/26', 2318.58, 2297.67, 2281.12, 2319.99],
            ['2013/3/27', 2299.38, 2301.26, 2289, 2323.48],
            ['2013/3/28', 2273.55, 2236.3, 2232.91, 2273.55],
            ['2013/3/29', 2238.49, 2236.62, 2228.81, 2246.87],
            ['2013/4/1', 2229.46, 2234.4, 2227.31, 2243.95],
            ['2013/4/2', 2234.9, 2227.74, 2220.44, 2253.42],
            ['2013/4/3', 2232.69, 2225.29, 2217.25, 2241.34],
            ['2013/4/8', 2196.24, 2211.59, 2180.67, 2212.59],
            ['2013/4/9', 2215.47, 2225.77, 2215.47, 2234.73],
            ['2013/4/10', 2224.93, 2226.13, 2212.56, 2233.04],
            ['2013/4/11', 2236.98, 2219.55, 2217.26, 2242.48],
            ['2013/4/12', 2218.09, 2206.78, 2204.44, 2226.26],
            ['2013/4/15', 2199.91, 2181.94, 2177.39, 2204.99],
            ['2013/4/16', 2169.63, 2194.85, 2165.78, 2196.43],
            ['2013/4/17', 2195.03, 2193.8, 2178.47, 2197.51],
            ['2013/4/18', 2181.82, 2197.6, 2175.44, 2206.03],
            ['2013/4/19', 2201.12, 2244.64, 2200.58, 2250.11],
            ['2013/4/22', 2236.4, 2242.17, 2232.26, 2245.12],
            ['2013/4/23', 2242.62, 2184.54, 2182.81, 2242.62],
            ['2013/4/24', 2187.35, 2218.32, 2184.11, 2226.12],
            ['2013/4/25', 2213.19, 2199.31, 2191.85, 2224.63],
            ['2013/4/26', 2203.89, 2177.91, 2173.86, 2210.58],
            ['2013/5/2', 2170.78, 2174.12, 2161.14, 2179.65],
            ['2013/5/3', 2179.05, 2205.5, 2179.05, 2222.81],
            ['2013/5/6', 2212.5, 2231.17, 2212.5, 2236.07],
            ['2013/5/7', 2227.86, 2235.57, 2219.44, 2240.26],
            ['2013/5/8', 2242.39, 2246.3, 2235.42, 2255.21],
            ['2013/5/9', 2246.96, 2232.97, 2221.38, 2247.86],
            ['2013/5/10', 2228.82, 2246.83, 2225.81, 2247.67],
            ['2013/5/13', 2247.68, 2241.92, 2231.36, 2250.85],
            ['2013/5/14', 2238.9, 2217.01, 2205.87, 2239.93],
            ['2013/5/15', 2217.09, 2224.8, 2213.58, 2225.19],
            ['2013/5/16', 2221.34, 2251.81, 2210.77, 2252.87],
            ['2013/5/17', 2249.81, 2282.87, 2248.41, 2288.09],
            ['2013/5/20', 2286.33, 2299.99, 2281.9, 2309.39],
            ['2013/5/21', 2297.11, 2305.11, 2290.12, 2305.3],
            ['2013/5/22', 2303.75, 2302.4, 2292.43, 2314.18],
            ['2013/5/23', 2293.81, 2275.67, 2274.1, 2304.95],
            ['2013/5/24', 2281.45, 2288.53, 2270.25, 2292.59],
            ['2013/5/27', 2286.66, 2293.08, 2283.94, 2301.7],
            ['2013/5/28', 2293.4, 2321.32, 2281.47, 2322.1],
            ['2013/5/29', 2323.54, 2324.02, 2321.17, 2334.33],
            ['2013/5/30', 2316.25, 2317.75, 2310.49, 2325.72],
            ['2013/5/31', 2320.74, 2300.59, 2299.37, 2325.53],
            ['2013/6/3', 2300.21, 2299.25, 2294.11, 2313.43],
            ['2013/6/4', 2297.1, 2272.42, 2264.76, 2297.1],
            ['2013/6/5', 2270.71, 2270.93, 2260.87, 2276.86],
            ['2013/6/6', 2264.43, 2242.11, 2240.07, 2266.69],
            ['2013/6/7', 2242.26, 2210.9, 2205.07, 2250.63],
            ['2013/6/13', 2190.1, 2148.35, 2126.22, 2190.1]
        ]);

        this.option0 = {
            title: {
                text: '上证指数',
                left: 0
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross'
                },
                formatter: function (param) {
                    let findSeriesData = function (serieses, seriesName, index) {
                        for (let i = 0; i < serieses.length; i++) {
                            if (serieses[i].seriesName === seriesName) {
                                if (!!index && !!serieses[i].data[index]) {
                                    return serieses[i].data[index];
                                } else {
                                    return serieses[i].data;
                                }
                            }
                        }
                        return "-";
                    }

                    let openPrice = findSeriesData(param, '日K', jOpenPrice);
                    let closePrice = findSeriesData(param, '日K', jClosePrice);
                    let lowPrice = findSeriesData(param, '日K', jLowPrice);
                    let highPrice = findSeriesData(param, '日K', jHighPrice);

                    let ma5Series = findSeriesData(param, 'MA5');
                    let ma10Series = findSeriesData(param, 'MA10');
                    let ma20Series = findSeriesData(param, 'MA20');
                    let ma30Series = findSeriesData(param, 'MA30');
                    let volumeSeries = findSeriesData(param, '成交量(手)');

                    return [
                        '日期: ' + param[0].name,
                        '<hr size=1 style="margin: 3px 0">',
                        '开盘价(￥): ' + openPrice + '<br/>',
                        '收盘价(￥): ' + closePrice + '<br/>',
                        '最低价(￥): ' + lowPrice + '<br/>',
                        '最高价(￥): ' + highPrice + '<br/>',
                        '成交量(手): ' + volumeSeries + '<br/>',
                        '<hr size=1 style="margin: 3px 0">',
                        'MA5: ' + ma5Series + '<br/>',
                        'MA10: ' + ma10Series + '<br/>',
                        'MA20: ' + ma20Series + '<br/>',
                        'MA30: ' + ma30Series + '<br/>'
                    ].join('');
                }
            },
            legend: {
                data: ['日K', 'MA5', 'MA10', 'MA20', 'MA30', '成交量(手)']
            },
            axisPointer: {
                link: {
                    xAxisIndex: 'all'
                },
                // label: {
                //     backgroundColor: '#777'
                // }
            },
            grid: [{
                    left: '10%',
                    right: '8%',
                    height: '50%'
                },
                {
                    left: '10%',
                    right: '8%',
                    top: '63%',
                    height: '16%'
                }
            ],
            xAxis: [{
                    type: 'category',
                    data: this.data0.categoryData,
                    scale: true,
                    boundaryGap: false,
                    axisLine: {
                        onZero: false
                    },
                    splitLine: {
                        show: false
                    },
                    splitNumber: 20,
                    min: 'dataMin',
                    max: 'dataMax'
                },
                {
                    type: 'category',
                    gridIndex: 1,
                    data: this.data0.categoryData,
                    scale: true,
                    boundaryGap: false,
                    axisLine: {
                        onZero: false
                    },
                    axisTick: {
                        show: false
                    },
                    splitLine: {
                        show: false
                    },
                    axisLabel: {
                        show: false
                    },
                    splitNumber: 20
                }
            ],
            yAxis: [{
                    scale: true,
                    splitArea: {
                        show: true
                    }
                },
                {
                    scale: true,
                    gridIndex: 1,
                    splitNumber: 2,
                    axisLabel: {
                        show: false
                    },
                    axisLine: {
                        show: false
                    },
                    axisTick: {
                        show: false
                    },
                    splitLine: {
                        show: false
                    }
                }
            ],
            dataZoom: [{
                    type: 'inside',
                    xAxisIndex: [0, 1],
                    start: 0,
                    end: 100
                },
                {
                    show: true,
                    xAxisIndex: [0, 1],
                    type: 'slider',
                    top: '85%',
                    start: 0,
                    end: 100
                }
            ],
            series: [{
                    name: '日K',
                    type: 'candlestick',
                    data: this.data0.values,
                    markLine: {
                        symbol: "none", //去掉警戒线最后面的箭头
                        label: {
                            position: "start" //将警示值放在哪个位置，三个值“start”,"middle","end"  开始  中点 结束
                        },
                        data: [{
                                name: '支撑价',
                                silent: true, //鼠标悬停事件  true没有，false有
                                lineStyle: { 
                                    //警戒线的样式  ，虚实  颜色
                                    type: "solid",
                                    color: "green",
                                    width: 1
                                },
                                label: {
                                    // 标题 值
                                    formatter:"{b}:{c}"
                                },
                                yAxis: 0 // 警戒线的标注值，可以有多个yAxis,多条警示线   或者采用   {type : 'average', name: '平均值'}，type值有  max  min  average，分为最大，最小，平均值
                            },
                            {
                                type: 'average',
                                name: '平均',
                                silent: true,
                                lineStyle: {
                                    type: "solid",
                                    color: "blue",
                                    width: 1
                                },
                                label: {
                                    formatter:"{b}:{c}"
                                }
                            },
                            {
                                type: 'min',
                                name: '最低',
                                silent: true,
                                lineStyle: {
                                    type: "solid",
                                    color: "orange",
                                    width: 1
                                },
                                label: {
                                    formatter:"{b}:{c}"
                                }
                            },
                            {
                                type: 'max',
                                name: '最高',
                                silent: true,
                                lineStyle: {
                                    type: "solid",
                                    color: "orange",
                                    width: 1
                                },
                                label: {
                                    formatter:"{b}:{c}"
                                }
                            }
                        ]
                    }
                },
                {
                    name: 'MA5',
                    type: 'line',
                    data: this.calculateMA(5, this.data0),
                    smooth: true,
                    lineStyle: {
                        normal: {
                            opacity: 0.5
                        }
                    }
                },
                {
                    name: 'MA10',
                    type: 'line',
                    data: this.calculateMA(10, this.data0),
                    smooth: true,
                    lineStyle: {
                        normal: {
                            opacity: 0.5
                        }
                    }
                },
                {
                    name: 'MA20',
                    type: 'line',
                    data: this.calculateMA(20, this.data0),
                    smooth: true,
                    lineStyle: {
                        normal: {
                            opacity: 0.5
                        }
                    }
                },
                {
                    name: 'MA30',
                    type: 'line',
                    data: this.calculateMA(30, this.data0),
                    smooth: true,
                    lineStyle: {
                        normal: {
                            opacity: 0.5
                        }
                    }
                },
                {
                    name: '成交量(手)',
                    type: 'bar',
                    xAxisIndex: 1,
                    yAxisIndex: 1,
                    data: null
                }
            ]
        };

        this.stockmetas = this.queryStockMetas();
        this.codelist;
        this.inputSelectCode;
        this.inputCode;
        this.inputStart;
        this.inputEnd;
        this.inputName;
        this.btnQuery;
        this.chartHistory;

        this.stockStat = new StockStat();
    }

    // HTML页面加载后初始化
    init(document) {
        //注意：代码中的回调函数都要用callback.bind(this), 避免回调里面的this不是类对象而是调用者

        if (document.querySelector('#doc-stock-history') === null) {
            return;
        }
        // 股票代码
        this.codelist = document.querySelector('#code-list'); //输入框模式
        //this.codelist = document.querySelector('#input-select-code'); //下拉框模式
        this.inputSelectCode = document.querySelector('#input-select-code');
        this.inputSelectCode.addEventListener('change', this.onCodeChange.bind(this));
        this.inputSelectCode.addEventListener('input', this.onCodeChange.bind(this));
        this.inputCode = document.querySelector('#input-code');

        // 起始时间 结束时间
        this.inputStart = document.querySelector('#input-start');
        this.inputEnd = document.querySelector('#input-end');
        this.inputStart.addEventListener('input', this.onStartEndChange.bind(this));
        this.inputEnd.addEventListener('input', this.onStartEndChange.bind(this));

        // 股票名称
        this.inputName = document.querySelector('#input-name');

        // 查询按钮
        this.btnQuery = document.querySelector('#btn-query');
        this.btnQuery.addEventListener('click', this.onBtnQueryClick.bind(this));

        // 图表
        this.chartHistory = echarts.init(document.querySelector('#chart-history'), chartTheme);
        this.initCodeOptions(this.codelist, this.stockmetas);
        this.initChart(this.chartHistory, this.option0);

        this.stockStat.init(document,this.chartHistory);
    }

    // 股票代码改变
    onCodeChange() {
        // 验证表单
        if (!this.validateForm()) {
            return;
        }

        // 更新股票名称
        let code = this.inputSelectCode.value;
        this.inputCode.value = code;
        this.updateStockName(this.inputName, code);
    }

    // 时间改变
    onStartEndChange() {
        // 验证表单
        this.validateForm();
    }

    // 查询按钮按下
    onBtnQueryClick(e) {
        // 阻止默认提交行为
        e.preventDefault();

        // 验证表单
        if (!this.validateForm()) {
            return;
        }

        let code = this.inputCode.value;
        let start = this.inputStart.value;
        let end = this.inputEnd.value;
        // 查询股票代码的历史并显示在echart里
        this.updateStockHistoryChart(this.chartHistory, code, start, end);
    }

    // 验证表单
    validateForm() {
        let v1 = this.validate(this.inputSelectCode, document.querySelector('#code-error'), "股票代码非法! 000001");
        let v2 = this.validate(this.inputStart, document.querySelector('#start-error'), "日期格式非法! 20000101");
        let v3 = this.validate(this.inputEnd, document.querySelector('#end-error'), "日期格式非法! 20300101");
        let v = v1 && v2 && v3;
        if (v) {
            this.btnQuery.removeAttribute('disabled');
        } else {
            this.btnQuery.setAttribute('disabled', true);
        }
        return v;
    }

    // 通用验证函数
    validate(input, error, errMsg) {
        // 当用户输入信息时，验证 stock code 字段
        if (input.validity.valid) {
            // 如果验证通过，清除已显示的错误消息
            error.innerHTML = ""; // 重置消息的内容
            error.className = "error"; // 重置消息的显示状态
            return true;
        } else {
            // 如果验证失败，显示一个自定义错误
            error.innerHTML = errMsg;
            error.className = "error active";
            return false;
        }
    }

    // 查股票元数据
    queryStockMetas() {
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
        return ret;
    }

    // 查询股票名
    queryStockName(code) {
        let metas = this.stockmetas;
        if (!!metas && metas.length > 0) {
            let meta = null;
            for (let i = 0; i < metas.length; i++) {
                if (metas[i].code === code) {
                    meta = metas[i];
                    break;
                }
            }
            if (meta !== null) {
                return meta.name;
            }
        }
        return null;
    }

    // 查询历史数据
    queryStockHistory(code, start, end, callback) {
        //let url = "/stock/data?code=603999&start=20100101&end=20190508";
        let url = StringUtil.stringFormat('/stock/data?code={0}&start={1}&end={2}', code, start, end);
        jQuery.get(url)
            .done(function (stockdata) {
                callback(stockdata);
            })
            .fail(function () {
                callback(null);
            });
    }

    // 初始化股票代码下拉框选项
    initCodeOptions(codelist, stockmetas) {
        codelist.innerHTML = "";
        for (let i = 0; i < stockmetas.length; i++) {
            let meta = stockmetas[i];
            let strOption = StringUtil.stringFormat('{0}({1})', meta.code, meta.name);
            let option = document.createElement("Option");
            option.setAttribute("value", meta.code);
            option.innerHTML = strOption;
            codelist.appendChild(option);
            //jQuery("#code-list").append("<option value='" + meta.code + "'>" + strOption + "</option>");
        }
    }

    // 显示标题，图例和空的坐标轴
    initChart(chart, option) {
        chart.setOption(option);
    }

    // 更新股票名称
    updateStockName(input, code) {
        input.value = this.queryStockName(code);
    }

    // 更新图表数据
    updateStockHistoryChart(chart, code, start, end) {
        let callback = function (stockdata) {
            if (stockdata === null) {
                return;
            }

            let optionNew = chart.getOption(); //this.option0; 避免每次把markline覆盖了
            // 更新标题
            let stockName = this.queryStockName(code);
            optionNew.title.text = StringUtil.stringFormat('代码：{0} 名称：{1}', code, stockName)

            // 填入数据
            let dataNew = this.extractStockData(stockdata);
            optionNew.xAxis[xDayK].data = dataNew.categoryData;
            optionNew.xAxis[xVolume].data = dataNew.categoryData;

            optionNew.series[iDayK].data = dataNew.values;
            optionNew.series[iMA5].data = this.calculateMA(5, dataNew);
            optionNew.series[iMA10].data = this.calculateMA(10, dataNew);
            optionNew.series[iMA20].data = this.calculateMA(20, dataNew);
            optionNew.series[iMA30].data = this.calculateMA(30, dataNew);
            optionNew.series[iVolume].data = dataNew.volumes;
            chart.setOption(optionNew);
        }

        this.queryStockHistory(code, start, end, callback.bind(this));
    }

    splitData(rawData) {
        let categoryData = [];
        let values = [];
        for (let i = 0; i < rawData.length; i++) {
            let date = rawData[i].splice(0, 1)[0];
            categoryData.push(date);
            values.push(rawData[i])
        }
        return {
            categoryData: categoryData,
            values: values,
            volumes: null
        };
    }

    extractStockData(stockdata) {
        let categoryData = [];
        let values = [];
        let volumes = [];
        for (let i = 0; i < stockdata.length; i++) {
            let row = stockdata[i];
            let dateObj = new Date();
            dateObj.setTime(Date.parse(row.date));
            let date = StringUtil.dateFormat(dateObj, "yyyy/MM/dd");
            let openPrice = row.openPrice;
            let closePrice = row.closePrice;
            let lowPrice = row.lowPrice;
            let highPrice = row.highPrice;

            let amount = row.amount; //成交量（手）
            // let code = row.code;
            // let exchangePercent = row.exchangePercent;
            // let amountMoney = row.amountMoney; //成交金额（万）
            // let growPercent = row.growPercent;
            // let growPrice = row.growPrice;

            // 每日价格
            categoryData.push(date);
            let rawData = [openPrice, closePrice, lowPrice, highPrice];
            values.push(rawData)

            // 成交量
            volumes.push(amount);
        }
        return {
            categoryData: categoryData,
            values: values,
            volumes: volumes
        };
    }

    calculateMA(dayCount, data) {
        var result = [];
        for (var i = 0, len = data.values.length; i < len; i++) {
            if (i < dayCount) {
                result.push('-');
                continue;
            }
            var sum = 0;
            for (var j = 0; j < dayCount; j++) {
                sum += data.values[i - j][jClosePrice];
            }
            result.push(sum / dayCount);
        }
        return result;
    }
}