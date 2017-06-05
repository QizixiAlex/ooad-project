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

    @Insert("INSERT INTO risk_check_item(id_risk_check,id_template_item,status) " +
            "VALUES(#{id_risk_check},#{item.item.id},#{item.status})")
    @Options(useGeneratedKeys=true, keyProperty="item.id",keyColumn = "id")
    void createRiskCheckItem(@Param("item") RiskCheckItem item,@Param("id_risk_check") int id_risk_check);

    @Update("UPDATE risk_check_item SET status = #{status}," +
            "finish_date = #{finishDate} WHERE id = #{id}")
    @Results({
            @Result(property = "finishDate", column = "finish_date")
    })
    //only status and finish date
    void updateRiskCheckItem(RiskCheckItem item);

    @Select("SELECT * FROM risk_check_item WHERE id = #{id}")
    @Results({
            @Result(property = "item",column = "id_template_item",one = @One(select = "getRiskCheckTemplateItemById")),
            @Result(property = "finishDate", column = "finish_date")
    })
    RiskCheckItem getRiskCheckItemById(@Param("id") int id);

    @Select("SELECT * FROM risk_check_item")
    @Results({
            //@Result(property = "templateItemId", column = "id_template_item"),
            @Result(property = "item",column = "id_template_item",one = @One(select = "getRiskCheckTemplateItemById")),
            @Result(property = "finishDate", column = "finish_date")
    })
    List<RiskCheckItem> getRiskCheckItems();

    //helper
    @Select("SELECT * FROM risk_check_template_item WHERE id = #{id_template_item}")
    RiskCheckTemplateItem getRiskCheckTemplateItemById(@Param("id_template_item") int id_template_item);
}