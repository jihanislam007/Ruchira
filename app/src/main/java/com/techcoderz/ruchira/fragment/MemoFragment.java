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
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shahriar on 9/18/2016.
 */
public class MemoFragment extends RuchiraFragment {
    private final static String TAG = "MemoFragment";
    Fragment toLaunchFragment = null;

    TextView shope_name_txt, order_id_txt, name_txt, cell_no_txt, status_txt, date_txt, order_time_txt;

    TextView subtotal_txt, grand_total_txt, total_paid_txt, total_refunded_txt, total_due_txt;

    Button confirm_btn;
    private Bundle bundle;
    private String shopeId;


    private List<Order> orderList;
    private RecyclerView report_rcview;
    private LinearLayoutManager manager;
    private OrderAdapter orderAdapter;

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
        fetchDataFromServer();
        action();
        return rootView;
    }

    private void setupToolbar() {
        ownerActivity.getSupportActionBar().show();
        ownerActivity.getSupportActionBar().setTitle("View Memo");
    }

    private void initialize(View rootView) {
        bundle = getArguments();
        orderList = new ArrayList<>();
        confirm_btn = (Button) rootView.findViewById(R.id.confirm_btn);
        shopeId = bundle.getString("getShopeId");
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

        manager = new LinearLayoutManager(ownerActivity);
        orderAdapter = new OrderAdapter(ownerActivity, orderList);
    }

    private void action() {
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddNewOrderFragment();
            }
        });
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

    private void execute(String result) {
    }

    private void processResult(String result) {
    }

    private void processResult() {

    }


    private void openAddNewOrderFragment() {
        toLaunchFragment = new AddNewOrderFragment();
        if (toLaunchFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString("getShopeId", shopeId);
            toLaunchFragment.setArguments(bundle);
            ViewUtils.launchFragmentKeepingInBackStack(ownerActivity, toLaunchFragment);
            toLaunchFragment = null;
        }
    }
}