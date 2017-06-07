package com.ooad.controllertest;

import com.ooad.RisksystemApplication;
import com.ooad.controller.CompanyController;
import com.ooad.entity.Company;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.*;
/**
 * Created by Alex on 2017/6/5.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RisksystemApplication.class, webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestCompany {

    @Autowired
    private CompanyController companyController;

    private List<String[]> testCases;
    private final int caseNum = 5;

    public TestCompany(){
        testCases = new ArrayList<>();
        for (int i=0;i<caseNum;i++){
            String[] testCase = {"id:"+i,"name:"+i,"正常","code"+i,"industryType:"+i,"industry:"+i,"trade:"+i,"contactName:"+i,"contactTel"+i};
            testCases.add(testCase);
        }
    }

    @Before
    public void initialzeData(){
        //create companies
        for (int i=0;i<caseNum;i++){
            String[] testCase = testCases.get(i);
            companyController.createCompany(testCase[0],testCase[1],testCase[2],testCase[3],testCase[4],testCase[5],testCase[6],testCase[7],testCase[8]);
        }
    }

    @Test
    public void testCreateRetriveCompany(){
        //test retrieve company
        for (int i=0;i<caseNum;i++){
            String[] testCase = testCases.get(i);
            String companyId = testCase[0];
            Company company = companyController.getCompany(companyId);
            assertEquals(testCase[0],company.getId());
            assertEquals(testCase[1],company.getName());
            assertEquals(testCase[2],company.getStatus().toString());
            assertEquals(testCase[3],company.getCode());
            assertEquals(testCase[4],company.getIndustryType());
            assertEquals(testCase[5],company.getIndustry());
            assertEquals(testCase[6],company.getTrade());
            assertEquals(testCase[7],company.getContactName());
            assertEquals(testCase[8],company.getContactTel());

        }
        //test retrieve company list
        List<Company> allCompanies = companyController.getCompanies();
        for (int i=0;i<caseNum;i++){
            String[] testCase = testCases.get(i);
            String companyId = testCase[0];
            Company company = allCompanies.stream().filter(company1 -> Objects.equals(company1.getId(), companyId)).findFirst().get();
            assertEquals(testCase[0],company.getId());
            assertEquals(testCase[1],company.getName());
            assertEquals(testCase[2],company.getStatus().toString());
            assertEquals(testCase[3],company.getCode());
            assertEquals(testCase[4],company.getIndustryType());
            assertEquals(testCase[5],company.getIndustry());
            assertEquals(testCase[6],company.getTrade());
            assertEquals(testCase[7],company.getContactName());
            assertEquals(testCase[8],company.getContactTel());
        }
    }

    @Test
    public void testUpdateCompany(){
        //update test cases
        for (String[] testCase:testCases){
            for (int i=0;i<testCases.size();i++){
                if (i==0||i==2){
                    continue;
                }
                testCase[i] += "changed";
            }
            //update status
            testCase[2] = "信息待完善";
            companyController.updateCompany(testCase[0],testCase[1],testCase[2],testCase[3],testCase[4],testCase[5],testCase[6],testCase[7],testCase[8]);
        }
        //check update
        for (int i=0;i<caseNum;i++){
            String[] testCase = testCases.get(i);
            String companyId = testCase[0];
            Company company = companyController.getCompany(companyId);
            assertEquals(testCase[0],company.getId());
            assertEquals(testCase[1],company.getName());
            assertEquals(testCase[2],company.getStatus().toString());
            assertEquals(testCase[3],company.getCode());
            assertEquals(testCase[4],company.getIndustryType());
            assertEquals(testCase[5],company.getIndustry());
            assertEquals(testCase[6],company.getTrade());
            assertEquals(testCase[7],company.getContactName());
            assertEquals(testCase[8],company.getContactTel());
        }
    }
}
