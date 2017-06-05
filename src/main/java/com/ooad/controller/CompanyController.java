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
        if (!companyService.idNotInDB(id)){
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
        if (companyService.idNotInDB(id)){
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

    public List<Company> getCompanies(){
        List<Company> companies = companyService.getCompanies();
        return companies;
    }

    public Company getCompany(String companyId){
        Company company = companyService.getCompanyById(companyId);
        return company;
    }
}
