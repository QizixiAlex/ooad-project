package com.ooad.controller;

import com.ooad.entity.RiskCheckTemplateItem;
import com.ooad.service.RiskCheckTemplateItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Shijian on 2017/6/6.
 */
@Component
public class RiskCheckTemplateItemController {

    private RiskCheckTemplateItemService riskCheckTemplateItemService;

    @Autowired
    public RiskCheckTemplateItemController ( RiskCheckTemplateItemService riskCheckTemplateItemService ){
        this.riskCheckTemplateItemService = riskCheckTemplateItemService;
    }

    //根据id得到检查项
    public RiskCheckTemplateItem getRiskCheckTemplateItemById( int id ){
        return riskCheckTemplateItemService.getRiskCheckTemplateItemById( id );
    }

    //得到所有检查项
    public List<RiskCheckTemplateItem> getRiskCheckTemplateItems(){
        return riskCheckTemplateItemService.getRiskCheckTemplateItems();
    }

    //创建检查项
    public int createRiskCheckTemplateItem( String name, String content ){
        int paramNum=0;
        RiskCheckTemplateItem item = new RiskCheckTemplateItem();

        if (name!=null){
            paramNum++;
            item.setName(name);
        }
        if (content!=null){
            paramNum++;
            item.setContent(content);
        }
        if (paramNum==0) {
            return -1;
        }

        riskCheckTemplateItemService.createRiskCheckTemplateItem(item);
        return item.getId();
    }

    //更新检查项
    public void updateRiskCheckTemplateItem( int id, String name, String content ){
        int paramNum=0;
        RiskCheckTemplateItem item = riskCheckTemplateItemService.getRiskCheckTemplateItemById( id );

        if (name!=null){
            paramNum++;
            item.setName(name);
        }
        if (content!=null){
            paramNum++;
            item.setContent(content);
        }
        if (paramNum==0){
            return;
        }

        riskCheckTemplateItemService.updateRiskCheckTemplateItem(item);
    }

}
