package com.ooad.mapper;

import com.ooad.entity.Company;
import com.ooad.entity.RiskCheckPlan;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Qizixi on 2017/6/2.
 */
@Mapper
@Component
public interface RiskCheckPlanMapper {

    //子查询：根据id_plan查询对应的所有company
    @Select(" SELECT company.*" +
            " FROM company_in_plan, company" +
            " WHERE company_in_plan.id_plan = #{id_plan}" +
            "       AND company_in_plan.id_company = company.id")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "name",column = "name"),
            @Result(property = "status",column = "status"),
            @Result(property = "code",column = "code"),
            @Result(property = "industryType",column = "industry_type"),
            @Result(property = "trade",column = "trade"),
            @Result(property = "contactName",column = "contact_name"),
            @Result(property = "contactTel",column = "contact_tel")
    })
    List<Company> getCompaniesByPlanId(@Param("id_plan") int id_plan);

    //根据id查询一个plan（以及包含的所有company与一个template)
    @Select(" SELECT *" +
            " FROM risk_check_plan" +
            " WHERE id = #{id}")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "name",column = "name"),
            @Result(property = "startDate",column = "start_date"),
            @Result(property = "finishDate",column = "finish_date"),
            @Result(property = "template",column = "id_template",one = @One(select = "com.ooad.mapper.RiskCheckTemplateMapper.getRiskCheckTemplateById")),
            @Result(property = "companies",column = "id",many = @Many(select="getCompaniesByPlanId"))
    })
    RiskCheckPlan getRiskCheckPlanById(@Param("id")int id);

    //返回所有plan
    @Select(" SELECT *" +
            " FROM risk_check_plan")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "name",column = "name"),
            @Result(property = "startDate",column = "start_date"),
            @Result(property = "finishDate",column = "finish_date"),
            @Result(property = "template",column = "id_template",one = @One(select = "com.ooad.mapper.RiskCheckTemplateMapper.getRiskCheckTemplateById")),
            @Result(property = "companies",column = "id",many = @Many(select="getCompaniesByPlanId"))
    })
    List<RiskCheckPlan> getRiskCheckPlans();

    //插入一个plan，指定name,start_date,finish_date,id_template，自动生成id
    @Insert(" INSERT INTO risk_check_plan(name,start_date,finish_date,id_template)" +
            " VALUES (#{name},#{startDate},#{finishDate},#{template.id})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void createRiskCheckPlan(RiskCheckPlan riskCheckPlan);

    //更新一个plan，根据id，写入新的name,start_date,finish_date,id_template
    @Update(" UPDATE risk_check_plan" +
            " SET name = #{name}," +
            "     start_date = #{startDate}," +
            "     finish_date = #{finishDate}," +
            "     id_template = #{template.id}" +
            " WHERE id = #{id}")
    void updateRiskCheckPlan(RiskCheckPlan riskCheckPlan);

    //为特定id_plan计划关联一个特定id_company公司
    @Insert(" INSERT INTO company_in_plan(id_plan,id_company)" +
            " VALUES (#{id_plan},#{id_company})")
    void createCompanyInPlan(@Param("id_plan") int id_plan,@Param("id_company") String id_company);

    //为特定id_plan计划取消关联一个特定id_company公司
    @Delete(" DELETE FROM company_in_plan" +
            " WHERE id_plan = #{id_plan}" +
            "       AND id_company = #{id_company}")
    void deleteCompanyInPlan(@Param("id_plan") int id_plan,@Param("id_company") String id_company);

    //为特定id_plan计划取消关联所有公司
    @Delete(" DELETE FROM company_in_plan" +
            " WHERE id_plan = #{id_plan}")
    void deleteCompanyInPlanByIdPlan(@Param("id_plan") int id_plan);

}
