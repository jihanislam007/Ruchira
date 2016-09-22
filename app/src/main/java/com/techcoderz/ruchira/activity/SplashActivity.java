package com.techcoderz.ruchira.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
import com.techcoderz.ruchira.R;

/**
 * Created by Shahriar on 6/30/2016.
 */
public class SplashActivity extends AppCompatActivity {
    private final String TAG= "SplashActivity";

    private DotProgressBar dotProgressBar;
    private RelativeLayout relativeLayout;

    private Runnable changeActivity = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initialize();
        action();


//        dotProgressBar.setStartColor(startColor);
//        dotProgressBar.setEndColor(endColor);
//        dotProgressBar.setDotAmount(amount);







//        Thread timer = new Thread() {
//            public void run() {
//                try {
//                    sleep(2000);
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } finally {
//                    Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            }
//        };
//        timer.start();
    }

    private void initialize(){
        relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout);
        dotProgressBar = (DotProgressBar)findViewById(R.id.dot_progress_bar);
    }

    private void action(){
        dotProgressBar.setAnimationTime(1000);
        relativeLayout.postDelayed(changeActivity, 4000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}