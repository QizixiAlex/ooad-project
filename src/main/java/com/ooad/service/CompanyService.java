package com.ooad.service;

import com.ooad.entity.Company;
import com.ooad.mapper.CompanyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Alex on 2017/6/4.
 */
@Component
public class CompanyService {

    private CompanyMapper companyMapper;

    @Autowired
    public CompanyService(CompanyMapper companyMapper) {
        this.companyMapper = companyMapper;
    }

    public void createCompany(Company company){
        companyMapper.createCompany(company);
    }

    public void updateCompany(Company company){
        companyMapper.updateCompany(company);
    }

    public Company getCompanyById(String companyId){
        return companyMapper.getCompanyById(companyId);
    }

    public List<Company> getCompanies(){
        return companyMapper.getCompanies();
    }

    public boolean idNotInDB(String id){
        Company company = companyMapper.getCompanyById(id);
        return company==null;
    }
}
