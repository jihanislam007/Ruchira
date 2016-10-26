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
import com.squareup.picasso.Picasso;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.adapter.AreaAdapter;
import com.techcoderz.ruchira.adapter.ReportAdapter;
import com.techcoderz.ruchira.application.RuchiraApplication;
import com.techcoderz.ruchira.model.Area;
import com.techcoderz.ruchira.model.Report;
import com.techcoderz.ruchira.utills.AppConfig;
import com.techcoderz.ruchira.utills.NetworkUtils;
import com.techcoderz.ruchira.utills.TaskUtils;
import com.techcoderz.ruchira.utills.UserPreferences;
import com.techcoderz.ruchira.utills.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Shahriar on 9/14/2016.
 */
public class ProfileFragment extends RuchiraFragment {
    private final static String TAG = "ProfileFragment";
    Fragment toLaunchFragment = null;

    private List<Area> areaList;
    private RecyclerView report_rcview;
    private CircleImageView profile_image;
    private TextView name_txt, joining_date_txt, designation_txt, account_txt;
    private LinearLayoutManager manager;
    private AreaAdapter areaAdapter;

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

        setupToolbar();
        initialize(rootView);
        if(NetworkUtils.hasInternetConnection(ownerActivity)) {
            fetchDataFromServer();
        }
        return rootView;
    }

    private void setupToolbar() {
        ownerActivity.getSupportActionBar().show();
        ownerActivity.getSupportActionBar().setTitle("Profile");
    }

    private void initialize(View rootView) {
        areaList = new ArrayList<>();
        joining_date_txt = (TextView) rootView.findViewById(R.id.joining_date_txt);
        report_rcview = (RecyclerView) rootView.findViewById(R.id.report_rcview);
        designation_txt = (TextView) rootView.findViewById(R.id.designation_txt);
        name_txt = (TextView) rootView.findViewById(R.id.name_txt);
        profile_image = (CircleImageView) rootView.findViewById(R.id.profile_image);
        account_txt = (TextView) rootView.findViewById(R.id.account_txt);

        manager = new LinearLayoutManager(ownerActivity);
        areaAdapter = new AreaAdapter(ownerActivity, areaList);
        report_rcview.setAdapter(areaAdapter);
        report_rcview.setHasFixedSize(true);
        report_rcview.setLayoutManager(manager);
    }

    private void fetchDataFromServer() {

        ProgressDialog progressDialog = null;

        progressDialog = new ProgressDialog(ownerActivity);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String tag_string_req = "req_profile";
        final ProgressDialog finalProgressDialog = progressDialog;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_PROFILE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "profile Response: " + response.toString());
                finalProgressDialog.dismiss();
                execute(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "profile Error: " + error.getMessage());
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
        areaList.clear();

        try {

            JSONObject obj = new JSONObject(result);

            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {

                joining_date_txt.setText(obj.getString("joiningDate"));
                designation_txt.setText(obj.getString("desgination"));
                name_txt.setText(obj.getString("userName"));
                account_txt.setText("");

                Picasso.with(ownerActivity)
                        .load(obj.getString("profileImg"))
                        .into(profile_image);

                areaList.addAll(TaskUtils.setArea(result));
                areaAdapter.notifyDataSetChanged();

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