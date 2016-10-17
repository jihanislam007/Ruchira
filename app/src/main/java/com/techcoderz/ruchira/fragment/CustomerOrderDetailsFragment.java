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
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.adapter.OrderAdapter;
import com.techcoderz.ruchira.adapter.ReportAdapter;
import com.techcoderz.ruchira.application.RuchiraApplication;
import com.techcoderz.ruchira.model.Order;
import com.techcoderz.ruchira.model.Report;
import com.techcoderz.ruchira.utills.AppConfig;
import com.techcoderz.ruchira.utills.TaskUtils;
import com.techcoderz.ruchira.utills.UserPreferences;
import com.techcoderz.ruchira.utills.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by priom on 9/19/16.
 */
public class CustomerOrderDetailsFragment extends RuchiraFragment {
    private final static String TAG = "CustomerOrderDetailsFragment";
    String url = "http://sondhan.com/articleApi/android/category";
    Fragment toLaunchFragment = null;

    private List<Order> orderList;
    private RecyclerView report_rcview;
    private TextView name_txt, phone_txt, id_txt, date_txt, total_txt;
    private LinearLayoutManager manager;
    private OrderAdapter orderAdapter;

    public CustomerOrderDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_customer_order_details, container, false);
        setupToolbar();
        initialize(rootView);
        fetchDataFromServer();
        action();
        return rootView;
    }

    private void setupToolbar() {
        ownerActivity.getSupportActionBar().show();
        ownerActivity.getSupportActionBar().setTitle("Customer Order Details");
    }
    private void initialize(View rootView) {
        report_rcview = (RecyclerView) rootView.findViewById(R.id.report_rcview);

        manager = new LinearLayoutManager(ownerActivity);
        orderAdapter = new OrderAdapter(ownerActivity, orderList);


    }

    private void action(){
        report_rcview.setAdapter(orderAdapter);
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

        String tag_string_req = "req_memo";
        final ProgressDialog finalProgressDialog = progressDialog;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ORDER_SUMMARY_DETAILS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "memo Response: " + response.toString());
                finalProgressDialog.dismiss();
                executeForMemo(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "memo Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", UserPreferences.getId(ownerActivity));
                params.put("tokenKey", UserPreferences.getToken(ownerActivity));
//                params.put("tokenKey", UserPreferences.getToken(ownerActivity));
                return params;
            }

        };

        // Adding request to request queue
        RuchiraApplication.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void executeForMemo(String result) {
        Log.d(TAG, result.toString());
        orderList.clear();

        try {

            JSONObject obj = new JSONObject(result);

            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                orderList.addAll(TaskUtils.setOrderList(result));
                orderAdapter.notifyDataSetChanged();

                shope_name_txt.setText(obj.getString("outletName"));
                order_id_txt.setText("#" + obj.getString("code"));
                name_txt.setText("ownerName");
                cell_no_txt.setText(obj.getString("phone"));
                status_txt.setText(obj.getString("status"));
                date_txt.setText(obj.getString("orderDate"));
                order_time_txt.setText(obj.getString("orderTime"));
                subtotal_txt.setText(obj.getString("subTotal"));
                grand_total_txt.setText(obj.getString("total"));
                total_paid_txt.setText(obj.getString("totalPaid"));
                total_refunded_txt.setText(obj.getString("totalRefund"));
                total_due_txt.setText(obj.getString("totalDue"));

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