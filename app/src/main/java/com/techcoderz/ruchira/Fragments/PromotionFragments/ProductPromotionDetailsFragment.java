package com.techcoderz.ruchira.Fragments.PromotionFragments;

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
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.Adapters.ProductPromotionDetailAdapter;
import com.techcoderz.ruchira.Application.RuchiraApplication;
import com.techcoderz.ruchira.Fragments.OtherFragments.RuchiraFragment;
import com.techcoderz.ruchira.ModelClasses.Promotion;
import com.techcoderz.ruchira.Utils.AppConfig;
import com.techcoderz.ruchira.Utils.NetworkUtils;
import com.techcoderz.ruchira.Utils.TaskUtils;
import com.techcoderz.ruchira.Utils.UserPreferences;
import com.techcoderz.ruchira.Utils.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shahriar on 9/18/2016.
 */
public class ProductPromotionDetailsFragment extends RuchiraFragment {
    private final static String TAG = "PromotionDetlsFragment";
    private Fragment toLaunchFragment = null;
    private TextView product_name_txt,title_product_name_txt, sku_txt, product_id_txt, selling_price_txt;
    private RecyclerView report_rcview;
    private ProductPromotionDetailAdapter productPromotionAdapter;
    private LinearLayoutManager manager;
    private List<Promotion> promotionList;

    public ProductPromotionDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product_promotion_details, container, false);

        setupToolbar();
        initialize(rootView);
        action();
        return rootView;
    }

    private void setupToolbar() {
        mFragmentContext.getSupportActionBar().show();
        mFragmentContext.getSupportActionBar().setTitle("Product Promotion Details");
    }

    private void initialize(View rootView) {
        promotionList = new ArrayList<>();
        title_product_name_txt = (TextView) rootView.findViewById(R.id.title_product_name_txt);
        product_name_txt = (TextView) rootView.findViewById(R.id.product_name_txt);
        sku_txt = (TextView) rootView.findViewById(R.id.sku_txt);
        product_id_txt = (TextView) rootView.findViewById(R.id.product_id_txt);
        selling_price_txt = (TextView) rootView.findViewById(R.id.selling_price_txt);

        report_rcview = (RecyclerView) rootView.findViewById(R.id.report_rcview);
        manager = new LinearLayoutManager(mFragmentContext);
        productPromotionAdapter = new ProductPromotionDetailAdapter(mFragmentContext, promotionList);

        report_rcview.setAdapter(productPromotionAdapter);
        report_rcview.setHasFixedSize(true);
        report_rcview.setLayoutManager(manager);
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


    }

    private void executeForPromotionDetails(String result) {
        promotionList.clear();
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
                promotionList.addAll(TaskUtils.setProductPromotion(result, 1));
                productPromotionAdapter.notifyDataSetChanged();
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