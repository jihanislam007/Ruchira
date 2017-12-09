package com.techcoderz.ruchira.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
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
import com.techcoderz.ruchira.Application.RuchiraApplication;
import com.techcoderz.ruchira.Fragments.OrderFragments.OrderSubmitFragment;
import com.techcoderz.ruchira.ModelClasses.ProductList;
import com.techcoderz.ruchira.Utils.AppConfig;
import com.techcoderz.ruchira.Utils.UserPreferences;
import com.techcoderz.ruchira.Utils.ViewUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String TAG = "ProductListAdapter";
    private List<ProductList> productList = new ArrayList<>();
    private Context context;
    private Fragment toLaunchFragment = null;
    private String shopeId, orderId;
    private Bundle bundle;
    private int selectedCategoryId;
    private SharedPreferences.Editor selectedItemEditor;

    public ProductListAdapter(Context context, List<ProductList> productList,
                              String shopeId, String orderId, int selectedCtgId, SharedPreferences.Editor editor) {
        this.productList = productList;
        this.context = context;
        this.shopeId = shopeId;
        this.orderId = orderId;
        this.selectedCategoryId = selectedCtgId;
        this.selectedItemEditor = editor;
        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(context, toLaunchFragment);
        }
    }

    @Override
    public ProductListAdapter.RecyclerViewSubHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_add_new_order, null);
        ProductListAdapter.RecyclerViewSubHolders rcv = new ProductListAdapter.RecyclerViewSubHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ProductListAdapter.RecyclerViewSubHolders) {
            if (productList.size() > 0) {
                ((RecyclerViewSubHolders) holder).item_btn.setText(productList.get(position).getProductName());
                ((ProductListAdapter.RecyclerViewSubHolders) holder).wholeContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedItemEditor.putInt("categoryId", selectedCategoryId);
                        selectedItemEditor.commit();
                        openOrderSubmitFragment(position);
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return this.productList.size();
    }

    private void openOrderSubmitFragment(int position) {
        toLaunchFragment = new OrderSubmitFragment();
        if (toLaunchFragment != null) {
            bundle = new Bundle();
            bundle.putString("getShopeId", shopeId);
            bundle.putString("getproductId", productList.get(position).getProductId());
            bundle.putString("getpromotionId", productList.get(position).getPromotionId());
            bundle.putInt("getPricePerCarton", productList.get(position).getPricePerCarton());
            bundle.putInt("getPricePerPiece", productList.get(position).getPricePerPiece());
            bundle.putString("getProductSku", productList.get(position).getProductSku());
            bundle.putString("getOrderId", orderId);

            Log.d(TAG, "getproductId  " + productList.get(position).getProductId());
            Log.d(TAG, "getpromotionId  " + productList.get(position).getPromotionId());
            Log.d(TAG, "getPricePerCarton  " + productList.get(position).getPricePerCarton());
            Log.d(TAG, "getPricePerPiece  " + productList.get(position).getPricePerPiece());
            Log.d(TAG, "getProductSku  " + productList.get(position).getProductSku());
            Log.d(TAG, "getOrderId  " + orderId);
            Log.d(TAG, "getOutletID  " + shopeId);

            fetchDataFromServerForProductList();
        }
    }


    class RecyclerViewSubHolders extends RecyclerView.ViewHolder {
        public TextView item_btn;
        public CardView wholeContent;

        public RecyclerViewSubHolders(View itemView) {
            super(itemView);
            item_btn = (TextView) itemView.findViewById(R.id.item_btn);
            wholeContent = (CardView) itemView.findViewById(R.id.card_view_ordered);
        }
    }


    private void fetchDataFromServerForProductList() {
        ProgressDialog progressDialog = null;
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String tag_string_req = "req_product_list";
        final ProgressDialog finalProgressDialog = progressDialog;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_VIEW_ORDER_ITEM, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "product_list Response: " + response.toString());
                finalProgressDialog.dismiss();
                processResult(response);
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
                Log.d(TAG, shopeId);
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", UserPreferences.getUserId(context));
                params.put("tokenKey", UserPreferences.getToken(context));
                params.put("outletId", bundle.getString("getShopeId"));
                params.put("productId", bundle.getString("getproductId"));
                params.put("orderId", orderId);
                return params;
            }
        };

        // Adding request to request queue
        RuchiraApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void processResult(String response) {
        JSONObject obj = null;
        String prevCtn, prevPiece;
        try {
            obj = new JSONObject(response);
            int responseResult = obj.getInt("success");
            JSONArray jsonArray = obj.getJSONArray("orderdetails");
            Log.d(TAG, response.toString());
            if (responseResult == 1) {
                for (int count = 0; count < jsonArray.length(); count++) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    prevCtn = object.getString("totalCtn");
                    prevPiece = object.getString("totalPcs");
                    Log.d(TAG, " prev ctn: " + prevCtn);
                    Log.d(TAG, " prev piece: " + prevPiece);
                    bundle.putString("prevCtn", prevCtn);
                    bundle.putString("prevPiece", prevPiece);
                }
                toLaunchFragment.setArguments(bundle);
                ViewUtils.launchFragmentKeepingInBackStack(context, toLaunchFragment);
                toLaunchFragment = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

