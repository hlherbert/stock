package com.hl.stock.core.base.data.mapper;

import com.hl.stock.core.base.model.StockDataUpdate;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * mybatis映射类 stock_data_update表
 * 记录各个股票数据更新的日期
 */
@Mapper
public interface StockDataUpdateMapper {
    @Select("SELECT * FROM stock_data_update ORDER BY code ASC")
    @Results({
            @Result(property = "code", column = "code"),
            @Result(property = "updateDate", column = "updateDate", javaType = Date.class)
    })
    List<StockDataUpdate> getAll();

    @Select("SELECT * FROM stock_data_update WHERE code = #{code}")
    @Results({
            @Result(property = "code", column = "code"),
            @Result(property = "updateDate", column = "updateDate", javaType = Date.class)
    })
    StockDataUpdate getOne(String code);

    // 避免重复插入 insert ignore into
    @Insert("INSERT IGNORE INTO stock_data_update(code, updateDate) VALUES (#{code}, #{updateDate})")
    void insert(StockDataUpdate stockDataUpdate);

    @Insert("<script>" +
            "INSERT IGNORE INTO stock_data_update(code, updateDate) VALUES " +
            "   <foreach collection='stockDataUpdates' item='item' index='index' separator=','>" +
            "       (#{item.code}, #{item.updateDate})" +
            "   </foreach>" +
            "</script>")
    void insertBatch(@Param("stockDataUpdates") List<StockDataUpdate> stockDataUpdates);


    @Update("UPDATE stock_data_update SET updateDate=#{updateDate} WHERE code =#{code}")
    void update(StockDataUpdate stockDataUpdate);

    @Delete("DELETE FROM stock_data_update WHERE code = #{code}")
    void delete(String code);
}
