package com.techcoderz.ruchira.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.application.RuchiraApplication;
import com.techcoderz.ruchira.utills.AppConfig;
import com.techcoderz.ruchira.utills.CustomAsyncTask;
import com.techcoderz.ruchira.utills.UserPreferences;
import com.techcoderz.ruchira.utills.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shahriar on 9/14/2016.
 */
public class DashBoardFragment extends RuchiraFragment {
    private final static String TAG = "DashBoardFragment";
    Fragment toLaunchFragment = null;
    TextView view_more_txt, blance_txt, ordersummary_txt, todays_target_txt, outlet_remainning_txt;

    public DashBoardFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        setupActionBar();
        initialize(rootView);
        action();
        fetchDataFromServer();
        return rootView;
    }

    private void setupActionBar() {
        ownerActivity.getSupportActionBar().show();
    }

    private void initialize(View rootView) {
        view_more_txt = (TextView) rootView.findViewById(R.id.view_more_txt);
        blance_txt = (TextView) rootView.findViewById(R.id.blance_txt);
        ordersummary_txt = (TextView) rootView.findViewById(R.id.ordersummary_txt);
        todays_target_txt = (TextView) rootView.findViewById(R.id.todays_target_txt);
        outlet_remainning_txt = (TextView) rootView.findViewById(R.id.outlet_remainning_txt);
    }

    private void action() {
        view_more_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openViewDetailsFragment();
            }
        });
    }

    private void fetchDataFromServer() {

        String tag_string_req = "req_dashboard";
        ProgressDialog progressDialog = null;

        progressDialog = new ProgressDialog(ownerActivity);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        final ProgressDialog finalProgressDialog = progressDialog;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DASHBOARD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "dashboard Response: " + response.toString());
                finalProgressDialog.dismiss();
                execute(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "dashboard Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", UserPreferences.getId(ownerActivity));
                params.put("token", UserPreferences.getToken(ownerActivity));
                return params;
            }

        };

        // Adding request to request queue
        RuchiraApplication.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void execute(String result) {
        Log.d(TAG, result.toString());

        try {

            JSONObject obj = new JSONObject(result);

            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                blance_txt.setText(obj.getString("balance"));
                ordersummary_txt.setText(obj.getString("orderSummary"));
                todays_target_txt.setText(obj.getString("todayTarget"));
                outlet_remainning_txt.setText(obj.getString("outletRemaining"));
                return;

            } else {
                ViewUtils.alertUser(ownerActivity, "Server Error");
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void openViewDetailsFragment() {
        toLaunchFragment = new ViewDetailsFragment();
        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(ownerActivity, toLaunchFragment);
            toLaunchFragment = null;
        }
    }
}