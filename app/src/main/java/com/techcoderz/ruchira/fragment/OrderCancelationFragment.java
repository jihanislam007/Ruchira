package com.techcoderz.ruchira.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.adapter.OrderCancelationSpinnerAdapter;
import com.techcoderz.ruchira.application.RuchiraApplication;
import com.techcoderz.ruchira.model.OrderCancelation;
import com.techcoderz.ruchira.utills.AppConfig;
import com.techcoderz.ruchira.utills.NetworkUtils;
import com.techcoderz.ruchira.utills.TaskUtils;
import com.techcoderz.ruchira.utills.UserPreferences;
import com.techcoderz.ruchira.utills.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by priom on 10/25/16.
 */
public class OrderCancelationFragment extends RuchiraFragment {
    private final static String TAG = "CancelationFragment";
    Fragment toLaunchFragment = null;
    private TextView shope_name_txt, address_txt;
    private Button ok_btn;
    private List<OrderCancelation> cancelationList;

    private AppCompatSpinner beat_spinner;
    private OrderCancelationSpinnerAdapter orderCancelationSpinnerAdapter;

    private int position2 = 0;
    private Bundle bundle;
    private String address = "";
    private String shopeName = "";
    private String getReasonId = "";
    private String shopeProfileId;


    public OrderCancelationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order_cancalation, container, false);

        initialize(rootView);
        action();
        if (NetworkUtils.hasInternetConnection(mFragmentContext)) {
            fetchDataFromServer();
        }
        return rootView;
    }

    private void initialize(View rootView) {
        shope_name_txt = (TextView) rootView.findViewById(R.id.shope_name_txt);
        address_txt = (TextView) rootView.findViewById(R.id.address_txt);

        beat_spinner = (AppCompatSpinner) rootView.findViewById(R.id.beat_spinner);
        ok_btn = (Button) rootView.findViewById(R.id.ok_btn);
        bundle = getArguments();
        shopeName = bundle.getString("getShopeName");
        address = bundle.getString("getAddress");
        shopeProfileId = bundle.getString("shopeProfileId");

        cancelationList = new ArrayList<>();
        orderCancelationSpinnerAdapter = new OrderCancelationSpinnerAdapter(mFragmentContext, R.layout.beat_list, cancelationList);
        orderCancelationSpinnerAdapter.setDropDownViewResource(R.layout.beat_list);

    }

    private void action() {

        beat_spinner.setAdapter(orderCancelationSpinnerAdapter);
        shope_name_txt.setText(shopeName);
        address_txt.setText(address);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtils.hasInternetConnection(mFragmentContext)) {
                    fetchDataFromServerForCancelationSubmit(beat_spinner.getSelectedItem().toString());
                }
            }
        });

        beat_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                position2 = position;
                getReasonId = cancelationList.get(position).getTitle();
                Log.e(TAG, "cancelationList.get(position).getCancelationId() : " + cancelationList.get(position).getCancelationId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            mFragmentContext.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    private void fetchDataFromServerForCancelationSubmit(final String selectedData) {
        ProgressDialog progressDialog = null;
        progressDialog = new ProgressDialog(mFragmentContext);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String tag_string_req = "req_order_cancelation";
        final ProgressDialog finalProgressDialog = progressDialog;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REASON_SUBMIT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response: " + response.toString());
                finalProgressDialog.dismiss();
                processResultForCancetation(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "order Error: " + error.getMessage());
                finalProgressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url

                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = df.format(c.getTime());

                Map<String, String> params = new HashMap<String, String>();
                Log.e(TAG, UserPreferences.getId(mFragmentContext) + UserPreferences.getToken(mFragmentContext) + getReasonId + shopeProfileId + formattedDate);
                params.put("userId", UserPreferences.getId(mFragmentContext));
                params.put("tokenKey", UserPreferences.getToken(mFragmentContext));
                params.put("reason", getReasonId);
                params.put("reasonDate", formattedDate);
                params.put("outletId", shopeProfileId);
                return params;
            }

        };

        // Adding request to request queue
        RuchiraApplication.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void fetchDataFromServer() {

        ProgressDialog progressDialog = null;

        progressDialog = new ProgressDialog(mFragmentContext);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String tag_string_req = "req_order_cancelation_reason";
        final ProgressDialog finalProgressDialog = progressDialog;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REASON_LIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "order Response: " + response.toString());
                finalProgressDialog.dismiss();
                executeForReason(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "order Error: " + error.getMessage());
                finalProgressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", UserPreferences.getId(mFragmentContext));
                params.put("tokenKey", UserPreferences.getToken(mFragmentContext));
                return params;
            }

        };

        // Adding request to request queue
        RuchiraApplication.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void executeForReason(String result) {
        Log.d(TAG, result.toString());
        cancelationList.clear();

        try {

            JSONObject obj = new JSONObject(result);

            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                cancelationList.addAll(TaskUtils.setOrderCancelation(result));
                orderCancelationSpinnerAdapter.notifyDataSetChanged();

                return;

            } else {
                ViewUtils.alertUser(mFragmentContext, "Server Error");
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void processResultForCancetation(String result) {
        Log.d(TAG, result.toString());
        try {

            JSONObject obj = new JSONObject(result);

            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                openOrderFragment();
                return;

            } else {
                ViewUtils.alertUser(mFragmentContext, "Server Error");
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void openOrderFragment() {
        toLaunchFragment = new OrderFragment();
        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(mFragmentContext, toLaunchFragment);
            toLaunchFragment = null;
        }
    }
}