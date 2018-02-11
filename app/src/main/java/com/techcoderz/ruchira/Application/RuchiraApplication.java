package com.techcoderz.ruchira.Application;

import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.activeandroid.ActiveAndroid;
import com.techcoderz.ruchira.Utils.FcsCacheManager;

/**
 * Created by Shahriar on 6/15/2016.
 */
public class RuchiraApplication extends MultiDexApplication {
    public static final String TAG = "RuchiraApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
        FcsCacheManager.getInstance().init(getApplicationContext());
    }




}
