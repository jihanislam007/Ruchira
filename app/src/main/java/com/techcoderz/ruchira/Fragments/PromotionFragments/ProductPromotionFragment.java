package com.techcoderz.ruchira.Fragments.PromotionFragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.techcoderz.ruchira.Activities.LoginActivity;
import com.techcoderz.ruchira.Db.OfflineInfo;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.Adapters.productPromotionAdapter;
import com.techcoderz.ruchira.Adapters.PromotionCompanySpinnerAdapter;
import com.techcoderz.ruchira.Application.RuchiraApplication;
import com.techcoderz.ruchira.Fragments.OtherFragments.RuchiraFragment;
import com.techcoderz.ruchira.ModelClasses.Company;
import com.techcoderz.ruchira.ModelClasses.Promotion;
import com.techcoderz.ruchira.ServerInfo.ServerInfo;
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

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

/**
 * Created by Shahriar on 9/14/2016.
 */
public class ProductPromotionFragment extends RuchiraFragment {
    private final static String TAG = "PromotionFragment";
    private Fragment toLaunchFragment = null;

    private List<Promotion> promotionList;
    private List<Company> companyList;

    private AppCompatSpinner beat_spinner;
    private PromotionCompanySpinnerAdapter promotionCompanySpinnerAdapter;

    private RecyclerView report_rcview;
    private productPromotionAdapter productPromotionAdapter;
    private LinearLayoutManager manager;
    private TextView company_title_txt;

    OfflineInfo offlineInfo;

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
        offlineInfo = new OfflineInfo(getContext());
        initialize(rootView);
        action();
        if(NetworkUtils.hasInternetConnection(mFragmentContext)) {
            fetchDataFromServer();
        }
        return rootView;
    }

    private void setupToolbar() {
        mFragmentContext.getSupportActionBar().show();
        mFragmentContext.getSupportActionBar().setTitle("Product Promotions");
    }

    private void initialize(View rootView) {
        beat_spinner = (AppCompatSpinner) rootView.findViewById(R.id.beat_spinner);
        report_rcview = (RecyclerView) rootView.findViewById(R.id.report_rcview);
      //  company_title_txt = (TextView) rootView.findViewById(R.id.company_title_txt);

        promotionList = new ArrayList<>();
        companyList = new ArrayList<>();

        promotionCompanySpinnerAdapter = new PromotionCompanySpinnerAdapter(mFragmentContext, R.layout.beat_list, companyList);
        promotionCompanySpinnerAdapter.setDropDownViewResource(R.layout.beat_list);

        manager = new LinearLayoutManager(mFragmentContext);
        productPromotionAdapter = new productPromotionAdapter(mFragmentContext, promotionList);

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
                if(NetworkUtils.hasInternetConnection(mFragmentContext) &&
                        !companyList.get(position).getCompanyName().matches("Select a Company")) {
                    fetchDataFromServerForPromotion(companyList.get(position).getCompanyId());
                }
                Log.e(TAG, "companyList.get(position).getCompanyId() : " + companyList.get(position).getCompanyId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void fetchDataFromServerForPromotion(final String id) {
        UserPreferences.saveCompanyId(mFragmentContext,id);

        ProgressDialog progressDialog = null;
        progressDialog = new ProgressDialog(mFragmentContext);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String tag_string_req = "req_promotion";
        final ProgressDialog finalProgressDialog = progressDialog;

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

/****************HttpClient responds****************/
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Bearer "+offlineInfo.getUserInfo().token);

        RequestParams params = new RequestParams();

        client.post(ServerInfo.BASE_ADDRESS+"dashboard",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


            }


            /*****************Must write*****************************/
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Intent intent=new Intent(getContext(), LoginActivity.class);
                getContext().startActivity(intent);
                offlineInfo.setUserInfo("");
            }

            @Override
            public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse
                    response) {
                finalProgressDialog.dismiss(); //Just change dialog name
            }
            /***************************************/
        });

    }


    private void executeForCompany(String result) {
        Log.d(TAG, result.toString());
        companyList.clear();
        try {
            JSONObject obj = new JSONObject(result);
            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                Company company = new Company();
                company.setCompanyName("Select a Company");
                companyList.add(company);
                companyList.addAll(TaskUtils.setCompany(result));
                promotionCompanySpinnerAdapter.notifyDataSetChanged();
                return;
            } else {
                ViewUtils.alertUser(mFragmentContext, "Server Error");
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
                promotionList.addAll(TaskUtils.setProductPromotion(result, 0));
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