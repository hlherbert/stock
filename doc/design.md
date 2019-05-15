# stock程序设计

## 包依赖
```
model--data 
        |--download
        |--analysis
```

## 数据模型模块
model
定义数据模型

* StockZone 交易所
```
enum StockZone {
    SHANGHAI,
    SHENZHEN
}
```

* StockMeta 股票元数据
```
code: String 代码
name: String 名称
zone: StockZone 交易所
```


* StockData 股票数据模型


```
date: Date      日期
code: String    代码
openPrice: double   开盘
closePrice: double  收盘
growPrice: double   涨跌额
growPercent: double 涨跌幅
lowPrice: double    最低
highPrice: double   最高
amount: double      成交量(手)
amountMoney: double 成交金额(万)
exchangePercent: double 换手率
```

## 数据获取模块
download
从网上获取数据

* StockDownloader 下载器
```
class StockDownloader {
    StockData[] downloadHistory(code, startDate, endDate) 下载股票历史数据
    StockMeta[] downloadMeta() 下载所有股票元数据
}
```
* StockDownloadSaver 下载保存器(下载数据并保存到数据库中)
```
class StockDownloadSave {
    downloadSaveHistory(String code, Date startDate, Date endDate)
    downloadSaveMeta()
    downloadAllHistoryData()    下载所有股票历史数据
    complementAllHistoryData()  补录所有股票历史数据
}
```

## 数据存储模块
data
保存数据、读取数据

* StockDao 股票数据存储操作对象
```
class StockDao {
    saveData(stockData);
    saveMeta(StockMeta stockMeta);

    StockData[] loadData(code, startDate, endDate)
    StockMeta[] loadMeta();
}
```


## 分析模块
analysis

### 计算平均价格
```
class StockAnalysis {
    avgPrice(StockData[] data)
}
```
avgPrice = sum(成交金额)/sum(成交量)


模拟指定在某日buyDay购入某股stockCode，另一日sellDay卖出，计算平均每日收益benifitDay。
指定选股策略，使用策略在历史数据中模拟交易，给策略评分。
找出最优策略。

## 仿真模块
emulation
实验性的各种策略交易，评估效果


