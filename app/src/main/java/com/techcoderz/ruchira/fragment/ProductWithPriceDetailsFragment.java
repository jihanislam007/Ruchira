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
import com.techcoderz.ruchira.utills.AppConfig;
import com.techcoderz.ruchira.utills.TaskUtils;
import com.techcoderz.ruchira.utills.UserPreferences;
import com.techcoderz.ruchira.utills.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shahriar on 9/18/2016.
 */
public class ProductWithPriceDetailsFragment extends RuchiraFragment {
    private final static String TAG = "WithPriceFragment";
    String url = "http://sondhan.com/articleApi/android/category";
    Fragment toLaunchFragment = null;

    private Bundle bundle;
    private String productId = "";
    private TextView title_product_name_txt, product_name_txt, sku_txt, product_id_txt, selling_price_txt, weight_txt, height_txt, dimension_txt;

    public ProductWithPriceDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product_with_price_details, container, false);

        setupToolbar();
        initialize(rootView);
        action();
        fetchDataFromServer();
        return rootView;
    }

    private void setupToolbar() {
        ownerActivity.getSupportActionBar().show();
        ownerActivity.getSupportActionBar().setTitle("All Product With Price");
    }

    private void initialize(View rootView) {
        bundle = this.getArguments();
        productId = bundle.getString("getProductId");

        title_product_name_txt = (TextView) rootView.findViewById(R.id.title_product_name_txt);
        product_name_txt = (TextView) rootView.findViewById(R.id.product_name_txt);
        sku_txt = (TextView) rootView.findViewById(R.id.sku_txt);
        product_id_txt = (TextView) rootView.findViewById(R.id.product_id_txt);
        selling_price_txt = (TextView) rootView.findViewById(R.id.selling_price_txt);
        weight_txt = (TextView) rootView.findViewById(R.id.weight_txt);
        height_txt = (TextView) rootView.findViewById(R.id.height_txt);
        dimension_txt = (TextView) rootView.findViewById(R.id.dimension_txt);
    }

    private void action() {
    }

    private void fetchDataFromServer() {

        ProgressDialog progressDialog = null;

        progressDialog = new ProgressDialog(ownerActivity);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String tag_string_req = "req_order";
        final ProgressDialog finalProgressDialog = progressDialog;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_PRODUCT_WITH_PRICE_DETAILS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "promotion: " + response.toString());
                finalProgressDialog.dismiss();
                executeForPromotionDetails(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "promotion Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", UserPreferences.getId(ownerActivity));
                params.put("tokenKey", UserPreferences.getToken(ownerActivity));
                params.put("productId", productId);
                return params;
            }

        };

        // Adding request to request queue
        RuchiraApplication.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


    private void executeForPromotionDetails(String result) {
        Log.d(TAG, result.toString());
        try {

            JSONObject obj = new JSONObject(result);

            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                title_product_name_txt.setText(obj.getString("productName"));
                product_name_txt.setText(obj.getString("productName"));
                sku_txt.setText(obj.getString("productSku"));
                product_id_txt.setText(obj.getString("productId"));
                selling_price_txt.setText(obj.getString("sellingPrice"));

                weight_txt.setText(obj.getString("weight"));
                height_txt.setText(obj.getString("height"));
                dimension_txt.setText(obj.getString("dimension"));

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