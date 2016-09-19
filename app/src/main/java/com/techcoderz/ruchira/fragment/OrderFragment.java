package com.techcoderz.ruchira.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
 * Created by Shahriar on 9/14/2016.
 */
public class OrderFragment extends RuchiraFragment {
    private final static String TAG = "OrderFragment";
    String url = "http://sondhan.com/articleApi/android/category";
    Fragment toLaunchFragment = null;
    private TextView yet_to_visit_btn,ordered_btn,not_ordered_btn;
    private LinearLayout first_layout,second_layout,third_layout;

    public OrderFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_yet_to_visit, container, false);

        initialize(rootView);
        action();
        return rootView;
    }

    private void initialize(View rootView) {
        yet_to_visit_btn = (TextView)rootView.findViewById(R.id.yet_to_visit_btn);
        ordered_btn = (TextView)rootView.findViewById(R.id.ordered_btn);
        not_ordered_btn = (TextView)rootView.findViewById(R.id.not_ordered_btn);
        first_layout = (LinearLayout)rootView.findViewById(R.id.first_layout);
        second_layout = (LinearLayout)rootView.findViewById(R.id.second_layout);
        third_layout = (LinearLayout)rootView.findViewById(R.id.third_layout);
    }
    private void action(){
        yet_to_visit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                third_layout.setVisibility(View.VISIBLE);
                third_layout.setBackgroundColor(Color.WHITE);
                second_layout.setBackgroundColor(Color.WHITE);
                second_layout.setVisibility(View.VISIBLE);
            }
        });
        ordered_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                third_layout.setVisibility(View.GONE);
            }
        });
        not_ordered_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                third_layout.setVisibility(View.GONE);
                second_layout.setVisibility(View.GONE);
                first_layout.setBackgroundColor(Color.WHITE);
            }
        });

        first_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOpenShopProfile();
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


    private void openOpenShopProfile() {
        toLaunchFragment = new ShopProfileFragment();
        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(ownerActivity, toLaunchFragment);
            toLaunchFragment = null;
        }
    }
}