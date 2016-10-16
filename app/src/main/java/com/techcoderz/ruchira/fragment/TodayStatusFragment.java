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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.application.RuchiraApplication;
import com.techcoderz.ruchira.model.OutletRemainning;
import com.techcoderz.ruchira.model.Target;
import com.techcoderz.ruchira.utills.AppConfig;
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
public class TodayStatusFragment extends RuchiraFragment {
    private final static String TAG = "TodayStatusFragment";
    private String url = "http://sondhan.com/articleApi/android/category";
    private Fragment toLaunchFragment = null;
    private TextView date_txt, this_month_txt, current_achive_txt, remainnig_target_txt, remainnig_visit_txt, avg_target_visit_txt;
    private TextView total_outlet_txt, outlet_remained_txt, outlet_visited_txt, achive_percent_sr_txt, achive_today_txt;

    private List<Target> targetList;
    private List<OutletRemainning> outletRemainningList;

    public TodayStatusFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_todays_status, container, false);

        initialize(rootView);
        fetchDataFromServer();
        return rootView;
    }

    private void initialize(View rootView) {
        date_txt = (TextView) rootView.findViewById(R.id.date_txt);

        this_month_txt = (TextView) rootView.findViewById(R.id.this_month_txt);
        current_achive_txt = (TextView) rootView.findViewById(R.id.current_achive_txt);
        remainnig_target_txt = (TextView) rootView.findViewById(R.id.remainnig_target_txt);
        remainnig_visit_txt = (TextView) rootView.findViewById(R.id.remainnig_visit_txt);
        avg_target_visit_txt = (TextView) rootView.findViewById(R.id.avg_target_visit_txt);

        total_outlet_txt = (TextView) rootView.findViewById(R.id.total_outlet_txt);
        outlet_remained_txt = (TextView) rootView.findViewById(R.id.outlet_remained_txt);
        outlet_visited_txt = (TextView) rootView.findViewById(R.id.outlet_visited_txt);

        achive_percent_sr_txt = (TextView) rootView.findViewById(R.id.achive_percent_sr_txt);
        achive_today_txt = (TextView) rootView.findViewById(R.id.achive_today_txt);

        targetList = new ArrayList<>();
        outletRemainningList = new ArrayList<>();
    }

    private void fetchDataFromServer() {

        ProgressDialog progressDialog = null;

        progressDialog = new ProgressDialog(ownerActivity);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String tag_string_req = "req_today_status";
        final ProgressDialog finalProgressDialog = progressDialog;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_TODAYS_STATUS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "today_status Response: " + response.toString());
                finalProgressDialog.dismiss();
                execute(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "today_status Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", UserPreferences.getId(ownerActivity));
                params.put("tokenKey", UserPreferences.getToken(ownerActivity));
                return params;
            }

        };

        // Adding request to request queue
        RuchiraApplication.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void execute(String result) {
        Log.d(TAG, result.toString());
        targetList.clear();
        outletRemainningList.clear();

        try {

            JSONObject obj = new JSONObject(result);

            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                date_txt.setText(obj.getString("orderDate"));
                targetList.addAll(TaskUtils.setTarget(result));
                outletRemainningList.addAll(TaskUtils.setOutletRemainning(result));

                this_month_txt.setText(targetList.get(0).getThisMonth());
                current_achive_txt.setText(targetList.get(0).getCurrentAchive());
                remainnig_target_txt.setText(targetList.get(0).getRemainningTarget());
                avg_target_visit_txt.setText(targetList.get(0).getAvgTargetVisit());
                remainnig_visit_txt.setText(targetList.get(0).getRemainingVisit());

                total_outlet_txt.setText(outletRemainningList.get(0).getTotalOutlet());
                outlet_remained_txt.setText(outletRemainningList.get(0).getOutletRemained());
                outlet_visited_txt.setText(outletRemainningList.get(0).getOutletVisited());
                achive_today_txt.setText(outletRemainningList.get(0).getOutletAchieve());
                achive_percent_sr_txt.setText(outletRemainningList.get(0).getAchieveInPercent());

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