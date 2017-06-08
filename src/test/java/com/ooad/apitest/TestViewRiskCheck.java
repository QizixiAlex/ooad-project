package com.ooad.apitest;

import com.ooad.RisksystemApplication;
import com.ooad.TestDataGenerator;
import com.ooad.entity.*;
import com.ooad.exception.RiskCheckException;
import com.ooad.mapper.CompanyMapper;
import com.ooad.mapper.RiskCheckPlanMapper;
import com.ooad.mapper.RiskCheckTemplateItemMapper;
import com.ooad.mapper.RiskCheckTemplateMapper;
import com.ooad.service.RiskCheckExecuteService;
import com.ooad.service.RiskCheckGenerateService;
import com.ooad.service.RiskCheckViewService;
import org.hamcrest.beans.SamePropertyValuesAs;
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

import static org.junit.Assert.*;
/**
 *  测试园区方对各公司安全检查状况的审视
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RisksystemApplication.class)
public class TestViewRiskCheck {

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
    private RiskCheckViewService riskCheckViewService;
    @Autowired
    private RiskCheckExecuteService riskCheckExecuteService;

    private List<Company> companies;
    private List<RiskCheckPlan> plans;
    private List<RiskCheck> riskChecks;
    private List<RiskCheckTemplateItem> templateItems;

    private static final int companyNum = 5;
    private static final int templateItemNum = 10;
    private static final int templateNum = 5;
    private static final int planNum = 5;

    private void refreshRiskChecks(){
        riskChecks = new ArrayList<>();
        companies.forEach(company -> {
            try {
                riskCheckViewService.getRiskChecksByCompanyId(company.getId()).forEach(riskCheck -> riskChecks.add(riskCheck));
            } catch (RiskCheckException e) {
                fail("get risk check by company failure");
            }
        });
    }

    private void completeRiskCheck(int riskCheckId){
        try {
            RiskCheck riskCheck = riskCheckViewService.getRiskCheck(riskCheckId);
            riskCheck.getItems().forEach(riskCheckItem->{
                try {
                    riskCheckExecuteService.updateRiskCheckItem(riskCheckItem.getId(),CheckStatus.已完成);
                } catch (RiskCheckException e) {
                    fail("update risk check item failure");
                }
            });
            riskCheckExecuteService.updateRiskCheckStatus(riskCheck.getId());
        } catch (RiskCheckException e) {
            fail("complete risk check failure");
        }
    }

    @Before
    public void setUp(){
        //创建测试数据
        //创建公司数据
        companies = TestDataGenerator.generateCompines(companyNum);
        companies.forEach(company -> companyMapper.createCompany(company));
        //创建检查项目
        templateItems = TestDataGenerator.generateRiskCheckTemplateItems(templateItemNum);
        templateItems.forEach(riskCheckTemplateItem -> riskCheckTemplateItemMapper.createRiskCheckTemplateItem(riskCheckTemplateItem));
        //创建检查模板
        List<RiskCheckTemplate> templates = TestDataGenerator.generateRiskCheckTemplates(templateNum);
        templates.forEach(template -> riskCheckTemplateMapper.createRiskCheckTemplate(template));
        //创建项目-模板关联
        templates.forEach((RiskCheckTemplate template) -> {templateItems.forEach(riskCheckTemplateItem -> riskCheckTemplateMapper.createItemInTemplate(template.getId(),riskCheckTemplateItem.getId()));});
        //创建检查计划
        plans = TestDataGenerator.generateRiskCheckPlans(planNum,templates);
        plans.forEach(riskCheckPlan -> riskCheckPlanMapper.createRiskCheckPlan(riskCheckPlan));
        //创建计划-公司关联
        companies.forEach(company -> {plans.forEach(riskCheckPlan -> riskCheckPlanMapper.createCompanyInPlan(riskCheckPlan.getId(),company.getId()));});
        List<RiskCheckPlan> updatedPlans = new ArrayList<>();
        plans.forEach(riskCheckPlan -> updatedPlans.add(riskCheckPlanMapper.getRiskCheckPlanById(riskCheckPlan.getId())));
        plans = updatedPlans;
        //分发检查计划
        plans.forEach(riskCheckPlan -> {
            try {
                riskCheckGenerateService.generateRiskCheck(riskCheckPlan.getId());
            } catch (RiskCheckException e) {
                fail("risk check generate failure");
            }
        });
    }

    @Test
    public void testViewRiskCheck(){
        //测试根据检查计划找到对应的安全检查
        plans.forEach(riskCheckPlan -> {
            try {
                //测试数量正确性
                assertEquals(companyNum,riskCheckViewService.getRiskChecksByPlanId(riskCheckPlan.getId()).size());
                riskCheckViewService.getRiskChecksByPlanId(riskCheckPlan.getId()).forEach(riskCheck -> {
                    //测试安全检查与检查计划对应属性
                    assertEquals(riskCheckPlan.getName(),riskCheck.getTaskSource());
                    assertTrue((riskCheckPlan.getStartDate().getTime()-riskCheck.getStartDate().getTime())<1000);
                    assertTrue((riskCheckPlan.getFinishDate().getTime()-riskCheck.getFinishDate().getTime())<1000);
                });
            } catch (RiskCheckException e) {
                fail("get risk checks by plan failure");
            }
        });
        //测试根据公司找到对应安全检查
        companies.forEach(company -> {
            try {
                //测试数量正确性
                assertEquals(planNum,riskCheckViewService.getRiskChecksByCompanyId(company.getId()).size());
                //测试安全检查对应公司正确性
                riskCheckViewService.getRiskChecksByCompanyId(company.getId()).forEach(riskCheck -> {
                    assertThat(riskCheck.getCompany(),new SamePropertyValuesAs<>(company));
                });
            } catch (RiskCheckException e) {
                fail("get risk checks by company failure");
            }
        });
        //测试检查项目与模板项目对应关系
        refreshRiskChecks();
        for (RiskCheck riskCheck:riskChecks) {
            List<RiskCheckItem> riskCheckItemList = riskCheck.getItems();
            for (int i = 0;i<riskCheckItemList.size();i++){
                RiskCheckTemplateItem templateItem = templateItems.get(i);
                //测试模板项目内容
                assertThat(riskCheckItemList.get(i).getItem(),new SamePropertyValuesAs<>(templateItem));
                //测试检查项目时间和状态
                //todo
                //assertNull(riskCheckItemList.get(i).getFinishDate());
                assertEquals(CheckStatus.排查中,riskCheckItemList.get(i).getStatus());
            }
        }
        //测试安全检查完成后的状态更新
        refreshRiskChecks();
        //初始状态应为排查中,完成后为已完成
        riskChecks.forEach(riskCheck -> assertEquals(CheckStatus.排查中,riskCheck.getStatus()));
        riskChecks.forEach(riskCheck -> completeRiskCheck(riskCheck.getId()));
        refreshRiskChecks();
        riskChecks.forEach(riskCheck -> assertEquals(CheckStatus.已完成,riskCheck.getStatus()));
        //完成时间应为当前时间5秒之内
        Timestamp currentTimestamp = new Timestamp(new Date().getTime());
        riskChecks.forEach(riskCheck -> assertTrue((currentTimestamp.getTime()-riskCheck.getFinishDate().getTime())<5000));
    }
}
