package com.techcoderz.ruchira.fragment;

import android.app.ProgressDialog;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.adapter.OrderBeatSpinnerAdapter;
import com.techcoderz.ruchira.adapter.OutletAdapter;
import com.techcoderz.ruchira.adapter.ProductCategorySpinnerAdapter;
import com.techcoderz.ruchira.adapter.ProductListAdapter;
import com.techcoderz.ruchira.application.RuchiraApplication;
import com.techcoderz.ruchira.model.Outlet;
import com.techcoderz.ruchira.model.ProductCategory;
import com.techcoderz.ruchira.model.ProductList;
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
public class AddNewOrderFragment extends RuchiraFragment {
    private final static String TAG = "AddNewOrderFragment";
    String url = "http://sondhan.com/articleApi/android/category";
    Fragment toLaunchFragment = null;
    private Button orderBtn, memoBtn;
    private Bundle bundle;
    private String shopeId = "";
    private RadioGroup order_radio_group;

    private List<ProductCategory> productCategoryList;
    private List<ProductList> productList;
    private int position2 = 0;

    private TextView outlet_name_txt, outletAddress_txt;

    private AppCompatSpinner categorySpinner;
    private ProductCategorySpinnerAdapter productCategorySpinnerAdapter;


    private RecyclerView outlet_rcview;
    private ProductListAdapter productListAdapter;
    private GridLayoutManager gridLayoutManager;

    private String relationshipStatus = "";

    public AddNewOrderFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_new_order, container, false);
        setupToolbar();
        initialize(rootView);
        action();
        fetchDataFromServerForProductCategory();
        return rootView;
    }

    private void setupToolbar() {
        ownerActivity.getSupportActionBar().show();
        ownerActivity.getSupportActionBar().setTitle("Add Order");
    }

    private void initialize(View rootView) {
        memoBtn = (Button) rootView.findViewById(R.id.memo_btn);
        outlet_name_txt = (TextView) rootView.findViewById(R.id.outlet_name_txt);
        outletAddress_txt = (TextView) rootView.findViewById(R.id.outletAddress_txt);
        order_radio_group = (RadioGroup) rootView.findViewById(R.id.order_radio_group);
        bundle = getArguments();
        shopeId = bundle.getString("getShopeId");


        categorySpinner = (AppCompatSpinner) rootView.findViewById(R.id.beat_spinner);

        outlet_rcview = (RecyclerView) rootView.findViewById(R.id.outlet_rcview);

        productCategoryList = new ArrayList<>();
        productList = new ArrayList<>();
        productCategorySpinnerAdapter = new ProductCategorySpinnerAdapter(ownerActivity, R.layout.beat_list, productCategoryList);
        productCategorySpinnerAdapter.setDropDownViewResource(R.layout.beat_list);

        gridLayoutManager = new GridLayoutManager(ownerActivity, 2);
        productListAdapter = new ProductListAdapter(ownerActivity, productList,shopeId);

        outlet_rcview.setAdapter(productListAdapter);
        outlet_rcview.setHasFixedSize(true);
        outlet_rcview.setLayoutManager(gridLayoutManager);

//        int selectedId=order_radio_group.getCheckedRadioButtonId();
//        radioSexButton=(RadioButton)findViewById(selectedId);

        relationshipStatus = ((RadioButton) rootView.findViewById(order_radio_group.getCheckedRadioButtonId())).getText().toString();
    }

    private void action() {

        memoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMemoFragment();
            }
        });

        categorySpinner.setAdapter(productCategorySpinnerAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                position2 = position;
                fetchDataFromServerForProductList(productCategoryList.get(position).getId(), "1");
                Log.e(TAG, "beatList.get(position).getId() : " + productCategoryList.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        order_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected

//                if (relationshipStatus.equals("Ordered")) {
//                    Log.e(TAG,relationshipStatus);
//                    fetchDataFromServerForProductList(productCategoryList.get(position2).getId(), "0");
//                } else if (relationshipStatus.equals("Not Ordered")) {
//                    Log.e(TAG,relationshipStatus);
//                    fetchDataFromServerForProductList(productCategoryList.get(position2).getId(), "1");
//                }


                if(checkedId == R.id.order_rb) {
                    fetchDataFromServerForProductList(productCategoryList.get(position2).getId(), "0");
                } else if(checkedId == R.id.not_order_rb) {
                    fetchDataFromServerForProductList(productCategoryList.get(position2).getId(), "1");
                }


//                if (checkedId == R.id.order_rb) {
//                    fetchDataFromServerForProductList(productCategoryList.get(position2).getId(), "0");
//                } else if (checkedId == R.id.not_order_rb) {
//                    fetchDataFromServerForProductList(productCategoryList.get(position2).getId(), "1");
//                }
            }

        });

    }

    private void fetchDataFromServerForProductCategory() {

        ProgressDialog progressDialog = null;

        progressDialog = new ProgressDialog(ownerActivity);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String tag_string_req = "req_product_category";
        final ProgressDialog finalProgressDialog = progressDialog;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_PRODUCT_CATEGORY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "product_category Response: " + response.toString());
                finalProgressDialog.dismiss();
                executeForProductCategory(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "product_category Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", UserPreferences.getId(ownerActivity));
                params.put("tokenKey", UserPreferences.getToken(ownerActivity));
                params.put("outletId", shopeId);
                return params;
            }

        };

        // Adding request to request queue
        RuchiraApplication.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


    private void fetchDataFromServerForProductList(final String id, final String option) {

        ProgressDialog progressDialog = null;

        progressDialog = new ProgressDialog(ownerActivity);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String tag_string_req = "req_product_list";
        final ProgressDialog finalProgressDialog = progressDialog;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_PRODUCT_LIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "product_list Response: " + response.toString());
                finalProgressDialog.dismiss();
                processResultForProductList(response, option);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "product_list Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", UserPreferences.getId(ownerActivity));
                params.put("tokenKey", UserPreferences.getToken(ownerActivity));
                params.put("id", id);
                return params;
            }

        };

        // Adding request to request queue
        RuchiraApplication.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


    private void executeForProductCategory(String result) {
        Log.d(TAG, result.toString());
        productCategoryList.clear();

        try {

            JSONObject obj = new JSONObject(result);

            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                outlet_name_txt.setText(obj.getString("outletName"));
                outletAddress_txt.setText(obj.getString("outletAddress"));
                productCategoryList.addAll(TaskUtils.setProductCategory(result));
                productCategorySpinnerAdapter.notifyDataSetChanged();
                return;

            } else {
                ViewUtils.alertUser(ownerActivity, "Server Error");
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void processResultForProductList(String result, String option) {
        Log.d(TAG, result.toString());
        productList.clear();

        try {

            JSONObject obj = new JSONObject(result);

            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                List<ProductList> tempproductList = new ArrayList<>();
                tempproductList.addAll(TaskUtils.setProductList(result));
                for (int i = 0; i < tempproductList.size(); i++) {
                    if (tempproductList.get(i).getFlag().equals(option)) {
                        productList.add(tempproductList.get(i));
                    }
                }
                productListAdapter.notifyDataSetChanged();

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


    private void openOrderDetailsFragment() {
        toLaunchFragment = new OrderDetailsFragment();
        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(ownerActivity, toLaunchFragment);
            toLaunchFragment = null;
        }
    }

    private void openMemoFragment() {
        toLaunchFragment = new MemoFragment();
        if (toLaunchFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString("getShopeId",shopeId);
            toLaunchFragment.setArguments(bundle);
            ViewUtils.launchFragmentKeepingInBackStack(ownerActivity, toLaunchFragment);
            toLaunchFragment = null;
        }
    }
}