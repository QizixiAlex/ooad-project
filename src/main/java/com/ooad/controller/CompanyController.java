package com.ooad.controller;

import com.ooad.entity.Company;
import com.ooad.entity.CompanyStatus;
import com.ooad.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Alex on 2017/6/4.
 */
@Component
public class CompanyController {

    private CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    public void createCompany(String id, String name, String statusStr, String code, String industryType, String industry, String trade, String contactName, String contactTel) {
        //check input id
        if (!companyService.validId(id)){
            return;
        }
        //check input status
        CompanyStatus status = null;
        switch (statusStr){
            case "正常":
                status = CompanyStatus.正常;
                break;
            case "信息待完善":
                status = CompanyStatus.信息待完善;
                break;
        }
        if (status==null){
            return;
        }
        Company company = new Company(id,name,status,code,industryType,industry,trade,contactName,contactTel);
        companyService.createCompany(company);
    }

    public void updateCompany(String id, String name, String statusStr, String code, String industryType, String industry, String trade, String contactName, String contactTel) {
        //check input id
        if (companyService.validId(id)){
            return;
        }
        //check input status
        CompanyStatus status = null;
        switch (statusStr){
            case "正常":
                status = CompanyStatus.正常;
                break;
            case "信息待完善":
                status = CompanyStatus.信息待完善;
                break;
        }
        if (status==null){
            return;
        }
        Company company = new Company(id,name,status,code,industryType,industry,trade,contactName,contactTel);
        companyService.updateCompany(company);
    }

    private Map<String,String> getCompanyDetail(Company company){
        Map<String,String> result = new TreeMap<>();
        result.put("id",company.getId());
        result.put("name",company.getName());
        result.put("status", String.valueOf(company.getStatus()));
        result.put("code",company.getCode());
        result.put("industryType",company.getIndustryType());
        result.put("industry",company.getIndustry());
        result.put("trade",company.getTrade());
        result.put("contactName",company.getContactName());
        result.put("contactTel",company.getContactTel());
        return result;
    }

    public List<Map<String,String>> getCompanies(){
        List<Map<String,String>> result = new LinkedList<>();
        List<Company> companies = companyService.getCompanies();
        for (Company company:companies){
            result.add(getCompanyDetail(company));
        }
        return result;
    }

    public Map<String,String> getCompany(String companyId){
        if (companyService.validId(companyId)){
            return new HashMap<>();
        }
        Company company = companyService.getCompanyById(companyId);
        return getCompanyDetail(company);
    }
}
