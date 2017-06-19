package com.ooad.acceptancetest;

import com.ooad.RisksystemApplication;
import com.ooad.TestDataGenerator;
import com.ooad.entity.Company;
import com.ooad.exception.EntityNotFoundException;
import com.ooad.exception.RiskCheckException;
import com.ooad.service.CompanyService;
import org.hamcrest.beans.SamePropertyValuesAs;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
/**
 * 测试对公司数据的创建，修改，删除
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RisksystemApplication.class)
public class TestCompanyService {

    @Autowired
    private CompanyService companyService;

    private List<Company> companies;
    private int testCaseNum = 10;

    public TestCompanyService(){
        companies = TestDataGenerator.generateCompines(testCaseNum);
    }

    @Before
    public void setUp(){
        //初始化测试数据
        companies.forEach(company -> {
            try {
                companyService.createCompany(company);
            } catch (RiskCheckException e) {
                fail("insert failure");
            }
        });
    }

    @Test
    public void test(){
        //测试取回公司数据：比对数据库取回的公司数据和测试用例的公司数据
        List<Company> dbCompanies = new ArrayList<>();
        for (Company company:companies){
            try {
                dbCompanies.add(companyService.getCompanyById(company.getId()));
            } catch (RiskCheckException e) {
                fail("get company failure");
            }
        }
        for (int i=0;i<testCaseNum;i++){
            assertThat(companies.get(i), new SamePropertyValuesAs<>(dbCompanies.get(i)));
        }
        //测试修改公司数据,比对修改后是否一致
        companies.forEach(company -> {
            company.setName(company.getName()+"changed");
            company.setIndustry(company.getIndustry()+"changed");
            company.setIndustryType(company.getIndustryType()+"changed");
            try {
                companyService.updateCompany(company);
            } catch (RiskCheckException e) {
                fail("update failure");
            }
        });
        dbCompanies = new ArrayList<>();
        for (Company company:companies){
            try {
                dbCompanies.add(companyService.getCompanyById(company.getId()));
            } catch (RiskCheckException e) {
                fail("add company failure");
            }
        }
        for (int i=0;i<testCaseNum;i++){
            assertThat(companies.get(i), new SamePropertyValuesAs<>(dbCompanies.get(i)));
        }
        //测试删除公司数据
        companies.forEach(company -> {
            try {
                companyService.deleteCompany(company);
            } catch (RiskCheckException e) {
                fail("delete failure");
            }
        });
        companies.forEach(company -> {
            try {
                companyService.getCompanyById(company.getId());
                fail("delete failure");
            }catch (RiskCheckException e){
                assertTrue(e instanceof EntityNotFoundException);
            }
        });
    }

}
