package com.ooad.service;

import com.ooad.entity.CheckStatus;
import com.ooad.entity.Company;
import com.ooad.entity.RiskCheck;
import com.ooad.entity.RiskCheckItem;
import com.ooad.exception.EntityNotFoundException;
import com.ooad.exception.RiskCheckException;
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

    public List<RiskCheck> getRiskCheckList(String companyId) throws RiskCheckException{
        //输入检查
        Company company = companyMapper.getCompanyById(companyId);
        if (company==null){
            throw new EntityNotFoundException("无对应公司");
        }
        List<RiskCheck> result = riskCheckMapper.getRiskCheckByCompanyId(company.getId());
        if (result.isEmpty()){
            throw new EntityNotFoundException("公司无待检查项");
        }
        return result;
    }

    public List<RiskCheckItem> getRiskCheckItems(int riskCheckId) throws RiskCheckException{
        //输入检查
        RiskCheck riskCheck = riskCheckMapper.getRiskCheckById(riskCheckId);
        if (riskCheck==null){
            throw new EntityNotFoundException("无对应检查");
        }
        if (riskCheck.getItems() == null||riskCheck.getItems().size()==0){
            throw new EntityNotFoundException("无检查项");
        }
        return riskCheck.getItems();
    }

    public void updateRiskCheckItem(int riskCheckItemId, CheckStatus status) throws RiskCheckException{
        //输入检查
        RiskCheckItem riskCheckItem = riskCheckItemMapper.getRiskCheckItemById(riskCheckItemId);
        if (riskCheckItem==null){
            throw new EntityNotFoundException("无对应检查");
        }
        //更新状态
        if (riskCheckItem.getStatus()!=status){
            riskCheckItem.setStatus(status);
            if (status == CheckStatus.已完成){
                Timestamp timestamp = new Timestamp(new Date().getTime());
                riskCheckItem.setFinishDate(timestamp);
            }
            //更新数据库
            riskCheckItemMapper.updateRiskCheckItem(riskCheckItem);
        }
    }

    public void updateRiskCheckStatus(int riskCheckId) throws RiskCheckException{
        //输入检查
        //不允许将已完成的检查项重置为未完成
        RiskCheck riskCheck = riskCheckMapper.getRiskCheckById(riskCheckId);
        if (riskCheck==null){
            throw new EntityNotFoundException("无对应检查");
        }
        if (riskCheck.getStatus()==CheckStatus.已完成){
            return;
        }
        //逐个检查检查项
        for (RiskCheckItem item:riskCheck.getItems()){
            if (item.getStatus()==CheckStatus.排查中){
                //检查未完成
                return;
            }
        }
        //检查已完成
        riskCheck.setStatus(CheckStatus.已完成);
        riskCheck.setFinishDate(new Timestamp(new Date().getTime()));
        //更新数据库
        riskCheckMapper.updateRiskCheck(riskCheck);
    }

}
