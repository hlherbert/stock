package com.hl.stock.core.base.data.mapper;

import com.hl.stock.core.base.model.StockMeta;
import com.hl.stock.core.base.model.StockZone;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * mybatis映射类 stock_mata表
 */
@Mapper
public interface StockMetaMapper {
    @Select("SELECT * FROM stock_meta")
    @Results({
            @Result(property = "code", column = "code"),
            @Result(property = "name", column = "name"),
            @Result(property = "zone", column = "zone", javaType = StockZone.class)
    })
    List<StockMeta> getAll();

    @Select("SELECT * FROM stock_meta WHERE code = #{code}")
    @Results({
            @Result(property = "code", column = "code"),
            @Result(property = "name", column = "name"),
            @Result(property = "zone", column = "zone", javaType = StockZone.class)
    })
    StockMeta getOne(String code);

    @Insert("INSERT INTO stock_meta(code, name, zone) VALUES(#{code}, #{name}, #{zone})")
    void insert(StockMeta stockMeta);

    @Update("UPDATE stock_meta SET name=#{name},zone=#{zone} WHERE code =#{code}")
    void update(StockMeta stockMeta);

    @Delete("DELETE FROM stock_meta WHERE code = #{code}")
    void delete(String code);
}
