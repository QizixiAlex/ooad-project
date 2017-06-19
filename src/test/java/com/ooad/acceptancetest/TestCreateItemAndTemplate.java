package com.ooad.acceptancetest;

import com.ooad.RisksystemApplication;
import com.ooad.entity.RiskCheckTemplate;
import com.ooad.entity.RiskCheckTemplateItem;
import com.ooad.exception.RiskCheckException;
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
 * 测试创建“检查项目”与“检查模板”，并向“检查模板”中添加“检查项目”
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RisksystemApplication.class)
public class TestCreateItemAndTemplate {

    @Autowired
    private RiskCheckTemplateItemService itemService;

    @Autowired
    private RiskCheckTemplateService templateService;

    private final int templateNum =2;
    private final int itemNum =5;
    private List<RiskCheckTemplate> templates;
    private List<RiskCheckTemplateItem> items;

    @Before
    public void setUp(){
        templates =new ArrayList<RiskCheckTemplate>();
        items =new ArrayList<RiskCheckTemplateItem>();

        //初始化测试数据
        for (int i = 1; i<= templateNum; i++){
            RiskCheckTemplate template=new RiskCheckTemplate();
            template.setName("检查模板["+i+"]");
            template.setDescription("检查模板内容["+i+"]");
            template.setItems(new ArrayList<RiskCheckTemplateItem>());
            templates.add(template);
        }
        for (int i = 1; i<= itemNum; i++){
            RiskCheckTemplateItem item=new RiskCheckTemplateItem();
            item.setName("检查项目["+i+"]");
            item.setContent("检查项目内容["+i+"]");
            items.add(item);
        }
    }

    @Test
    public void test(){

        //1：新建2个模板(数量 = templateNum)
        for (RiskCheckTemplate template:templates){
            try{
                templateService.createRiskCheckTemplate(template);//id将被自动更新为数据库中为其分配的id
            } catch (RiskCheckException e){
                Assert.fail("create template failure");
            }

        }
        //测试1：测试新建模板
        for (RiskCheckTemplate template:templates){
            try {
                RiskCheckTemplate retrieveTemplate = templateService.getRiskCheckTemplateById(template.getId());
                Assert.assertEquals(template.getName(),retrieveTemplate.getName());
                Assert.assertEquals(template.getDescription(),retrieveTemplate.getDescription());
            } catch (RiskCheckException e){
                Assert.fail("get template failure");
            }
        }

        //2：新建5个模板项目(数量 = itemNum)
        for (RiskCheckTemplateItem item:items){
            try{
                itemService.createRiskCheckTemplateItem(item);//id将被自动更新为数据库中为其分配的id
            } catch (RiskCheckException e){
                Assert.fail("create item failure");
            }
        }
        //测试2：测试新建模板项目
        for (RiskCheckTemplateItem item:items){
            try{
                RiskCheckTemplateItem retrieveItem = itemService.getRiskCheckTemplateItemById(item.getId());
                Assert.assertEquals(item.getName(),retrieveItem.getName());
                Assert.assertEquals(item.getContent(),retrieveItem.getContent());
            } catch (RiskCheckException e){
                Assert.fail("get item failure");
            }
        }

        //3：将模板项目关联到模板
        for (RiskCheckTemplate template:templates){
            List<RiskCheckTemplateItem> itemList=new ArrayList<RiskCheckTemplateItem>();
            itemList.addAll(items);
            template.setItems(itemList);
            try{
                templateService.updateRiskCheckTemplate(template);
            } catch (RiskCheckException e){
                Assert.fail("update template failure");
            }

        }
        //测试3：测试模板中对应的模板项目
        for (RiskCheckTemplate template:templates){
            try{
                RiskCheckTemplate retrieveTemplate = templateService.getRiskCheckTemplateById(template.getId());
                List<RiskCheckTemplateItem> templateItems=template.getItems();
                List<RiskCheckTemplateItem> retrieveTemplateItems=retrieveTemplate.getItems();
                //检查数量是否一致
                Assert.assertEquals(templateItems.size(),retrieveTemplateItems.size());
                //检查关联的模板项是否一致
                for (RiskCheckTemplateItem templateItem:templateItems){
                    boolean found=false;
                    for (RiskCheckTemplateItem retrieveTemplateItem:retrieveTemplateItems){
                        if (templateItem.getId()==retrieveTemplateItem.getId()){
                            found=true;
                            break;
                        }
                    }
                    Assert.assertTrue(found);
                }
            } catch (RiskCheckException e){
                Assert.fail("get template failure");
            }
        }
    }

}
