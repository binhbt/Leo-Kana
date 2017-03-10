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
 * Created by binhbt on 2/28/2017.
 */
public class AddWordActivity extends BaseActivity{
    @Bind(R.id.mean1)EditText txtMean1;
    @Bind(R.id.mean2)EditText txtMean2;
    @Bind(R.id.mean3)EditText txtMean3;
    @Bind(R.id.des)EditText txtDes;
    @Bind(R.id.btn_save)Button btnSave;
    private int topicId;
    @OnClick(R.id.btn_save)
    public void saveTopic(){
        // All writes must be wrapped in a transaction to facilitate safe multi threading
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Add a person
                Word topic = realm.createObject(Word.class);
                topic.setId(getNextKey(Word.class));
                topic.setTopic_id(topicId);
                topic.setMean1(txtMean1.getText().toString());
                topic.setMean2(txtMean2.getText().toString());
                topic.setMean3(txtMean3.getText().toString());
                topic.setDes(txtDes.getText().toString());
                showToastMessage("Word is added");
                sendEvent(new NotifyEvent(topic, NotifyEvent.Type.WORD_ADDED));
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
            topicId = bundle.getInt(Const.TOPIC_ID, 0);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_word;
    }
}

