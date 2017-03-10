package com.leo.study.model;

import com.leo.study.viewmodel.TopicItemView;
import com.vn.vega.adapter.multipleviewtype.DataBinder;
import com.vn.vega.adapter.multipleviewtype.IViewBinder;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by binhbt on 2/28/2017.
 */
public class Topic extends RealmObject implements IViewBinder, Serializable{
    private int id;
    private String name;
    private String des;
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public DataBinder getViewBinder() {
        return new TopicItemView(this);
    }
}