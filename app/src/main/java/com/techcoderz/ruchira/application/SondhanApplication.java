package com.techcoderz.ruchira.application;

import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.activeandroid.ActiveAndroid;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.techcoderz.ruchira.utills.FcsCacheManager;

/**
 * Created by Shahriar on 6/15/2016.
 */
public class SondhanApplication extends MultiDexApplication {

    public static final String TAG = SondhanApplication.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static SondhanApplication mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        ActiveAndroid.initialize(this);
        FcsCacheManager.getInstance().init(getApplicationContext());
    }

    public static synchronized SondhanApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
