package com.ooad.mapper;

import com.ooad.entity.Company;
import com.ooad.entity.RiskCheck;
import com.ooad.entity.RiskCheckItem;
import com.ooad.entity.RiskCheckTemplateItem;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Qizixi on 2017/6/2.
 */
@Mapper
@Component
public interface RiskCheckMapper {

    @Select("SELECT * FROM risk_check WHERE id = #{id}")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "status",column = "status"),
            @Result(property = "items",column = "id",many = @Many(select = "getRiskCheckItemsByRiskCheckId")),
            @Result(property = "company",column = "id_company",one = @One(select = "getCompanyById")),
            @Result(property = "actualFinishDate",column = "actual_finish_date"),
            @Result(property = "taskSource",column = "id_plan",one = @One(select = "getPlanName")),
            @Result(property = "startDate",column = "id_plan",one = @One(select = "getPlanStartDate")),
            @Result(property = "finishDate",column = "id_plan",one = @One(select = "getPlanFinishDate")),
    })
    RiskCheck getRiskCheckById(@Param("id") int id);

    @Select("SELECT * FROM risk_check")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "status",column = "status"),
            @Result(property = "items",column = "id",many = @Many(select = "getRiskCheckItemsByRiskCheckId")),
            @Result(property = "company",column = "id_company",one = @One(select = "getCompanyById")),
            @Result(property = "actualFinishDate",column = "actual_finish_date"),
            @Result(property = "taskSource",column = "id_plan",one = @One(select = "getPlanName")),
            @Result(property = "startDate",column = "id_plan",one = @One(select = "getPlanStartDate")),
            @Result(property = "finishDate",column = "id_plan",one = @One(select = "getPlanFinishDate")),
    })
    List<RiskCheck> getRiskChecks();

    @Insert("INSERT INTO risk_check(id_plan,id_company,actual_finish_date,status)" +
            "VALUES(#{id_plan},#{riskCheck.company.id},#{riskCheck.actualFinishDate},#{riskCheck.status})")
    @Options(useGeneratedKeys=true, keyProperty="riskCheck.id",keyColumn = "id")
    void createRiskCheck(@Param("riskCheck") RiskCheck riskCheck,@Param("id_plan") int id_plan);

    //only status can be updated
    @Update("UPDATE risk_check SET status = #{status} WHERE id = #{id}")
    void updateRiskCheck(RiskCheck riskCheck);

    @Select("SELECT * FROM risk_check WHERE id_company = #{id_company}")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "status",column = "status"),
            @Result(property = "items",column = "id",many = @Many(select = "getRiskCheckItemsByRiskCheckId")),
            @Result(property = "company",column = "id_company",one = @One(select = "getCompanyById")),
            @Result(property = "actualFinishDate",column = "actual_finish_date"),
            @Result(property = "taskSource",column = "id_plan",one = @One(select = "getPlanName")),
            @Result(property = "startDate",column = "id_plan",one = @One(select = "getPlanStartDate")),
            @Result(property = "finishDate",column = "id_plan",one = @One(select = "getPlanFinishDate")),
    })
    List<RiskCheck> getRiskCheckByCompanyId(@Param("id_company")String id_company);

    //helper
    @Select("SELECT * FROM risk_check_item WHERE id_risk_check = #{id_risk_check}")
    @Results({
            @Result(property = "item",column = "id_template_item",one = @One(select = "getRiskCheckTemplateItemById")),
            @Result(property = "finishDate", column = "finish_date")
    })
    List<RiskCheckItem> getRiskCheckItemsByRiskCheckId(@Param("id_risk_check") int id);

    @Select("SELECT * FROM risk_check_template_item WHERE id = #{id_template_item}")
    RiskCheckTemplateItem getRiskCheckTemplateItemById(@Param("id_template_item") int id_template_item);

    @Select("SELECT * FROM company WHERE id = #{id}")
    @Results({
            @Result(property = "industryType",column = "industry_type"),
            @Result(property = "contactName",column = "contact_name"),
            @Result(property = "contactTel",column = "contact_tel")
    })
    Company getCompanyById(@Param("id") String id_company);

    @Select("SELECT name FROM risk_check_plan WHERE id = #{id}")
    String getPlanName(@Param("id") int id_plan);

    @Select("SELECT start_date FROM risk_check_plan WHERE id = #{id}")
    Timestamp getPlanStartDate(@Param("id") int id_plan);

    @Select("SELECT finish_date FROM risk_check_plan WHERE id = #{id}")
    Timestamp getPlanFinishDate(@Param("id") int id_plan);
}
