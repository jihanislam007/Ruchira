package com.techcoderz.ruchira.Fragments.OrderFragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
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
import com.techcoderz.ruchira.Adapters.OrderAdapter;
import com.techcoderz.ruchira.Application.RuchiraApplication;
import com.techcoderz.ruchira.Fragments.OtherFragments.RuchiraFragment;
import com.techcoderz.ruchira.ModelClasses.Order;
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

/**
 * Created by Shahriar on 9/18/2016.
 */
public class MemoFragment extends RuchiraFragment {
    private final static String TAG = "MemoFragment";
    private TextView shope_name_txt, tv_order_id, tv_owner_name, tv_cell_number, tv_status, tv_order_date, tv_order_time;
    private TextView tv_subtotal, grand_total_txt, total_paid_txt, total_refunded_txt, total_due_txt;
    private Button confirm_btn, cancel_btn;
    private Bundle bundle;
    private String outletId = "", orderId = "", outletName = "", outletAddress = "";
    private List<Order> orderList;
    private RecyclerView report_rcview;
    private LinearLayoutManager manager;
    private OrderAdapter orderAdapter;
    private ProgressDialog progressDialog;
    private SharedPreferences.Editor editor;

    public MemoFragment(SharedPreferences.Editor edit) {
        this.editor = edit;
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
        confirm_btn = (Button) rootView.findViewById(R.id.memo_confirm_btn);
        cancel_btn = (Button) rootView.findViewById(R.id.memo_cancel_btn);
        outletId = bundle.getString("getShopeId");
        orderId = bundle.getString("orderId");
        outletName = bundle.getString("getShopeName");
        outletAddress = bundle.getString("getShopeAddress");
        Log.d(TAG, " Memo Fragment order Id: " + orderId);
        Log.d(TAG, " Memo Fragment Shop Name: " + outletName);
        Log.d(TAG, " Memo Fragment Shop Address: " + outletAddress);
        report_rcview = (RecyclerView) rootView.findViewById(R.id.report_rcview);

        shope_name_txt = (TextView) rootView.findViewById(R.id.shope_name_txt);
        tv_order_id = (TextView) rootView.findViewById(R.id.order_id_txt);
        tv_owner_name = (TextView) rootView.findViewById(R.id.name_txt);
        tv_cell_number = (TextView) rootView.findViewById(R.id.cell_no_txt);
        tv_status = (TextView) rootView.findViewById(R.id.status_txt);
        tv_order_date = (TextView) rootView.findViewById(R.id.date_txt);
        tv_order_time = (TextView) rootView.findViewById(R.id.order_time_txt);

        tv_subtotal = (TextView) rootView.findViewById(R.id.subtotal_txt);
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
        shope_name_txt.setText("Shop Name : " + outletName);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear().commit();
                fetchDataFromServerMemoConformation();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("outletName", outletName);
                bundle.putString("outletAddress", outletAddress);
                bundle.putString("outletId", outletId);
                bundle.putString("orderId", orderId);
                OrderCancellationFragment orderCancellationFragment = new OrderCancellationFragment();
                orderCancellationFragment.setArguments(bundle);
                ViewUtils.launchFragmentKeepingInBackStack(mFragmentContext, orderCancellationFragment);

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
                tv_subtotal.setText(obj.getString("total") + " ৳");
//                grand_total_txt.setText(obj.getString("total") + " ৳");
//                total_paid_txt.setText(obj.getString("totalPaid") + " ৳");
//                total_refunded_txt.setText(obj.getString("totalRefund") + " ৳");
//                total_due_txt.setText(obj.getString("totalDue") + " ৳");
//                shope_name_txt.setText("Shop Name : " + obj.getString("outletName"));
                tv_order_id.setText("Order Id : #" + obj.getString("orderId"));
                tv_owner_name.setText(obj.getString("ownerName"));
                tv_cell_number.setText("Cell : " + obj.getString("phone"));
                tv_status.setText("Status : " + obj.getString("status"));
                tv_order_date.setText(obj.getString("orderDate"));
                tv_order_time.setText("Order Time : " + obj.getString("orderTime"));

            } else {
                ViewUtils.alertUser(mFragmentContext, "Server Error");
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

                    Log.e(TAG, " userId: " + UserPreferences.getUserId(mFragmentContext));
                    Log.e(TAG, " tokenKey: " + UserPreferences.getToken(mFragmentContext));
                    Log.e(TAG, " order Id: " + orderId);

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