package com.techcoderz.ruchira.Fragments.TodaysStatusFragment;

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
import com.techcoderz.ruchira.ModelClasses.OutletRemainning;
import com.techcoderz.ruchira.ModelClasses.Target;
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
public class TodayStatusFragment extends RuchiraFragment {
    private final static String TAG = "TodayStatusFragment";

    private TextView date_txt,
            this_month_txt,
            current_achive_txt,
            remainnig_target_txt,
            remainnig_visit_txt,
            avg_target_visit_txt;

    private TextView total_outlet_txt,
            outlet_remained_txt,
            outlet_visited_txt,
            achive_percent_sr_txt,
            achive_today_txt;

    private List<Target> targetList;
    private List<OutletRemainning> outletRemainingList;

    OfflineInfo offlineInfo;

    public TodayStatusFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_todays_status, container, false);
        offlineInfo=new OfflineInfo(getContext());
        initialize(rootView);
        action();
        return rootView;
    }

    private void action() {
        mFragmentContext.getSupportActionBar().setTitle("Today\'s Status");
        if (NetworkUtils.hasInternetConnection(mFragmentContext)) {
            fetchDataFromServer();
        }
    }

    private void initialize(View rootView) {
        date_txt = (TextView) rootView.findViewById(R.id.date_txt);
        this_month_txt = (TextView) rootView.findViewById(R.id.this_month_txt);
        current_achive_txt = (TextView) rootView.findViewById(R.id.current_achive_txt);
        remainnig_target_txt = (TextView) rootView.findViewById(R.id.remainnig_target_txt);
        remainnig_visit_txt = (TextView) rootView.findViewById(R.id.remainnig_visit_txt);
        avg_target_visit_txt = (TextView) rootView.findViewById(R.id.avg_target_visit_txt);

        total_outlet_txt = (TextView) rootView.findViewById(R.id.total_outlet_txt);
        outlet_remained_txt = (TextView) rootView.findViewById(R.id.outlet_remained_txt);
        outlet_visited_txt = (TextView) rootView.findViewById(R.id.outlet_visited_txt);

        achive_percent_sr_txt = (TextView) rootView.findViewById(R.id.achive_percent_sr_txt);
        achive_today_txt = (TextView) rootView.findViewById(R.id.achive_today_txt);

        targetList = new ArrayList<>();
        outletRemainingList = new ArrayList<>();
    }

    private void fetchDataFromServer() {
        ProgressDialog progressDialog = null;
        progressDialog = new ProgressDialog(mFragmentContext);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String tag_string_req = "req_today_status";
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


                try {
                    String todaySell=response.getString("todaySell");
                    /*todaysSale.setText(todaySell+ " ৳");

                    String todayOrder=response.getString("todayOrder");
                    orderSummary.setText(todayOrder);

                    int todayTarget=response.getInt("todayTarget");
                    todayTargetTv.setText(Integer.toString(todayTarget));

                    String outletRemaining=response.getString("outletRemaining");
                    remainningOutletTv.setText(outletRemaining);

                    String overSell=response.getString("overSell");
                    remainningTv.setText(overSell);*/

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
        targetList.clear();
        outletRemainingList.clear();
        try {
            JSONObject obj = new JSONObject(result);
            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                date_txt.setText(obj.getString("orderDate"));
                targetList.addAll(TaskUtils.setTarget(result));
                outletRemainingList.addAll(TaskUtils.setOutletRemainning(result));

                try {
                    this_month_txt.setText(targetList.get(0).getThisMonth() + " ৳");
                    current_achive_txt.setText(targetList.get(0).getCurrentAchive() + " ৳");

                    String remaining = targetList.get(0).getRemainningTarget();
                    if (remaining.charAt(0) == '-') {
                        remaining.substring(1);
                        remainnig_target_txt.setTextColor(getResources().getColor(R.color.matGreen));
                    } else{
                        remainnig_target_txt.setTextColor(getResources().getColor(R.color.matRed));
                    }
                    remainnig_target_txt.setText(remaining + " ৳");

                    avg_target_visit_txt.setText(targetList.get(0).getAvgTargetVisit() + " ৳");
                    remainnig_visit_txt.setText(targetList.get(0).getRemainingVisit());

                    total_outlet_txt.setText(outletRemainingList.get(0).getTotalOutlet());
                    outlet_remained_txt.setText(outletRemainingList.get(0).getOutletRemained());
                    outlet_visited_txt.setText(outletRemainingList.get(0).getOutletVisited());
                    achive_today_txt.setText(outletRemainingList.get(0).getOutletAchieve() + " ৳");
                    achive_percent_sr_txt.setText(outletRemainingList.get(0).getAchieveInPercent());
                } catch (Exception e) {
                    e.printStackTrace();
                }
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