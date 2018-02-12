package com.techcoderz.ruchira.Fragments.DashBoardFragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.techcoderz.ruchira.Activities.LoginActivity;
import com.techcoderz.ruchira.Db.OfflineInfo;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.Application.RuchiraApplication;
import com.techcoderz.ruchira.Fragments.OtherFragments.RuchiraFragment;
import com.techcoderz.ruchira.ModelClasses.TodayOrder;
import com.techcoderz.ruchira.ModelClasses.TodaySale;
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

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.entity.mime.Header;

/**
 * Created by Shahriar on 9/14/2016.
 */
public class ViewDetailsFragment extends RuchiraFragment {
    private final static String TAG = "ViewDetailsFragment";

    private TextView total_bit_txt,
            sale_yesterDay_txt,
            sale_monthAccumulation_txt,
            sale_month_day_avg_txt,
            sale_month_week_avg_txt,
            today_sale_txt;

    private TextView order_yesterday_txt,
            order_monthAccumulation_txt,
            order_monthDailyAvg_txt,
            order_monthWeekly_avg_txt,
            today_order_txt,
            totalOutletTv;

    private List<TodaySale> todaySaleList;
    private List<TodayOrder> todayOrderList;

    OfflineInfo offlineInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_deatils, container, false);
        setupToolbar();
        offlineInfo = new OfflineInfo(getContext());
        initialize(rootView);
        action();
        return rootView;
    }

    private void action() {
        if (NetworkUtils.hasInternetConnection(mFragmentContext)) {
            fetchDataFromServer();
        }
    }

    private void setupToolbar() {
        mFragmentContext.getSupportActionBar().show();
        mFragmentContext.getSupportActionBar().setTitle("View Details");
    }

    private void initialize(View rootView) {
        total_bit_txt = (TextView) rootView.findViewById(R.id.total_bit_txt);
        totalOutletTv = (TextView) rootView.findViewById(R.id.totalOutletTv);

        today_sale_txt = (TextView) rootView.findViewById(R.id.today_sale_txt);
        sale_yesterDay_txt = (TextView) rootView.findViewById(R.id.sale_yesterDay_txt);
        sale_monthAccumulation_txt = (TextView) rootView.findViewById(R.id.sale_monthAccumulation_txt);
        sale_month_week_avg_txt = (TextView) rootView.findViewById(R.id.sale_month_week_avg_txt);
        sale_month_day_avg_txt = (TextView) rootView.findViewById(R.id.sale_month_day_avg_txt);

        today_order_txt = (TextView) rootView.findViewById(R.id.today_order_txt);
        order_yesterday_txt = (TextView) rootView.findViewById(R.id.order_yesterday_txt);
        order_monthAccumulation_txt = (TextView) rootView.findViewById(R.id.order_monthAccumulation_txt);
        order_monthWeekly_avg_txt = (TextView) rootView.findViewById(R.id.order_monthWeekly_avg_txt);
        order_monthDailyAvg_txt = (TextView) rootView.findViewById(R.id.order_monthDailyAvg_txt);

        todayOrderList = new ArrayList<>();
        todaySaleList = new ArrayList<>();
    }

    private void fetchDataFromServer() {
        ProgressDialog progressDialog = null;
        progressDialog = new ProgressDialog(mFragmentContext);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String tag_string_req = "req_viewmore";
        final ProgressDialog finalProgressDialog = progressDialog;

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Bearer " + offlineInfo.getUserInfo().token);

        RequestParams params = new RequestParams();

        client.post(ServerInfo.BASE_ADDRESS+"dashboard-details",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                /*
                    "{
                        ""totalBeat"": ""2"",
                        ""totalOutlet"": ""3"",
                        ""todaySale"": ""2088.8"",
                        ""yesterdaySale"": ""3492"",
                        ""monthSale"": ""10232.8"",
                        ""weekAvgSale"": 2558.2,
                        ""dayAvgSale"": 330.09,
                        ""todayOrder"": ""2"",
                        ""yesterdayOrder"": ""2"",
                        ""monthOrder"": ""5"",
                        ""weekAvgOrder"": 1.25,
                        ""dayAvgOrder"": 0.16
                        }"
                */

                try {
                    String totalBeat=response.getString("totalOutlet");
                    total_bit_txt.setText(totalBeat);
                    String totalOutlet=response.getString("totalOutlet");
                    totalOutletTv.setText(totalOutlet);

                    String todaySale=response.getString("todaySale");
                    today_sale_txt.setText(todaySale);
                    String yesterdaySale=response.getString("yesterdaySale");
                    sale_yesterDay_txt.setText(yesterdaySale);
                    String monthSale=response.getString("monthSale");
                    sale_monthAccumulation_txt.setText(monthSale);
                    Double weekAvgSale = response.getDouble("weekAvgSale");
                    sale_month_week_avg_txt.setText(Double.toString(weekAvgSale));
                    Double dayAvgSale = response.getDouble("dayAvgSale");
                    sale_month_day_avg_txt.setText(Double.toString(dayAvgSale));


                    String todayOrder = response.getString("todayOrder");
                    today_order_txt.setText(todayOrder);
                    String yesterdayOrder = response.getString("yesterdayOrder");
                    order_yesterday_txt.setText(yesterdayOrder);
                    String monthOrder = response.getString("monthOrder");
                    order_monthAccumulation_txt.setText(monthOrder);
                    Double weekAvgOrder = response.getDouble("weekAvgSale");
                    order_monthWeekly_avg_txt.setText(Double.toString(weekAvgOrder));
                    Double dayAvgOrder = response.getDouble("dayAvgSale");
                    order_monthDailyAvg_txt.setText(Double.toString(dayAvgOrder));


                } catch (JSONException e) {

                }

            }

            /*****************Must write*****************************/
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
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
        todaySaleList.clear();
        todayOrderList.clear();
        try {
            JSONObject obj = new JSONObject(result);
            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                total_bit_txt.setText(obj.getString("totalBeat"));
                totalOutletTv.setText(obj.getString("totalOutlet"));
                todayOrderList.addAll(TaskUtils.setTodayOrder(result));
                todaySaleList.addAll(TaskUtils.setTodayTotalSale(result));

                sale_yesterDay_txt.setText(todaySaleList.get(0).getYesterdeay() + " ৳");
                sale_monthAccumulation_txt.setText(todaySaleList.get(0).getMonthAccumulation() + " ৳");
                sale_month_day_avg_txt.setText(todaySaleList.get(0).getMonthDailyAvg() + " ৳");
                sale_month_week_avg_txt.setText(todaySaleList.get(0).getMonthWeeklyAvg() + " ৳");
                today_sale_txt.setText(todaySaleList.get(0).getTodaySale() + " ৳");

                order_yesterday_txt.setText(todayOrderList.get(0).getYesterdeay() + "  Pic/s");
                order_monthAccumulation_txt.setText(todayOrderList.get(0).getMonthAccumulation() + "  Pic/s");
                order_monthDailyAvg_txt.setText(todayOrderList.get(0).getMonthDailyAvg() + "  Pic/s");
                order_monthWeekly_avg_txt.setText(todayOrderList.get(0).getMonthWeeklyAvg() + "  Pic/s");
                today_order_txt.setText(todayOrderList.get(0).getTodayOrder() + "  Pic/s");

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