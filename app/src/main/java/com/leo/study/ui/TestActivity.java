package com.leo.study.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.leo.study.BaseActivity;
import com.leo.study.Const;
import com.leo.study.R;
import com.leo.study.event.NotifyEvent;
import com.leo.study.model.SelectedTopicList;
import com.leo.study.model.TestOption;
import com.leo.study.model.Topic;
import com.leo.study.model.Word;
import com.vn.vega.adapter.multipleviewtype.IViewBinder;
import com.vn.vega.adapter.multipleviewtype.VegaBindAdapter;
import com.vn.vega.widget.RecyclerViewWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by binhbt on 2/28/2017.
 */
public class TestActivity extends BaseActivity {
    @Bind(R.id.list)
    RecyclerViewWrapper mRecycler;
    private VegaBindAdapter mAdapter;

    private List<Integer> selectedTopic;
    //private List<Word> allWord = new ArrayList<>();;
    private List<Word> testList = new ArrayList<>();
    private int totalTestData;
    private int wrong;
    private int testCount;
    private Word currentSampleWord;
    @Bind(R.id.txt_word)
    TextView txtTest;
    @Bind(R.id.txt_count)
    TextView txtCount;

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
        mRecycler.getRecyclerView().setVerticalScrollBarEnabled(false);
        if (getIntent().getExtras() != null) {
            SelectedTopicList selectedTopicList = (SelectedTopicList) getIntent().getExtras().getSerializable(Const.SELECTED_TOPIC);
            if (selectedTopicList != null) {
                selectedTopic = selectedTopicList.getSelectedTopic();
                if (selectedTopic != null && selectedTopic.size() > 0) {
                    //loadData();
                    showOptionTest();
                }
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }
    private TestOption option = TestOption.KANA;
    private void loadData() {
        RealmQuery<Word> query = realm.where(Word.class).equalTo("status", 0);
        int i = 0;
        for (Integer topic : selectedTopic) {
            if (i++ > 0) {
                query = query.or();
            }
            query = query.equalTo("topic_id", topic);
        }
        RealmResults<Word> results = query.findAll();
        Log.e("Result", "result " + results.size());

        if (results != null && results.size() > 0) {
            for (Word word : results) {
                word.setType(Word.Type.WORD_TEST_VIEW);
                word.setOption(option);
                lsWord.add(word);
            }
            //loadDataToView(lsWord);
            shuffleData();
            generateTestData();
        }
    }

    private void shuffleData() {
        long seed = System.nanoTime();
        Collections.shuffle(lsWord, new Random(seed));
        totalTestData = lsWord.size();
        testCount = 0;
        wrong = 0;
    }

    private void generateTestData() {
        testList.clear();
        currentSampleWord = lsWord.get(testCount);
        txtTest.setText(currentSampleWord.getMean1());
        if (option == TestOption.ENVI){
            String txt = currentSampleWord.getMean2();
            if (txt != null && txt.length() >0){
                if (currentSampleWord.getMean3() != null && currentSampleWord.getMean3().length() >0){
                    txt+=" - "+ currentSampleWord.getMean3();
                }
            }else{
                txt = currentSampleWord.getMean2();
            }
            txtTest.setTextSize(16);
            txtTest.setText(txt);
        }else{
            txtTest.setText(currentSampleWord.getMean1());

        }
        testList.add(currentSampleWord);
        int size = 9;
        if (size > lsWord.size() - 1) {
            size = lsWord.size() - 1;
        }
        for (int i = 0; i < size; i++) {
            Word word = getRandomWord();
            while (testList.contains(word)) {
                word = getRandomWord();
            }
            testList.add(word);
        }
        long seed = System.nanoTime();
        Collections.shuffle(testList, new Random(seed));
        loadDataToView(testList);
    }

    private Word getRandomWord() {
        return lsWord.get(new Random().nextInt(lsWord.size()));
    }

    private List<Word> lsWord = new ArrayList<>();

    private void loadDataToView(List<Word> lsWord) {

        txtCount.setText(testCount + "/" + totalTestData);
        if (mAdapter == null) {
            mAdapter = new VegaBindAdapter();
            mRecycler.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
        }
        List<IViewBinder> viewBinders = (List<IViewBinder>) (List) lsWord;
        mAdapter.addAllDataObject(viewBinders);

    }

    private void testProcess(Word word) {
        if (word.getId() == currentSampleWord.getId()) {
            showToastMessage("Perfect");
            testCount++;
            if (testCount == totalTestData) {
                showResult();
            } else {
                generateTestData();
            }
        } else {
            showToastMessage("You Wrong");
            wrong++;
        }

    }

    private void showResult() {
        new AlertDialog.Builder(this)
                .setTitle("Test Result")
                .setMessage("You correct " + (totalTestData - wrong) + " in total " + totalTestData + " word. It " + ((totalTestData - wrong) * 100 / totalTestData) + "%")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {
                        // continue with delete
                        dialog.dismiss();
                        finish();
                    }
                })
                .show();
    }
    private void showOptionTest() {
        new AlertDialog.Builder(this)
                .setTitle("Test Options")
                .setMessage("Kana=>En/Vi Or En/Vi=>Kana")
                .setPositiveButton("En/Vi=>Kana", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {
                        option =TestOption.ENVI;
                        loadData();
                    }
                })
                .setNegativeButton("Kana=>En", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {
                        option =TestOption.KANA;
                        loadData();
                    }
                })
                .show();
    }
    @Override
    public void handleEvent(Object event) {
        super.handleEvent(event);
        if (event instanceof NotifyEvent) {
            NotifyEvent evt = (NotifyEvent) event;
            if (evt.getType() == NotifyEvent.Type.WORD_TEST_SELECTED) {
                Word selectedWord = (Word) evt.getPayload();
                testProcess(selectedWord);
            }
        }
    }
}
