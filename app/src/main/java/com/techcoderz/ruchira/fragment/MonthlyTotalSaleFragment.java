package com.techcoderz.ruchira.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.adapter.ReportAdapter;
import com.techcoderz.ruchira.application.RuchiraApplication;
import com.techcoderz.ruchira.model.Report;
import com.techcoderz.ruchira.utills.AppConfig;
import com.techcoderz.ruchira.utills.TaskUtils;
import com.techcoderz.ruchira.utills.UserPreferences;
import com.techcoderz.ruchira.utills.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by priom on 9/19/16.
 */
public class MonthlyTotalSaleFragment extends RuchiraFragment {
    private final static String TAG = "MonthlyTotalSaleFragment";
    Fragment toLaunchFragment = null;

    private List<Report> reportList;
    private RecyclerView report_rcview;
    private TextView name_txt, phone_txt, id_txt, date_txt, total_txt;
    private LinearLayoutManager manager;
    private ReportAdapter reportAdapter;

    public MonthlyTotalSaleFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_monthly_total_sell, container, false);

        setupToolbar();
        initialize(rootView);
        action();
        fetchDataFromServer();
        return rootView;
    }

    private void setupToolbar() {
        ownerActivity.getSupportActionBar().show();
        ownerActivity.getSupportActionBar().setTitle("Monthly Total Sale");
    }

    private void initialize(View rootView) {
        reportList = new ArrayList<>();
        id_txt = (TextView) rootView.findViewById(R.id.id_txt);
        report_rcview = (RecyclerView) rootView.findViewById(R.id.report_rcview);
        phone_txt = (TextView) rootView.findViewById(R.id.phone_txt);
        name_txt = (TextView) rootView.findViewById(R.id.name_txt);
        date_txt = (TextView) rootView.findViewById(R.id.date_txt);
        total_txt = (TextView) rootView.findViewById(R.id.total_txt);


        manager = new LinearLayoutManager(ownerActivity);
        reportAdapter = new ReportAdapter(ownerActivity, reportList);

        report_rcview.setAdapter(reportAdapter);
        report_rcview.setHasFixedSize(true);
        report_rcview.setLayoutManager(manager);
    }

    private void action() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DATE);
        String monthname = (String) android.text.format.DateFormat.format("MMMM", new Date());

        date_txt.setText(monthname + " " + day + "," + year);

    }

    private void fetchDataFromServer() {

        ProgressDialog progressDialog = null;

        progressDialog = new ProgressDialog(ownerActivity);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String tag_string_req = "req_monthly_total_sale";
        final ProgressDialog finalProgressDialog = progressDialog;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_MONTHLY_TOTAL_SALE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response: " + response.toString());
                finalProgressDialog.dismiss();
                execute(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", UserPreferences.getId(ownerActivity));
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DATE);
                params.put("tokenKey", UserPreferences.getToken(ownerActivity));
                params.put("year", year + "");
                params.put("month", month + "");
                return params;
            }

        };

        // Adding request to request queue
        RuchiraApplication.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void execute(String result) {
        Log.d(TAG, result.toString());
        reportList.clear();

        try {

            JSONObject obj = new JSONObject(result);

            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                id_txt.setText("RS ID # " + obj.getString("userId"));
                phone_txt.setText("Cell : " + obj.getString("userPhone"));
                name_txt.setText(obj.getString("userName"));
                total_txt.setText(obj.getString("total"));
                reportList.addAll(TaskUtils.setMonthlyReport(result));
                reportAdapter.notifyDataSetChanged();

                return;

            } else {
                ViewUtils.alertUser(ownerActivity, "Server Error");
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}