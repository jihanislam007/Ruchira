package com.techcoderz.ruchira.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
 * Created by Shahriar on 9/18/2016.
 */
public class AddNewOrderFragment extends RuchiraFragment {
    private final static String TAG = "AddNewOrderFragment";
    String url = "http://sondhan.com/articleApi/android/category";
    Fragment toLaunchFragment = null;
    private LinearLayout linear_layout;
    private Button orderBtn,memoBtn;
    private TextView item_btn;

    public AddNewOrderFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_new_order, container, false);
        setupToolbar();
        initialize(rootView);
        action();
        return rootView;
    }

    private void setupToolbar() {
        ownerActivity.getSupportActionBar().show();
        ownerActivity.getSupportActionBar().setTitle("Add Order");
    }

    private void initialize(View rootView) {
        memoBtn = (Button)rootView.findViewById(R.id.memo_btn);
        item_btn = (TextView) rootView.findViewById(R.id.item_btn);
    }
    private void action(){
        item_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOrderDetailsFragment();
            }
        });

        memoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMemoFragment();
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


    private void openOrderDetailsFragment() {
        toLaunchFragment = new OrderDetailsFragment();
        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(ownerActivity, toLaunchFragment);
            toLaunchFragment = null;
        }
    }

    private void openMemoFragment() {
        toLaunchFragment = new MemoFragment();
        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(ownerActivity, toLaunchFragment);
            toLaunchFragment = null;
        }
    }
}