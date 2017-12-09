package com.techcoderz.ruchira.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.Activities.LoginActivity;
import com.techcoderz.ruchira.Activities.RuchiraActivity;


public class ViewUtils {

    public static void alertUser(Context context, String message) {
        TDialogHandler.showDialog(context, null, message, "Ok", null, null, null);
    }

    public static void openNavigationDrawerItems(Context context, Fragment fragmentToLaunch) {
        FragmentManager supportFragmentManager = ((RuchiraActivity) context).getSupportFragmentManager();
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); //clear all fragments from back stack
        supportFragmentManager.beginTransaction().replace(R.id.container_body, fragmentToLaunch)
                .commit();

    }

    public static void startLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }


    private static void launchFragmentKeepingInBackStack(Context context, Fragment fragmentToLaunch, String fragmentTag) {
        FragmentManager supportFragmentManager = ((RuchiraActivity) context).getSupportFragmentManager();
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.container_body, fragmentToLaunch, fragmentTag)
                .addToBackStack(null)
                .commit();
    }

    public static void launchFragmentKeepingInBackStack(Context context, Fragment fragmentToLaunch) {
        Log.e("ViewUtil", fragmentToLaunch.getClass().getName());
        launchFragmentKeepingInBackStack(context, fragmentToLaunch, fragmentToLaunch.getClass().getName());
    }

    public static void launchFragmentWithoutKeepingInBackStack(Context context, Fragment fragmentToLaunch) {
        launchFragmentWithoutKeepingInBackStack(context, fragmentToLaunch, fragmentToLaunch.getClass().getName());
    }

    private static void launchFragmentWithoutKeepingInBackStack(Context context, Fragment fragmentToLaunch, String fragmentTag) {
        FragmentManager supportFragmentManager = ((RuchiraActivity) context).getSupportFragmentManager();
        supportFragmentManager.beginTransaction().replace(R.id.container_body, fragmentToLaunch, fragmentTag).commit();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static boolean isPointOutOfView(View w, float x, float y) {
        return x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom();
    }
}
