package com.ooad.service;

import com.ooad.entity.*;
import com.ooad.exception.EntityNotFoundException;
import com.ooad.exception.RiskCheckException;
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
    private RiskCheckMapper riskCheckMapper;
    private RiskCheckItemMapper riskCheckItemMapper;

    @Autowired
    public RiskCheckGenerateService(RiskCheckPlanMapper riskCheckPlanMapper, RiskCheckMapper riskCheckMapper, RiskCheckItemMapper riskCheckItemMapper) {
        this.riskCheckPlanMapper = riskCheckPlanMapper;
        this.riskCheckMapper = riskCheckMapper;
        this.riskCheckItemMapper = riskCheckItemMapper;
    }

    public void generateRiskCheck(int planId) throws RiskCheckException{
        //输入检查
        RiskCheckPlan riskCheckPlan = riskCheckPlanMapper.getRiskCheckPlanById(planId);
        if (riskCheckPlan == null){
            throw new EntityNotFoundException("无对应检查计划");
        }
        //分发检查计划
        for (Company company:riskCheckPlan.getCompanies()){
            sendCheckListToCompany(riskCheckPlan.getTemplate(),company,planId);
        }
    }

    private void sendCheckListToCompany(RiskCheckTemplate template,Company company,int planId){
        //创建时完成时间为空，检查状态为排查中
        CheckStatus currentStatus = CheckStatus.排查中;
        RiskCheck riskCheck = new RiskCheck();
        riskCheck.setStatus(currentStatus);
        riskCheck.setCompany(company);
        riskCheckMapper.createRiskCheck(riskCheck,planId);
        //创建检查项
        List<RiskCheckTemplateItem> templateItems = template.getItems();
        for (RiskCheckTemplateItem templateItem:templateItems){
            RiskCheckItem riskCheckItem = new RiskCheckItem();
            riskCheckItem.setItem(templateItem);
            //初始状态为排查中，完成时间为空
            riskCheckItem.setStatus(currentStatus);
            riskCheckItemMapper.createRiskCheckItem(riskCheckItem,riskCheck.getId());
        }
    }
}
