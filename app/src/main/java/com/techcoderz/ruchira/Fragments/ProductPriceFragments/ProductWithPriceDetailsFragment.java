package com.techcoderz.ruchira.Fragments.ProductPriceFragments;

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
import com.android.volley.toolbox.StringRequest;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.Application.RuchiraApplication;
import com.techcoderz.ruchira.Fragments.OtherFragments.RuchiraFragment;
import com.techcoderz.ruchira.Utils.AppConfig;
import com.techcoderz.ruchira.Utils.NetworkUtils;
import com.techcoderz.ruchira.Utils.UserPreferences;
import com.techcoderz.ruchira.Utils.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shahriar on 9/18/2016.
 */
public class ProductWithPriceDetailsFragment extends RuchiraFragment {
    private final static String TAG = "WithPriceFragment";
    Fragment toLaunchFragment = null;

    private Bundle bundle;
    private String productId = "";
    private TextView title_product_name_txt, product_name_txt,
            sku_txt, product_id_txt, selling_price_txt, weight_txt, height_txt, dimension_txt;

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
        return rootView;
    }

    private void setupToolbar() {
        mFragmentContext.getSupportActionBar().show();
        mFragmentContext.getSupportActionBar().setTitle("All Product With Price");
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
        if(NetworkUtils.hasInternetConnection(mFragmentContext)) {
            fetchDataFromServer();
        }
    }

    private void fetchDataFromServer() {
        ProgressDialog progressDialog = null;
        progressDialog = new ProgressDialog(mFragmentContext);
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
                finalProgressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", UserPreferences.getUserId(mFragmentContext));
                params.put("tokenKey", UserPreferences.getToken(mFragmentContext));
                params.put("productId", productId);
                Log.d(TAG, " product Id: " + productId);
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

                weight_txt.setText("Weight : " + obj.getString("weight"));
                height_txt.setText("Height : " + obj.getString("height"));
                dimension_txt.setText("Dimension : " + obj.getString("dimension"));
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