package com.leo.study.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.leo.study.BaseActivity;
import com.leo.study.Const;
import com.leo.study.R;
import com.leo.study.event.NotifyEvent;
import com.leo.study.model.Topic;
import com.leo.study.model.Word;

import butterknife.Bind;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by binhbt on 3/2/2017.
 */
public class EditTopicActivity  extends BaseActivity {
    @Bind(R.id.name)EditText txtName;
    @Bind(R.id.des)EditText txtDes;
    @Bind(R.id.btn_save)Button btnSave;
    private int id;
    @OnClick(R.id.btn_save)
    public void saveTopic(){
        // All writes must be wrapped in a transaction to facilitate safe multi threading
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Add a person
                final Topic topic = realm.where(Topic.class).equalTo("id", id).findFirst();
                topic.setName(txtName.getText().toString());
                topic.setDes(txtDes.getText().toString());
                showToastMessage("Topic is saved");
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
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getInt(Const.TOPIC_ID, 0) >0) {
            id = bundle.getInt(Const.TOPIC_ID, 0);
            loadData();
        }
    }
    private void loadData(){
        final Topic topic = realm.where(Topic.class).equalTo("id", id).findFirst();
        txtName.setText(topic.getName());
        txtDes.setText(topic.getDes());
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_topic;
    }
}
