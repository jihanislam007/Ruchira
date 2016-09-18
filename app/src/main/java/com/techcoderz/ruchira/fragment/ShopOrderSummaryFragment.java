package com.techcoderz.ruchira.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
 * Created by priom on 9/19/16.
 */
public class ShopOrderSummaryFragment extends RuchiraFragment {
    private final static String TAG = "ShopOrderSummaryFragment";
    String url = "http://sondhan.com/articleApi/android/category";
    Fragment toLaunchFragment = null;
    private LinearLayout first_layout,first_layout2,first_layout3;

    public ShopOrderSummaryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shop_order_summary, container, false);

        initialize(rootView);
        action();
        return rootView;
    }

    private void initialize(View rootView) {
        first_layout = (LinearLayout)rootView.findViewById(R.id.first_layout);
        first_layout2 = (LinearLayout)rootView.findViewById(R.id.first_layout2);
        first_layout3 = (LinearLayout)rootView.findViewById(R.id.first_layout3);
    }

    private void action(){
        first_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCustomerOrderDetailsFragment();
            }
        });

        first_layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCustomerOrderDetailsFragment();
            }
        });

        first_layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCustomerOrderDetailsFragment();
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


    private void openCustomerOrderDetailsFragment() {
        toLaunchFragment = new CustomerOrderDetailsFragment();
        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(ownerActivity, toLaunchFragment);
            toLaunchFragment = null;
        }
    }
}