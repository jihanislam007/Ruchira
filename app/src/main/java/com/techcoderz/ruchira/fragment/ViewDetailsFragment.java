package com.techcoderz.ruchira.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.techcoderz.ruchira.application.RuchiraApplication;
import com.techcoderz.ruchira.model.TodayOrder;
import com.techcoderz.ruchira.model.TodaySale;
import com.techcoderz.ruchira.utills.AppConfig;
import com.techcoderz.ruchira.utills.NetworkUtils;
import com.techcoderz.ruchira.utills.TaskUtils;
import com.techcoderz.ruchira.utills.UserPreferences;
import com.techcoderz.ruchira.utills.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shahriar on 9/14/2016.
 */
public class ViewDetailsFragment extends RuchiraFragment {
    private final static String TAG = "ViewDetailsFragment";

    Fragment toLaunchFragment = null;
    TextView total_bit_txt, sale_yesterDay_txt, sale_monthAccumulation_txt, sale_month_day_avg_txt, sale_month_week_avg_txt, today_sale_txt;
    TextView order_yesterday_txt, order_monthAccumulation_txt, order_monthDailyAvg_txt, order_monthWeekly_avg_txt, today_order_txt, total_outlet_txt;

    private List<TodaySale> todaySaleList;
    private List<TodayOrder> todayOrderList;

    public ViewDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_deatils, container, false);
        setupToolbar();
        initialize(rootView);
        action();
        if (NetworkUtils.hasInternetConnection(ownerActivity)) {
            fetchDataFromServer();
        }
        return rootView;
    }

    private void action() {
    }

    private void setupToolbar() {
        ownerActivity.getSupportActionBar().show();
        ownerActivity.getSupportActionBar().setTitle("View Details");
    }

    private void initialize(View rootView) {
        total_bit_txt = (TextView) rootView.findViewById(R.id.total_bit_txt);

        sale_yesterDay_txt = (TextView) rootView.findViewById(R.id.sale_yesterDay_txt);
        sale_monthAccumulation_txt = (TextView) rootView.findViewById(R.id.sale_monthAccumulation_txt);
        sale_month_day_avg_txt = (TextView) rootView.findViewById(R.id.sale_month_day_avg_txt);
        sale_month_week_avg_txt = (TextView) rootView.findViewById(R.id.sale_month_week_avg_txt);
        today_sale_txt = (TextView) rootView.findViewById(R.id.today_sale_txt);
        total_outlet_txt = (TextView) rootView.findViewById(R.id.total_outlet_txt);

        order_yesterday_txt = (TextView) rootView.findViewById(R.id.order_yesterday_txt);
        order_monthAccumulation_txt = (TextView) rootView.findViewById(R.id.order_monthAccumulation_txt);
        order_monthDailyAvg_txt = (TextView) rootView.findViewById(R.id.order_monthDailyAvg_txt);
        order_monthWeekly_avg_txt = (TextView) rootView.findViewById(R.id.order_monthWeekly_avg_txt);
        today_order_txt = (TextView) rootView.findViewById(R.id.today_order_txt);

        todayOrderList = new ArrayList<>();
        todaySaleList = new ArrayList<>();
    }

    private void fetchDataFromServer() {

        ProgressDialog progressDialog = null;

        progressDialog = new ProgressDialog(ownerActivity);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String tag_string_req = "req_viewmore";
        final ProgressDialog finalProgressDialog = progressDialog;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_VIEWMORE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "viewmore Response: " + response.toString());
                finalProgressDialog.dismiss();
                execute(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "viewmore Error: " + error.getMessage());
                finalProgressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", UserPreferences.getId(ownerActivity));
                params.put("tokenKey", UserPreferences.getToken(ownerActivity));
                return params;
            }

        };

        // Adding request to request queue
        RuchiraApplication.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void execute(String result) {
        Log.d(TAG, result.toString());
        todaySaleList.clear();
        todayOrderList.clear();

        try {

            JSONObject obj = new JSONObject(result);

            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                total_bit_txt.setText(obj.getString("totalBeat"));
                total_outlet_txt.setText(obj.getString("totalOutlet"));
                todayOrderList.addAll(TaskUtils.setTodayOrder(result));
                todaySaleList.addAll(TaskUtils.setTodayTotalSale(result));


                sale_yesterDay_txt.setText(todaySaleList.get(0).getYesterdeay());
                sale_monthAccumulation_txt.setText(todaySaleList.get(0).getMonthAccumulation());
                sale_month_day_avg_txt.setText(todaySaleList.get(0).getMonthDailyAvg());
                sale_month_week_avg_txt.setText(todaySaleList.get(0).getMonthWeeklyAvg());
                today_sale_txt.setText(todaySaleList.get(0).getTodaySale());

                order_yesterday_txt.setText(todayOrderList.get(0).getYesterdeay());
                order_monthAccumulation_txt.setText(todayOrderList.get(0).getMonthAccumulation());
                order_monthDailyAvg_txt.setText(todayOrderList.get(0).getMonthDailyAvg());
                order_monthWeekly_avg_txt.setText(todayOrderList.get(0).getMonthWeeklyAvg());
                today_order_txt.setText(todayOrderList.get(0).getTodayOrder());

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