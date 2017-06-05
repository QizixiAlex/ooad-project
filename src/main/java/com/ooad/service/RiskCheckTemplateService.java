package com.ooad.service;

import com.ooad.entity.RiskCheckTemplate;
import com.ooad.entity.RiskCheckTemplateItem;
import com.ooad.mapper.RiskCheckTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Shijian on 2017/6/5.
 */
@Component
public class RiskCheckTemplateService {

    private RiskCheckTemplateMapper riskCheckTemplateMapper;

    @Autowired
    public RiskCheckTemplateService(RiskCheckTemplateMapper riskCheckTemplateMapper){
        this.riskCheckTemplateMapper=riskCheckTemplateMapper;
    }

    //根据id得到一个模板
    public RiskCheckTemplate getRiskCheckTemplateById(int id){
        return riskCheckTemplateMapper.getRiskCheckTemplateById(id);
    }

    //得到所有的模板
    public List<RiskCheckTemplate> getRiskCheckTemplates(){
        return riskCheckTemplateMapper.getRiskCheckTemplates();
    }

    //创建一个模板（对象传引用，id值将在执行时被设置为数据库中生成的id）
    public void createRiskCheckTemplate(RiskCheckTemplate riskCheckTemplate){
        riskCheckTemplateMapper.createRiskCheckTemplate(riskCheckTemplate);
        riskCheckTemplateMapper.deleteItemInTemplateByIdTemplate(riskCheckTemplate.getId());
        for ( RiskCheckTemplateItem item : riskCheckTemplate.getItems() ){
            riskCheckTemplateMapper.createItemInTemplate(riskCheckTemplate.getId(),item.getId());
        }
    }

    //更新一个模板
    public void updateRiskCheckTemplate(RiskCheckTemplate riskCheckTemplate){
        riskCheckTemplateMapper.updateRiskCheckTemplate(riskCheckTemplate);
        riskCheckTemplateMapper.deleteItemInTemplateByIdTemplate(riskCheckTemplate.getId());
        for ( RiskCheckTemplateItem item : riskCheckTemplate.getItems() ){
            riskCheckTemplateMapper.createItemInTemplate(riskCheckTemplate.getId(),item.getId());
        }
    }
    
}
