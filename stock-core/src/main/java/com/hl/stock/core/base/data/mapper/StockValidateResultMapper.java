package com.hl.stock.core.base.data.mapper;

import com.hl.stock.core.base.analysis.validate.StockValidateResult;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * mybatis映射类 stock_meta表
 */
@Mapper
public interface StockValidateResultMapper {
    @Select("SELECT strategy, date, total, passed FROM stock_validate_result ORDER BY strategy ASC, date ASC")
    @Results({
            @Result(property = "strategy", column = "strategy"),
            @Result(property = "date", column = "date", javaType = Date.class),
            @Result(property = "total", column = "total"),
            @Result(property = "passed", column = "passed")
    })
    List<StockValidateResult> getAll();

    @Select("SELECT strategy, date, total, passed FROM stock_validate_result WHERE strategy = #{strategy} ORDER BY date ASC")
    @Results
    List<StockValidateResult> getSeries(String strategy);

    @Select("SELECT strategy, date, total, passed FROM stock_validate_result WHERE strategy = #{strategy} AND date >= #{startDate} AND date < #{endDate} ORDER BY date ASC")
    @Results
    List<StockValidateResult> getSeriesByTime(@Param("strategy") String strategy, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Select("SELECT strategy, date, total, passed FROM stock_validate_result WHERE strategy = #{strategy} AND date = #{date}")
    @Results({
            @Result(property = "strategy", column = "strategy"),
            @Result(property = "date", column = "date", javaType = Date.class),
            @Result(property = "total", column = "total"),
            @Result(property = "passed", column = "passed")
    })
    StockValidateResult getPoint(@Param("strategy") String strategy, @Param("date") Date date);

    @Select("SELECT EXISTS(SELECT 1 FROM stock_validate_result WHERE strategy=#{strategy})")
    @Results
    boolean hasSeries(String strategy);

    // 避免重复插入 insert ignore into
    @Insert("INSERT IGNORE INTO " +
            " stock_validate_result(date, strategy, total, passed)" +
            " VALUES " +
            " (#{date}, #{strategy}, #{total}, #{passed})")
    void insert(StockValidateResult stockValidateResult);

    @Insert("<script>" +
            "INSERT IGNORE INTO " +
            " stock_validate_result(date, strategy, total, passed)" +
            " VALUES " +
            "   <foreach collection='stockValidateResults' item='x' index='index' separator=','>" +
            "   (#{x.date}, #{x.strategy}, #{x.total}, #{x.passed})" +
            "   </foreach>" +
            "</script>")
    void insertBatch(@Param("stockValidateResults") List<StockValidateResult> stockValidateResults);

    @Update("UPDATE stock_validate_result SET" +
            " total=#{total}, passed=#{passed}" +
            " WHERE strategy = #{strategy} AND date = #{date}")
    void update(StockValidateResult stockValidateResult);

    @Delete("DELETE FROM stock_validate_result WHERE strategy = #{strategy}")
    void deleteSeries(String strategy);

    @Delete("DELETE FROM stock_validate_result WHERE strategy = #{strategy} AND date = #{date}")
    void deletePoint(String strategy, Date date);

}
