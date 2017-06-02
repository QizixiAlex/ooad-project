package com.ooad.entity;

import java.sql.Timestamp;

/**
 * Created by Qizixi on 2017/5/31.
 */
public class RiskCheckItem {
    private int id;
    private RiskCheckTemplateItem item;
    private CheckStatus status;
    private Timestamp finishDate;

    public RiskCheckItem(){}

    public RiskCheckItem(RiskCheckTemplateItem item, CheckStatus status, Timestamp finishDate) {
        this.item = item;
        this.status = status;
        this.finishDate = finishDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CheckStatus getStatus() {
        return status;
    }

    public void setStatus(CheckStatus status) {
        this.status = status;
    }

    public Timestamp getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Timestamp finishDate) {
        this.finishDate = finishDate;
    }

    public RiskCheckTemplateItem getItem() {
        return item;
    }

    public void setItem(RiskCheckTemplateItem item) {
        this.item = item;
    }
}
