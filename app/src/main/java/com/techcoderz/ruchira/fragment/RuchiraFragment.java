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
    protected FragmentCallbacks activityCallbacks;

    public void setFragmentName() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ownerActivity = (AppCompatActivity) activity;
//        this.activityCallbacks = (FragmentCallbacks) ownerActivity;
    }

    @Override
    public void onResume() {
        super.onResume();
        ownerActivity.getSupportActionBar().show();
    }

    public void refreshFragmentView(@Nullable Intent refreshPayload) {
    }

    public void log(String message) {
        Log.d(getTagText(), message);
    }

    public void logError(String message) {
        Log.e(getTagText(), message);
    }

    public static final void setAppFont(ViewGroup mContainer, Typeface mFont)
    {
        if (mContainer == null || mFont == null) return;

        final int mCount = mContainer.getChildCount();

        // Loop through all of the children.
        for (int i = 0; i < mCount; ++i)
        {
            final View mChild = mContainer.getChildAt(i);
            if (mChild instanceof TextView)
            {
                // Set the font if it is a TextView.
                ((TextView) mChild).setTypeface(mFont);
            }
            else if (mChild instanceof ViewGroup)
            {
                // Recursively attempt another ViewGroup.
                setAppFont((ViewGroup) mChild, mFont);
            }
//            else if (reflect)
//            {
//                try {
//                    Method mSetTypeface = mChild.getClass().getMethod("setTypeface", Typeface.class);
//                    mSetTypeface.invoke(mChild, mFont);
//                } catch (Exception e) { /* Do something... */ }
//            }
        }
    }

    private String getTagText() {
        if (TaskUtils.isEmpty(getTag())) {
            return this.getClass().getName();
        }
        return getTag();
    }

//    public void startDrawerToggoling() {
//        if (!CommonActivity.toggle.isDrawerIndicatorEnabled()) {
//            CommonActivity.toggle.setDrawerIndicatorEnabled(true);
//            CommonActivity.toggle.syncState();
//        }
//    }
}