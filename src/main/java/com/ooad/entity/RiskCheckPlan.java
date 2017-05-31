package com.ooad.entity;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Qizixi on 2017/5/31.
 */
public class RiskCheckPlan {
    private int id;
    private RiskCheckTemplate template;
    private List<Company> companies;
    private String name;
    private Timestamp startDate;
    private Timestamp finishDate;

    public RiskCheckPlan(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RiskCheckTemplate getTemplate() {
        return template;
    }

    public void setTemplate(RiskCheckTemplate template) {
        this.template = template;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
