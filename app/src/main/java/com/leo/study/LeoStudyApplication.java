package com.leo.study;
import com.vn.vega.base.VegaApplication;

import io.realm.Realm;

/**
 * Created by binhbt on 2/28/2017.
 */
public class LeoStudyApplication extends VegaApplication{

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Realm. Should only be done once when the application starts.
        Realm.init(this);
    }
}
