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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.adapter.OrderSummaryAdapter;
import com.techcoderz.ruchira.adapter.ReportAdapter;
import com.techcoderz.ruchira.application.RuchiraApplication;
import com.techcoderz.ruchira.model.OrderSummary;
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
 * Created by priom on 9/19/16.
 */
public class ShopOrderSummaryFragment extends RuchiraFragment {
    private final static String TAG = "ShopOrderSummaryFragment";
    String url = "http://sondhan.com/articleApi/android/category";
    Fragment toLaunchFragment = null;

    private List<OrderSummary> orderSummaryList;
    private RecyclerView report_rcview;
    private LinearLayoutManager manager;
    private OrderSummaryAdapter orderSummaryAdapter;

    private TextView name_txt, sale_date_txt, address_txt, date_txt;

    public ShopOrderSummaryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shop_order_summary, container, false);

        setupToolbar();
        initialize(rootView);
        fetchDataFromServer();
        action();
        return rootView;
    }

    private void setupToolbar() {
        ownerActivity.getSupportActionBar().show();
        ownerActivity.getSupportActionBar().setTitle("Shop Order Summary");
    }

    private void initialize(View rootView) {
        orderSummaryList = new ArrayList<>();
        report_rcview = (RecyclerView) rootView.findViewById(R.id.report_rcview);

        name_txt = (TextView) rootView.findViewById(R.id.name_txt);
        sale_date_txt = (TextView) rootView.findViewById(R.id.sale_date_txt);
        address_txt = (TextView) rootView.findViewById(R.id.address_txt);
        date_txt = (TextView) rootView.findViewById(R.id.date_txt);

        manager = new LinearLayoutManager(ownerActivity);
        orderSummaryAdapter = new OrderSummaryAdapter(ownerActivity, orderSummaryList);
        report_rcview.setAdapter(orderSummaryAdapter);
        report_rcview.setHasFixedSize(true);
        report_rcview.setLayoutManager(manager);


    }

    private void action() {

    }

    private void fetchDataFromServer() {

        if (UserPreferences.getOrderId(ownerActivity) != null) {

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
                    AppConfig.URL_ORDER_SUMMARY, new Response.Listener<String>() {

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
                    params.put("orderId", UserPreferences.getOrderId(ownerActivity));
                    return params;
                }

            };

            // Adding request to request queue
            RuchiraApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
        } else {
            ViewUtils.alertUser(ownerActivity, "No Memo Available");
        }

    }

    private void executeForMemo(String result) {
        Log.d(TAG, result.toString());
        orderSummaryList.clear();

        try {

            JSONObject obj = new JSONObject(result);

            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {

                name_txt.setText(obj.getString("outletName"));
                sale_date_txt.setText(obj.getString("startDate"));
                address_txt.setText(obj.getString("outletAddress"));
                date_txt.setText(obj.getString("orderDate"));

                orderSummaryList.addAll(TaskUtils.setOrderSummaryList(result));
                orderSummaryAdapter.notifyDataSetChanged();
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