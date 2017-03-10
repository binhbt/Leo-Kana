package com.leo.study.model;

import com.leo.study.viewmodel.WordItemTestView;
import com.leo.study.viewmodel.WordItemView;
import com.vn.vega.adapter.multipleviewtype.DataBinder;
import com.vn.vega.adapter.multipleviewtype.IViewBinder;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by binhbt on 2/28/2017.
 */
public class Word extends RealmObject implements IViewBinder, Serializable {
    private int id;
    private String mean1;
    private String mean2;
    private String mean3;
    private String des;
    private int topic_id;
    private int status;
    @Ignore
    private TestOption option =TestOption.KANA;
    @Ignore
    private Type type;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(int topic_id) {
        this.topic_id = topic_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMean1() {
        return mean1;
    }

    public void setMean1(String mean1) {
        this.mean1 = mean1;
    }

    public String getMean2() {
        return mean2;
    }

    public void setMean2(String mean2) {
        this.mean2 = mean2;
    }

    public String getMean3() {
        return mean3;
    }

    public void setMean3(String mean3) {
        this.mean3 = mean3;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    @Override
    public DataBinder getViewBinder() {
        if (type == Type.WORD_TEST_VIEW){
            return new WordItemTestView(this);
        }
        return new WordItemView(this);
    }
    public enum Type{
        WORD_TEST_VIEW,
        WORD_LIST_VIEW
    }

    public TestOption getOption() {
        return option;
    }

    public void setOption(TestOption option) {
        this.option = option;
    }
}
