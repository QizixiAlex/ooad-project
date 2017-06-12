package com.ooad.service;

import com.ooad.entity.Company;
import com.ooad.exception.*;
import com.ooad.mapper.CompanyMapper;
import com.ooad.mapper.RiskCheckPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Alex on 2017/6/4.
 */
@Component
public class CompanyService {

    private CompanyMapper companyMapper;
    private RiskCheckPlanMapper riskCheckPlanMapper;

    @Autowired
    public CompanyService(CompanyMapper companyMapper) {
        this.companyMapper = companyMapper;
    }

    public void createCompany(Company company) throws RiskCheckException {
        //检查输入合法性
        if (company==null||company.getId()==null){
            throw new NullAttributeException("输入为空");
        }
        Company dbCompany = companyMapper.getCompanyById(company.getId());
        if (dbCompany!=null){
            throw new DuplicateAttributeException("公司已存在");
        }
        companyMapper.createCompany(company);
    }

    public void updateCompany(Company company) throws RiskCheckException{
        //检查输入合法性
        if (company==null||company.getId()==null){
            throw new NullAttributeException("输入为空");
        }
        Company dbCompany = companyMapper.getCompanyById(company.getId());
        if (dbCompany==null){
            throw new DuplicateAttributeException("公司不存在");
        }
        companyMapper.updateCompany(company);
    }

    public Company getCompanyById(String companyId) throws RiskCheckException{
        //检查输入合法性
        if (companyId==null){
            throw new NullAttributeException("输入为空");
        }
        Company company = companyMapper.getCompanyById(companyId);
        if (company==null){
            throw new EntityNotFoundException("无对应公司");
        }
        return company;
    }

    public List<Company> getCompanies() throws RiskCheckException{
        List<Company> companies = companyMapper.getCompanies();
        if (companies.isEmpty()){
            throw new EntityNotFoundException("无公司");
        }
        return companies;
    }

    public void deleteCompany(Company company) throws RiskCheckException{
        //输入检查
        if (company==null){
            throw new EntityNotFoundException("无对应公司");
        }
        if(company.getId()==null){
            throw new InvalidAttributeException("ID为空");
        }
        companyMapper.deleteCompany(company);
    }

    public boolean hasNoCompany(String id){
        if (id==null){
            return false;
        }
        Company company = companyMapper.getCompanyById(id);
        return company==null;
    }
}
