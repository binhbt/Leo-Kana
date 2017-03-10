package com.leo.study.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.leo.study.BaseActivity;
import com.leo.study.R;
import com.leo.study.event.NotifyEvent;
import com.leo.study.model.Topic;

import butterknife.Bind;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmModel;

/**
 * Created by binhbt on 2/28/2017.
 */
public class AddTopicActivity extends BaseActivity{
    @Bind(R.id.name)EditText txtName;
    @Bind(R.id.des)EditText txtDes;
    @Bind(R.id.btn_save)Button btnSave;
    @OnClick(R.id.btn_save)
    public void saveTopic(){
        // All writes must be wrapped in a transaction to facilitate safe multi threading
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Add a person
                Topic topic = realm.createObject(Topic.class);
                topic.setId(getNextKey(Topic.class));
                topic.setName(txtName.getText().toString());
                topic.setDes(txtDes.getText().toString());
                showToastMessage("Topic is added");
                sendEvent(new NotifyEvent(topic, NotifyEvent.Type.TOPIC_ADDED));
                finish();
            }
        });
    }
    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_topic;
    }
}
