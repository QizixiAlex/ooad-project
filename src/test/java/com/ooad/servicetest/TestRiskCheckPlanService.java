package com.ooad.servicetest;

import com.ooad.RisksystemApplication;
import com.ooad.entity.*;
import com.ooad.exception.EntityNotFoundException;
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
 * Created by Shijian on 2017/6/6.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RisksystemApplication.class)
public class TestRiskCheckPlanService {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private RiskCheckTemplateItemService riskCheckTemplateItemService;
    @Autowired
    private RiskCheckTemplateService riskCheckTemplateService;
    @Autowired
    private RiskCheckPlanService riskCheckPlanService;

    private List<Company> companies;
    private List<RiskCheckTemplateItem> riskCheckTemplateItems;
    private List<RiskCheckTemplate> riskCheckTemplates;
    private List<RiskCheckPlan> riskCheckPlans;

    @Before
    public void initializeData() throws RiskCheckException {

        companies = new ArrayList<Company>();
        riskCheckTemplateItems = new ArrayList<RiskCheckTemplateItem>();
        riskCheckTemplates = new ArrayList<RiskCheckTemplate>();
        riskCheckPlans = new ArrayList<RiskCheckPlan>();

        for (int i=1;i<=3;i++){
            Company company = new Company();
            company.setId("id"+i+"(TestPlanService)");
            company.setName("公司"+i);
            company.setStatus(CompanyStatus.信息待完善);
            companies.add(company);
            companyService.createCompany(company);

            RiskCheckTemplateItem item = new RiskCheckTemplateItem();
            item.setName("检查项"+i);
            item.setContent("检查项"+i+"的内容与说明");
            riskCheckTemplateItems.add(item);
            riskCheckTemplateItemService.createRiskCheckTemplateItem(item);

            RiskCheckTemplate template = new RiskCheckTemplate();
            template.setName("检查模板"+i);
            template.setDescription("检查模板"+i+"的内容与说明");
            List<RiskCheckTemplateItem> items = new ArrayList<RiskCheckTemplateItem>();
            items.addAll(riskCheckTemplateItems);
            template.setItems(items);
            riskCheckTemplates.add(template);
            riskCheckTemplateService.createRiskCheckTemplate(template);

            RiskCheckPlan plan = new RiskCheckPlan();
            plan.setName("检查计划"+i);
            plan.setStartDate(new Timestamp(new Date().getTime()+24*60*60*1000));
            plan.setFinishDate(new Timestamp(new Date().getTime()+3*24*60*60*1000));
            plan.setTemplate(template);
            List<Company> cs = new ArrayList<Company>();
            cs.addAll(companies);
            plan.setCompanies(cs);
            riskCheckPlans.add(plan);
        }
    }//end of initializeData()

    @Test
    public void testRiskCheckPlanService() throws RiskCheckException  {
        //get 'number' of tuples before create
        List<RiskCheckPlan> dbPlans = riskCheckPlanService.getRiskCheckPlans();
        final int ORINUM=dbPlans.size();

        //create
        for (RiskCheckPlan plan : riskCheckPlans){
            riskCheckPlanService.createRiskCheckPlan(plan);
        }

        //test retrieve all and create
        dbPlans = riskCheckPlanService.getRiskCheckPlans();
        Assert.assertEquals(3,dbPlans.size()-ORINUM);
        for (RiskCheckPlan plan : riskCheckPlans){
            boolean found=false;
            for (RiskCheckPlan dbPlan : dbPlans){
                if (plan.getId()==dbPlan.getId()&&
                        plan.getName().equals(dbPlan.getName())&&
                        (plan.getStartDate().getTime()>=dbPlan.getStartDate().getTime()-1000||plan.getStartDate().getTime()<=dbPlan.getStartDate().getTime()+1000)&&
                        (plan.getFinishDate().getTime()>=dbPlan.getFinishDate().getTime()-1000||plan.getFinishDate().getTime()<=dbPlan.getFinishDate().getTime()+1000)&&
                        plan.getTemplate().getId()==dbPlan.getTemplate().getId()&&
                        plan.getCompanies().size()==dbPlan.getCompanies().size()){
                    found=true;
                    break;
                }
            }
            Assert.assertTrue(found);
        }

        //test retrieve by id
        for (RiskCheckPlan plan : riskCheckPlans){
            RiskCheckPlan retrievePlan = riskCheckPlanService.getRiskCheckPlanById(plan.getId());
            Assert.assertEquals(plan.getId(),retrievePlan.getId());
            Assert.assertEquals(plan.getName(),retrievePlan.getName());
            Assert.assertTrue(plan.getStartDate().getTime()>=retrievePlan.getStartDate().getTime()-1000||plan.getStartDate().getTime()<=retrievePlan.getStartDate().getTime()+1000);
            Assert.assertTrue(plan.getFinishDate().getTime()>=retrievePlan.getFinishDate().getTime()-1000||plan.getFinishDate().getTime()<=retrievePlan.getFinishDate().getTime()+1000);
            Assert.assertEquals(plan.getTemplate().getId(),retrievePlan.getTemplate().getId());
            Assert.assertEquals(plan.getTemplate().getName(),retrievePlan.getTemplate().getName());
            Assert.assertEquals(plan.getTemplate().getDescription(),retrievePlan.getTemplate().getDescription());
            Assert.assertEquals(plan.getTemplate().getItems().size(),retrievePlan.getTemplate().getItems().size());
            Assert.assertEquals(plan.getCompanies().size(),retrievePlan.getCompanies().size());
            for (RiskCheckTemplateItem item : plan.getTemplate().getItems()){
                boolean found=false;
                for (RiskCheckTemplateItem retrieveItem : retrievePlan.getTemplate().getItems()){
                    if (item.getId()==retrieveItem.getId()){
                        found=true;
                        break;
                    }
                }
                Assert.assertTrue(found);
            }
            for (Company company : plan.getCompanies()){
                boolean found=false;
                for (Company retrieveCompany : retrievePlan.getCompanies()){
                    if (company.getId().equals(retrieveCompany.getId())){
                        found=true;
                        break;
                    }
                }
                Assert.assertTrue(found);
            }
        }//end of test retrieve by id

        //test update
        int index=1;
        for (RiskCheckPlan plan : riskCheckPlans){
            plan.setName("更新(update)过的计划名字["+(index++)+"]");
            plan.setStartDate(new Timestamp(new Date().getTime()+10*24*60*60*1000));
            plan.setFinishDate(new Timestamp(new Date().getTime()+20*24*60*60*1000));
            plan.setTemplate(riskCheckTemplates.get(4-index));
            List<Company> cs=new ArrayList<Company>();
            for (int i=index-1;i<3;i++)
                cs.add(companies.get(i));
            plan.setCompanies(cs);
            riskCheckPlanService.updateRiskCheckPlan(plan);
            RiskCheckPlan retrievePlan=riskCheckPlanService.getRiskCheckPlanById(plan.getId());
            Assert.assertEquals(plan.getId(),retrievePlan.getId());
            Assert.assertEquals(plan.getName(),retrievePlan.getName());
            Assert.assertTrue(plan.getStartDate().getTime()>=retrievePlan.getStartDate().getTime()-1000||plan.getStartDate().getTime()<=retrievePlan.getStartDate().getTime()+1000);
            Assert.assertTrue(plan.getFinishDate().getTime()>=retrievePlan.getFinishDate().getTime()-1000||plan.getFinishDate().getTime()<=retrievePlan.getFinishDate().getTime()+1000);
            Assert.assertEquals(plan.getTemplate().getId(),retrievePlan.getTemplate().getId());
            Assert.assertEquals(plan.getTemplate().getName(),retrievePlan.getTemplate().getName());
            Assert.assertEquals(plan.getTemplate().getDescription(),retrievePlan.getTemplate().getDescription());
            Assert.assertEquals(plan.getTemplate().getItems().size(),retrievePlan.getTemplate().getItems().size());
            Assert.assertEquals(plan.getCompanies().size(),retrievePlan.getCompanies().size());
            for (RiskCheckTemplateItem item : plan.getTemplate().getItems()){
                boolean found=false;
                for (RiskCheckTemplateItem retrieveItem : retrievePlan.getTemplate().getItems()){
                    if (item.getId()==retrieveItem.getId()){
                        found=true;
                        break;
                    }
                }
                Assert.assertTrue(found);
            }
            for (Company company : plan.getCompanies()){
                boolean found=false;
                for (Company retrieveCompany : retrievePlan.getCompanies()){
                    if (company.getId().equals(retrieveCompany.getId())){
                        found=true;
                        break;
                    }
                }
                Assert.assertTrue(found);
            }
        }//end of test update

        //test delete
        for (RiskCheckPlan plan:riskCheckPlans){
            riskCheckPlanService.deleteRiskCheckPlan(plan);
            try {
                RiskCheckPlan retrievePlan=riskCheckPlanService.getRiskCheckPlanById(plan.getId());
                Assert.assertTrue(false);
            } catch (Exception e){
                Assert.assertEquals(EntityNotFoundException.class,e.getClass());
            }
        }

    }

}
