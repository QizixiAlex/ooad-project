package com.ooad.service;

import com.ooad.entity.RiskCheckTemplateItem;
import com.ooad.exception.DuplicateAttributeException;
import com.ooad.exception.EntityNotFoundException;
import com.ooad.exception.NullAttributeException;
import com.ooad.exception.RiskCheckException;
import com.ooad.mapper.RiskCheckTemplateItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Shijian on 2017/6/5.
 */
@Component
public class RiskCheckTemplateItemService {

    private RiskCheckTemplateItemMapper riskCheckTemplateItemMapper;

    @Autowired
    public RiskCheckTemplateItemService(RiskCheckTemplateItemMapper riskCheckTemplateItemMapper){
        this.riskCheckTemplateItemMapper=riskCheckTemplateItemMapper;
    }

    //根据id得到一个模板项目
    public RiskCheckTemplateItem getRiskCheckTemplateItemById(int id) throws RiskCheckException {
        RiskCheckTemplateItem item=riskCheckTemplateItemMapper.getRiskCheckTemplateItemById(id);
        if (item==null){
            throw new EntityNotFoundException("无对应模板项目");
        }
        return item;
    }

    //得到所有的模板项目
    public List<RiskCheckTemplateItem> getRiskCheckTemplateItems() throws RiskCheckException {
        List<RiskCheckTemplateItem> items=riskCheckTemplateItemMapper.getRiskCheckTemplateItems();
        if (items==null||items.isEmpty()){
            throw new EntityNotFoundException("无模板项目");
        }
        return items;
    }

    //创建一个模板项目（对象传引用，id值将在执行时被设置为数据库中生成的id）
    public void createRiskCheckTemplateItem(RiskCheckTemplateItem riskCheckTemplateItem) throws RiskCheckException {
        //检查输入合法性
        if (riskCheckTemplateItem==null
                ||riskCheckTemplateItem.getName()==null
                ||riskCheckTemplateItem.getContent()==null){
            throw new NullAttributeException("输入为空");
        }
        //创建
        riskCheckTemplateItemMapper.createRiskCheckTemplateItem(riskCheckTemplateItem);
    }

    //更新一个模板项目
    public void updateRiskCheckTemplateItem(RiskCheckTemplateItem riskCheckTemplateItem) throws RiskCheckException {
        //检查输入合法性
        if (riskCheckTemplateItem==null
                ||riskCheckTemplateItem.getName()==null
                ||riskCheckTemplateItem.getContent()==null){
            throw new NullAttributeException("输入为空");
        }
        //检查更新的目标存在与否
        RiskCheckTemplateItem dbItem=riskCheckTemplateItemMapper.getRiskCheckTemplateItemById(riskCheckTemplateItem.getId());
        if (dbItem==null){
            throw new EntityNotFoundException("模板项目不存在");
        }
        //更新
        riskCheckTemplateItemMapper.updateRiskCheckTemplateItem(riskCheckTemplateItem);
    }

    //删除一个模板项目
    public void deleteRiskCheckTemplateItem(RiskCheckTemplateItem riskCheckTemplateItem) throws RiskCheckException {
        //检查输入合法性
        if (riskCheckTemplateItem==null){
            throw new NullAttributeException("输入为空");
        }
        //检查更新的目标存在与否
        RiskCheckTemplateItem dbItem=riskCheckTemplateItemMapper.getRiskCheckTemplateItemById(riskCheckTemplateItem.getId());
        if (dbItem==null){
            throw new EntityNotFoundException("模板项目不存在");
        }
        //删除
        riskCheckTemplateItemMapper.deleteRiskCheckTemplateItem(riskCheckTemplateItem);
    }

}
