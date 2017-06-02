package com.ooad.mapper;

import com.ooad.entity.RiskCheckTemplateItem;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Shijian on 2017/6/2.
 */
@Mapper
@Component
public interface RiskCheckTemplateItemMapper {

    @Select("SELECT * FROM risk_check_template_item WHERE id = #{id}")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "name",column = "name"),
            @Result(property = "content",column = "content")
    })
    RiskCheckTemplateItem getRiskCheckTemplateItemById(@Param("id") int id);

    @Insert("INSERT INTO risk_check_template_item(name,content) VALUES " +
            "(#{name},#{content})")
    @Results({
            //@Result(property = "id",column = "id"),
            @Result(property = "name",column = "name"),
            @Result(property = "content",column = "content")
    })
    void createRiskCheckTemplateItem(RiskCheckTemplateItem riskCheckTemplateItem);

    @Update("UPDATE risk_check_template_item SET name = #{name},content = #{content}" +
            "WHERE id = #{id}")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "name",column = "name"),
            @Result(property = "content",column = "content")
    })
    void updateRiskCheckTemplateItem(RiskCheckTemplateItem riskCheckTemplateItem);

    @Select("SELECT * FROM risk_check_template_item")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "name",column = "name"),
            @Result(property = "content",column = "content")
    })
    List<RiskCheckTemplateItem> getRiskCheckTemplateItems();

}
