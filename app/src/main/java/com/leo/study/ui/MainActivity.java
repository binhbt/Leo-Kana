package com.leo.study.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.leo.study.BaseActivity;
import com.leo.study.Const;
import com.leo.study.R;
import com.leo.study.event.NotifyEvent;
import com.leo.study.model.SelectedTopicList;
import com.leo.study.model.Topic;
import com.leo.study.model.Word;
import com.vn.vega.adapter.multipleviewtype.IViewBinder;
import com.vn.vega.adapter.multipleviewtype.VegaBindAdapter;
import com.vn.vega.base.model.VegaObjectList;
import com.vn.vega.net.RequestLoader;
import com.vn.vega.widget.RecyclerViewWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;

public class MainActivity extends BaseActivity {
    @Bind(R.id.list)
    RecyclerViewWrapper mRecycler;
    private VegaBindAdapter mAdapter;
    private List<Topic> selectedTopic = new ArrayList<>();
    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddTopicActivity.class));
            }
        });
        loadAllData();
//        test();
    }
/*    private void test(){
        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            Log.d("TETS", "Installed package :" + packageInfo.packageName);
            Log.d("TETS", "Source dir : " + packageInfo.sourceDir);
            Log.d("TETS", "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
        }
    }*/
    private void loadAllData(){
        RealmResults<Topic> results = realm.where(Topic.class).equalTo("status", 0).findAll();
        Log.e("Result", "result "+results.size());
        List<Topic> topics = new ArrayList<>();
        if (results.size() >0){
            for (Topic topic: results){
                topics.add(topic);
            }
        }
        loadDataToView(topics);
    }
    private void loadDataToView(List<Topic> topics){
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings && selectedTopic.size() >0) {
            Intent i = new Intent(this, TestActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Const.SELECTED_TOPIC, new SelectedTopicList(selectedTopic));
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
            if (evt.getType() == NotifyEvent.Type.TOPIC_ADDED){
                loadAllData();
            }
            if (evt.getType() == NotifyEvent.Type.TOPIC_DELETE){
                int deleteId = (int)evt.getPayload();
                showDeleteDialog(deleteId);
            }
            if (evt.getType() == NotifyEvent.Type.TOPIC_SELECTED){
                Topic topic = (Topic)evt.getPayload();
                selectedTopic.add(topic);
            }
            if (evt.getType() == NotifyEvent.Type.TOPIC_REMOVED){
                Topic topic = (Topic)evt.getPayload();
                selectedTopic.remove(topic);
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
                        final Topic person = realm.where(Topic.class).equalTo("id", id).findFirst();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                person.setStatus(-1);
                                dialog.dismiss();
                                loadAllData();
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


}
