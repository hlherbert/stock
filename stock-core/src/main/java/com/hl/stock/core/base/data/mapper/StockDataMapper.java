package com.hl.stock.core.base.data.mapper;

import com.hl.stock.core.base.model.StockData;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * mybatis映射类 stock_data表 股票天数据
 */
@Mapper
public interface StockDataMapper {
    @Select("SELECT * FROM stock_data ORDER BY code ASC, date ASC")
    @Results({
            @Result(property = "date", column = "date", javaType = Date.class),
            @Result(property = "code", column = "code"),
            @Result(property = "openPrice", column = "openPrice"),
            @Result(property = "closePrice", column = "closePrice"),
            @Result(property = "growPrice", column = "growPrice"),
            @Result(property = "growPercent", column = "growPercent"),
            @Result(property = "lowPrice", column = "lowPrice"),
            @Result(property = "highPrice", column = "highPrice"),
            @Result(property = "amount", column = "amount"),
            @Result(property = "amountMoney", column = "amountMoney"),
            @Result(property = "exchangePercent", column = "exchangePercent")
    })
    List<StockData> getAll();

    @Select("SELECT * FROM stock_data WHERE code = #{code} ORDER BY date ASC")
    @Results
    List<StockData> getSeries(String code);

    @Select("SELECT * FROM stock_data WHERE code = #{code} AND date >= #{startDate} AND date < #{endDate} ORDER BY date ASC")
    @Results
    List<StockData> getSeriesByTime(@Param("code") String code, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Select("SELECT * FROM stock_data WHERE code = #{code} AND date = #{date}")
    @Results
    StockData getPoint(String code, Date date);

    @Select("SELECT EXISTS(SELECT 1 FROM stock_data WHERE code=#{code})")
    @Results
    boolean hasSeries(String code);

    @Select("SELECT date FROM stock_data WHERE code=#{code} ORDER BY date DESC LIMIT 1")
    @Results
    Date lastDateOfSeries(String code);

    // 避免重复插入 insert ignore into
    @Insert("INSERT IGNORE INTO " +
            " stock_data(date, code, openPrice, closePrice, growPrice, growPercent, lowPrice, highPrice, amount, amountMoney, exchangePercent)" +
            " VALUES " +
            " (#{date}, #{code}, #{openPrice}, #{closePrice}, #{growPrice}, #{growPercent}, #{lowPrice}, #{highPrice}, #{amount}, #{amountMoney}, #{exchangePercent})")
    void insert(StockData stockData);

    @Insert("<script>" +
            "INSERT IGNORE INTO " +
            " stock_data(date, code, openPrice, closePrice, growPrice, growPercent, lowPrice, highPrice, amount, amountMoney, exchangePercent) " +
            " VALUES " +
            "   <foreach collection='stockDatas' item='x' index='index' separator=','>" +
            "   (#{x.date}, #{x.code}, #{x.openPrice}, #{x.closePrice}, #{x.growPrice}, #{x.growPercent}, #{x.lowPrice}, #{x.highPrice}, #{x.amount}, #{x.amountMoney}, #{x.exchangePercent})" +
            "   </foreach>" +
            "</script>")
    void insertBatch(@Param("stockDatas") List<StockData> stockDatas);

    @Update("UPDATE stock_data SET" +
            " openPrice=#{openPrice}, closePrice=#{closePrice}, growPrice=#{growPrice}, growPercent=#{growPercent}," +
            " lowPrice=#{lowPrice}, highPrice=#{highPrice}, amount=#{amount}, amountMoney=#{amountMoney}, exchangePercent=#{exchangePercent} " +
            " WHERE code = #{code} AND date = #{date}")
    void update(StockData stockData);

    @Delete("DELETE FROM stock_data WHERE code = #{code}")
    void deleteSeries(String code);

    @Delete("DELETE FROM stock_data WHERE code = #{code} AND date = #{date}")
    void deletePoint(String code, Date date);

    @Delete("DELETE FROM stock_data WHERE date > now()")
    void washData();
}
