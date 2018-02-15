package com.techcoderz.ruchira.Fragments.OtherFragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.squareup.picasso.Picasso;
import com.techcoderz.ruchira.Activities.LoginActivity;
import com.techcoderz.ruchira.Db.OfflineInfo;
import com.techcoderz.ruchira.Profile_listview_adapter;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.Adapters.AreaAdapter;
import com.techcoderz.ruchira.Application.RuchiraApplication;
import com.techcoderz.ruchira.ModelClasses.Area;
import com.techcoderz.ruchira.ServerInfo.ServerInfo;
import com.techcoderz.ruchira.Utils.AppConfig;
import com.techcoderz.ruchira.Utils.NetworkUtils;
import com.techcoderz.ruchira.Utils.TaskUtils;
import com.techcoderz.ruchira.Utils.UserPreferences;
import com.techcoderz.ruchira.Utils.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Shahriar on 9/14/2016.
 */
public class ProfileFragment extends RuchiraFragment {
    private final static String TAG = "ProfileFragment";
    private Fragment toLaunchFragment = null;
    private List<Area> areaList;
    private ListView profileListView;
    private CircleImageView ProfileImageIconIv;
    private TextView ProfileUserNameTv,
            ProfileDesignationTv,
            ProfileJoiningDate,
            status_txt;

    private LinearLayoutManager manager;
    private AreaAdapter areaAdapter;
    OfflineInfo offlineInfo;


    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        offlineInfo = new OfflineInfo(getContext());
        setupToolbar();
//        initialize(rootView);
        if(NetworkUtils.hasInternetConnection(mFragmentContext)) {
            fetchDataFromServer();
        }
        return rootView;
    }

    private void setupToolbar() {
        mFragmentContext.getSupportActionBar().show();
        mFragmentContext.getSupportActionBar().setTitle("Profile");
    }

    private void initialize(View rootView) {
        areaList = new ArrayList<>();
        ProfileImageIconIv = (CircleImageView) rootView.findViewById(R.id.ProfileImageIconIv);
        ProfileUserNameTv = (TextView) rootView.findViewById(R.id.ProfileUserNameTv);
        ProfileDesignationTv = (TextView) rootView.findViewById(R.id.ProfileDesignationTv);
        ProfileJoiningDate = (TextView) rootView.findViewById(R.id.ProfileJoiningDate);

        profileListView = (ListView) rootView.findViewById(R.id.profileListView);
        status_txt = (TextView) rootView.findViewById(R.id.status_txt);

        manager = new LinearLayoutManager(mFragmentContext);
        areaAdapter = new AreaAdapter(mFragmentContext, areaList);
     //   profileListView.setAdapter(areaAdapter);
     //   profileListView.setHasFixedSize(true);
     //   profileListView.setLayoutManager(manager);

        ///////////////////////////////////////////////////
       /* areaList = new ArrayList<>();
        listView = (ListView) rootView.findViewById(R.id.profileListView);

        Profile_listview_adapter adapter = new Profile_listview_adapter(getContext() , Name);
        listView.setAdapter(adapter);*/
    }

    private void fetchDataFromServer() {
        ProgressDialog progressDialog = null;
        progressDialog = new ProgressDialog(mFragmentContext);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String tag_string_req = "req_profile";
        final ProgressDialog finalProgressDialog = progressDialog;

/*************Must write*************************************/
        AsyncHttpClient client=new AsyncHttpClient();
        client.addHeader("Authorization","Bearer "+offlineInfo.getUserInfo().token);
        /***********************************************************/

        /*client.get("URL",new JsonHttpResponseHandler(){

        });*/

        RequestParams params=new RequestParams();

        client.post(ServerInfo.BASE_ADDRESS+"user-profile",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                /* "{
                ""full_name"": ""Abul Kalam"",
                        ""email"": ""kalam@gmail.com"",
                        ""mobile_no"": ""01714343456"",
                        ""designation "": ""Sales Representative"",
                        ""joining_date"": ""07-01-2018"",
                        ""image"": ""http://ruchira.techcoderz.com/public/Backend/image/user/mjUifMenWshZY8yTk9VP.jpg"",
                                ""area"":
                                    [
                                        {
                                        ""beat_name"": ""Laboni More""
                                        },
                                        {
                                        ""beat_name"": ""Katia Bazar""
                                        }
                                    ]
            }"*/

                try {

                    /*URL url = new URL("http://image10.bizrate-images.com/resize?sq=60&uid=2216744464");
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    ProfileImageIconIv.setImageBitmap(bmp);*/

                    String full_name=response.getString("full_name");
                 //   ProfileUserNameTv.setText(full_name);

                    String designation=response.getString("designation");
                    ProfileDesignationTv.setText(designation);

                    int joining_date=response.getInt("joining_date");
                    ProfileJoiningDate.setText(Integer.toString(joining_date));

                    /*String outletRemaining=response.getString("outletRemaining");
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
        Log.d(TAG, result.toString());
        areaList.clear();
        try {
            JSONObject obj = new JSONObject(result);
            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                ProfileJoiningDate.setText("Joining Date : "+obj.getString("joiningDate"));
                ProfileDesignationTv.setText("Designation : "+obj.getString("desgination"));
                ProfileUserNameTv.setText(obj.getString("userName"));
                status_txt.setText(obj.getString("name"));
                Picasso.with(mFragmentContext)
                        .load(obj.getString("profileImg"))
                        .into(ProfileImageIconIv);

                areaList.addAll(TaskUtils.setArea(result));
                areaAdapter.notifyDataSetChanged();
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