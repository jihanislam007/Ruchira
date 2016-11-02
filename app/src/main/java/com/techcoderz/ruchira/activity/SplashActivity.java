package com.techcoderz.ruchira.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.daasuu.ahp.AnimateHorizontalProgressBar;
import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.utills.UserPreferences;

/**
 * Created by Shahriar on 6/30/2016.
 */
public class SplashActivity extends AppCompatActivity {
    private final String TAG = "SplashActivity";
//    private DotProgressBar dotProgressBar;
    private RelativeLayout relativeLayout;
    private AnimateHorizontalProgressBar progressBar;

    private Runnable loginActivity = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }
    };

    private Runnable mainActivity = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
    };

    String checkToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initialize();
        action();
    }

    private void initialize() {
        relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout);
//        dotProgressBar = (DotProgressBar) findViewById(R.id.dot_progress_bar);
        progressBar = (AnimateHorizontalProgressBar) findViewById(R.id.animate_progress_bar);
        checkToken = UserPreferences.getToken(SplashActivity.this);
    }

    private void action() {
        progressBar.setMax(1000);
        progressBar.setProgress(0);
        progressBar.setProgressWithAnim(1000);
        progressBar.setMaxWithAnim(800);
//        dotProgressBar.setAnimationTime(1000);

        if (checkToken != null) {
            if (getIntent().getBooleanExtra("EXIT", false)) {
                relativeLayout.postDelayed(mainActivity, 4000);
            } else {
                relativeLayout.postDelayed(mainActivity, 4000);
            }
        } else {
            relativeLayout.postDelayed(loginActivity, 4000);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}