package com.ooad.mapper;

import com.ooad.entity.RiskCheckItem;
import com.ooad.entity.RiskCheckTemplateItem;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Qizixi on 2017/6/1.
 */
@Mapper
@Component
public interface RiskCheckItemMapper {

    @Insert("INSERT INTO risk_check_item(id_template_item,status,finish_date) " +
            "VALUES(#{item},#{riskCheckId},#{status},#{finishDate})")
    @Results({
//            @Result(property = "templateItemId", column = "id_template_item"),
            @Result(property = "item",column = "id_template_item",one = @One(select = "getRiskCheckTemplateItemById")),
            @Result(property = "riskCheckId", column = "id_risk_check"),
            @Result(property = "finishDate", column = "finishDate")
    })
    void createRiskCheckItem(RiskCheckItem item);

    @Select("SELECT * FROM risk_check_template_item WHERE id = #{id_template_item}")
    RiskCheckTemplateItem getRiskCheckTemplateItemById(@Param("id_template_item") int id_template_item);

    @Update("UPDATE risk_check_item SET id_template_item = #{item},id_risk_check = #{riskCheckId},status = #{status}," +
            "finish_date = #{finishDate} WHERE id = #{id}")
    @Results({
            //@Result(property = "templateItemId", column = "id_template_item"),
            @Result(property = "item",column = "id_template_item",one = @One(select = "getRiskCheckTemplateItemById")),
            @Result(property = "riskCheckId", column = "id_risk_check"),
            @Result(property = "finishDate", column = "finishDate")
    })
    //only status and finish date
    void updateRiskCheckItem(RiskCheckItem item);

    @Select("SELECT * FROM risk_check_item WHERE id = #{id}")
    @Results({
            //@Result(property = "templateItemId", column = "id_template_item"),
            @Result(property = "item",column = "id_template_item",one = @One(select = "getRiskCheckTemplateItemById")),
            @Result(property = "riskCheckId", column = "id_risk_check"),
            @Result(property = "finishDate", column = "finishDate")
    })
    RiskCheckItem getRiskCheckItemById(@Param("id") int id);

    @Select("SELECT * FROM risk_check_item")
    @Results({
            //@Result(property = "templateItemId", column = "id_template_item"),
            @Result(property = "item",column = "id_template_item",one = @One(select = "getRiskCheckTemplateItemById")),
            @Result(property = "riskCheckId", column = "id_risk_check"),
            @Result(property = "finishDate", column = "finishDate")
    })
    List<RiskCheckItem> getRiskCheckItems();
}