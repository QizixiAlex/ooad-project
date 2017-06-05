package com.ooad.service;

import com.ooad.entity.RiskCheckTemplate;
import com.ooad.entity.RiskCheckTemplateItem;
import com.ooad.mapper.RiskCheckTemplateItemMapper;
import com.ooad.mapper.RiskCheckTemplateMapper;
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
    public RiskCheckTemplateItem getRiskCheckTemplateItemById(int id){
        return riskCheckTemplateItemMapper.getRiskCheckTemplateItemById(id);
    }

    //得到所有的模板项目
    public List<RiskCheckTemplateItem> getRiskCheckTemplateItems(){
        return riskCheckTemplateItemMapper.getRiskCheckTemplateItems();
    }

    //创建一个模板项目（对象传引用，id值将在执行时被设置为数据库中生成的id）
    public void createRiskCheckTemplateItem(RiskCheckTemplateItem riskCheckTemplateItem){
        riskCheckTemplateItemMapper.createRiskCheckTemplateItem(riskCheckTemplateItem);
    }

    //更新一个模板项目
    public void updateRiskCheckTemplateItem(RiskCheckTemplateItem riskCheckTemplateItem){
        riskCheckTemplateItemMapper.updateRiskCheckTemplateItem(riskCheckTemplateItem);
    }

}
