package com.techcoderz.ruchira.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.Utils.UserPreferences;

/**
 * Created by Shahriar on 6/30/2016.
 */
public class SplashActivity extends AppCompatActivity {
    private final String TAG = "SplashActivity";
    private RelativeLayout relativeLayout;
    private ProgressBar pb;


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
    int intValue = 0;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initialize();
        action();
    }

    private void initialize() {
        relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout);
        checkToken = UserPreferences.getToken(SplashActivity.this);
    }

    private void action() {
        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.colorStatusBar));
        }
        taskProgressBar();
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

    private void taskProgressBar() {
        pb = (ProgressBar) findViewById(R.id.progressBar1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (intValue < 1000) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pb.setProgress(intValue);
                        }
                    });
                    try {
                        Thread.sleep(40);
                        intValue++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}