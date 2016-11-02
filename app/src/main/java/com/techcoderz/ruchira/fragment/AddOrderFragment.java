package com.techcoderz.ruchira.fragment;

import android.app.ProgressDialog;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.adapter.ProductCategorySpinnerAdapter;
import com.techcoderz.ruchira.adapter.ProductListAdapter;
import com.techcoderz.ruchira.application.RuchiraApplication;
import com.techcoderz.ruchira.model.ProductCategory;
import com.techcoderz.ruchira.model.ProductList;
import com.techcoderz.ruchira.utills.AppConfig;
import com.techcoderz.ruchira.utills.NetworkUtils;
import com.techcoderz.ruchira.utills.TaskUtils;
import com.techcoderz.ruchira.utills.UserPreferences;
import com.techcoderz.ruchira.utills.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shahriar on 9/18/2016.
 */
public class AddOrderFragment extends RuchiraFragment {
    private final static String TAG = "AddOrderFragment";
    private Fragment toLaunchFragment = null;
    private Button orderBtn, memoBtn;
    private Bundle bundle;
    private String shopeId = "", shopName = "", shopAddress = "";
    private RadioGroup order_radio_group;
    private TextView tvShopName, tvshopAddress;
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
    private int selected = 1;

    public AddOrderFragment() {
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
        if (NetworkUtils.hasInternetConnection(mFragmentContext)) {
            fetchDataFromServerForProductCategory();
        }
        return rootView;
    }

    private void setupToolbar() {
        mFragmentContext.getSupportActionBar().show();
        mFragmentContext.getSupportActionBar().setTitle("Add Order");
    }

    private void initialize(View rootView) {
        memoBtn = (Button) rootView.findViewById(R.id.memo_btn);
        outlet_name_txt = (TextView) rootView.findViewById(R.id.outlet_name_txt);
        outletAddress_txt = (TextView) rootView.findViewById(R.id.outletAddress_txt);
        order_radio_group = (RadioGroup) rootView.findViewById(R.id.order_radio_group);
        bundle = getArguments();
        shopeId = bundle.getString("getShopeId");
        shopName = bundle.getString("getShopName");
        shopAddress = bundle.getString("getAddress");

        categorySpinner = (AppCompatSpinner) rootView.findViewById(R.id.beat_spinner);

        outlet_rcview = (RecyclerView) rootView.findViewById(R.id.outlet_rcview);

        productCategoryList = new ArrayList<>();
        productList = new ArrayList<>();
        productCategorySpinnerAdapter = new ProductCategorySpinnerAdapter(mFragmentContext, R.layout.beat_list, productCategoryList);
        productCategorySpinnerAdapter.setDropDownViewResource(R.layout.beat_list);

        gridLayoutManager = new GridLayoutManager(mFragmentContext, 2);
        productListAdapter = new ProductListAdapter(mFragmentContext, productList, shopeId);

        outlet_rcview.setAdapter(productListAdapter);
        outlet_rcview.setHasFixedSize(true);
        outlet_rcview.setLayoutManager(gridLayoutManager);

        relationshipStatus = ((RadioButton) rootView.findViewById(order_radio_group.getCheckedRadioButtonId())).getText().toString();
    }

    private void action() {
        outlet_name_txt.setText(shopName);
        outletAddress_txt.setText(shopAddress);
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
                if (NetworkUtils.hasInternetConnection(mFragmentContext) &&
                        !productCategoryList.get(position).getName().matches("Select Category")) {
                    fetchDataFromServerForProductList(productCategoryList.get(position).getId(), String.valueOf(selected));
                }
                Log.e(TAG, "beatList.get(position).getId() : " + productCategoryList.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        order_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.order_rb) {
                    selected = 0;
                    if (NetworkUtils.hasInternetConnection(mFragmentContext)) {
                        try {
                            fetchDataFromServerForProductList(productCategoryList.get(position2).getId(), String.valueOf(selected));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (checkedId == R.id.not_order_rb) {
                    selected = 1;
                    if (NetworkUtils.hasInternetConnection(mFragmentContext)) {
                        try {
                            fetchDataFromServerForProductList(productCategoryList.get(position2).getId(), String.valueOf(selected));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        });
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

    private void fetchDataFromServerForProductCategory() {
        ProgressDialog progressDialog = null;
        progressDialog = new ProgressDialog(mFragmentContext);
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
                finalProgressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DATE);

                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", UserPreferences.getId(mFragmentContext));
                params.put("tokenKey", UserPreferences.getToken(mFragmentContext));
                params.put("outletId", shopeId);
                params.put("orderDate", day + "/" + month + "/" + year);
                return params;
            }

        };
        // Adding request to request queue
        RuchiraApplication.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void fetchDataFromServerForProductList(final String id, final String option) {
        ProgressDialog progressDialog = null;
        progressDialog = new ProgressDialog(mFragmentContext);
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
                finalProgressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", UserPreferences.getId(mFragmentContext));
                params.put("tokenKey", UserPreferences.getToken(mFragmentContext));
                params.put("outletId", id);
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
            ProductCategory productCategory = new ProductCategory();
            productCategory.setName("Select Category");
            productCategoryList.add(productCategory);
            if (responseResult == 1) {
                outlet_name_txt.setText(obj.getString("outletName"));
                outletAddress_txt.setText(obj.getString("outletAddress"));
                UserPreferences.saveOrderId(mFragmentContext, obj.getString("orderId"));
                productCategoryList.addAll(TaskUtils.setProductCategory(result));
                productCategorySpinnerAdapter.notifyDataSetChanged();
                return;

            } else {
                ViewUtils.alertUser(mFragmentContext, "Server Error");
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

                Log.d(TAG, " Size Task Utils: " + TaskUtils.setProductList(result).size());
                Log.d(TAG, " Size Product List: " + tempproductList.size());

                for (int i = 0; i < tempproductList.size(); i++) {
                    if (tempproductList.get(i).getFlag().equals(option)) {
                        productList.add(tempproductList.get(i));
                    }
                }
                productListAdapter.notifyDataSetChanged();
                return;
            } else {
                ViewUtils.alertUser(mFragmentContext, "Server Error");
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void openMemoFragment() {
        toLaunchFragment = new MemoFragment();
        if (toLaunchFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString("getShopeId", shopeId);
            bundle.putString("getOrderId", UserPreferences.getOrderId(mFragmentContext));
            toLaunchFragment.setArguments(bundle);
            ViewUtils.launchFragmentKeepingInBackStack(mFragmentContext, toLaunchFragment);
            toLaunchFragment = null;
        }
    }
}