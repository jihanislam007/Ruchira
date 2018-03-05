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
    private TextView view_more_txt,
            todaysSale,
            orderSummary,
            todayTargetTv,
            remainningOutletTv,
            remainningTv;

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

        todaysSale = (TextView) rootView.findViewById(R.id.todaysSale);
        orderSummary = (TextView) rootView.findViewById(R.id.orderSummary);
        todayTargetTv = (TextView) rootView.findViewById(R.id.todayTarget);
        remainningOutletTv = (TextView) rootView.findViewById(R.id.remainningOutlet);
        remainningTv = (TextView) rootView.findViewById(R.id.remainning);
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

        client.get(ServerInfo.BASE_ADDRESS+"dashboard",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response);
                /*
                    {
    "todaySell": 566,
    "orderSummary": "00",
    "todayTarget": 6390.71,
    "outletRemaining": 15,
    "remainingTarget": 44735
}
                */

                try {

                    int todaySell=response.getInt("todaySell");
                    todaysSale.setText(Integer.toString(todaySell)+ " ৳");

                    String todayOrder=response.getString("orderSummary");
                    orderSummary.setText(todayOrder);

                    String todayTarget=response.getString("todayTarget");
                    todayTargetTv.setText(todayTarget);

                    String outletRemaining=response.getString("outletRemaining");
                    remainningOutletTv.setText(outletRemaining);

                    String remainingTarget=response.getString("remainingTarget");
                    remainningTv.setText(remainingTarget);


                } catch (JSONException e) {
e.printStackTrace();
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
                todaysSale.setText(obj.getString("todaySale") + " ৳");
                orderSummary.setText(obj.getString("orderSummary"));
                String remaining = obj.getString("remaining");
                if (remaining.charAt(0) == '-')
                    remainningTv.setTextColor(getResources().getColor(R.color.matRed));
                else remainningTv.setTextColor(getResources().getColor(R.color.matGreen));
                remainningTv.setText(remaining);
                remainningOutletTv.setText(obj.getString("outletRemaining"));
                todayTargetTv.setText(obj.getString("todayTarget"));
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