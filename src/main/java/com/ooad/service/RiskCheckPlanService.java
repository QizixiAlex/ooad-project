package com.ooad.service;

import com.ooad.entity.Company;
import com.ooad.entity.RiskCheckPlan;
import com.ooad.mapper.RiskCheckPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Shijian on 2017/6/6.
 */
@Component
public class RiskCheckPlanService {

    private RiskCheckPlanMapper riskCheckPlanMapper;

    @Autowired
    public RiskCheckPlanService( RiskCheckPlanMapper riskCheckPlanMapper ){
        this.riskCheckPlanMapper = riskCheckPlanMapper;
    }

    //根据id得到一个计划
    public RiskCheckPlan getRiskCheckPlanById( int id ){
        return riskCheckPlanMapper.getRiskCheckPlanById( id );
    }

    //得到所有的计划
    public List<RiskCheckPlan> getRiskCheckPlans(){
        return riskCheckPlanMapper.getRiskCheckPlans();
    }

    //创建一个计划（对象传引用，id值将在执行时被设置为数据库中生成的id）
    public void createRiskCheckPlan( RiskCheckPlan riskCheckPlan ){
        riskCheckPlanMapper.createRiskCheckPlan( riskCheckPlan );
        riskCheckPlanMapper.deleteCompanyInPlanByIdPlan( riskCheckPlan.getId() );
        for ( Company company : riskCheckPlan.getCompanies() ){
            riskCheckPlanMapper.createCompanyInPlan( riskCheckPlan.getId(), company.getId() );
        }
    }

    //更新一个计划
    public void updateRiskCheckPlan( RiskCheckPlan riskCheckPlan ){
        riskCheckPlanMapper.updateRiskCheckPlan( riskCheckPlan );
        riskCheckPlanMapper.deleteCompanyInPlanByIdPlan( riskCheckPlan.getId() );
        for ( Company company : riskCheckPlan.getCompanies() ){
            riskCheckPlanMapper.createCompanyInPlan( riskCheckPlan.getId(), company.getId() );
        }
    }

}
