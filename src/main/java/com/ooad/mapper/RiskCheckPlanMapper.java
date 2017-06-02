package com.ooad.mapper;

import com.ooad.entity.RiskCheckPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Qizixi on 2017/6/2.
 */
@Mapper
@Component
public interface RiskCheckPlanMapper {

    RiskCheckPlan getRiskCheckPlanById(@Param("id")int id);

    List<RiskCheckPlan> getRiskCheckPlans();

    void createRiskCheckPlan(RiskCheckPlan riskCheckPlan);

    void updateRiskCheckPlan(RiskCheckPlan riskCheckPlan);

    void createCompanyInPlan(@Param("id_plan") int id_plan,@Param("id_company") String id_company);

    void deleteCompanyInPlan(@Param("id_plan") int id_plan,@Param("id_company") String id_company);

    void deleteCompanyInPlanByIdPlan(@Param("id_plan") int id_plan);
}
