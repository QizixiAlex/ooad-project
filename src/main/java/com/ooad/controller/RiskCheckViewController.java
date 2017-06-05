package com.ooad.controller;

import com.ooad.entity.RiskCheck;
import com.ooad.entity.RiskCheckPlan;
import com.ooad.service.RiskCheckViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Qizixi on 2017/6/4.
 */
@Component
public class RiskCheckViewController {

    private RiskCheckViewService riskCheckViewService;

    @Autowired
    public RiskCheckViewController(RiskCheckViewService riskCheckViewService) {
        this.riskCheckViewService = riskCheckViewService;
    }

    //view one riskcheck
    public RiskCheck getRiskCheck(int riskCheckId){
        return riskCheckViewService.getRiskCheck(riskCheckId);
    }

    //view all riskcheck plans
    public List<RiskCheckPlan> getRiskCheckPlans(){
        return riskCheckViewService.getRiskCheckPlans();
    }

    //view all riskchecks
    public List<RiskCheck> getRiskChecks(){
        return riskCheckViewService.getRiskChecks();
    }

    //view riskchecks by planid
    public List<RiskCheck> getRiskChecksByPlanId(int planId){
        return riskCheckViewService.getRiskChecksByPlanId(planId);
    }

    //view riskchecks by companyId
    public List<RiskCheck> getRiskChecksByCompanyId(String companyId){
        return riskCheckViewService.getRiskChecksByCompanyId(companyId);
    }
}
