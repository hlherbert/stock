# stock程序设计

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
    StockData[] download(code, startDate, endDate)
    String[] downCodeList()
}
```


## 数据存储模块
data
保存数据、读取数据

* StockDao 股票数据存储操作对象
```
class StockDao {
    save(stockData)
    StockData[] load(code, startDate, endDate)
}
```


## 查询模块
query
定义筛选条件，筛选查询符合条件的股票。

* interface Condition 条件
```
interface Condition {
    boolean match(Object src) 是否符合条件
}
```

* RangeCondition<T> implement Condition 范围条件
```
class RangeCondition<T> implement Condition {
    T a; // 最小值
    T b; // 最大值
}

如果a==null, b==null, 不限制
如果a!=null, b!=null, a<=x<=b,
如果a==null, b!=null, x<=b
如果a!=null, b==null, x>=a
```

* interface Query 查询
```
interface Query {
    List<Object> query(Collection<Object> data)
}
```

* abstract AbstractQuery implements Query 抽象查询
```
abstract class AbstractQuery implements Query {
    Map<String, Condition> conditionMap;

    void put(field, condition);
    Condition get(field);
    void remove(field);
    List<Condition> getConditions();
    List<Object> query(Collection<Object> data);
}

```

* StockQuery extends AbstractQuery



## 仿真模块
emulation
模拟指定在某日buyDay购入某股stockCode，另一日sellDay卖出，计算平均每日收益benifitDay。



## 分析模块
analysis
指定选股策略，使用策略在历史数据中模拟交易，给策略评分。
找出最优策略。