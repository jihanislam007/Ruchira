package com.techcoderz.ruchira.Activities;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import com.techcoderz.ruchira.Utils.ViewUtils;

/**
 * Created by Shahriar on 6/16/2016.
 */
public class RuchiraActivity extends AppCompatActivity {

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
    }

    public void logDebug(String message) {
        Log.d(getClass().getName(), message);
    }

    public void logError(String message) {
        Log.e(getClass().getName(), message);
    }


}
