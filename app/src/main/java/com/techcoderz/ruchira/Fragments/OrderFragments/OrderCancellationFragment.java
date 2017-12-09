package com.techcoderz.ruchira.Fragments.OrderFragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.Adapters.OrderCancelationSpinnerAdapter;
import com.techcoderz.ruchira.Application.RuchiraApplication;
import com.techcoderz.ruchira.Fragments.OtherFragments.RuchiraFragment;
import com.techcoderz.ruchira.ModelClasses.OrderCancelation;
import com.techcoderz.ruchira.Utils.AppConfig;
import com.techcoderz.ruchira.Utils.NetworkUtils;
import com.techcoderz.ruchira.Utils.TaskUtils;
import com.techcoderz.ruchira.Utils.UserPreferences;
import com.techcoderz.ruchira.Utils.ViewUtils;

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
public class OrderCancellationFragment extends RuchiraFragment {
    private final static String TAG = "CancellationFragment";
    private TextView shope_name_txt, address_txt;
    private Button ok_btn;
    private List<OrderCancelation> cancelationList;
    private AppCompatSpinner beat_spinner;
    private OrderCancelationSpinnerAdapter orderCancelationSpinnerAdapter;
    private int position2 = 0;
    private Bundle bundle;
    private String address = "";
    private String shopName = "";
    private String getReason = "";
    private String outletId, orderId;

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
        mFragmentContext.getSupportActionBar().setTitle("Cancel Order");
        shope_name_txt = (TextView) rootView.findViewById(R.id.shope_name_txt);
        address_txt = (TextView) rootView.findViewById(R.id.address_txt);

        beat_spinner = (AppCompatSpinner) rootView.findViewById(R.id.beat_spinner);
        ok_btn = (Button) rootView.findViewById(R.id.ok_btn);
        bundle = getArguments();
        shopName = bundle.getString("outletName");
        address = bundle.getString("outletAddress");
        outletId = bundle.getString("outletId");
        orderId = bundle.getString("orderId");
        Log.d(TAG, " outlet Id: " + outletId);

        cancelationList = new ArrayList<>();
        orderCancelationSpinnerAdapter = new OrderCancelationSpinnerAdapter(mFragmentContext, R.layout.beat_list, cancelationList);
        orderCancelationSpinnerAdapter.setDropDownViewResource(R.layout.beat_list);
    }

    private void action() {
        beat_spinner.setAdapter(orderCancelationSpinnerAdapter);
        shope_name_txt.setText(shopName);
        address_txt.setText(address);
        beat_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                position2 = position;
                if (!cancelationList.get(position2).getTitle().equals("Select a Reason")) {
                    getReason = cancelationList.get(position).getTitle();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtils.hasInternetConnection(mFragmentContext)) {
                   if (getReason.equals("") || getReason == "") {
                       Toast.makeText(mFragmentContext, "Please Select a Reason", Toast.LENGTH_LONG).show();
                   } else fetchDataFromServerForCancellationSubmit(getReason, orderId);
                }
            }
        });
    }

    private void fetchDataFromServerForCancellationSubmit(final String reason, final String orderId) {
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
                finalProgressDialog.dismiss();
                processResultForCancellation(response);
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
                params.put("userId", UserPreferences.getUserId(mFragmentContext));
                params.put("tokenKey", UserPreferences.getToken(mFragmentContext));
                params.put("reason", reason);
                params.put("orderId", orderId);
                params.put("outletId", outletId);

                Log.d(TAG, "user id: " + UserPreferences.getUserId(mFragmentContext));
                Log.d(TAG, "Token key: " + UserPreferences.getToken(mFragmentContext));
                Log.d(TAG, "Reason: " + reason);
                Log.d(TAG, "outlet Id: " + outletId);
                Log.d(TAG, "order Id: " + orderId);

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
                executeReasonList(response);
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", UserPreferences.getUserId(mFragmentContext));
                params.put("tokenKey", UserPreferences.getToken(mFragmentContext));
                return params;
            }
        };

        // Adding request to request queue
        RuchiraApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void executeReasonList(String result) {
        Log.d(TAG, result.toString());
        cancelationList.clear();
        OrderCancelation orderCancelation = new OrderCancelation();
        orderCancelation.setTitle("Select a Reason");
        cancelationList.add(orderCancelation);
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

    private void processResultForCancellation(String result) {
        try {
            JSONObject obj = new JSONObject(result);
            int responseResult = obj.getInt("success");
            if (responseResult == 1) {
                openOrderFragment();
            } else {
                ViewUtils.alertUser(mFragmentContext, "Server Error");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void openOrderFragment() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        ViewUtils.launchFragmentWithoutKeepingInBackStack(mFragmentContext, new OrderFragment());
    }
}