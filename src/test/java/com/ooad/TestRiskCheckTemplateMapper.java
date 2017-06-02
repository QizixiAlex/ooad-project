package com.ooad;

import com.ooad.entity.RiskCheckTemplate;
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

    private List<RiskCheckTemplate> riskCheckTemplates;

    @Before
    public void initializeData(){
        riskCheckTemplates = new ArrayList<>();
        for (int i=1;i<=3;i++){
            RiskCheckTemplate template =new RiskCheckTemplate();
            template.setName("检查模板"+i);
            template.setDescription("检查模板"+i+"的内容与说明");
            riskCheckTemplates.add(template);
        }
    }

    @Test
    public void testRiskCheckTemplateItemMapper(){

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
    }
}
