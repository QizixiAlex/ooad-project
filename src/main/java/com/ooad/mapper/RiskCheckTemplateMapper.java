package com.ooad.mapper;

import com.ooad.entity.RiskCheckTemplateItem;
import com.ooad.entity.RiskCheckTemplate;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Shijian on 2017/6/2.
 */
@Mapper
@Component
public interface RiskCheckTemplateMapper {

    //子查询：根据template.id查询对应的所有item
    @Select(" SELECT risk_check_template_item.* " +
            " FROM item_in_template, risk_check_template_item " +
            " WHERE item_in_template.id_template = #{id_template}" +
            "       AND item_in_template.id_template_item = risk_check_template_item.id")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "name",column = "name"),
            @Result(property = "content",column = "content")
    })
    List<RiskCheckTemplateItem> getRiskCheckTemplateItemsByTemplateId(@Param("id_template") int id_template);

    //根据id查询一个template（以及包含的所有item）
    @Select(" SELECT *" +
            " FROM risk_check_template" +
            " WHERE id = #{id}")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "name",column = "name"),
            @Result(property = "description",column = "description"),
            @Result(property = "items",column = "id",many = @Many(select="getRiskCheckTemplateItemsByTemplateId"))
            //这里我们使用了@Many注解的select属性来指向一个完全限定名方法，
            //该方法将返回一个List<>对象。使用column="id"，template数据表中的"id"列值将会作为输入参数传递给指定的方法。
    })
    RiskCheckTemplate getRiskCheckTemplateById(@Param("id") int id);

    //返回所有template
    @Select(" SELECT *" +
            " FROM risk_check_template")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "name",column = "name"),
            @Result(property = "description",column = "description"),
            @Result(property = "items",column = "id",many = @Many(select="getRiskCheckTemplateItemsByTemplateId"))
            //这里我们使用了@Many注解的select属性来指向一个完全限定名方法，
            //该方法将返回一个List<>对象。使用column="id"，template数据表中的"id"列值将会作为输入参数传递给指定的方法。
    })
    List<RiskCheckTemplate> getRiskCheckTemplates();

    //插入一个template，指定name与description，自动生成id
    @Insert(" INSERT INTO risk_check_template(name,description)" +
            " VALUES (#{name},#{description})")
    void createRiskCheckTemplate(RiskCheckTemplate riskCheckTemplate);

    //更新一个template，根据id，写入新的name与description
    @Update(" UPDATE risk_check_template" +
            " SET name = #{name},description = #{description}" +
            " WHERE id = #{id}")
    void updateRiskCheckTemplate(RiskCheckTemplate riskCheckTemplate);

    //
    void createItemInTemplate(@Param("id_template")int id_template,@Param("id_template_item") int id_template_item);

    void deleteItemInTemplate(@Param("id_template")int id_template,@Param("id_template_item") int id_template_item);

    void deleteItemInTemplateByIdTemplate(@Param("id_template")int id_template);
}
