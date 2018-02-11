package com.techcoderz.ruchira.Fragments.DashBoardFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.techcoderz.ruchira.ServerInfo.ServerInfo;
import com.techcoderz.ruchira.Utils.AppConfig;
import com.techcoderz.ruchira.Utils.NetworkUtils;
import com.techcoderz.ruchira.Utils.UserPreferences;
import com.techcoderz.ruchira.Utils.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

/**
 * Created by Shahriar on 9/14/2016.
 */
public class DashBoardFragment extends RuchiraFragment {
    private final static String TAG = "DashBoardFragment";
    private Fragment toLaunchFragment = null;
    private TextView view_more_txt, tvTodaysSale, ordersummary_txt,
            todays_target_txt, outlet_remainning_txt, remainning_txt,order_summery;

    OfflineInfo offlineInfo;

   // Context context;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        offlineInfo=new OfflineInfo(getContext());
        setupActionBar();
        initialize(rootView);
        action();
        if(NetworkUtils.hasInternetConnection(mFragmentContext)) {
            fetchDataFromServer();
        }



        return rootView;
    }

    private void setupActionBar() {
        mFragmentContext.getSupportActionBar().show();
        mFragmentContext.getSupportActionBar().setTitle("DashBoard");
    }

    private void initialize(View rootView) {
        view_more_txt = (TextView) rootView.findViewById(R.id.view_more_txt);
        order_summery = (TextView) rootView.findViewById(R.id.order_summery);
        tvTodaysSale = (TextView) rootView.findViewById(R.id.blance_txt);
        ordersummary_txt = (TextView) rootView.findViewById(R.id.ordersummary_txt);
        todays_target_txt = (TextView) rootView.findViewById(R.id.todays_target_txt);
        outlet_remainning_txt = (TextView) rootView.findViewById(R.id.outlet_remainning_txt);
        remainning_txt = (TextView) rootView.findViewById(R.id.remainning_txt);
    }

    private void action() {
        view_more_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openViewDetailsFragment();
            }
        });
    }

    private void fetchDataFromServer() {
        String tag_string_req = "req_dashboard";
        ProgressDialog progressDialog = null;
        progressDialog = new ProgressDialog(mFragmentContext);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        final ProgressDialog finalProgressDialog = progressDialog;

        /*************Must write*************************************/
        AsyncHttpClient client=new AsyncHttpClient();
        client.addHeader("Authorization","Bearer "+offlineInfo.getUserInfo().token);
        /***********************************************************/

        /*client.get("URL",new JsonHttpResponseHandler(){

        });*/

        RequestParams params=new RequestParams();

        client.post(ServerInfo.BASE_ADDRESS+"dashboard",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /*
                    "todaySell": "2088.8",
                    "todayOrder": "2",
                    "todayTarget": 4127,
                    "overSell": "00",
                    "outletRemaining": "00"
                */

                try {
                    String todaySell=response.getString("todaySell");
                    tvTodaysSale.setText(todaySell);
                    String todayOrder=response.getString("todayOrder");
                    order_summery.setText(todayOrder);
                    int todayTarget=response.getInt("todayTarget");
                    String overSell=response.getString("overSell");
                    String outletRemaining=response.getString("outletRemaining");


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
        try {
            JSONObject obj = new JSONObject(result);
            int responseResult = obj.getInt("success");
            if (responseResult == 1) {
                tvTodaysSale.setText(obj.getString("todaySale") + " ৳");
                ordersummary_txt.setText(obj.getString("orderSummary"));
                String remaining = obj.getString("remaining");
                if (remaining.charAt(0) == '-')
                    remainning_txt.setTextColor(getResources().getColor(R.color.matRed));
                else remainning_txt.setTextColor(getResources().getColor(R.color.matGreen));
                remainning_txt.setText(remaining);
                outlet_remainning_txt.setText(obj.getString("outletRemaining"));
                todays_target_txt.setText(obj.getString("todayTarget"));
                return;

            } else {
                ViewUtils.alertUser(mFragmentContext, "Server Error");
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void openViewDetailsFragment() {
        toLaunchFragment = new ViewDetailsFragment();
        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(mFragmentContext, toLaunchFragment);
            toLaunchFragment = null;
        }
    }

    /*@Override
    public void onAttach(Context context) {
        this.context=context;
    }*/
}