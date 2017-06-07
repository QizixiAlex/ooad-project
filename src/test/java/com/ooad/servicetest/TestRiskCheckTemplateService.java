package com.ooad.servicetest;

import com.ooad.RisksystemApplication;
import com.ooad.entity.RiskCheckTemplate;
import com.ooad.entity.RiskCheckTemplateItem;
import com.ooad.service.RiskCheckTemplateItemService;
import com.ooad.service.RiskCheckTemplateService;
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
public class TestRiskCheckTemplateService {

    @Autowired
    private RiskCheckTemplateItemService riskCheckTemplateItemService;
    @Autowired
    private RiskCheckTemplateService riskCheckTemplateService;

    private List<RiskCheckTemplate> riskCheckTemplates;
    private List<RiskCheckTemplateItem> riskCheckTemplateItems;

    @Before
    public void initializeData(){

        riskCheckTemplateItems = new ArrayList<RiskCheckTemplateItem>();
        riskCheckTemplates = new ArrayList<RiskCheckTemplate>();

        for (int i=1;i<=3;i++){
            RiskCheckTemplateItem item =new RiskCheckTemplateItem();
            item.setName("检查项"+i);
            item.setContent("检查项"+i+"的内容与说明");
            riskCheckTemplateItems.add(item);
            riskCheckTemplateItemService.createRiskCheckTemplateItem(item);

            RiskCheckTemplate template =new RiskCheckTemplate();
            template.setName("检查模板"+i);
            template.setDescription("检查模板"+i+"的内容与说明");
            List<RiskCheckTemplateItem> items = new ArrayList<RiskCheckTemplateItem>();
            items.addAll(riskCheckTemplateItems);
            template.setItems(items);
            riskCheckTemplates.add(template);
        }
    }//end of initializeData()

    @Test
    public void testRiskCheckTemplateService(){

        //get 'number' of tuples before create
        List<RiskCheckTemplate> dbTemplates = riskCheckTemplateService.getRiskCheckTemplates();
        final int ORINUM=dbTemplates.size();

        //create
        for (RiskCheckTemplate template:riskCheckTemplates) {
            riskCheckTemplateService.createRiskCheckTemplate(template);
        }

        //test retrieve all and create
        dbTemplates = riskCheckTemplateService.getRiskCheckTemplates();
        Assert.assertEquals(3, dbTemplates.size()-ORINUM);
        for (RiskCheckTemplate template:riskCheckTemplates){
            boolean found=false;
            for (RiskCheckTemplate dbTemplate:dbTemplates){
                if (template.getId()==dbTemplate.getId()&&
                        template.getName().equals(dbTemplate.getName())&&
                        template.getDescription().equals(dbTemplate.getDescription())&&
                        template.getItems().size()==dbTemplate.getItems().size()){
                    found=true;
                    break;
                }
            }
            Assert.assertTrue(found);
        }

        //test retrieve by id
        for (RiskCheckTemplate template:riskCheckTemplates){
            RiskCheckTemplate retrieveTemplate = riskCheckTemplateService.getRiskCheckTemplateById(template.getId());
            Assert.assertEquals(template.getId(),retrieveTemplate.getId());
            Assert.assertEquals(template.getName(),retrieveTemplate.getName());
            Assert.assertEquals(template.getDescription(),retrieveTemplate.getDescription());
            Assert.assertEquals(template.getItems().size(),retrieveTemplate.getItems().size());
            for (RiskCheckTemplateItem item : template.getItems()){
                boolean found=false;
                for (RiskCheckTemplateItem retrieveItem : retrieveTemplate.getItems()){
                    if (item.getId()==retrieveItem.getId()){
                        found=true;
                        break;
                    }
                }
                Assert.assertTrue(found);
            }
        }

        //test update
        int index=1;
        for (RiskCheckTemplate template : riskCheckTemplates){
            template.setName("更新(update)过的模板名字["+index+"]");
            template.setDescription("相应的升级过的模板描述["+(index++)+"]");
            List<RiskCheckTemplateItem> items=new ArrayList<RiskCheckTemplateItem>();
            for (int i=index-1;i<3;i++)
                items.add(riskCheckTemplateItems.get(i));
            template.setItems(items);
            riskCheckTemplateService.updateRiskCheckTemplate(template);
            RiskCheckTemplate retrieveTemplate=riskCheckTemplateService.getRiskCheckTemplateById(template.getId());
            Assert.assertEquals(template.getId(),retrieveTemplate.getId());
            Assert.assertEquals(template.getName(),retrieveTemplate.getName());
            Assert.assertEquals(template.getDescription(),retrieveTemplate.getDescription());
            Assert.assertEquals(template.getItems().size(),retrieveTemplate.getItems().size());
            for (RiskCheckTemplateItem item : template.getItems()){
                boolean found=false;
                for (RiskCheckTemplateItem retrieveItem : retrieveTemplate.getItems()){
                    if (item.getId()==retrieveItem.getId()){
                        found=true;
                        break;
                    }
                }
                Assert.assertTrue(found);
            }
        }//end of test update

    }//end of testRiskCheckTemplateService()

}
