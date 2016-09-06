package com.techcoderz.ruchira.activity;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.techcoderz.ruchira.utills.ViewUtils;

/**
 * Created by Shahriar on 6/16/2016.
 */
public class RuchiraActivity extends AppCompatActivity {

    /*hide soft keyboard if click outside of edit text*/
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View oldFocusedView = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (oldFocusedView instanceof EditText) {
            View newFocusedView = getCurrentFocus();

            int screenCoordinates[] = new int[2];
            newFocusedView.getLocationOnScreen(screenCoordinates);
            float x = event.getRawX() + newFocusedView.getLeft() - screenCoordinates[0];
            float y = event.getRawY() + newFocusedView.getTop() - screenCoordinates[1];

            if (event.getAction() == MotionEvent.ACTION_UP && ViewUtils.isPointOutOfView(newFocusedView, x, y)) {
                ViewUtils.hideSoftKeyboard(this);
            }
        }

        return ret;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        isPlayServiceAvailable();
    }

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    /**
     * Method to verify google play services on the device
     */
//    protected boolean isPlayServiceAvailable() {
//        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
//
//            } else {
//                Toast.makeText(this.getApplicationContext(), "This device is not supported.", Toast.LENGTH_LONG).show();
//                TaskUtils.showGpsTurnOnAlert(this);
//                return false;
//            }
//        }
//        return true;
//    }

    public void logDebug(String message) {
        Log.d(getClass().getName(), message);
    }

    public void logInfo(String message) {
        Log.i(getClass().getName(), message);
    }

    public void logError(String message) {
        Log.e(getClass().getName(), message);
    }


}
