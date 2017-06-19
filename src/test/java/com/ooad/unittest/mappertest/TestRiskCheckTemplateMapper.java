package com.ooad.unittest.mappertest;

import com.ooad.RisksystemApplication;
import com.ooad.entity.RiskCheckTemplate;
import com.ooad.entity.RiskCheckTemplateItem;
import com.ooad.mapper.RiskCheckTemplateItemMapper;
import com.ooad.mapper.RiskCheckTemplateMapper;
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
 * Created by Shijian on 2017/6/2.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RisksystemApplication.class)
public class TestRiskCheckTemplateMapper {

    @Autowired
    private RiskCheckTemplateMapper riskCheckTemplateMapper;
    @Autowired
    private RiskCheckTemplateItemMapper riskCheckTemplateItemMapper;

    private List<RiskCheckTemplate> riskCheckTemplates;
    private List<RiskCheckTemplateItem> riskCheckTemplateItems;

    @Before
    public void initializeData(){
        riskCheckTemplates = new ArrayList<RiskCheckTemplate>();
        for (int i=1;i<=3;i++){
            RiskCheckTemplate template =new RiskCheckTemplate();
            template.setName("检查模板"+i);
            template.setDescription("检查模板"+i+"的内容与说明");
            riskCheckTemplates.add(template);
        }

        riskCheckTemplateItems = new ArrayList<RiskCheckTemplateItem>();
        for (int i=1;i<=3;i++){
            RiskCheckTemplateItem item =new RiskCheckTemplateItem();
            item.setName("检查项"+i);
            item.setContent("检查项"+i+"的内容与说明");
            riskCheckTemplateItems.add(item);
        }
    }

    @Test
    public void testRiskCheckTemplateMapper(){

        //get 'number' of tuples before create
        List<RiskCheckTemplate> dbTemplates = riskCheckTemplateMapper.getRiskCheckTemplates();
        final int ORINUM=dbTemplates.size();

        //create
        for (RiskCheckTemplate template:riskCheckTemplates) {
            riskCheckTemplateMapper.createRiskCheckTemplate(template);
        }

        //test retrieve all and create
        dbTemplates = riskCheckTemplateMapper.getRiskCheckTemplates();
        Assert.assertEquals(3, dbTemplates.size()-ORINUM);
        for (RiskCheckTemplate template:riskCheckTemplates){
            boolean found=false;
            for (RiskCheckTemplate dbTemplate:dbTemplates){
                if (template.getName().equals(dbTemplate.getName())&&
                        template.getDescription().equals(dbTemplate.getDescription())){
                    found=true;
                    break;
                }
            }
            Assert.assertTrue(found);
        }

        //test retrieve by id
        for (RiskCheckTemplate dbTemplate:dbTemplates){
            RiskCheckTemplate retrieveTemplate=riskCheckTemplateMapper.getRiskCheckTemplateById(dbTemplate.getId());
            Assert.assertThat(dbTemplate, new SamePropertyValuesAs<>(retrieveTemplate));
        }

        //test update
        int index=1;
        for (RiskCheckTemplate dbTemplate:dbTemplates){
            dbTemplate.setName("更新(update)过的模板名字["+index+"]");
            dbTemplate.setDescription("相应的升级过的模板描述["+(index++)+"]");
            riskCheckTemplateMapper.updateRiskCheckTemplate(dbTemplate);
            RiskCheckTemplate retrieveTemplate=riskCheckTemplateMapper.getRiskCheckTemplateById(dbTemplate.getId());
            Assert.assertThat(dbTemplate, new SamePropertyValuesAs<>(retrieveTemplate));
        }

        {//test about "item_in_template"
            //create items
            for (RiskCheckTemplateItem item : riskCheckTemplateItems) {
                riskCheckTemplateItemMapper.createRiskCheckTemplateItem(item);
            }

            //create item_in_template
            for (RiskCheckTemplateItem item : riskCheckTemplateItems) {
                riskCheckTemplateMapper.createItemInTemplate(riskCheckTemplates.get(0).getId(),item.getId());
                riskCheckTemplateMapper.createItemInTemplate(riskCheckTemplates.get(1).getId(),item.getId());
            }

            //retrieve
            RiskCheckTemplate template0 = riskCheckTemplateMapper.getRiskCheckTemplateById(riskCheckTemplates.get(0).getId());
            RiskCheckTemplate template1 = riskCheckTemplateMapper.getRiskCheckTemplateById(riskCheckTemplates.get(1).getId());

            Assert.assertEquals(riskCheckTemplateItems.size(),template0.getItems().size());
            Assert.assertEquals(riskCheckTemplateItems.size(),template1.getItems().size());

            //delete item_in_template
            riskCheckTemplateMapper.deleteItemInTemplate(riskCheckTemplates.get(0).getId(),riskCheckTemplateItems.get(0).getId());

            //delete item_in_template by id_template
            riskCheckTemplateMapper.deleteItemInTemplateByIdTemplate(riskCheckTemplates.get(1).getId());

            //retrieve
            template0 = riskCheckTemplateMapper.getRiskCheckTemplateById(riskCheckTemplates.get(0).getId());
            template1 = riskCheckTemplateMapper.getRiskCheckTemplateById(riskCheckTemplates.get(1).getId());

            Assert.assertEquals(riskCheckTemplateItems.size()-1,template0.getItems().size());
            Assert.assertEquals(0,template1.getItems().size());

            //check property of object 'item' in 'template.items'
            for ( RiskCheckTemplateItem item : template0.getItems() ){
                for ( RiskCheckTemplateItem itemOri : riskCheckTemplateItems){
                    if (itemOri.getId()==item.getId()){
                        Assert.assertThat(itemOri, new SamePropertyValuesAs<>(item));
                        break;
                    }
                }
            }
        }//end of test about "item_in_template"

        //test delete
        for (RiskCheckTemplate template: riskCheckTemplates){
            riskCheckTemplateMapper.deleteItemInTemplateByIdTemplate(template.getId());
            riskCheckTemplateMapper.deleteRiskCheckTemplate(template);
            RiskCheckTemplate retrieveTemplate = riskCheckTemplateMapper.getRiskCheckTemplateById(template.getId());
            Assert.assertEquals(null, retrieveTemplate);
        }

    }
}
