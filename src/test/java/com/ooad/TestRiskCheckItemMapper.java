package com.ooad;

import com.ooad.entity.CheckStatus;
import com.ooad.entity.RiskCheckItem;
import com.ooad.entity.RiskCheckTemplateItem;
import com.ooad.mapper.RiskCheckItemMapper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Qizixi on 2017/6/2.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RisksystemApplication.class)
public class TestRiskCheckItemMapper {

    @Autowired
    RiskCheckItemMapper itemMapper;

    private RiskCheckTemplateItem templateItem;
    private RiskCheckItem checkItem;

    public TestRiskCheckItemMapper() throws ParseException {
        templateItem = new RiskCheckTemplateItem();
        templateItem.setContent("check fire alarm");
        templateItem.setId(1);
        templateItem.setName("firecheck");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/09/2007");
        long time = date.getTime();
        Timestamp timestamp = new Timestamp(time);
        checkItem = new RiskCheckItem(templateItem,CheckStatus.已完成,timestamp);
    }

}
