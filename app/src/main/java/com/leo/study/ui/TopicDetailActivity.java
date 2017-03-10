package com.leo.study.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.leo.study.BaseActivity;
import com.leo.study.Const;
import com.leo.study.R;
import com.leo.study.event.NotifyEvent;
import com.leo.study.model.SelectedTopicList;
import com.leo.study.model.Topic;
import com.leo.study.model.Word;
import com.vn.vega.adapter.multipleviewtype.IViewBinder;
import com.vn.vega.adapter.multipleviewtype.VegaBindAdapter;
import com.vn.vega.widget.RecyclerViewWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by binhbt on 2/28/2017.
 */
public class TopicDetailActivity extends BaseActivity{
    @Bind(R.id.list)
    RecyclerViewWrapper mRecycler;
    private VegaBindAdapter mAdapter;
    private int topicId;
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TopicDetailActivity.this, AddWordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(Const.TOPIC_ID, topicId);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getInt(Const.TOPIC_ID, 0) >0) {
            topicId = bundle.getInt(Const.TOPIC_ID, 0);
            loadAllData(topicId);
            showToastMessage("Topic"+ topicId);
        }

    }
    private void loadAllData(int topicId){
        RealmResults<Word> results = realm.where(Word.class).equalTo("topic_id", topicId).equalTo("status", 0).findAll();
        Log.e("Result", "result "+results.size());
        List<Word> topics = new ArrayList<>();
        if (results.size() >0){
            for (Word topic: results){
                topics.add(topic);
            }
        }
        loadDataToView(topics);
    }
    private void loadDataToView(List<Word> topics){

        if (mAdapter == null) {
            mAdapter = new VegaBindAdapter();
            mRecycler.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
        }
        List<IViewBinder> viewBinders = (List<IViewBinder>) (List) topics;
        mAdapter.addAllDataObject(viewBinders);

    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    public void handleEvent(Object event) {
        super.handleEvent(event);
        if (event instanceof NotifyEvent){
            NotifyEvent evt = (NotifyEvent)event;
            if (evt.getType() == NotifyEvent.Type.WORD_ADDED){
                loadAllData(topicId);
            }
            if (evt.getType() == NotifyEvent.Type.WORD_DELETE){
                int deleteId = (int)evt.getPayload();
                Log.e("Delete word", "Id "+deleteId);
                showDeleteDialog(deleteId);
            }
        }
    }

    @Override
    protected boolean isListenOnSleep() {
        return true;
    }
    private void showDeleteDialog(final int id){
        new AlertDialog.Builder(this)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {
                        // continue with delete
                        final Word person = realm.where(Word.class).equalTo("id", id).findFirst();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                person.setStatus(-1);
                                dialog.dismiss();
                                loadAllData(topicId);
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_topic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings && topicId >0) {
            Intent i = new Intent(this, EditTopicActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(Const.TOPIC_ID, topicId);
            i.putExtras(bundle);
            startActivity(i);
            return true;
        }else{
            showToastMessage("You must select at least one Topic.");
        }

        return super.onOptionsItemSelected(item);
    }

}

