package com.ooad.service;

import com.ooad.entity.*;
import com.ooad.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Alex on 2017/6/4.
 */
@Component
public class RiskCheckGenerateService {

    private RiskCheckPlanMapper riskCheckPlanMapper;
    private RiskCheckTemplateMapper riskCheckTemplateMapper;
    private RiskCheckTemplateItemMapper riskCheckTemplateItemMapper;
    private RiskCheckMapper riskCheckMapper;
    private RiskCheckItemMapper riskCheckItemMapper;

    @Autowired
    public RiskCheckGenerateService(RiskCheckPlanMapper riskCheckPlanMapper, RiskCheckTemplateMapper riskCheckTemplateMapper, RiskCheckTemplateItemMapper riskCheckTemplateItemMapper, RiskCheckMapper riskCheckMapper, RiskCheckItemMapper riskCheckItemMapper) {
        this.riskCheckPlanMapper = riskCheckPlanMapper;
        this.riskCheckTemplateMapper = riskCheckTemplateMapper;
        this.riskCheckTemplateItemMapper = riskCheckTemplateItemMapper;
        this.riskCheckMapper = riskCheckMapper;
        this.riskCheckItemMapper = riskCheckItemMapper;
    }

    public void generateRiskCheck(int planId){

        RiskCheckPlan riskCheckPlan = riskCheckPlanMapper.getRiskCheckPlanById(planId);
        //check id
        if (riskCheckPlan == null){
            return;
        }
        //generate riskcheck for each company
        for (Company company:riskCheckPlan.getCompanies()){
            sendCheckListToCompany(riskCheckPlan.getTemplate(),company,planId);
        }
    }

    private void sendCheckListToCompany(RiskCheckTemplate template,Company company,int planId){
        CheckStatus currentStatus = CheckStatus.排查中;
        //create a riskcheck actualfinishdate is null
        RiskCheck riskCheck = new RiskCheck();
        riskCheck.setStatus(currentStatus);
        riskCheck.setCompany(company);
        riskCheckMapper.createRiskCheck(riskCheck,planId);
        //create riskcheckitems
        List<RiskCheckTemplateItem> templateItems = template.getItems();
        for (RiskCheckTemplateItem templateItem:templateItems){
            RiskCheckItem riskCheckItem = new RiskCheckItem();
            riskCheckItem.setItem(templateItem);
            riskCheckItem.setStatus(currentStatus);
            //finish date is null
            riskCheckItemMapper.createRiskCheckItem(riskCheckItem,riskCheck.getId());
        }
    }
}
