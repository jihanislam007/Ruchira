package com.techcoderz.ruchira.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.adapter.OrderAdapter;
import com.techcoderz.ruchira.application.RuchiraApplication;
import com.techcoderz.ruchira.model.Order;
import com.techcoderz.ruchira.utills.AppConfig;
import com.techcoderz.ruchira.utills.NetworkUtils;
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
 * Created by Shahriar on 9/18/2016.
 */
public class MemoFragment extends RuchiraFragment {
    private final static String TAG = "MemoFragment";
    private TextView shope_name_txt, order_id_txt, name_txt, cell_no_txt, status_txt, date_txt, order_time_txt;
    private TextView subtotal_txt, grand_total_txt, total_paid_txt, total_refunded_txt, total_due_txt;
    private Button confirm_btn;
    private Bundle bundle;
    private String shopeId = "", orderId = "";
    private List<Order> orderList;
    private RecyclerView report_rcview;
    private LinearLayoutManager manager;
    private OrderAdapter orderAdapter;
    private ProgressDialog progressDialog;

    public MemoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_memo, container, false);
        setupToolbar();
        initialize(rootView);
        if (NetworkUtils.hasInternetConnection(mFragmentContext)) {
            fetchDataFromServer();
        }
        action();
        return rootView;
    }

    private void setupToolbar() {
        mFragmentContext.getSupportActionBar().show();
        mFragmentContext.getSupportActionBar().setTitle("View Memo");
    }

    private void initialize(View rootView) {
        bundle = this.getArguments();
        orderList = new ArrayList<>();
        confirm_btn = (Button) rootView.findViewById(R.id.confirm_btn);
        shopeId = bundle.getString("getShopeId");
        orderId = bundle.getString("orderId");
        Log.d(TAG, " Memo Fragment order Id: " + orderId);
        report_rcview = (RecyclerView) rootView.findViewById(R.id.report_rcview);

        shope_name_txt = (TextView) rootView.findViewById(R.id.shope_name_txt);
        order_id_txt = (TextView) rootView.findViewById(R.id.order_id_txt);
        name_txt = (TextView) rootView.findViewById(R.id.name_txt);
        cell_no_txt = (TextView) rootView.findViewById(R.id.cell_no_txt);
        status_txt = (TextView) rootView.findViewById(R.id.status_txt);
        date_txt = (TextView) rootView.findViewById(R.id.date_txt);
        order_time_txt = (TextView) rootView.findViewById(R.id.order_time_txt);

        subtotal_txt = (TextView) rootView.findViewById(R.id.subtotal_txt);
        grand_total_txt = (TextView) rootView.findViewById(R.id.grand_total_txt);
        total_paid_txt = (TextView) rootView.findViewById(R.id.total_paid_txt);
        total_refunded_txt = (TextView) rootView.findViewById(R.id.total_refunded_txt);
        total_due_txt = (TextView) rootView.findViewById(R.id.total_due_txt);

        manager = new LinearLayoutManager(mFragmentContext);
        orderAdapter = new OrderAdapter(mFragmentContext, orderList);

        progressDialog = null;
        progressDialog = new ProgressDialog(mFragmentContext);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

    }

    private void action() {
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchDataFromServerMemoConformation();
            }
        });
        report_rcview.setAdapter(orderAdapter);
        report_rcview.setHasFixedSize(true);
        report_rcview.setLayoutManager(manager);
    }

    private void fetchDataFromServer() {
        if (!orderId.equals("")) {
            progressDialog.show();
            String tag_string_req = "req_memo";
            final ProgressDialog finalProgressDialog = progressDialog;
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_MEMO, new Response.Listener<String>() {

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
                    finalProgressDialog.dismiss();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("userId", UserPreferences.getUserId(mFragmentContext));
                    params.put("tokenKey", UserPreferences.getToken(mFragmentContext));
                    params.put("orderId", orderId);
                    return params;
                }

            };

            // Adding request to request queue
            RuchiraApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
        } else {
            ViewUtils.alertUser(mFragmentContext, "No Memo Available");
        }
    }

    private void executeForMemo(String result) {
        Log.d(TAG, result.toString());
        orderList.clear();
        try {
            JSONObject obj = new JSONObject(result);
            int responseResult = obj.getInt("response");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                orderList.addAll(TaskUtils.setOrderList(result));
                orderAdapter.notifyDataSetChanged();
                subtotal_txt.setText(obj.getString("subTotal") + " ৳");
                grand_total_txt.setText(obj.getString("total") + " ৳");
                total_paid_txt.setText(obj.getString("totalPaid") + " ৳");
                total_refunded_txt.setText(obj.getString("totalRefund") + " ৳");
                total_due_txt.setText(obj.getString("totalDue") + " ৳");
                shope_name_txt.setText("Shop Name : " + obj.getString("outletName"));
                order_id_txt.setText("Order Id : #" + obj.getString("orderId"));
                name_txt.setText(obj.getString("ownerName"));
                cell_no_txt.setText("Cell : " + obj.getString("phone"));
                status_txt.setText("Status : " + obj.getString("status"));
                date_txt.setText(obj.getString("orderDate"));
                order_time_txt.setText("Order Time : " + obj.getString("orderTime"));

                return;

            } else {
                ViewUtils.alertUser(mFragmentContext, "Server Error");
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fetchDataFromServerMemoConformation() {
        if (!orderId.equals("")) {
            progressDialog.show();
            String tag_string_req = "req_memo";
            final ProgressDialog finalProgressDialog = progressDialog;
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_MEMO_CONFORMATION, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.e(TAG, "memo Response: " + response.toString());
                    finalProgressDialog.dismiss();
                    executeForMemoConformation(response);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "memo Error: " + error.getMessage());
                    finalProgressDialog.dismiss();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("userId", UserPreferences.getUserId(mFragmentContext));
                    params.put("tokenKey", UserPreferences.getToken(mFragmentContext));
                    params.put("orderId", orderId);
                    return params;
                }

            };

            // Adding request to request queue
            RuchiraApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
        } else {
            ViewUtils.alertUser(mFragmentContext, "No Memo Available");
        }
    }

    private void executeForMemoConformation(String result) {
        Log.d(TAG, result.toString());
        orderList.clear();
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
            FragmentManager fm = getActivity().getSupportFragmentManager();
            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
        ViewUtils.launchFragmentWithoutKeepingInBackStack(mFragmentContext, new OrderFragment());
    }
}