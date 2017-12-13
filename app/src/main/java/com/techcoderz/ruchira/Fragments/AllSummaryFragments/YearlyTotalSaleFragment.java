package com.techcoderz.ruchira.Fragments.AllSummaryFragments;

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
import com.android.volley.toolbox.StringRequest;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.Adapters.ReportAdapter;
import com.techcoderz.ruchira.Application.RuchiraApplication;
import com.techcoderz.ruchira.Fragments.OtherFragments.RuchiraFragment;
import com.techcoderz.ruchira.ModelClasses.Report;
import com.techcoderz.ruchira.Utils.AppConfig;
import com.techcoderz.ruchira.Utils.NetworkUtils;
import com.techcoderz.ruchira.Utils.TaskUtils;
import com.techcoderz.ruchira.Utils.UserPreferences;
import com.techcoderz.ruchira.Utils.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YearlyTotalSaleFragment extends RuchiraFragment {
    private final static String TAG = "YearlyTotalSaleFragment";
    private List<Report> reportList;
    private RecyclerView report_rcview;
    private TextView name_txt, phone_txt, id_txt, date_txt, total_txt;
    private LinearLayoutManager manager;
    private ReportAdapter reportAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_yearly_total_sell, container, false);
        setupToolbar();
//        initialize(rootView);
        action();
        return rootView;
    }

    private void setupToolbar() {
        mFragmentContext.getSupportActionBar().show();
        mFragmentContext.getSupportActionBar().setTitle("All Summary");
    }

    private void initialize(View rootView) {
        reportList = new ArrayList<>();
        id_txt = (TextView) rootView.findViewById(R.id.id_txt);
        report_rcview = (RecyclerView) rootView.findViewById(R.id.report_rcview);
        phone_txt = (TextView) rootView.findViewById(R.id.phone_txt);
        name_txt = (TextView) rootView.findViewById(R.id.name_txt);
        date_txt = (TextView) rootView.findViewById(R.id.date_txt);
        total_txt = (TextView) rootView.findViewById(R.id.TotalSellTextView);

        manager = new LinearLayoutManager(mFragmentContext);
        reportAdapter = new ReportAdapter(mFragmentContext, reportList);

//        report_rcview.setAdapter(reportAdapter);
        report_rcview.setHasFixedSize(true);
        report_rcview.setLayoutManager(manager);
    }

    private void action() {
        if (NetworkUtils.hasInternetConnection(mFragmentContext)) {
            fetchDataFromServer();
        }
    }

    private void fetchDataFromServer() {
        ProgressDialog progressDialog = null;
        progressDialog = new ProgressDialog(mFragmentContext);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String tag_string_req = "req_yearly_total_sale";
        final ProgressDialog finalProgressDialog = progressDialog;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_YEARLY_TOTAL_SALE, new Response.Listener<String>() {

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
                finalProgressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("userId", UserPreferences.getUserId(mFragmentContext));
                params.put("tokenKey", UserPreferences.getToken(mFragmentContext));
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
                id_txt.setText("SR ID # " + obj.getString("userId"));
                phone_txt.setText("Cell: " + obj.getString("userPhone"));
                name_txt.setText(obj.getString("userName"));
                date_txt.setText(obj.getString("year"));
                total_txt.setText(obj.getString("total") + " BDT");
                reportList.addAll(TaskUtils.setYearlyReport(result));
                reportAdapter.notifyDataSetChanged();
            } else {
                ViewUtils.alertUser(mFragmentContext, "Server Error");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}