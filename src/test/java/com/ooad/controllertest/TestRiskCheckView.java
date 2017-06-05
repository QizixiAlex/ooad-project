package com.ooad.controllertest;

/**
 * Created by Alex on 2017/6/5.
 */

import com.ooad.RisksystemApplication;
import com.ooad.controller.RiskCheckExecutionController;
import com.ooad.controller.RiskCheckViewController;
import com.ooad.entity.*;
import com.ooad.mapper.*;
import com.ooad.TestDataGenerator;
import com.ooad.service.CompanyService;
import com.ooad.service.RiskCheckGenerateService;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.*;

import static org.junit.Assert.*;
/**
 * Created by Alex on 2017/6/5.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RisksystemApplication.class, webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestRiskCheckView {

    @Autowired
    private RiskCheckGenerateService riskCheckGenerateService;
    @Autowired
    private RiskCheckTemplateMapper riskCheckTemplateMapper;
    @Autowired
    private RiskCheckTemplateItemMapper riskCheckTemplateItemMapper;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private RiskCheckPlanMapper riskCheckPlanMapper;
    @Autowired
    private RiskCheckViewController riskCheckViewController;
    @Autowired
    private RiskCheckExecutionController riskCheckExecuteController;
    @Autowired
    private CompanyService companyService;

    private List<Company> companies;
    private List<RiskCheckPlan> plans;

    private final int companyNum = 5;
    private final int templateNum = 5;
    private final int templateItemNum = 10;
    private final int planNum = 5;

    @Before
    public void initializeData(){
        //init companies
        companies = TestDataGenerator.generateCompines(companyNum);
        companies.forEach(company -> companyMapper.createCompany(company));
        //init templateItems
        List<RiskCheckTemplateItem> templateItems = TestDataGenerator.generateRiskCheckTemplateItems(templateItemNum);
        templateItems.forEach(riskCheckTemplateItem -> riskCheckTemplateItemMapper.createRiskCheckTemplateItem(riskCheckTemplateItem));
        //init templates
        List<RiskCheckTemplate> templates = TestDataGenerator.generateRiskCheckTemplates(templateNum);
        templates.forEach(template -> riskCheckTemplateMapper.createRiskCheckTemplate(template));
        templates.forEach((RiskCheckTemplate template) -> {templateItems.forEach(riskCheckTemplateItem -> riskCheckTemplateMapper.createItemInTemplate(template.getId(),riskCheckTemplateItem.getId()));});
        //init plans
        plans = TestDataGenerator.generateRiskCheckPlans(planNum,templates);
        plans.forEach(riskCheckPlan -> riskCheckPlanMapper.createRiskCheckPlan(riskCheckPlan));
        //add companies to plans
        companies.forEach(company -> {plans.forEach(riskCheckPlan -> riskCheckPlanMapper.createCompanyInPlan(riskCheckPlan.getId(),company.getId()));});
        //update plans
        List<RiskCheckPlan> updatedPlans = new ArrayList<>();
        plans.forEach(riskCheckPlan -> updatedPlans.add(riskCheckPlanMapper.getRiskCheckPlanById(riskCheckPlan.getId())));
        plans = updatedPlans;
        //execute plans
        plans.forEach(riskCheckPlan -> riskCheckGenerateService.generateRiskCheck(riskCheckPlan.getId()));
    }

    //complete a riskcheck
    private void completeRiskCheck(RiskCheck riskCheck){
        riskCheck.getItems().forEach(riskCheckItem -> riskCheckExecuteController.updateRiskCheckItemStatus(riskCheckItem.getId(),CheckStatus.已完成.toString()));
        riskCheckExecuteController.updateRiskCheckStatus(riskCheck.getId());
    }

    private List<RiskCheck> retrieveAllTestRiskChecks(){
        List<RiskCheck> riskChecks = new ArrayList<>();
        for (Company company:companies){
            riskChecks.addAll(riskCheckViewController.getRiskChecksByCompanyId(company.getId()));
        }
        return riskChecks;
    }


    @After
    public void undoDataChanges(){
        List<RiskCheck> riskChecks = riskCheckViewController.getRiskChecks();
        //undo change to all riskchecks and riskcheckitems
        riskChecks.forEach(riskCheck -> {riskCheck.getItems().forEach(riskCheckItem -> riskCheckExecuteController.updateRiskCheckItemStatus(riskCheckItem.getId(),CheckStatus.排查中.toString()));});
        riskChecks.forEach(riskCheck -> riskCheckExecuteController.updateRiskCheckStatus(riskCheck.getId()));
    }

    @Test
    public void testViewRiskCheck(){
        //check total risk check number
        List<RiskCheck> riskChecks = riskCheckViewController.getRiskChecks();
        //add one to it because there is one from data-mysql.sql
        assertEquals(companyNum*templateNum+1,riskChecks.size());
        //check find riskcheck by company and plan
        //by plan
        plans.forEach(riskCheckPlan -> assertEquals(companyNum,riskCheckViewController.getRiskChecksByPlanId(riskCheckPlan.getId()).size()));
        //by company
        companies.forEach(company -> assertEquals(planNum,riskCheckViewController.getRiskChecksByCompanyId(company.getId()).size()));
        //update riskchecks
        riskChecks = retrieveAllTestRiskChecks();
        //check status
        riskChecks.forEach(riskCheck->assertEquals(CheckStatus.排查中,riskCheck.getStatus()));
        //complete even numbers and check
        riskChecks.stream().filter(riskCheck->riskCheck.getId()%2==0).forEach(this::completeRiskCheck);
        //update riskchecks
        riskChecks = retrieveAllTestRiskChecks();
        //odd ones should not be completed
        riskChecks.stream().filter(riskCheck->riskCheck.getId()%2!=0).forEach(riskCheck -> assertEquals(CheckStatus.排查中,riskCheck.getStatus()));
        //even ones should be completed
        riskChecks.stream().filter(riskCheck->riskCheck.getId()%2==0).forEach(riskCheck -> assertEquals(CheckStatus.已完成,riskCheck.getStatus()));
        //complete the odd ones
        riskChecks.stream().filter(riskCheck->riskCheck.getId()%2!=0).forEach(this::completeRiskCheck);
        //update riskchecks
        riskChecks = retrieveAllTestRiskChecks();
        //all should be completed
        riskChecks.forEach(riskCheck->assertEquals(CheckStatus.已完成,riskCheck.getStatus()));
    }

}
