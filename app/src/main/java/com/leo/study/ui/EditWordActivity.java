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
import com.leo.study.model.Word;

import butterknife.Bind;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by binhbt on 3/2/2017.
 */
public class EditWordActivity extends BaseActivity {
    @Bind(R.id.mean1)
    EditText txtMean1;
    @Bind(R.id.mean2)
    EditText txtMean2;
    @Bind(R.id.mean3)
    EditText txtMean3;
    @Bind(R.id.des)
    EditText txtDes;
    private int id;

    @OnClick(R.id.btn_save)
    public void saveTopic() {
        // All writes must be wrapped in a transaction to facilitate safe multi threading

        // Add a person
        final Word word = realm.where(Word.class).equalTo("id", id).findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                word.setMean1(txtMean1.getText().toString());
                word.setMean2(txtMean2.getText().toString());
                word.setMean3(txtMean3.getText().toString());
                word.setDes(txtDes.getText().toString());

                showToastMessage("Word is saved");
                sendEvent(new NotifyEvent(word, NotifyEvent.Type.WORD_ADDED));
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
        if (bundle != null && bundle.getInt(Const.WORD_ID, 0) > 0) {
            id = bundle.getInt(Const.WORD_ID, 0);
            loadData();
        }
    }

    private void loadData() {
        final Word word = realm.where(Word.class).equalTo("id", id).findFirst();
        txtMean1.setText(word.getMean1());
        txtMean2.setText(word.getMean2());
        txtMean3.setText(word.getMean3());
        txtDes.setText(word.getDes());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_word;
    }
}

