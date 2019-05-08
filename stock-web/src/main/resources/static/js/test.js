var myChart = echarts.init(document.getElementById('main'));
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
var url = "localhost:8080/stock/data?code=603999&start=20100101&end=20190508";
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
