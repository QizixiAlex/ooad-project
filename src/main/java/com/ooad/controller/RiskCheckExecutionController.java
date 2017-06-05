package com.ooad.controller;

import com.ooad.entity.CheckStatus;
import com.ooad.entity.RiskCheck;
import com.ooad.entity.RiskCheckItem;
import com.ooad.service.CompanyService;
import com.ooad.service.RiskCheckExecuteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qizixi on 2017/6/4.
 */
@Component
public class RiskCheckExecutionController {

    private RiskCheckExecuteService riskCheckExecuteService;
    private CompanyService companyService;

    @Autowired
    public RiskCheckExecutionController(RiskCheckExecuteService riskCheckExecuteService,CompanyService companyService) {
        this.riskCheckExecuteService = riskCheckExecuteService;
        this.companyService = companyService;
    }

    //get all riskchecks for a company
    public List<RiskCheck> getRiskCheckList(String companyId){
        if (companyService.idNotInDB(companyId)){
            return new ArrayList<>();
        }
        return riskCheckExecuteService.getRiskCheckList(companyId);
    }

    //get riskcheckitems of a checklist
    public List<RiskCheckItem> getRiskCheckItems(int riskCheckId){
        return riskCheckExecuteService.getRiskCheckItems(riskCheckId);
    }

    //update item status
    public void updateRiskCheckItemStatus(int riskCheckId, String statusStr){
        //check input status
        CheckStatus status = null;
        switch (statusStr){
            case "已完成":
                status = CheckStatus.已完成;
                break;
            case "排查中":
                status = CheckStatus.排查中;
                break;
        }
        if (status==null){
            return;
        }
        riskCheckExecuteService.updateRiskCheckItem(riskCheckId,status);
    }

    public void updateRiskCheckStatus(int riskCheckId){
        riskCheckExecuteService.updateRiskCheckStatus(riskCheckId);
    }
}
