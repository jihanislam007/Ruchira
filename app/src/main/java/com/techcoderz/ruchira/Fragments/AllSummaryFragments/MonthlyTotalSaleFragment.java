package com.techcoderz.ruchira.Fragments.AllSummaryFragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.techcoderz.ruchira.Activities.LoginActivity;
import com.techcoderz.ruchira.Db.OfflineInfo;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.Adapters.ReportAdapter;
import com.techcoderz.ruchira.Application.RuchiraApplication;
import com.techcoderz.ruchira.Fragments.OtherFragments.RuchiraFragment;
import com.techcoderz.ruchira.ModelClasses.Report;
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
 * Created by priom on 9/19/16.
 */
public class MonthlyTotalSaleFragment extends RuchiraFragment {
    private final static String TAG = "MonthlyTotalSale";
    private List<Report> reportList;
    private RecyclerView MonthlyAllSummaryRecyclerView;
    private TextView MonthlyUserIDTv,
            MonthlyphoneNumberTv,
            MonthlySRidTv,
            MonthlyDateTv,
            MonthlyTotalSellTextView;

    private LinearLayoutManager manager;
    private ReportAdapter reportAdapter;
    OfflineInfo offlineInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_monthly_total_sell, container, false);
        offlineInfo = new OfflineInfo(getContext());
        setupToolbar();
        initialize(rootView);
        if (NetworkUtils.hasInternetConnection(mFragmentContext)) {
            fetchDataFromServer();
        }
        return rootView;
    }

    private void setupToolbar() {
        mFragmentContext.getSupportActionBar().show();
        mFragmentContext.getSupportActionBar().setTitle("All Summary");
    }

    private void initialize(View rootView) {
        reportList = new ArrayList<>();

        MonthlyUserIDTv = (TextView) rootView.findViewById(R.id.MonthlyUserIDTv);
        MonthlyphoneNumberTv = (TextView) rootView.findViewById(R.id.MonthlyphoneNumberTv);
        MonthlySRidTv = (TextView) rootView.findViewById(R.id.MonthlySRidTv);
        MonthlyDateTv = (TextView) rootView.findViewById(R.id.MonthlyDateTv);
        MonthlyAllSummaryRecyclerView = (RecyclerView) rootView.findViewById(R.id.MonthlyAllSummaryRecyclerView);
        MonthlyTotalSellTextView = (TextView) rootView.findViewById(R.id.MonthlyTotalSellTextView);

        manager = new LinearLayoutManager(mFragmentContext);
        reportAdapter = new ReportAdapter(mFragmentContext, reportList);

        MonthlyAllSummaryRecyclerView.setAdapter(reportAdapter);
        MonthlyAllSummaryRecyclerView.setHasFixedSize(true);
        MonthlyAllSummaryRecyclerView.setLayoutManager(manager);
    }

    private void fetchDataFromServer() {
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(mFragmentContext);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String tag_string_req = "req_monthly_total_sale";
        final ProgressDialog finalProgressDialog = progressDialog;

        /*************Must write*************************************/
        AsyncHttpClient client=new AsyncHttpClient();
        client.addHeader("Authorization","Bearer "+offlineInfo.getUserInfo().token);
        /***********************************************************/

        /*client.get("URL",new JsonHttpResponseHandler(){

        });*/

        RequestParams params=new RequestParams();

        client.get(ServerInfo.BASE_ADDRESS+"monthly-sale",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                /*
                {
    "user_name": "Ruchira Admin",
    "sr_id": "U007",
    "cell_on": "01577832252",
    "month": "February",

    "monthlyList": {
        "total": 4,
        "per_page": 20,
        "current_page": 1,
        "last_page": 1,
        "next_page_url": null,
        "prev_page_url": null,
        "from": 1,
        "to": 4,
        "data": [
            {
                "order_date": "2018-02-01",
                "orders": 2,
                "amount": 820
            },
            {
                "order_date": "2018-02-21",
                "orders": 1,
                "amount": 1400
            },
            {
                "order_date": "2018-02-22",
                "orders": 1,
                "amount": 566
            },
            {
                "order_date": "2018-02-25",
                "orders": 4,
                "amount": 9870
            }
        ]
    },
    "monthlyTotal": 12656
}*/

                try {

                    String user_name=response.getString("user_name");
                    MonthlyUserIDTv.setText(user_name);

                    String cell_on=response.getString("cell_on");
                    MonthlyphoneNumberTv.setText(cell_on);

                    String sr_id=response.getString("sr_id");
                    MonthlySRidTv.setText(sr_id);

                    String month=response.getString("month");
                    MonthlyDateTv.setText(month);

                    String monthlyTotal =response.getString("monthlyTotal");
                    MonthlyTotalSellTextView.setText(monthlyTotal+" ৳");


                } catch (JSONException e) {

                }

            }

            /*****************Must write*****************************/
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Intent intent=new Intent(getContext(), LoginActivity.class);
                getContext().startActivity(intent);
                offlineInfo.setUserInfo("");
            }

            @Override
            public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
                finalProgressDialog.dismiss(); //Just change dialog name
            }
            /***************************************/
        });


    }

    private void execute(String result) {
        Log.d(TAG, result.toString());
        reportList.clear();
        try {
            JSONObject obj = new JSONObject(result);
            int responseResult = obj.getInt("success");
            if (responseResult == 1) {
                MonthlyUserIDTv.setText(obj.getString("userName"));
                MonthlyphoneNumberTv.setText("Cell: " + obj.getString("userPhone"));
                MonthlySRidTv.setText("SR ID # " + obj.getString("userId"));
                MonthlyDateTv.setText(obj.getString("month"));

                MonthlyTotalSellTextView.setText(obj.getString("total") + " BDT");
                reportList.addAll(TaskUtils.setMonthlyReport(result));
                reportAdapter.notifyDataSetChanged();
            } else {
                ViewUtils.alertUser(mFragmentContext, "Server Error");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}