package com.ooad.entity;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Qizixi on 2017/5/31.
 */
public class RiskCheck {
    private List<RiskCheckItem> items;
    private Company company;
    private Timestamp actualFinishDate;
    private CheckStatus status;
    //task source is plan name
    private String taskSource;
    private Timestamp startDate;
    private Timestamp finishDate;

    public RiskCheck(){}

    public List<RiskCheckItem> getItems() {
        return items;
    }

    public void setItems(List<RiskCheckItem> items) {
        this.items = items;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Timestamp getActualFinishDate() {
        return actualFinishDate;
    }

    public void setActualFinishDate(Timestamp actualFinishDate) {
        this.actualFinishDate = actualFinishDate;
    }

    public CheckStatus getStatus() {
        return status;
    }

    public void setStatus(CheckStatus status) {
        this.status = status;
    }

    public String getTaskSource() {
        return taskSource;
    }

    public void setTaskSource(String taskSource) {
        this.taskSource = taskSource;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Timestamp finishDate) {
        this.finishDate = finishDate;
    }
}
