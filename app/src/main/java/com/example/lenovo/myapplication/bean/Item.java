package com.example.lenovo.myapplication.bean;

/**
 * Created by lenovo on 2018/3/28.
 */

public class Item {
    private int id;
    private String desc;

    public Item(int id,String desc) {
        this.id = id;
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
