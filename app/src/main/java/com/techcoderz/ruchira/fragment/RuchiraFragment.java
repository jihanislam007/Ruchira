package com.techcoderz.ruchira.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.techcoderz.ruchira.utills.TaskUtils;

/**
 * Created by Shahriar Workspace on 04-Jan-16.
 */
public abstract class RuchiraFragment extends Fragment {
    protected AppCompatActivity mFragmentContext;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFragmentContext = (AppCompatActivity) activity;
    }

    @Override
    public void onResume() {
        super.onResume();
        mFragmentContext.getSupportActionBar().show();
    }

    public void log(String message) {
        Log.d(getTagText(), message);
    }


    private String getTagText() {
        if (TaskUtils.isEmpty(getTag())) {
            return this.getClass().getName();
        }
        return getTag();
    }

    public void onBackPressed(){

    }
}