package com.ooad.controllertest;

import com.ooad.RisksystemApplication;
import com.ooad.controller.RiskCheckTemplateItemController;
import com.ooad.entity.RiskCheckTemplateItem;
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
 * Created by Shijian on 2017/6/6.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RisksystemApplication.class, webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestRiskCheckTemplateItem {

    @Autowired
    RiskCheckTemplateItemController riskCheckTemplateItemController;

    private List<RiskCheckTemplateItem> riskCheckTemplateItems;

    @Before
    public void initializeData(){
        riskCheckTemplateItems = new ArrayList<RiskCheckTemplateItem>();
        for (int i=1;i<=3;i++){
            RiskCheckTemplateItem item = new RiskCheckTemplateItem();
            item.setName("检查项"+i+"无内容版");
            riskCheckTemplateItems.add(item);
            item =new RiskCheckTemplateItem();
            item.setName("检查项"+i);
            item.setContent("检查项"+i+"的内容与说明");
            riskCheckTemplateItems.add(item);
        }
        riskCheckTemplateItems.add(new RiskCheckTemplateItem());
    }

    @Test
    public void testRiskCheckTemplateItem(){

        //get 'number' of tuples before create
        List<RiskCheckTemplateItem> dbItems = riskCheckTemplateItemController.getRiskCheckTemplateItems();
        final int ORINUM = dbItems.size();

        //create
        for ( RiskCheckTemplateItem item : riskCheckTemplateItems ) {
            int tempId;
            tempId = riskCheckTemplateItemController.createRiskCheckTemplateItem( item.getName(), item.getContent() );
            item.setId(tempId);
        }

        //test retrieve all and create
        dbItems = riskCheckTemplateItemController.getRiskCheckTemplateItems();
        Assert.assertEquals(3*2, dbItems.size()-ORINUM);
        for ( RiskCheckTemplateItem item : riskCheckTemplateItems ){
            if (item.getId()==-1){
                continue;
            }
            boolean found = false;
            for (RiskCheckTemplateItem dbItem : dbItems){
                if (
                        item.getId()==dbItem.getId()&&
                        ((item.getName()==null&&dbItem.getName()==null)||item.getName().equals(dbItem.getName()))&&
                        ((item.getContent()==null&&dbItem.getContent()==null)||item.getContent().equals(dbItem.getContent()))
                    ){
                    found=true;
                    break;
                }
            }
            Assert.assertTrue(found);
        }

        //test retrieve by id
        for (RiskCheckTemplateItem item:riskCheckTemplateItems){
            if (item.getId()==-1) {
                continue;
            }
            RiskCheckTemplateItem retrieveItem=riskCheckTemplateItemController.getRiskCheckTemplateItemById(item.getId());
            Assert.assertThat(item, new SamePropertyValuesAs<>(retrieveItem));
        }

        //test update
        int index=1;
        for (RiskCheckTemplateItem item:riskCheckTemplateItems){
            if (item.getId()==-1) {
                continue;
            }
            item.setContent("更新过的内容["+(index++)+"]");
            riskCheckTemplateItemController.updateRiskCheckTemplateItem(item.getId(),null,item.getContent());
            RiskCheckTemplateItem retrieveItem=riskCheckTemplateItemController.getRiskCheckTemplateItemById(item.getId());
            Assert.assertThat(item, new SamePropertyValuesAs<>(retrieveItem));
        }
        index=1;
        for (RiskCheckTemplateItem item:riskCheckTemplateItems){
            if (item.getId()==-1) {
                continue;
            }
            item.setName("更新过的名字["+(index++)+"]");
            item.setContent("");
            riskCheckTemplateItemController.updateRiskCheckTemplateItem(item.getId(), item.getName(), "");
            RiskCheckTemplateItem retrieveItem=riskCheckTemplateItemController.getRiskCheckTemplateItemById(item.getId());
            Assert.assertThat(item, new SamePropertyValuesAs<>(retrieveItem));
        }//end of test update

    }

}
