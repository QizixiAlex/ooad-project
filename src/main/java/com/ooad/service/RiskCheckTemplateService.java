package com.ooad.service;

import com.ooad.entity.RiskCheckTemplate;
import com.ooad.entity.RiskCheckTemplateItem;
import com.ooad.exception.EntityNotFoundException;
import com.ooad.exception.NullAttributeException;
import com.ooad.exception.RiskCheckException;
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
    public RiskCheckTemplate getRiskCheckTemplateById(int id) throws RiskCheckException {
        RiskCheckTemplate template=riskCheckTemplateMapper.getRiskCheckTemplateById(id);
        if (template==null){
            throw new EntityNotFoundException("无对应模板");
        }
        return template;
    }

    //得到所有的模板
    public List<RiskCheckTemplate> getRiskCheckTemplates() throws RiskCheckException {
        List<RiskCheckTemplate> templates=riskCheckTemplateMapper.getRiskCheckTemplates();
        if (templates==null||templates.isEmpty()){
            throw new EntityNotFoundException("无模板");
        }
        return templates;
    }

    //创建一个模板（对象传引用，id值将在执行时被设置为数据库中生成的id）
    public void createRiskCheckTemplate(RiskCheckTemplate riskCheckTemplate) throws RiskCheckException {
        //检查输入合法性
        if (riskCheckTemplate==null
                ||riskCheckTemplate.getName()==null
                ||riskCheckTemplate.getDescription()==null
                ||riskCheckTemplate.getItems()==null){
            throw new NullAttributeException("输入为空");
        }
        //创建
        riskCheckTemplateMapper.createRiskCheckTemplate(riskCheckTemplate);
        //创建模板与模板项关联
        riskCheckTemplateMapper.deleteItemInTemplateByIdTemplate(riskCheckTemplate.getId());
        for ( RiskCheckTemplateItem item : riskCheckTemplate.getItems() ){
            riskCheckTemplateMapper.createItemInTemplate(riskCheckTemplate.getId(),item.getId());
        }
    }

    //更新一个模板
    public void updateRiskCheckTemplate(RiskCheckTemplate riskCheckTemplate) throws RiskCheckException {
        //检查输入合法性
        if (riskCheckTemplate==null
                ||riskCheckTemplate.getName()==null
                ||riskCheckTemplate.getDescription()==null
                ||riskCheckTemplate.getItems()==null){
            throw new NullAttributeException("输入为空");
        }
        //检查更新的目标存在与否
        RiskCheckTemplate dbTemplate=riskCheckTemplateMapper.getRiskCheckTemplateById(riskCheckTemplate.getId());
        if (dbTemplate==null){
            throw new EntityNotFoundException("模板不存在");
        }
        //更新
        riskCheckTemplateMapper.updateRiskCheckTemplate(riskCheckTemplate);
        //更新模板与模板项关联
        riskCheckTemplateMapper.deleteItemInTemplateByIdTemplate(riskCheckTemplate.getId());
        for ( RiskCheckTemplateItem item : riskCheckTemplate.getItems() ){
            riskCheckTemplateMapper.createItemInTemplate(riskCheckTemplate.getId(),item.getId());
        }
    }

    //删除一个模板
    public void deleteRiskCheckTemplate(RiskCheckTemplate riskCheckTemplate) throws RiskCheckException {
        //检查输入合法性
        if (riskCheckTemplate==null){
            throw new NullAttributeException("输入为空");
        }
        //检查更新的目标存在与否
        RiskCheckTemplate dbTemplate=riskCheckTemplateMapper.getRiskCheckTemplateById(riskCheckTemplate.getId());
        if (dbTemplate==null){
            throw new EntityNotFoundException("模板项目不存在");
        }
        //删除模板与模板项的关联
        riskCheckTemplateMapper.deleteItemInTemplateByIdTemplate(riskCheckTemplate.getId());
        //删除
        riskCheckTemplateMapper.deleteRiskCheckTemplate(riskCheckTemplate);
    }

}
