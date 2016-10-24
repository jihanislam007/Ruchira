package com.techcoderz.ruchira.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.ecommerce.Product;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.adapter.OrderBeatSpinnerAdapter;
import com.techcoderz.ruchira.adapter.OutletAdapter;
import com.techcoderz.ruchira.adapter.ProductPromotionAdapter;
import com.techcoderz.ruchira.adapter.PromotionCompanySpinnerAdapter;
import com.techcoderz.ruchira.application.RuchiraApplication;
import com.techcoderz.ruchira.model.Beat;
import com.techcoderz.ruchira.model.Company;
import com.techcoderz.ruchira.model.Outlet;
import com.techcoderz.ruchira.model.Promotion;
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
 * Created by Shahriar on 9/14/2016.
 */
public class ProductPromotionFragment extends RuchiraFragment {
    private final static String TAG = "PromotionFragment";
    String url = "http://sondhan.com/articleApi/android/category";
    Fragment toLaunchFragment = null;

    private List<Promotion> promotionList;
    private List<Company> companyList;

    private AppCompatSpinner beat_spinner;
    private PromotionCompanySpinnerAdapter promotionCompanySpinnerAdapter;

    private RecyclerView report_rcview;
    private ProductPromotionAdapter productPromotionAdapter;
    private LinearLayoutManager manager;
    private TextView company_title_txt;

    public ProductPromotionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product_promotion, container, false);

        setupToolbar();
        initialize(rootView);
        action();
        fetchDataFromServer();
        return rootView;
    }

    private void setupToolbar() {
        ownerActivity.getSupportActionBar().show();
        ownerActivity.getSupportActionBar().setTitle("Product Promotions");
    }

    private void initialize(View rootView) {
        beat_spinner = (AppCompatSpinner) rootView.findViewById(R.id.beat_spinner);
        report_rcview = (RecyclerView) rootView.findViewById(R.id.report_rcview);
        company_title_txt = (TextView) rootView.findViewById(R.id.company_title_txt);

        promotionList = new ArrayList<>();
        companyList = new ArrayList<>();

        promotionCompanySpinnerAdapter = new PromotionCompanySpinnerAdapter(ownerActivity, R.layout.beat_list, companyList);
        promotionCompanySpinnerAdapter.setDropDownViewResource(R.layout.beat_list);

        manager = new LinearLayoutManager(ownerActivity);
        productPromotionAdapter = new ProductPromotionAdapter(ownerActivity, promotionList);

        report_rcview.setAdapter(productPromotionAdapter);
        report_rcview.setHasFixedSize(true);
        report_rcview.setLayoutManager(manager);
    }

    private void action() {
        beat_spinner.setAdapter(promotionCompanySpinnerAdapter);
        beat_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                company_title_txt.setText(companyList.get(position).getCompanyName());
                fetchDataFromServerForPromotion(companyList.get(position).getCompanyId());
                Log.e(TAG, "companyList.get(position).getCompanyId() : " + companyList.get(position).getCompanyId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void fetchDataFromServerForPromotion(final String id) {
        UserPreferences.saveCompanyId(ownerActivity,id);

        ProgressDialog progressDialog = null;

        progressDialog = new ProgressDialog(ownerActivity);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String tag_string_req = "req_promotion";
        final ProgressDialog finalProgressDialog = progressDialog;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_PROMOTION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "order Response: " + response.toString());
                finalProgressDialog.dismiss();
                processResultForPromotion(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "order Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", UserPreferences.getId(ownerActivity));
                params.put("tokenKey", UserPreferences.getToken(ownerActivity));
                params.put("companyId", id);
                return params;
            }

        };

        // Adding request to request queue
        RuchiraApplication.getInstance().addToRequestQueue(strReq, tag_string_req);

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
                AppConfig.URL_COMPANY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "order Response: " + response.toString());
                finalProgressDialog.dismiss();
                executeForCompany(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "order Error: " + error.getMessage());
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


    private void executeForCompany(String result) {
        Log.d(TAG, result.toString());
        companyList.clear();

        try {

            JSONObject obj = new JSONObject(result);

            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                companyList.addAll(TaskUtils.setCompany(result));
                promotionCompanySpinnerAdapter.notifyDataSetChanged();

                return;

            } else {
                ViewUtils.alertUser(ownerActivity, "Server Error");
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void processResultForPromotion(String result) {
        Log.d(TAG, result.toString());
        promotionList.clear();

        try {

            JSONObject obj = new JSONObject(result);

            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {

                promotionList.addAll(TaskUtils.setProductPromotion(result,0));
                productPromotionAdapter.notifyDataSetChanged();

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