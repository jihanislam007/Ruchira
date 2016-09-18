package com.techcoderz.ruchira.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.techcoderz.ruchira.R;

/**
 * Created by Shahriar on 9/18/2016.
 */
public class ForgetPasswordActivity extends AppCompatActivity {
    private final String TAG = "ForgetPasswordActivity";

    Button Send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        Send = (Button) findViewById(R.id.send_btn);

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Check Your Mail to get your password", Toast.LENGTH_SHORT).show();
            }
        });

    }

}