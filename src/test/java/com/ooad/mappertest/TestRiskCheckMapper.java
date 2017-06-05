package com.ooad.mappertest;

import com.ooad.RisksystemApplication;
import com.ooad.TestDataGenerator;
import com.ooad.entity.*;
import com.ooad.mapper.CompanyMapper;
import com.ooad.mapper.RiskCheckItemMapper;
import com.ooad.mapper.RiskCheckMapper;
import com.ooad.mapper.RiskCheckTemplateItemMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alex on 2017/6/3.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RisksystemApplication.class, webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestRiskCheckMapper {

    @Autowired
    private RiskCheckTemplateItemMapper riskCheckTemplateItemMapper;
    @Autowired
    private RiskCheckItemMapper riskCheckItemMapper;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private RiskCheckMapper riskCheckMapper;

    private final int batch = 5;
    private List<RiskCheckItem> riskCheckItems;
    private List<Company> companies;
    private List<RiskCheckTemplateItem> templateItems;
    private List<RiskCheck> riskChecks;

    @Before
    public void initializeData() throws ParseException {

        //initialize companies
        companies = TestDataGenerator.generateCompines(batch);
        for (Company company:companies){
            companyMapper.createCompany(company);
        }

        java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf("2007-09-24 10:10:10.0");
        riskChecks = TestDataGenerator.generateRiskChecks(batch,companies,timestamp,CheckStatus.排查中);

        templateItems = TestDataGenerator.generateRiskCheckTemplateItems(batch);
        for (RiskCheckTemplateItem item:templateItems){
            riskCheckTemplateItemMapper.createRiskCheckTemplateItem(item);
        }

        timestamp = java.sql.Timestamp.valueOf("2007-09-24 10:10:10.0");
        riskCheckItems = TestDataGenerator.generateRiskCheckItems(templateItems, CheckStatus.排查中,timestamp);

    }

    @Test
    public void testRetrieveUpdate(){
        //insert riskChecks into database
        for (RiskCheck riskCheck:riskChecks){
            riskCheckMapper.createRiskCheck(riskCheck,1);
        }
        List<RiskCheck> retrievedRiskChecks = new LinkedList<>();
        for (RiskCheck riskCheck:riskChecks){
            retrievedRiskChecks.add(riskCheckMapper.getRiskCheckById(riskCheck.getId()));
        }
        //test create and retrieve
        for (int i=0;i<batch;i++){
            RiskCheck riskCheck = riskChecks.get(i);
            RiskCheck retrievedRiskCheck = retrievedRiskChecks.get(i);
            assertEquals(riskCheck.getStatus(),retrievedRiskCheck.getStatus());
            //assertEquals(riskCheck.getActualFinishDate().getTime(),retrievedRiskCheck.getActualFinishDate().getTime());
            assertEquals(retrievedRiskCheck.getTaskSource(),"firstplan");
        }
        //test retrieve of riskcheckitems
        int sampleId = riskChecks.get(0).getId();
        //insert riskcheckitems
        for (RiskCheckItem item:riskCheckItems){
            riskCheckItemMapper.createRiskCheckItem(item,sampleId);
        }
        RiskCheck sampleRiskCheck = riskCheckMapper.getRiskCheckById(sampleId);
        List<RiskCheckItem> riskCheckItemsOfRiskCheck = sampleRiskCheck.getItems();
        for (int i=0;i<riskCheckItems.size();i++){
            RiskCheckItem item = riskCheckItems.get(i);
            RiskCheckItem retrievedItem = riskCheckItemsOfRiskCheck.get(i);
            assertEquals(item.getStatus(),retrievedItem.getStatus());
            assertEquals(item.getItem().getId(),retrievedItem.getItem().getId());
            //assertEquals(item.getFinishDate().getTime(),retrievedItem.getFinishDate().getTime());
        }
        //test update
        java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf("2017-09-24 10:10:10.0");
        for (RiskCheck riskCheck:riskChecks){
            riskCheck.setStatus(CheckStatus.已完成);
            riskCheck.setFinishDate(timestamp);
            riskCheckMapper.updateRiskCheck(riskCheck);
            RiskCheck retrievedRiskCheck = riskCheckMapper.getRiskCheckById(riskCheck.getId());
            assertEquals(riskCheck.getStatus(),retrievedRiskCheck.getStatus());
            assertEquals(riskCheck.getActualFinishDate().getTime(),retrievedRiskCheck.getActualFinishDate().getTime());
        }
    }
}
