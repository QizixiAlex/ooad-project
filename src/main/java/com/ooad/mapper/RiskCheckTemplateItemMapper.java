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

    //根据id查询对应的item
    @Select(" SELECT *" +
            " FROM risk_check_template_item" +
            " WHERE id = #{id}")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "name",column = "name"),
            @Result(property = "content",column = "content")
    })
    RiskCheckTemplateItem getRiskCheckTemplateItemById(@Param("id") int id);

    //查询所有item
    @Select(" SELECT *" +
            " FROM risk_check_template_item")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "name",column = "name"),
            @Result(property = "content",column = "content")
    })
    List<RiskCheckTemplateItem> getRiskCheckTemplateItems();

    //插入新的item，name与content指定，id自动产生
    @Insert(" INSERT INTO risk_check_template_item(name,content)" +
            " VALUES (#{name},#{content})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void createRiskCheckTemplateItem(RiskCheckTemplateItem riskCheckTemplateItem);

    //根据id，更新name与content
    @Update(" UPDATE risk_check_template_item" +
            " SET name = #{name},content = #{content}" +
            " WHERE id = #{id}")
    void updateRiskCheckTemplateItem(RiskCheckTemplateItem riskCheckTemplateItem);

    //根据id，删除item
    @Delete(" DELETE FROM risk_check_template_item" +
            " WHERE id = #{id}")
    void deleteRiskCheckTemplateItem(RiskCheckTemplateItem riskCheckTemplateItem);
}
