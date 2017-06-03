package com.ooad.mappertest;

import com.ooad.RisksystemApplication;
import com.ooad.entity.Company;
import com.ooad.mapper.CompanyMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import org.hamcrest.beans.SamePropertyValuesAs;

import static com.ooad.entity.CompanyStatus.信息待完善;
import static com.ooad.entity.CompanyStatus.正常;
import static org.junit.Assert.*;
/**
 * Created by Alex on 2017/6/1.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RisksystemApplication.class, webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestCompanyMapper {

    @Autowired
    private CompanyMapper companyMapper;

    private List<Company> companies;

    @Before
    public void initializeData(){
        companies = new ArrayList<>();
        Company company1 = new Company("1","first_company",正常,"a1","production1","production_type1","trade1","jack","123");
        Company company2 = new Company("2","second_company",信息待完善,"a2","production2","production_type2","trade2","bob","456");
        Company company3 = new Company("3","third_company",正常,"a3","production3","production_type3","trade3","lee","789");
        companies.add(company1);
        companies.add(company2);
        companies.add(company3);
    }

    @Test
    public void testCompanyMapper(){
        //test create and retrieve
        for (Company company:companies){
            companyMapper.createCompany(company);
            Company dbCompany = companyMapper.getCompanyById(company.getId());
            assertThat(company, new SamePropertyValuesAs<>(dbCompany));
        }
        //test retrieve all
        List<Company> dbCompines = companyMapper.getCompanies();
        for (int i=0;i<companies.size();i++){
            Company company = companies.get(i);
            Company dbCompany = dbCompines.get(i);
            assertThat(company, new SamePropertyValuesAs<>(dbCompany));
        }
        //test update
        int testIndex = 0;
        Company company = companies.get(testIndex);
        company.setName("newname");
        companyMapper.updateCompany(company);
        Company dbCompany = companyMapper.getCompanyById(company.getId());
        assertThat(company, new SamePropertyValuesAs<>(dbCompany));
    }

}
