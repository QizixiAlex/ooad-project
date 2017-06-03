package com.ooad;

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
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import java.util.Objects;

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

        companies = TestTools.getCompines(batch);

        for (Company company:companies){
            companyMapper.createCompany(company);
        }

        companies = companyMapper.getCompanies();

        for (int i=0;i<companies.size();i++){
            if (Objects.equals(companies.get(i).getId(), "a123")){
                companies.remove(companies.get(i));
                break;
            }
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        riskChecks = TestTools.getRiskChecks(batch,companies,timestamp,CheckStatus.排查中);

        templateItems = TestTools.getRiskCheckTemplateItems(batch);
        for (RiskCheckTemplateItem item:templateItems){
            riskCheckTemplateItemMapper.createRiskCheckTemplateItem(item);
        }
        templateItems = riskCheckTemplateItemMapper.getRiskCheckTemplateItems();

        timestamp = new Timestamp(System.currentTimeMillis());
        riskCheckItems = TestTools.getRiskCheckItems(templateItems, CheckStatus.排查中,timestamp);

    }

    @Test
    public void testRetrieveUpdate(){
        for (RiskCheck riskCheck:riskChecks){
            riskCheckMapper.createRiskCheck(riskCheck,1);
        }
        List<RiskCheck> retrievedRiskChecks = riskCheckMapper.getRiskChecks();
        for (int i=0;i<batch;i++){
            RiskCheck riskCheck = riskChecks.get(i);
            RiskCheck retrievedRiskCheck = retrievedRiskChecks.get(i);
            assertEquals(riskCheck.getStatus(),retrievedRiskCheck.getStatus());
            assertEquals(riskCheck.getFinishDate().getTime(),retrievedRiskCheck.getFinishDate().getTime());
            assertEquals(retrievedRiskCheck.getTaskSource(),"firstplan");
            //todo
            //check plan dates
        }
        int sampleId = riskChecks.get(0).getId();
        for (RiskCheckItem item:riskCheckItems){
            riskCheckItemMapper.createRiskCheckItem(item,sampleId);
        }
        riskCheckItems = riskCheckItemMapper.getRiskCheckItems();

        RiskCheck sampleRiskCheck = riskCheckMapper.getRiskCheckById(sampleId);

        List<RiskCheckItem> riskCheckItemsOfRiskCheck = sampleRiskCheck.getItems();

        for (int i=0;i<riskCheckItems.size();i++){
            RiskCheckItem item = riskCheckItems.get(i);
            RiskCheckItem retrievedItem = riskCheckItemsOfRiskCheck.get(i);
            assertEquals(item.getStatus(),retrievedItem.getStatus());
            assertEquals(item.getItem().getId(),retrievedItem.getItem().getId());
            assertEquals(item.getFinishDate().getTime(),retrievedItem.getFinishDate().getTime());
        }

    }
}
