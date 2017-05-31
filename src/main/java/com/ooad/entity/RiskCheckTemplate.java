package com.ooad.entity;

import java.util.List;

/**
 * Created by Qizixi on 2017/5/31.
 */
public class RiskCheckTemplate {
    private String name;
    private int id;
    private List<RiskCheckTemplateItem> items;
    private String description;

    public RiskCheckTemplate(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<RiskCheckTemplateItem> getItems() {
        return items;
    }

    public void setItems(List<RiskCheckTemplateItem> items) {
        this.items = items;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
