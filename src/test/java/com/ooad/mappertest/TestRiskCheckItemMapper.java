package com.ooad.mappertest;

import com.ooad.RisksystemApplication;
import com.ooad.TestDataGenerator;
import com.ooad.entity.CheckStatus;
import com.ooad.entity.RiskCheckItem;
import com.ooad.entity.RiskCheckTemplateItem;
import com.ooad.mapper.RiskCheckItemMapper;
import com.ooad.mapper.RiskCheckTemplateItemMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Alex on 2017/6/3.
 */
@SuppressWarnings("ALL")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RisksystemApplication.class, webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestRiskCheckItemMapper {

    @Autowired
    private RiskCheckItemMapper itemMapper;

    @Autowired
    private RiskCheckTemplateItemMapper templateItemMapper;

    private List<RiskCheckTemplateItem> templateItems;
    private List<RiskCheckItem> items;
    private final int batchSize = 5;
    //this id_risk_check is from data sql
    private final int id_risk_check = 1;

    @Before
    public void initializeData() throws ParseException {
        //templateItems do not have id
        templateItems = TestDataGenerator.generateRiskCheckTemplateItems(batchSize);
        for (RiskCheckTemplateItem item:templateItems){
            templateItemMapper.createRiskCheckTemplateItem(item);
        }
        //they do now
        templateItems = templateItemMapper.getRiskCheckTemplateItems();
        java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf("2007-09-24 10:10:10.0");
        items = TestDataGenerator.generateRiskCheckItems(templateItems,CheckStatus.排查中,null);
    }

    @Test
    public void testCreateRetrieveUpdate(){
        for (RiskCheckItem item:items){
            itemMapper.createRiskCheckItem(item,id_risk_check);
        }
        //now we assume that retrievedItems have the same order as items
        List<RiskCheckItem> retrievedItems = itemMapper.getRiskCheckItems();
        //check create and retrieve
        for (int i=0;i<items.size();i++){
            RiskCheckItem item = items.get(i);
            RiskCheckItem retrievedItem = retrievedItems.get(i);
            assertEquals(item.getStatus(),retrievedItem.getStatus());
            assertEquals(item.getItem().getId(),retrievedItem.getItem().getId());
            //assertEquals(item.getFinishDate().getTime(),retrievedItem.getFinishDate().getTime());
        }

        //check update
        java.sql.Timestamp newTimestamp = java.sql.Timestamp.valueOf("2008-09-24 10:10:10.0");
        for (int i=0;i<items.size();i++){
            RiskCheckItem item = items.get(i);
            item.setStatus(CheckStatus.已完成);
            item.setFinishDate(newTimestamp);
            item.setId(retrievedItems.get(i).getId());
            itemMapper.updateRiskCheckItem(item);
        }
        retrievedItems = itemMapper.getRiskCheckItems();

        for (int i=0;i<items.size();i++){
            RiskCheckItem item = items.get(i);
            RiskCheckItem retrievedItem = retrievedItems.get(i);
            assertEquals(item.getStatus(),retrievedItem.getStatus());
            assertEquals(item.getItem().getId(),retrievedItem.getItem().getId());
            assertEquals(item.getFinishDate().getTime(),retrievedItem.getFinishDate().getTime());
        }

    }

}
