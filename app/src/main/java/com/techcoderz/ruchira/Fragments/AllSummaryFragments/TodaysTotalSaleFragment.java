package com.techcoderz.ruchira.Fragments.AllSummaryFragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class TodaysTotalSaleFragment extends RuchiraFragment {
    private final static String TAG = "TodaysTotalSaleFragment";
    private List<Report> reportList;
    private RecyclerView TodayAllSummaryListView;

    private TextView TodayUserIdTv,
            TodayPhoneTv,
            TodaySRidTv,
            TodayDateTv,
            TodayTotalSellTv;
    private LinearLayoutManager manager;
    private ReportAdapter reportAdapter;
    OfflineInfo offlineInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_todays_sell_status, container, false);
        offlineInfo = new OfflineInfo(getContext());
        setupToolbar();
        initialize(rootView);
        action();
        return rootView;
    }

    private void setupToolbar() {
        mFragmentContext.getSupportActionBar().show();
        mFragmentContext.getSupportActionBar().setTitle("All Summary");
    }

    private void initialize(View rootView) {
        reportList = new ArrayList<>();

        TodayUserIdTv = (TextView) rootView.findViewById(R.id.TodayUserIdTv);
        TodayPhoneTv = (TextView) rootView.findViewById(R.id.TodayPhoneTv);
        TodaySRidTv = (TextView) rootView.findViewById(R.id.TodaySRidTv);
        TodayDateTv = (TextView) rootView.findViewById(R.id.TodayDateTv);
        TodayAllSummaryListView = (RecyclerView) rootView.findViewById(R.id.TodayAllSummaryListView);
        TodayTotalSellTv = (TextView) rootView.findViewById(R.id.TodayTotalSellTv);

        manager = new LinearLayoutManager(mFragmentContext);
        reportAdapter = new ReportAdapter(mFragmentContext, reportList);

        TodayAllSummaryListView.setAdapter(reportAdapter);
        TodayAllSummaryListView.setHasFixedSize(true);
        TodayAllSummaryListView.setLayoutManager(manager);
    }

    private void action() {
        if (NetworkUtils.hasInternetConnection(mFragmentContext)) {
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

        String tag_string_req = "req_today_total_sale";
        final ProgressDialog finalProgressDialog = progressDialog;

        /*************Must write*************************************/
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Bearer " + offlineInfo.getUserInfo().token);
        /***********************************************************/

        /*client.get("URL",new JsonHttpResponseHandler(){

        });*/

        RequestParams params = new RequestParams();

        client.get(ServerInfo.BASE_ADDRESS + "today-sale", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


                /*
                {
    "user_name": "Ruchira Admin",
    "sr_id": "U007",
    "cell_on": "01577832252",
    "date": "2018-02-25",

    "todayList": {
        "total": 8,
        "per_page": 10,
        "current_page": 1,
        "last_page": 1,
        "next_page_url": null,
        "prev_page_url": null,
        "from": 1,
        "to": 8,
        "data": [
            {
                "item_name": "Horlicks-18g",
                "quantity": 10,
                "amount": 105
            },
            {
                "item_name": "Sensodyne Fresh Mint 130gm",
                "quantity": 5,
                "amount": 1250
            },
            {
                "item_name": "Sensodyne Brush",
                "quantity": 10,
                "amount": 640
            }
        ]
    },
    "todayTotal": 9870
}*/


                try {
                    String user_name = response.getString("user_name");
                    TodayUserIdTv.setText(user_name);

                    String cell_on = response.getString("cell_on");
                    TodayPhoneTv.setText(cell_on);

                    String sr_id = response.getString("sr_id");
                    TodaySRidTv.setText(sr_id);

                    String date = response.getString("date");
                    TodayDateTv.setText(date);

                    String todayTotal = response.getString("todayTotal");
                    TodayTotalSellTv.setText(todayTotal+" ৳");


                } catch (JSONException e) {

                }

            }

            /*****************Must write*****************************/
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
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
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                TodaySRidTv.setText("SR ID # " + obj.getString("userId"));
                TodayPhoneTv.setText("Cell : " + obj.getString("userPhone"));
                TodayUserIdTv.setText(obj.getString("userName"));
                TodayDateTv.setText(obj.getString("orderDate"));
                TodayTotalSellTv.setText(obj.getString("total") + " BDT");
                reportList.addAll(TaskUtils.setTodayReport(result));
                reportAdapter.notifyDataSetChanged();
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