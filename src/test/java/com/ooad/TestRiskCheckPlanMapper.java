package com.ooad;

import com.ooad.entity.Company;
import com.ooad.entity.RiskCheckPlan;
import com.ooad.entity.RiskCheckTemplate;
import com.ooad.entity.RiskCheckTemplateItem;
import com.ooad.mapper.CompanyMapper;
import com.ooad.mapper.RiskCheckPlanMapper;
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ooad.entity.CompanyStatus.正常;

/**
 * Created by Shijian on 2017/6/3.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RisksystemApplication.class)
public class TestRiskCheckPlanMapper {

    @Autowired
    private RiskCheckPlanMapper riskCheckPlanMapper;

    @Autowired
    private RiskCheckTemplateMapper riskCheckTemplateMapper;

    @Autowired
    private RiskCheckTemplateItemMapper riskCheckTemplateItemMapper;

    @Autowired
    private CompanyMapper companyMapper;

    private RiskCheckTemplateItem item;
    private RiskCheckTemplate template;
    private List<RiskCheckPlan> riskCheckPlans;

    @Before
    public void initializeData(){

        item=new RiskCheckTemplateItem();
        item.setName("测试用项目");
        item.setContent("测试用项目的内容");
        riskCheckTemplateItemMapper.createRiskCheckTemplateItem(item);

        template=new RiskCheckTemplate();
        template.setName("测试用模板");
        template.setDescription("测试用模板的描述");
        riskCheckTemplateMapper.createRiskCheckTemplate(template);
        riskCheckTemplateMapper.createItemInTemplate(template.getId(),item.getId());

        companyMapper.createCompany(new Company("1","first_company",正常,"a1","production1","production_type1","trade1","jack","123"));
        companyMapper.createCompany(new Company("2","second_company",正常,"a1","production2","production_type2","trade2","jack2","1233"));

        riskCheckPlans=new ArrayList<RiskCheckPlan>();
        for (int i=1;i<=3;i++){
            RiskCheckPlan plan=new RiskCheckPlan();
            plan.setName("检查计划"+i);
            plan.setStartDate(new Timestamp(new Date().getTime()));
            plan.setFinishDate(new Timestamp(new Date().getTime()+24*60*60*1000));
            plan.setTemplate(template);
            riskCheckPlans.add(plan);
        }
    }

    @Test
    public void testRiskCheckPlanMapper(){

        //get 'number' of tuples before create
        List<RiskCheckPlan> dbPlans = riskCheckPlanMapper.getRiskCheckPlans();
        final int ORINUM=dbPlans.size();

        //create
        for (RiskCheckPlan plan : riskCheckPlans) {
            riskCheckPlanMapper.createRiskCheckPlan(plan);
        }

        //test retrieve all and create
        dbPlans = riskCheckPlanMapper.getRiskCheckPlans();
        Assert.assertEquals(3, dbPlans.size()-ORINUM);
        for (RiskCheckPlan plan:riskCheckPlans){
            boolean found=false;
            for (RiskCheckPlan dbPlan:dbPlans){
                if (plan.getName().equals(dbPlan.getName())){
                    found=true;
                    break;
                }
            }
            Assert.assertTrue(found);
        }

        //test retrieve by id
        for (RiskCheckPlan dbPlan:dbPlans){
            RiskCheckPlan retrievePlan=riskCheckPlanMapper.getRiskCheckPlanById(dbPlan.getId());

            Assert.assertEquals(dbPlan.getId(),retrievePlan.getId());
            Assert.assertTrue(dbPlan.getName().equals(retrievePlan.getName()));
            //Assert.assertTrue(dbPlan.getStartDate().toString().equals(retrievePlan.getStartDate().toString()));
            //Assert.assertTrue(dbPlan.getFinishDate().toString().equals(retrievePlan.getFinishDate().toString()));

            Assert.assertEquals(dbPlan.getTemplate().getId(),retrievePlan.getTemplate().getId());
            Assert.assertEquals(dbPlan.getTemplate().getName(),retrievePlan.getTemplate().getName());
            Assert.assertEquals(dbPlan.getTemplate().getDescription(),retrievePlan.getTemplate().getDescription());
            Assert.assertEquals(dbPlan.getTemplate().getItems().size(),retrievePlan.getTemplate().getItems().size());

            Assert.assertEquals(dbPlan.getCompanies().size(),retrievePlan.getCompanies().size());
        }

        //test update
        int index=1;
        for (RiskCheckPlan dbPlan:dbPlans){
            dbPlan.setName("更新(update)过的计划名字["+index+"]");
            riskCheckPlanMapper.updateRiskCheckPlan(dbPlan);
            RiskCheckPlan retrievePlan=riskCheckPlanMapper.getRiskCheckPlanById(dbPlan.getId());
            Assert.assertTrue(dbPlan.getName().equals(retrievePlan.getName()));
        }

        {//test about "company_in_plan"
            //create company_in_plan
            for (int i=1;i<=2;i++) {
                riskCheckPlanMapper.createCompanyInPlan(riskCheckPlans.get(0).getId(),""+i);
                riskCheckPlanMapper.createCompanyInPlan(riskCheckPlans.get(1).getId(),""+i);
            }

            //retrieve
            RiskCheckPlan plan0 = riskCheckPlanMapper.getRiskCheckPlanById(riskCheckPlans.get(0).getId());
            RiskCheckPlan plan1 = riskCheckPlanMapper.getRiskCheckPlanById(riskCheckPlans.get(1).getId());

            Assert.assertEquals(2, plan0.getCompanies().size());
            Assert.assertEquals(2, plan1.getCompanies().size());

            //delete item_in_template
            riskCheckPlanMapper.deleteCompanyInPlan(riskCheckPlans.get(0).getId(),"1");

            //delete item_in_template by id_template
            riskCheckPlanMapper.deleteCompanyInPlanByIdPlan(riskCheckPlans.get(1).getId());

            //retrieve
            plan0 = riskCheckPlanMapper.getRiskCheckPlanById(riskCheckPlans.get(0).getId());
            plan1 = riskCheckPlanMapper.getRiskCheckPlanById(riskCheckPlans.get(1).getId());

            Assert.assertEquals(2-1,plan0.getCompanies().size());
            Assert.assertEquals(0,plan1.getCompanies().size());

        }//end of test about "company_in_plan"

    }

}
