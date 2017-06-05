package com.ooad.service;

import com.ooad.entity.CheckStatus;
import com.ooad.entity.Company;
import com.ooad.entity.RiskCheck;
import com.ooad.entity.RiskCheckItem;
import com.ooad.mapper.CompanyMapper;
import com.ooad.mapper.RiskCheckItemMapper;
import com.ooad.mapper.RiskCheckMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 2017/6/4.
 */
@Component
public class RiskCheckExecuteService {

    private RiskCheckMapper riskCheckMapper;
    private CompanyMapper companyMapper;
    private RiskCheckItemMapper riskCheckItemMapper;

    @Autowired
    public RiskCheckExecuteService(RiskCheckMapper riskCheckMapper, CompanyMapper companyMapper, RiskCheckItemMapper riskCheckItemMapper) {
        this.riskCheckMapper = riskCheckMapper;
        this.companyMapper = companyMapper;
        this.riskCheckItemMapper = riskCheckItemMapper;
    }

    public List<RiskCheck> getRiskCheckList(String companyId){
        //check companyId
        //if no company then company would be null, then return emptyList
        Company company = companyMapper.getCompanyById(companyId);
        if (company==null){
            return new ArrayList<>();
        }
        //if no riskcheck return emptyList
        List<RiskCheck> result = riskCheckMapper.getRiskCheckByCompanyId(company.getId());
        return result;
    }

    public List<RiskCheckItem> getRiskCheckItems(int riskCheckId){
        RiskCheck riskCheck = riskCheckMapper.getRiskCheckById(riskCheckId);
        //check id
        if (riskCheck==null){
            return new ArrayList<>();
        }
        if (riskCheck.getItems() == null){
            return new ArrayList<>();
        }
        return riskCheck.getItems();
    }

    public void updateRiskCheckItem(int riskCheckItemId, CheckStatus status){
        RiskCheckItem riskCheckItem = riskCheckItemMapper.getRiskCheckItemById(riskCheckItemId);
        //check id
        if (riskCheckItem==null){
            return;
        }
        //update if status change
        if (riskCheckItem.getStatus()!=status){
            riskCheckItem.setStatus(status);
            if (status == CheckStatus.已完成){
                Timestamp timestamp = new Timestamp(new Date().getTime());
                riskCheckItem.setFinishDate(timestamp);
            }
            //set to database
            riskCheckItemMapper.updateRiskCheckItem(riskCheckItem);
        }
    }

    public void updateRiskCheckStatus(int riskCheckId){
        RiskCheck riskCheck = riskCheckMapper.getRiskCheckById(riskCheckId);
        //check riskCheckId
        if (riskCheck==null){
            return;
        }
        //check riskCheckItem
        for (RiskCheckItem item:riskCheck.getItems()){
            if (item.getStatus()==CheckStatus.排查中){
                //riskCheck not finished
                return;
            }
        }
        //riskCheck finished
        riskCheck.setStatus(CheckStatus.已完成);
        riskCheck.setFinishDate(new Timestamp(new Date().getTime()));
        //set to database
        riskCheckMapper.updateRiskCheck(riskCheck);
    }

}
