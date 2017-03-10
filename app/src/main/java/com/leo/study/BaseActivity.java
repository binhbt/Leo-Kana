package com.leo.study;

import android.os.Bundle;

import com.vn.vega.base.ui.VegaActivity;

import io.realm.Realm;
import io.realm.RealmModel;

/**
 * Created by binhbt on 2/28/2017.
 */
public class BaseActivity extends VegaActivity {
    protected Realm realm;

    @Override
    protected void initView(Bundle savedInstanceState) {
        // Create the Realm instance
        realm = Realm.getDefaultInstance();
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
    }

    public int getNextKey(Class<? extends RealmModel> clazz) {
        try {
            return realm.where(clazz).max("id").intValue() + 1;
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }
}
