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
import com.android.volley.toolbox.StringRequest;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.adapter.OrderAdapter;
import com.techcoderz.ruchira.application.RuchiraApplication;
import com.techcoderz.ruchira.model.Billing;
import com.techcoderz.ruchira.model.Order;
import com.techcoderz.ruchira.model.Shipping;
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
 * Created by priom on 9/19/16.
 */
public class CustomerOrderDetailsFragment extends RuchiraFragment {
    private final static String TAG = "OrderSubmitFragment";
    String url = "http://sondhan.com/articleApi/android/category";
    Fragment toLaunchFragment = null;

    private List<Order> orderList;
    private List<Billing> billingList;
    private List<Shipping> shippingList;
    private RecyclerView report_rcview;
    private TextView shope_name_txt, order_id_txt, name_txt, cell_no_txt, customer_id_txt, email_txt, status_txt;
    private TextView billing_to_txt, billing_address_txt, billing_city_post_code_txt, billing_mail_txt, billing_cell_txt, shipping_to_txt, shipping_address_txt, shipping_city_post_code_txt, shipping_cell_txt, shipping_mail_txt;
    private TextView date_txt, total_sale_txt;

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
        if(NetworkUtils.hasInternetConnection(mFragmentContext)) {
            fetchDataFromServer();
        }
        action();
        return rootView;
    }

    private void setupToolbar() {
        mFragmentContext.getSupportActionBar().show();
        mFragmentContext.getSupportActionBar().setTitle("Customer Order Details");
    }

    private void initialize(View rootView) {
        report_rcview = (RecyclerView) rootView.findViewById(R.id.report_rcview);


        shope_name_txt = (TextView) rootView.findViewById(R.id.shope_name_txt);
        order_id_txt = (TextView) rootView.findViewById(R.id.order_id_txt);
        name_txt = (TextView) rootView.findViewById(R.id.name_txt);
        cell_no_txt = (TextView) rootView.findViewById(R.id.cell_no_txt);
        customer_id_txt = (TextView) rootView.findViewById(R.id.customer_id_txt);
        email_txt = (TextView) rootView.findViewById(R.id.email_txt);
        status_txt = (TextView) rootView.findViewById(R.id.status_txt);
        billing_to_txt = (TextView) rootView.findViewById(R.id.billing_to_txt);
        billing_address_txt = (TextView) rootView.findViewById(R.id.billing_address_txt);
        billing_city_post_code_txt = (TextView) rootView.findViewById(R.id.billing_city_post_code_txt);
        billing_mail_txt = (TextView) rootView.findViewById(R.id.billing_mail_txt);
        billing_cell_txt = (TextView) rootView.findViewById(R.id.billing_cell_txt);
        shipping_to_txt = (TextView) rootView.findViewById(R.id.shipping_to_txt);
        shipping_address_txt = (TextView) rootView.findViewById(R.id.shipping_address_txt);
        shipping_city_post_code_txt = (TextView) rootView.findViewById(R.id.shipping_city_post_code_txt);
        shipping_cell_txt = (TextView) rootView.findViewById(R.id.shipping_cell_txt);
        shipping_mail_txt = (TextView) rootView.findViewById(R.id.shipping_mail_txt);
        date_txt = (TextView) rootView.findViewById(R.id.date_txt);
        total_sale_txt = (TextView) rootView.findViewById(R.id.total_sale_txt);

        orderList = new ArrayList<>();
        shippingList = new ArrayList<>();
        billingList = new ArrayList<>();

        manager = new LinearLayoutManager(mFragmentContext);
        orderAdapter = new OrderAdapter(mFragmentContext, orderList);


    }

    private void action() {
        report_rcview.setAdapter(orderAdapter);
        report_rcview.setHasFixedSize(true);
        report_rcview.setLayoutManager(manager);
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

    private void fetchDataFromServer() {

        if (UserPreferences.getOrderId(mFragmentContext) != null) {

        ProgressDialog progressDialog = null;

        progressDialog = new ProgressDialog(mFragmentContext);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String tag_string_req = "customer_order_details_req";
        final ProgressDialog finalProgressDialog = progressDialog;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ORDER_SUMMARY_DETAILS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "customer_order_details Response: " + response.toString());
                finalProgressDialog.dismiss();
                executeForMemo(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "customer_order_details Error: " + error.getMessage());
                finalProgressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", UserPreferences.getId(mFragmentContext));
                params.put("tokenKey", UserPreferences.getToken(mFragmentContext));
                params.put("orderId", UserPreferences.getOrderId(mFragmentContext));
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
        shippingList.clear();
        billingList.clear();

        try {

            JSONObject obj = new JSONObject(result);

            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                orderList.addAll(TaskUtils.setOrderList(result));
                shippingList.addAll(TaskUtils.setShippingList(result));
                billingList.addAll(TaskUtils.setBillingList(result));
                orderAdapter.notifyDataSetChanged();

                shope_name_txt.setText("Shope Name "+obj.getString("outletName"));
                order_id_txt.setText("Order Id : #"+obj.getString("orderId"));
                name_txt.setText(obj.getString("ownerName"));
                cell_no_txt.setText("Cell : "+obj.getString("ownerPhone"));
                customer_id_txt.setText("Customer ID : "+obj.getString("ownerId"));
                email_txt.setText("Email : "+obj.getString("ownerEmail"));
                status_txt.setText("Status : "+obj.getString("status"));

                billing_to_txt.setText(billingList.get(0).getTo());
                billing_address_txt.setText(billingList.get(0).getAddress());
                billing_city_post_code_txt.setText(billingList.get(0).getCity()+" - "+billingList.get(0).getPostcode());
                billing_mail_txt.setText(billingList.get(0).getEmail());
                billing_cell_txt.setText(billingList.get(0).getPhone());

                shipping_to_txt.setText(shippingList.get(0).getTo());
                shipping_address_txt.setText(shippingList.get(0).getAddress());
                shipping_city_post_code_txt.setText(shippingList.get(0).getCity()+" - "+shippingList.get(0).getPostcode());
                shipping_cell_txt.setText(shippingList.get(0).getPhone());
                shipping_mail_txt.setText(shippingList.get(0).getEmail());

                date_txt.setText(obj.getString("orderDate"));
                total_sale_txt.setText(obj.getString("total")+" BDT");

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