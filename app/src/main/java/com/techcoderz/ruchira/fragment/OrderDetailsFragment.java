package com.techcoderz.ruchira.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.application.RuchiraApplication;
import com.techcoderz.ruchira.utills.AppConfig;
import com.techcoderz.ruchira.utills.TaskUtils;
import com.techcoderz.ruchira.utills.UserPreferences;
import com.techcoderz.ruchira.utills.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by Shahriar on 9/18/2016.
 */
public class OrderDetailsFragment extends RuchiraFragment {
    private final static String TAG = "OrderDetailsFragment";
    String url = "http://sondhan.com/articleApi/android/category";
    Fragment toLaunchFragment = null;
    private LinearLayout linear_layout;

    Button submit_btn, cancel_btn;
    private Bundle bundle;
    private String shopeId, productId, promotionId, sku;
    int pricePerCarton, pricePerPiece;
    private EditText ctn_et, pcs_et;
    private TextView value_et, sku_txt, price_txt;

    public OrderDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_new_order_2, container, false);
        setupToolbar();
        initialize(rootView);
        action();
        return rootView;
    }

    private void setupToolbar() {
        ownerActivity.getSupportActionBar().show();
        ownerActivity.getSupportActionBar().setTitle("Add New Order");
    }

    private void initialize(View rootView) {
        bundle = getArguments();
        submit_btn = (Button) rootView.findViewById(R.id.submit_btn);
        cancel_btn = (Button) rootView.findViewById(R.id.cancel_btn);
        shopeId = bundle.getString("getShopeId");
        productId = bundle.getString("getproductId");
        promotionId = bundle.getString("getpromotionId");
        sku = bundle.getString("getProductSku");
        pricePerCarton = bundle.getInt("getPricePerCarton");
        pricePerPiece = bundle.getInt("getPricePerPiece");

        ctn_et = (EditText) rootView.findViewById(R.id.ctn_et);
        pcs_et = (EditText) rootView.findViewById(R.id.pcs_et);
        value_et = (TextView) rootView.findViewById(R.id.value_et);
        sku_txt = (TextView) rootView.findViewById(R.id.sku_txt);
        price_txt = (TextView) rootView.findViewById(R.id.price_txt);
    }

    private void action() {
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchDataFromServerOrderItemSubmit();
            }
        });

        sku_txt.setText(sku);
        price_txt.setText("Price : " + pricePerCarton + " per ctn," + pricePerPiece + " per pcs");

        ctn_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!ctn_et.getText().toString().equals("")) {
                    if (pcs_et.getText().toString().equals("")) {
                        value_et.setText(Integer.parseInt(ctn_et.getText().toString()) * pricePerCarton);
                    } else {
                        value_et.setText(((Integer.parseInt(ctn_et.getText().toString()) * pricePerCarton) +
                                ((Integer.parseInt(pcs_et.getText().toString())) * pricePerPiece))+"");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pcs_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!pcs_et.getText().toString().equals("")) {
                    if (ctn_et.getText().toString().equals("")) {
                        value_et.setText((Integer.parseInt(pcs_et.getText().toString()) * pricePerPiece));
                    } else {
                        value_et.setText((((Integer.parseInt(ctn_et.getText().toString()) * pricePerCarton)) +
                                ((Integer.parseInt(pcs_et.getText().toString()) * pricePerPiece)))+"");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchDataFromServerOrderCancelation();
            }
        });
    }

    private void fetchDataFromServerOrderItemSubmit() {

        ProgressDialog progressDialog = null;

        progressDialog = new ProgressDialog(ownerActivity);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String tag_string_req = "req_submit_order";
        final ProgressDialog finalProgressDialog = progressDialog;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ORDER_ITEM_SUBMIT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "submit_order Response: " + response.toString());
                finalProgressDialog.dismiss();
                executeForOrderItemSubmit(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "submit_order Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", UserPreferences.getId(ownerActivity));
                params.put("tokenKey", UserPreferences.getToken(ownerActivity));
                params.put("orderId", UserPreferences.getOrderId(ownerActivity));
                params.put("productId", productId);
                params.put("ctn", ctn_et.getText().toString());
                params.put("pcs", pcs_et.getText().toString());
                params.put("cost", value_et.getText().toString());
                params.put("promotionId", promotionId);
                return params;
            }

        };

        // Adding request to request queue
        RuchiraApplication.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void fetchDataFromServerOrderCancelation() {

        ProgressDialog progressDialog = null;

        progressDialog = new ProgressDialog(ownerActivity);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String tag_string_req = "req_cancel_order";
        final ProgressDialog finalProgressDialog = progressDialog;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ORDER_CANCEL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "cancel_order Response: " + response.toString());
                finalProgressDialog.dismiss();
                executeForOrderCancelation(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "cancel_order Error: " + error.getMessage());
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

    }


    private void executeForOrderCancelation(String result) {
        Log.d(TAG, result.toString());

        try {

            JSONObject obj = new JSONObject(result);

            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                TaskUtils.clearOrderId(ownerActivity);
                openAddNewOrderFragment();
                return;

            } else {
                ViewUtils.alertUser(ownerActivity, "Server Error");
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void executeForOrderItemSubmit(String result) {
        Log.d(TAG, result.toString());

        try {

            JSONObject obj = new JSONObject(result);

            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                ViewUtils.alertUser(ownerActivity, "Submitted");
                return;

            } else {
                ViewUtils.alertUser(ownerActivity, "Server Error");
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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