package com.techcoderz.ruchira.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
public class ProfileFragment extends RuchiraFragment {
    private final static String TAG = "ProfileFragment";
    String url = "http://sondhan.com/articleApi/android/category";
    Fragment toLaunchFragment = null;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        initialize(rootView);
        return rootView;
    }

    private void initialize(View rootView) {
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


    private void openDirectoryFragment() {
//        toLaunchFragment = new DirectoryFragment();
//        if (toLaunchFragment != null) {
//            ViewUtils.launchFragmentWithoutKeepingInBackStack(ownerActivity, toLaunchFragment);
//            toLaunchFragment = null;
//        }
    }
}