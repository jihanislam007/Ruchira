package com.techcoderz.ruchira.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.utills.ViewUtils;

import org.json.JSONObject;

/**
 * Created by Shahriar on 9/14/2016.
 */
public class OutletsFragment extends RuchiraFragment {
    private final static String TAG = "OutletsFragment";
    String url = "http://sondhan.com/articleApi/android/category";
    Fragment toLaunchFragment = null;

    private LinearLayout linear_layout;

    public OutletsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_outlets, container, false);

        initialize(rootView);
        action();
        return rootView;
    }

    private void initialize(View rootView) {
        linear_layout = (LinearLayout)rootView.findViewById(R.id.linear_layout);
    }

    private void action() {
        linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openShopOrderSummaryFragment();
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


    private void openShopOrderSummaryFragment() {
        toLaunchFragment = new ShopOrderSummaryFragment();
        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentWithoutKeepingInBackStack(ownerActivity, toLaunchFragment);
            toLaunchFragment = null;
        }
    }
}