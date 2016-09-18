package com.techcoderz.ruchira.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.utills.ViewUtils;

import org.json.JSONObject;

/**
 * Created by priom on 9/19/16.
 */
public class AllSummaryFragment extends RuchiraFragment {
    private final static String TAG = "AllSummaryFragment";
    String url = "http://sondhan.com/articleApi/android/category";
    Fragment toLaunchFragment = null;

    private RelativeLayout editProfileLayout,sendFeedbackLayout,aboutLayout;

    public AllSummaryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_summary, container, false);
        setupActionBar();
        initialize(rootView);
        action();
        return rootView;
    }

    private void setupActionBar() {
        ownerActivity.getSupportActionBar().show();
//        ownerActivity.getSupportActionBar().setIcon(R.drawable.logo);
//        ownerActivity.getSupportActionBar().setLogo(R.drawable.logo);
    }

    private void initialize(View rootView) {
        editProfileLayout = (RelativeLayout)rootView.findViewById(R.id.editProfileLayout);
        sendFeedbackLayout = (RelativeLayout)rootView.findViewById(R.id.sendFeedbackLayout);
        aboutLayout = (RelativeLayout)rootView.findViewById(R.id.aboutLayout);
    }

    private void action(){
        editProfileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTodaysTotalSaleFragment();
            }
        });

        sendFeedbackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMonthlyTotalSaleFragment();
            }
        });

        aboutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openYearlyTotalSaleFragment();
            }
        });
    }

    private void fetchDataFromServer() {

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.GET, url, "", new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());
                execute(response.toString());

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        Volley.newRequestQueue(ownerActivity).add(jsonRequest);

    }

    private void execute(String result) {
    }

    private void processResult(String result) {
    }

    private void processResult() {

    }


    private void openMonthlyTotalSaleFragment() {
        toLaunchFragment = new MonthlyTotalSaleFragment();
        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(ownerActivity, toLaunchFragment);
            toLaunchFragment = null;
        }
    }

    private void openTodaysTotalSaleFragment() {
        toLaunchFragment = new TodaysTotalSaleFragment();
        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(ownerActivity, toLaunchFragment);
            toLaunchFragment = null;
        }
    }

    private void openYearlyTotalSaleFragment() {
        toLaunchFragment = new YearlyTotalSaleFragment();
        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(ownerActivity, toLaunchFragment);
            toLaunchFragment = null;
        }
    }
}