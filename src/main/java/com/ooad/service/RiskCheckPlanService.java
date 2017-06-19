package com.ooad.service;

import com.ooad.entity.Company;
import com.ooad.entity.RiskCheckPlan;
import com.ooad.exception.EntityNotFoundException;
import com.ooad.exception.NullAttributeException;
import com.ooad.exception.RiskCheckException;
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
    public RiskCheckPlan getRiskCheckPlanById( int id ) throws RiskCheckException {
        RiskCheckPlan plan=riskCheckPlanMapper.getRiskCheckPlanById( id );
        if (plan==null){
            throw new EntityNotFoundException("无对应计划");
        }
        return plan;
    }

    //得到所有的计划
    public List<RiskCheckPlan> getRiskCheckPlans() throws RiskCheckException {
        List<RiskCheckPlan> plans=riskCheckPlanMapper.getRiskCheckPlans();
        if (plans==null||plans.isEmpty()){
            throw new EntityNotFoundException("无计划");
        }
        return plans;
    }

    //创建一个计划（对象传引用，id值将在执行时被设置为数据库中生成的id）
    public void createRiskCheckPlan( RiskCheckPlan riskCheckPlan ) throws RiskCheckException {
        //检查输入合法性
        if (riskCheckPlan==null||
                riskCheckPlan.getName()==null||
                riskCheckPlan.getStartDate()==null||
                riskCheckPlan.getFinishDate()==null||
                riskCheckPlan.getTemplate()==null||
                riskCheckPlan.getCompanies()==null){
            throw new NullAttributeException("输入为空");
        }
        //创建
        riskCheckPlanMapper.createRiskCheckPlan( riskCheckPlan );
        //创建计划与公司关联
        riskCheckPlanMapper.deleteCompanyInPlanByIdPlan( riskCheckPlan.getId() );
        for ( Company company : riskCheckPlan.getCompanies() ){
            riskCheckPlanMapper.createCompanyInPlan( riskCheckPlan.getId(), company.getId() );
        }
    }

    //更新一个计划
    public void updateRiskCheckPlan( RiskCheckPlan riskCheckPlan ) throws RiskCheckException {
        //检查输入合法性
        if (riskCheckPlan==null||
                riskCheckPlan.getName()==null||
                riskCheckPlan.getStartDate()==null||
                riskCheckPlan.getFinishDate()==null||
                riskCheckPlan.getTemplate()==null||
                riskCheckPlan.getCompanies()==null){
            throw new NullAttributeException("输入为空");
        }
        //检查更新的目标存在与否
        RiskCheckPlan dbPlan=riskCheckPlanMapper.getRiskCheckPlanById(riskCheckPlan.getId());
        if (dbPlan==null){
            throw new EntityNotFoundException("计划不存在");
        }
        //更新
        riskCheckPlanMapper.updateRiskCheckPlan( riskCheckPlan );
        //更新计划与公司关联
        riskCheckPlanMapper.deleteCompanyInPlanByIdPlan( riskCheckPlan.getId() );
        for ( Company company : riskCheckPlan.getCompanies() ){
            riskCheckPlanMapper.createCompanyInPlan( riskCheckPlan.getId(), company.getId() );
        }
    }

    //删除一个模板
    public void deleteRiskCheckPlan(RiskCheckPlan riskCheckPlan) throws RiskCheckException {
        //检查输入合法性
        if (riskCheckPlan==null){
            throw new NullAttributeException("输入为空");
        }
        //检查更新的目标存在与否
        RiskCheckPlan dbPlan=riskCheckPlanMapper.getRiskCheckPlanById(riskCheckPlan.getId());
        if (dbPlan==null){
            throw new EntityNotFoundException("计划不存在");
        }
        //删除计划与公司的关联
        riskCheckPlanMapper.deleteCompanyInPlanByIdPlan(riskCheckPlan.getId());
        //删除
        riskCheckPlanMapper.deleteRiskCheckPlan(riskCheckPlan);
    }

}
