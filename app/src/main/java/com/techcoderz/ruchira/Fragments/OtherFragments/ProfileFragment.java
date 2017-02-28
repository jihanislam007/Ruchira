package com.techcoderz.ruchira.Fragments.OtherFragments;

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
import com.squareup.picasso.Picasso;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.Adapters.AreaAdapter;
import com.techcoderz.ruchira.Application.RuchiraApplication;
import com.techcoderz.ruchira.ModelClasses.Area;
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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Shahriar on 9/14/2016.
 */
public class ProfileFragment extends RuchiraFragment {
    private final static String TAG = "ProfileFragment";
    private Fragment toLaunchFragment = null;
    private List<Area> areaList;
    private RecyclerView report_rcview;
    private CircleImageView profile_image;
    private TextView name_txt, joining_date_txt, designation_txt, status_txt;
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
        if(NetworkUtils.hasInternetConnection(mFragmentContext)) {
            fetchDataFromServer();
        }
        return rootView;
    }

    private void setupToolbar() {
        mFragmentContext.getSupportActionBar().show();
        mFragmentContext.getSupportActionBar().setTitle("Profile");
    }

    private void initialize(View rootView) {
        areaList = new ArrayList<>();
        joining_date_txt = (TextView) rootView.findViewById(R.id.joining_date_txt);
        report_rcview = (RecyclerView) rootView.findViewById(R.id.report_rcview);
        designation_txt = (TextView) rootView.findViewById(R.id.designation_txt);
        name_txt = (TextView) rootView.findViewById(R.id.name_txt);
        profile_image = (CircleImageView) rootView.findViewById(R.id.profile_image);
        status_txt = (TextView) rootView.findViewById(R.id.status_txt);

        manager = new LinearLayoutManager(mFragmentContext);
        areaAdapter = new AreaAdapter(mFragmentContext, areaList);
        report_rcview.setAdapter(areaAdapter);
        report_rcview.setHasFixedSize(true);
        report_rcview.setLayoutManager(manager);
    }

    private void fetchDataFromServer() {
        ProgressDialog progressDialog = null;
        progressDialog = new ProgressDialog(mFragmentContext);
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
        areaList.clear();
        try {
            JSONObject obj = new JSONObject(result);
            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                joining_date_txt.setText("Joining Date : "+obj.getString("joiningDate"));
                designation_txt.setText("Designation : "+obj.getString("desgination"));
                name_txt.setText(obj.getString("userName"));
                status_txt.setText(obj.getString("name"));
                Picasso.with(mFragmentContext)
                        .load(obj.getString("profileImg"))
                        .into(profile_image);

                areaList.addAll(TaskUtils.setArea(result));
                areaAdapter.notifyDataSetChanged();
                return;

            } else {
                ViewUtils.alertUser(mFragmentContext, "Server Error");
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}