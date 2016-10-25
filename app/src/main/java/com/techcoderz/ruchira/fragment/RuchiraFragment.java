package com.techcoderz.ruchira.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techcoderz.ruchira.utills.FragmentCallbacks;
import com.techcoderz.ruchira.utills.TaskUtils;

/**
 * Created by Shahriar Workspace on 04-Jan-16.
 */
public abstract class RuchiraFragment extends Fragment {
    protected AppCompatActivity ownerActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ownerActivity = (AppCompatActivity) activity;
    }

    @Override
    public void onResume() {
        super.onResume();
        ownerActivity.getSupportActionBar().show();
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
}