package com.ooad.acceptancetest;

import com.ooad.RisksystemApplication;
import com.ooad.entity.*;
import com.ooad.exception.RiskCheckException;
import com.ooad.service.CompanyService;
import com.ooad.service.RiskCheckPlanService;
import com.ooad.service.RiskCheckTemplateItemService;
import com.ooad.service.RiskCheckTemplateService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 测试创建引用“检查模板”的“检查计划”，并向“检查计划”中添加“公司”
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RisksystemApplication.class)
public class TestCreateRiskCheckPlan {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private RiskCheckTemplateItemService itemService;
    @Autowired
    private RiskCheckTemplateService templateService;
    @Autowired
    private RiskCheckPlanService planService;

    private final int num =3;
    private List<Company> companies;
    private List<RiskCheckTemplateItem> items;
    private List<RiskCheckTemplate> templates;
    private List<RiskCheckPlan> plans;

    //初始化数据
    @Before
    public void setUp(){

        companies = new ArrayList<Company>();
        items = new ArrayList<RiskCheckTemplateItem>();
        templates = new ArrayList<RiskCheckTemplate>();
        plans = new ArrayList<RiskCheckPlan>();

        for (int i=1;i<=num;i++){
            //(1/4)初始化公司并写入数据库
            Company company = new Company();
            company.setId("company"+i+"(TestCreateRiskCheckPlan)");
            company.setName("公司["+i+"]");
            company.setStatus(CompanyStatus.信息待完善);
            try {
                companyService.createCompany(company);
            }catch(RiskCheckException e){
                Assert.fail("create company failure");
            }
            companies.add(company);

            //(2/4)初始化模板项目并写入数据库
            RiskCheckTemplateItem item = new RiskCheckTemplateItem();
            item.setName("检查项"+i);
            item.setContent("检查项"+i+"的内容与说明");
            try{
                itemService.createRiskCheckTemplateItem(item);
            }catch(Exception e){
                Assert.fail("create item failure");
            }
            items.add(item);

            //(3/4)初始化模板并写入数据库
            RiskCheckTemplate template = new RiskCheckTemplate();
            template.setName("检查模板"+i);
            template.setDescription("检查模板"+i+"的内容与说明");
            List<RiskCheckTemplateItem> itemList = new ArrayList<RiskCheckTemplateItem>();
            itemList.addAll(items);
            template.setItems(itemList);
            try{
                templateService.createRiskCheckTemplate(template);
            }catch(Exception e){
                Assert.fail("create template fail");
            }
            templates.add(template);

            //(4/4)初始化计划
            RiskCheckPlan plan = new RiskCheckPlan();
            plan.setName("检查计划"+i);
            plan.setStartDate(new Timestamp(new Date().getTime()+24*60*60*1000));
            plan.setFinishDate(new Timestamp(new Date().getTime()+3*24*60*60*1000));
            plan.setTemplate(template);
            plan.setCompanies(new ArrayList<Company>());
            plans.add(plan);
        }
    }//初始化结束

    @Test
    public void test() {

        //1：新建3个计划(数量 = num)
        for (RiskCheckPlan plan : plans){
            try{
                planService.createRiskCheckPlan(plan);
            }catch(RiskCheckException e){
                Assert.fail("create plan failure");
            }
        }
        //测试1：测试新建计划
        for (RiskCheckPlan plan:plans){
            try {
                RiskCheckPlan retrievePlan = planService.getRiskCheckPlanById(plan.getId());
                Assert.assertEquals(plan.getName(),retrievePlan.getName());
                Assert.assertTrue(plan.getStartDate().getTime()>=retrievePlan.getStartDate().getTime()-1000||plan.getStartDate().getTime()<=retrievePlan.getStartDate().getTime()+1000);
                Assert.assertTrue(plan.getFinishDate().getTime()>=retrievePlan.getFinishDate().getTime()-1000||plan.getFinishDate().getTime()<=retrievePlan.getFinishDate().getTime()+1000);
                Assert.assertEquals(plan.getTemplate().getId(),retrievePlan.getTemplate().getId());
            } catch (RiskCheckException e){
                Assert.fail("get template failure");
            }
        }

        //2：将公司关联到计划
        for (RiskCheckPlan plan : plans){
            List <Company> companyList = new ArrayList<Company>();
            companyList.addAll(companies);
            plan.setCompanies(companyList);
            try{
                planService.updateRiskCheckPlan(plan);
            }catch(RiskCheckException e){
                Assert.fail("update plan failure");
            }
        }
        //测试2：测试公司关联到计划
        for (RiskCheckPlan plan:plans){
            try {
                RiskCheckPlan retrievePlan = planService.getRiskCheckPlanById(plan.getId());
                List<Company> planCompanies=plan.getCompanies();
                List<Company> retrievePlanCompanies=retrievePlan.getCompanies();
                //检查数量是否一致
                Assert.assertEquals(planCompanies.size(),retrievePlanCompanies.size());
                //检查关联的公司是否一致
                for (Company planCompany:planCompanies){
                    boolean found=false;
                    for (Company retrievePlanCompany:retrievePlanCompanies){
                        if (planCompany.getId().equals(retrievePlanCompany.getId())){
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

        //3：减少一家关联到计划的公司
        for (RiskCheckPlan plan : plans){
            List <Company> companyList = new ArrayList<Company>();
            for (int i=0;i<num-1;i++){
                companyList.add(companies.get(i));
            }
            plan.setCompanies(companyList);
            try{
                planService.updateRiskCheckPlan(plan);
            }catch(RiskCheckException e){
                Assert.fail("update plan failure");
            }
        }
        //测试3：减少一家关联到计划的公司
        for (RiskCheckPlan plan:plans){
            try {
                RiskCheckPlan retrievePlan = planService.getRiskCheckPlanById(plan.getId());
                List<Company> planCompanies=plan.getCompanies();
                List<Company> retrievePlanCompanies=retrievePlan.getCompanies();
                //检查数量是否一致
                Assert.assertEquals(planCompanies.size(),retrievePlanCompanies.size());
                //检查关联的公司是否一致
                for (Company planCompany:planCompanies){
                    boolean found=false;
                    for (Company retrievePlanCompany:retrievePlanCompanies){
                        if (planCompany.getId().equals(retrievePlanCompany.getId())){
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
