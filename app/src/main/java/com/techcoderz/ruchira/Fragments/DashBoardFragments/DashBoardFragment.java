package com.techcoderz.ruchira.Fragments.DashBoardFragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.Application.RuchiraApplication;
import com.techcoderz.ruchira.Fragments.OtherFragments.RuchiraFragment;
import com.techcoderz.ruchira.Utils.AppConfig;
import com.techcoderz.ruchira.Utils.NetworkUtils;
import com.techcoderz.ruchira.Utils.UserPreferences;
import com.techcoderz.ruchira.Utils.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shahriar on 9/14/2016.
 */
public class DashBoardFragment extends RuchiraFragment {
    private final static String TAG = "DashBoardFragment";
    private Fragment toLaunchFragment = null;
    private TextView view_more_txt, tvTodaysSale, ordersummary_txt,
            todays_target_txt, outlet_remainning_txt, remainning_txt;

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
        if(NetworkUtils.hasInternetConnection(mFragmentContext)) {
            fetchDataFromServer();
        }
        return rootView;
    }

    private void setupActionBar() {
        mFragmentContext.getSupportActionBar().show();
        mFragmentContext.getSupportActionBar().setTitle("DashBoard");
    }

    private void initialize(View rootView) {
        view_more_txt = (TextView) rootView.findViewById(R.id.view_more_txt);
        tvTodaysSale = (TextView) rootView.findViewById(R.id.blance_txt);
        ordersummary_txt = (TextView) rootView.findViewById(R.id.ordersummary_txt);
        todays_target_txt = (TextView) rootView.findViewById(R.id.todays_target_txt);
        outlet_remainning_txt = (TextView) rootView.findViewById(R.id.outlet_remainning_txt);
        remainning_txt = (TextView) rootView.findViewById(R.id.remainning_txt);
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
        progressDialog = new ProgressDialog(mFragmentContext);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        final ProgressDialog finalProgressDialog = progressDialog;

    }

    private void execute(String result) {
        Log.d(TAG, result.toString());
        try {
            JSONObject obj = new JSONObject(result);
            int responseResult = obj.getInt("success");
            if (responseResult == 1) {
                tvTodaysSale.setText(obj.getString("todaySale") + " ৳");
                ordersummary_txt.setText(obj.getString("orderSummary"));
                String remaining = obj.getString("remaining");
                if (remaining.charAt(0) == '-')
                    remainning_txt.setTextColor(getResources().getColor(R.color.matRed));
                else remainning_txt.setTextColor(getResources().getColor(R.color.matGreen));
                remainning_txt.setText(remaining);
                outlet_remainning_txt.setText(obj.getString("outletRemaining"));
                todays_target_txt.setText(obj.getString("todayTarget"));
                return;

            } else {
                ViewUtils.alertUser(mFragmentContext, "Server Error");
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void openViewDetailsFragment() {
        toLaunchFragment = new ViewDetailsFragment();
        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(mFragmentContext, toLaunchFragment);
            toLaunchFragment = null;
        }
    }
}