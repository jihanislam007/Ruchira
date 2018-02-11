package com.techcoderz.ruchira.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.Application.RuchiraApplication;
import com.techcoderz.ruchira.Utils.AppConfig;
import com.techcoderz.ruchira.Utils.NetworkUtils;
import com.techcoderz.ruchira.Utils.TaskUtils;
import com.techcoderz.ruchira.Utils.UserPreferences;
import com.techcoderz.ruchira.Utils.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private Button loginBtn;
    private EditText email, password;
    TextView forgetpass;
    private final String TAG = "LoginActivity";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setToolbar();
        initialize();
        action();
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void initialize() {
        loginBtn = (Button) findViewById(R.id.email_sign_in_button);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        forgetpass = (TextView) findViewById(R.id.forget_password_button);
    }

    private void action() {
        if (UserPreferences.getEmail(this) != "" && UserPreferences.getPassword(this) != "") {
            email.setText(UserPreferences.getEmail(this));
            password.setText(UserPreferences.getPassword(this));
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUserLogin();
            }
        });

        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startForgetPasswordActivity();
            }
        });
    }

    private void handleUserLogin() {
        if (TaskUtils.isEmpty(email.getText().toString()) || TaskUtils.isEmpty(password.getText().toString())) {
            ViewUtils.alertUser(LoginActivity.this, getResources().getString(R.string.please_key_in_all_information));
            return;
        }
        Log.d(TAG, "login param " + email.getText().toString() + " " + password.getText().toString() + " ");
        if (NetworkUtils.hasInternetConnection(this)) {
            fetchDataFromServer(email.getText().toString(), password.getText().toString());
        }
    }

    private void fetchDataFromServer(final String email, final String password) {
        String tag_string_req = "req_login";
        ProgressDialog progressDialog = null;
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        final ProgressDialog finalProgressDialog = progressDialog;

    }

    //    public void handleResult(JSONObject result, String mail, String password) {
    public void handleResult(String result, String mail, String password) {
        Log.d(TAG, result.toString());
        try {
            JSONObject obj = new JSONObject(result);
            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                UserPreferences.savePassword(this, password.toString().trim());
                UserPreferences.saveEmail(this, mail.toString().trim());
                UserPreferences.saveToken(this, obj.getString("tokenKey"));
                Log.d(TAG, UserPreferences.getToken(LoginActivity.this));
                UserPreferences.saveId(this, obj.getString("userId"));
                UserPreferences.saveProfilePicLogin(this, obj.getString("profileImg"));
                UserPreferences.saveDisplayName(this, obj.getString("name"));
                UserPreferences.saveCompanyName(this, obj.getString("companyName"));
                Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show();
                startLauncherActivity();
                return;

            } else {
                ViewUtils.alertUser(LoginActivity.this, "Email address or password entered is incorrect. Please try again.");
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Quit?");
        alertDialogBuilder.setMessage("Are You Sure, Want To Quit?");
        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                System.exit(0);
            }
        });
        alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void startLauncherActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void startForgetPasswordActivity() {
        Intent intent = new Intent(this, ForgetPasswordActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}