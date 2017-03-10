package com.leo.study.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.leo.study.BaseActivity;
import com.leo.study.Const;
import com.leo.study.R;
import com.leo.study.event.NotifyEvent;
import com.leo.study.model.Word;

import butterknife.Bind;

/**
 * Created by binhbt on 2/28/2017.
 */
public class WordDetailActivity extends BaseActivity{
    private int wordId;
    @Bind(R.id.txtMean1)TextView txtMean1;
    @Bind(R.id.txtMean2)TextView txtMean2;
    @Bind(R.id.txtMean3)TextView txtMean3;
    @Bind(R.id.txtDes)TextView txtDes;

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

        if (getIntent().getExtras() != null){
            wordId = getIntent().getExtras().getInt(Const.WORD_ID, 0);
            loadData();
        }
    }
    private void loadData(){
        final Word person = realm.where(Word.class).equalTo("id", wordId).findFirst();
        if (person != null){
            txtMean1.setText(person.getMean1());
            txtMean2.setText(person.getMean2());
            txtMean3.setText(person.getMean3());
            txtDes.setText(person.getDes());
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_word_detail;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_word, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings && wordId >0) {
            Intent i = new Intent(this, EditWordActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(Const.WORD_ID, wordId);
            i.putExtras(bundle);
            startActivity(i);
            return true;
        }else{
            showToastMessage("You must select at least one Topic.");
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void handleEvent(Object event) {
        super.handleEvent(event);
        if (event instanceof NotifyEvent){
            NotifyEvent evt = (NotifyEvent)event;
            if (evt.getType() == NotifyEvent.Type.WORD_ADDED){
                loadData();
            }
        }
    }
}
