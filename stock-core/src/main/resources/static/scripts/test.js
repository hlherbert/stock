import {echarts} from 'echarts';
import {jQuery} from 'jquery-3.4.1'

let myChart = echarts.init(document.querySelector('#chart'));
// 显示标题，图例和空的坐标轴
myChart.setOption({
    title: {
        text: '异步数据加载示例'
    },
    tooltip: {},
    legend: {
        data:['销量']
    },
    xAxis: {
        data: []
    },
    yAxis: {},
    series: [{
        name: '销量',
        type: 'bar',
        data: []
    }]
});

// 异步加载数据
let url = "localhost:8081/stock/data?code=603999&start=20100101&end=20190508";
jQuery.get('data.json').done(function (data) {
    // 填入数据
    myChart.setOption({
        xAxis: {
            data: data.date
        },
        series: [{
            // 根据名字对应到相应的系列
            name: '销量',
            data: data.openPrice
        }]
    });
});
