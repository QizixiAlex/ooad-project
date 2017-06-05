package com.ooad.servicetest;

import com.ooad.RisksystemApplication;
import com.ooad.entity.*;
import com.ooad.mapper.*;
import com.ooad.TestDataGenerator;
import com.ooad.service.RiskCheckGenerateService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by Alex on 2017/6/4.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RisksystemApplication.class, webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestRiskCheckGenerateService {

    @Autowired
    private RiskCheckGenerateService riskCheckGenerateService;
    @Autowired
    private RiskCheckTemplateMapper riskCheckTemplateMapper;
    @Autowired
    private RiskCheckTemplateItemMapper riskCheckTemplateItemMapper;
    @Autowired
    private RiskCheckItemMapper riskCheckItemMapper;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private RiskCheckMapper riskCheckMapper;
    @Autowired
    private RiskCheckPlanMapper riskCheckPlanMapper;

    private RiskCheckPlan plan;
    private List<Company> companies;
    @Before
    public void initializeData(){
        //init compines
        companies = TestDataGenerator.generateCompines(2);
        for (Company company:companies){
            companyMapper.createCompany(company);
        }
        //init templateItems
        List<RiskCheckTemplateItem> templateItems = TestDataGenerator.generateRiskCheckTemplateItems(0);
        for (RiskCheckTemplateItem item:templateItems){
            riskCheckTemplateItemMapper.createRiskCheckTemplateItem(item);
        }
        //init template
        RiskCheckTemplate template = TestDataGenerator.generateRiskCheckTemplates(1).get(0);
        riskCheckTemplateMapper.createRiskCheckTemplate(template);
        for (RiskCheckTemplateItem item:templateItems){
            riskCheckTemplateMapper.createItemInTemplate(template.getId(),item.getId());
        }
        //init plan
        plan = new RiskCheckPlan();
        plan.setName("检查计划");
        plan.setStartDate(new Timestamp(new Date().getTime()));
        plan.setFinishDate(new Timestamp(new Date().getTime()+24*60*60*1000));
        plan.setTemplate(template);
        riskCheckPlanMapper.createRiskCheckPlan(plan);
        for (Company company:companies){
            riskCheckPlanMapper.createCompanyInPlan(plan.getId(),company.getId());
        }
    }

    @Test
    public void testRiskCheckService(){
        riskCheckGenerateService.generateRiskCheck(plan.getId());
        for (Company company:companies){
            List<RiskCheck> riskChecks = riskCheckMapper.getRiskCheckByCompanyId(company.getId());
            //test riskChecks size
            assertEquals(1,riskChecks.size());
            RiskCheck riskCheck = riskChecks.get(0);

        }
    }
}
