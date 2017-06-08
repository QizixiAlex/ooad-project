package com.ooad.service;

import com.ooad.entity.Company;
import com.ooad.entity.RiskCheck;
import com.ooad.entity.RiskCheckPlan;
import com.ooad.exception.EntityNotFoundException;
import com.ooad.exception.InvalidAttributeException;
import com.ooad.exception.RiskCheckException;
import com.ooad.mapper.CompanyMapper;
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

    private RiskCheckMapper riskCheckMapper;
    private RiskCheckPlanMapper riskCheckPlanMapper;
    private CompanyMapper companyMapper;

    @Autowired
    public RiskCheckViewService(RiskCheckMapper riskCheckMapper, RiskCheckPlanMapper riskCheckPlanMapper, CompanyMapper companyMapper) {
        this.riskCheckMapper = riskCheckMapper;
        this.riskCheckPlanMapper = riskCheckPlanMapper;
        this.companyMapper = companyMapper;
    }

    public List<RiskCheck> getRiskChecks() throws RiskCheckException{
        //结果检查
        List<RiskCheck> result = riskCheckMapper.getRiskChecks();
        if (result==null||result.isEmpty()){
            throw new EntityNotFoundException("无对应检查");
        }
        return result;
    }

    public List<RiskCheck> getRiskChecksByPlanId(int planId) throws RiskCheckException{
        //输入检查
        RiskCheckPlan plan = riskCheckPlanMapper.getRiskCheckPlanById(planId);
        if (plan==null){
            throw new EntityNotFoundException("无对应计划");
        }
        //结果检查
        List<RiskCheck> result = riskCheckMapper.getRiskCheckByPlanId(plan.getId());
        if (result==null||result.isEmpty()){
            throw new EntityNotFoundException("无对应检查");
        }
        return result;
    }

    public List<RiskCheck> getRiskChecksByCompanyId(String companyId) throws RiskCheckException{
        //输入检查
        if (companyId==null){
            throw new InvalidAttributeException("ID不能为空值");
        }
        Company company = companyMapper.getCompanyById(companyId);
        if (company==null){
            throw new EntityNotFoundException("无对应公司");
        }
        List<RiskCheck> result = riskCheckMapper.getRiskCheckByCompanyId(company.getId());
        if (result==null||result.isEmpty()){
            throw new EntityNotFoundException("无对应检查");
        }
        return result;
    }

    public RiskCheck getRiskCheck(int riskCheckId) throws RiskCheckException{
        //输入检查
        RiskCheck riskCheck = riskCheckMapper.getRiskCheckById(riskCheckId);
        if (riskCheck==null){
            throw new EntityNotFoundException("无对应检查");
        }
        return riskCheck;
    }
}
