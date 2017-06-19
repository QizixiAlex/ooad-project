package com.ooad.acceptancetest;

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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.Assert.*;
/**
 * 测试公司方对安全检查的执行
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RisksystemApplication.class, webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestExecuteRiskCheck {

    @Autowired
    private RiskCheckExecuteService riskCheckExecuteService;
    @Autowired
    private RiskCheckTemplateMapper riskCheckTemplateMapper;
    @Autowired
    private RiskCheckTemplateItemMapper riskCheckTemplateItemMapper;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private RiskCheckPlanMapper riskCheckPlanMapper;
    @Autowired
    private RiskCheckGenerateService riskCheckGenerateService;
    @Autowired
    private RiskCheckViewService riskCheckViewService;


    private List<Company> companies;
    private List<RiskCheckPlan> plans;

    @Before
    public void setUp(){
        //创建测试数据
        //创建公司数据
        int companyNum = 5;
        companies = TestDataGenerator.generateCompines(companyNum);
        companies.forEach(company -> companyMapper.createCompany(company));
        //创建检查项目
        int templateItemNum = 10;
        List<RiskCheckTemplateItem> templateItems = TestDataGenerator.generateRiskCheckTemplateItems(templateItemNum);
        templateItems.forEach(riskCheckTemplateItem -> riskCheckTemplateItemMapper.createRiskCheckTemplateItem(riskCheckTemplateItem));
        //创建检查模板
        int templateNum = 5;
        List<RiskCheckTemplate> templates = TestDataGenerator.generateRiskCheckTemplates(templateNum);
        templates.forEach(template -> riskCheckTemplateMapper.createRiskCheckTemplate(template));
        //创建项目-模板关联
        templates.forEach((RiskCheckTemplate template) -> {templateItems.forEach(riskCheckTemplateItem -> riskCheckTemplateMapper.createItemInTemplate(template.getId(),riskCheckTemplateItem.getId()));});
        //创建检查计划
        int planNum = 5;
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
    public void testExecuteRiskCheck(){
        //对每一家公司的每一轮安全检查进行测试
        for (Company company:companies){
            try {
                List<RiskCheck> riskChecks = riskCheckExecuteService.getRiskCheckList(company.getId());
                for (RiskCheck riskCheck:riskChecks){
                    //实际完成时间应为空
                    assertNull(riskCheck.getActualFinishDate());
                    //检查情况应为排查中
                    assertEquals(CheckStatus.排查中,riskCheck.getStatus());
                    //完成部分排查，更新安全检查状况，状况应为排查中
                    List<RiskCheckItem> items = riskCheck.getItems();
                    //初始情况下检查项应为排查中，完成时间为空
                    items.forEach(item -> {
                        assertNull(item.getFinishDate());
                        assertEquals(CheckStatus.排查中,item.getStatus());
                    });
                    //完成奇数项
                    items.stream().filter(item -> item.getId()%2==0).forEach(item -> {
                        try {
                            riskCheckExecuteService.updateRiskCheckItem(item.getId(),CheckStatus.已完成);
                        } catch (RiskCheckException e) {
                            fail("update item failure");
                        }
                    });
                    //安全检查应仍然为排查中
                    riskCheckExecuteService.updateRiskCheckStatus(riskCheck.getId());
                    riskCheck = riskCheckViewService.getRiskCheck(riskCheck.getId());
                    assertEquals(CheckStatus.排查中,riskCheck.getStatus());
                    //完成偶数项
                    items.stream().filter(item -> item.getId()%2!=0).forEach(item -> {
                        try {
                            riskCheckExecuteService.updateRiskCheckItem(item.getId(),CheckStatus.已完成);
                        } catch (RiskCheckException e) {
                            fail("update item failure");
                        }
                    });
                    //安全检查应仍然为已完成
                    riskCheckExecuteService.updateRiskCheckStatus(riskCheck.getId());
                    riskCheck = riskCheckViewService.getRiskCheck(riskCheck.getId());
                    assertEquals(CheckStatus.已完成,riskCheck.getStatus());
                    //实际完成时间应为当前时间，误差在5秒之内
                    Timestamp currentTimestamp = new Timestamp(new Date().getTime());
                    assertTrue((currentTimestamp.getTime()-riskCheck.getFinishDate().getTime())<5000);
                }
            } catch (RiskCheckException e) {
                fail("get risk checks failure");
            }
        }
    }
}
