package com.ooad;

import com.ooad.entity.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alex on 2017/6/3.
 */
//this class is used to generate test data
public class TestDataGenerator {

    public static List<RiskCheckItem> generateRiskCheckItems(List<RiskCheckTemplateItem> templateItems, CheckStatus status, Timestamp timestamp) throws ParseException {
        int size = templateItems.size();
        List<RiskCheckItem> result = new ArrayList<>();
        for (int i=0;i<size;i++){
            RiskCheckItem item = new RiskCheckItem();
            item.setFinishDate(timestamp);
            item.setStatus(CheckStatus.已完成);
            item.setItem(templateItems.get(i));
            result.add(item);
        }
        return result;
    }

    public static List<Company> generateCompines(int size){
        List<Company> result = new ArrayList<>();
        for (int i=0;i<size;i++){
            Company company = new Company();
            company.setId("id:"+String.valueOf(i));
            company.setCode("code:"+String.valueOf(i));
            company.setName("company:"+String.valueOf(i));
            company.setStatus(CompanyStatus.信息待完善);
            company.setIndustry("industry:"+String.valueOf(i));
            company.setIndustryType("industryType:"+String.valueOf(i));
            company.setTrade("trade:"+String.valueOf(i));
            company.setContactName("contactName:"+String.valueOf(i));
            company.setContactTel("contactTel:"+String.valueOf(i));
            result.add(company);
        }
        return result;
    }

    public static List<RiskCheck> generateRiskChecks(int size, List<Company> companies, Timestamp timestamp, CheckStatus status){
        assert companies.size() == size;
        List<RiskCheck> result = new ArrayList<>();
        for (int i=0;i<size;i++){
            RiskCheck riskCheck = new RiskCheck();
            riskCheck.setActualFinishDate(timestamp);
            riskCheck.setCompany(companies.get(i));
            riskCheck.setStatus(status);
            result.add(riskCheck);
        }
        return result;
    }

    public static List<RiskCheckTemplateItem> generateRiskCheckTemplateItems(int size){
        List<RiskCheckTemplateItem> result = new LinkedList<>();
        for (int i=0;i<size;i++){
            RiskCheckTemplateItem item = new RiskCheckTemplateItem();
            item.setName("检查项"+i);
            item.setContent("检查项"+i+"的内容与说明");
            result.add(item);
        }
        return result;
    }

    public static List<RiskCheckTemplate> generateRiskCheckTemplates(int size){
        List<RiskCheckTemplate> result = new ArrayList<>();
        for (int i=0;i<size;i++){
            RiskCheckTemplate template =new RiskCheckTemplate();
            template.setName("检查模板"+i);
            template.setDescription("检查模板"+i+"的内容与说明");
            result.add(template);
        }
        return result;
    }

    public static List<RiskCheckPlan> generateRiskCheckPlans(int size,List<RiskCheckTemplate> templates){
        assert size==templates.size();
        List<RiskCheckPlan> result = new ArrayList<>();
        for (int i=0;i<size;i++){
            RiskCheckTemplate template = templates.get(i);
            RiskCheckPlan plan=new RiskCheckPlan();
            plan.setName("check plan:"+i);
            plan.setStartDate(new Timestamp(new Date().getTime()));
            plan.setFinishDate(new Timestamp(new Date().getTime()+24*60*60*1000));
            plan.setTemplate(template);
            result.add(plan);
        }
        return result;
    }
}
