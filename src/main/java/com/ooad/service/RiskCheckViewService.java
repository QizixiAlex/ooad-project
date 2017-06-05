package com.ooad.service;

import com.ooad.entity.RiskCheck;
import com.ooad.entity.RiskCheckPlan;
import com.ooad.mapper.RiskCheckMapper;
import com.ooad.mapper.RiskCheckPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Alex on 2017/6/4.
 */
@Component
public class RiskCheckViewService {

    private RiskCheckPlanMapper riskCheckPlanMapper;
    private RiskCheckMapper riskCheckMapper;

    @Autowired
    public RiskCheckViewService(RiskCheckPlanMapper riskCheckPlanMapper, RiskCheckMapper riskCheckMapper) {
        this.riskCheckPlanMapper = riskCheckPlanMapper;
        this.riskCheckMapper = riskCheckMapper;
    }

    public List<RiskCheckPlan> getRiskCheckPlans(){
        return riskCheckPlanMapper.getRiskCheckPlans();
    }

    public List<RiskCheck> getRiskChecks(){
        return riskCheckMapper.getRiskChecks();
    }

    public List<RiskCheck> getRiskChecksByPlanId(int planId){
        return riskCheckMapper.getRiskCheckByPlanId(planId);
    }

    public List<RiskCheck> getRiskChecksByCompanyId(String companyId){
        return riskCheckMapper.getRiskCheckByCompanyId(companyId);
    }

    public RiskCheck getRiskCheck(int riskCheckId){
        return riskCheckMapper.getRiskCheckById(riskCheckId);
    }
}
