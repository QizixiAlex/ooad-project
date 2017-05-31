package com.ooad.entity;

/**
 * Created by Qizixi on 2017/5/31.
 */
public class RiskCheckTemplateItem {
    private String name;
    private int id;
    private String content;

    public RiskCheckTemplateItem(){}

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

    public void setContent(String content) {
        this.content = content;
    }
}
