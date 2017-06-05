package com.ooad.servicetest;

import com.ooad.RisksystemApplication;
import com.ooad.entity.RiskCheckTemplateItem;
import com.ooad.service.RiskCheckTemplateItemService;
import org.hamcrest.beans.SamePropertyValuesAs;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shijian on 2017/6/5.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RisksystemApplication.class)
public class TestRiskCheckTemplateItemService {

    @Autowired
    private RiskCheckTemplateItemService riskCheckTemplateItemService;

    private List<RiskCheckTemplateItem> riskCheckTemplateItems;

    @Before
    public void initializeData(){
        riskCheckTemplateItems = new ArrayList<RiskCheckTemplateItem>();
        for (int i=1;i<=3;i++){
            RiskCheckTemplateItem item =new RiskCheckTemplateItem();
            item.setName("检查项"+i);
            item.setContent("检查项"+i+"的内容与说明");
            riskCheckTemplateItems.add(item);
        }
    }

    @Test
    public void testRiskCheckTemplateItemMapper(){

        //get 'number' of tuples before create
        List<RiskCheckTemplateItem> dbItems = riskCheckTemplateItemService.getRiskCheckTemplateItems();
        final int ORINUM=dbItems.size();

        //create
        for (RiskCheckTemplateItem item:riskCheckTemplateItems) {
            riskCheckTemplateItemService.createRiskCheckTemplateItem(item);
        }

        //test retrieve all and create
        dbItems = riskCheckTemplateItemService.getRiskCheckTemplateItems();
        Assert.assertEquals(3, dbItems.size()-ORINUM);
        for (RiskCheckTemplateItem item:riskCheckTemplateItems){
            boolean found=false;
            for (RiskCheckTemplateItem dbItem:dbItems){
                if (item.getName().equals(dbItem.getName())&&
                        item.getContent().equals(dbItem.getContent())){
                    found=true;
                    break;
                }
            }
            Assert.assertTrue(found);
        }

        //test retrieve by id
        for (RiskCheckTemplateItem item:riskCheckTemplateItems){
            RiskCheckTemplateItem retrieveItem=riskCheckTemplateItemService.getRiskCheckTemplateItemById(item.getId());
            Assert.assertThat(item, new SamePropertyValuesAs<>(retrieveItem));
        }

        //test update
        int index=1;
        for (RiskCheckTemplateItem item:riskCheckTemplateItems){
            item.setName("更新(update)过的名字["+index+"]");
            item.setContent("相应的升级过的内容["+(index++)+"]");
            riskCheckTemplateItemService.updateRiskCheckTemplateItem(item);
            RiskCheckTemplateItem retrieveItem=riskCheckTemplateItemService.getRiskCheckTemplateItemById(item.getId());
            Assert.assertThat(item, new SamePropertyValuesAs<>(retrieveItem));
        }
    }

}
