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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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

import org.json.JSONArray;
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
    private RecyclerView profileRecyclerView;

    private ImageView ProfileImageIconIv;

    private TextView ProfileUserNameTv,
            ProfileDesignationTv,
            ProfileJoiningDate,
            ProfileTargetTv,
            ProfieRemainingTv;
    //   status_txt;

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
        initialize(rootView);
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

        ProfileImageIconIv = (ImageView) rootView.findViewById(R.id.ProfileImageIconIv);
        ProfileUserNameTv = (TextView) rootView.findViewById(R.id.ProfileUserNameTv);
        ProfileDesignationTv = (TextView) rootView.findViewById(R.id.ProfileDesignationTv);
        ProfileJoiningDate = (TextView) rootView.findViewById(R.id.ProfileJoiningDate);
        ProfileTargetTv = (TextView) rootView.findViewById(R.id.ProfileTargetTv);
        ProfieRemainingTv = (TextView) rootView.findViewById(R.id.ProfieRemainingTv);

        profileRecyclerView = (RecyclerView) rootView.findViewById(R.id.profileRecyclerView);
        // status_txt = (TextView) rootView.findViewById(R.id.status_txt);

        manager = new LinearLayoutManager(mFragmentContext);
        areaAdapter = new AreaAdapter(mFragmentContext, areaList);

        profileRecyclerView.setAdapter(areaAdapter);
        profileRecyclerView.setHasFixedSize(true);
        profileRecyclerView.setLayoutManager(manager);


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

                /* {
    "image": "http://localhost/ruchira/public/Backend/image/user/user.png",
    "user_name": "Ruchira Admin",
    "designation ": "Admin",
    "joining_date": "11-04-2017",
    "totalTarget": 55500,
    "remaingTarget": 42844,


    "area_list": [
        {
            "beat_name": "Boro Bazar"
        },
        {
            "beat_name": "Laboni More"
        }
    ]
}*/

                try {

                    Glide.with(getContext())
                            .load("image")
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(ProfileImageIconIv);

                    String user_name=response.getString("user_name");
                    ProfileUserNameTv.setText(user_name);

                    String designation =response.getString("designation ");
                    ProfileDesignationTv.setText(designation);

                    String joining_date =response.getString("joining_date");
                    ProfileJoiningDate.setText(joining_date);

                    int totalTarget =response.getInt("totalTarget");
                    ProfileTargetTv.setText(Integer.toString(totalTarget));

                    String remaingTarget =response.getString("remaingTarget");
                    ProfieRemainingTv.setText(remaingTarget);

             //       System.out.println("Try to load area");
             //       System.out.println(response.toString());

                    JSONArray jsonArray = response.getJSONArray("areaList");
                    System.out.println("area size "+jsonArray);
                    for(int i=0 ; i<jsonArray.length(); i++){

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        areaList.add(new Area(0+"",jsonObject.getString("beat_name")));

                    }
            //        System.out.print("location size "+areaList.size());
                    areaAdapter.notifyDataSetChanged();

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
        areaList.clear();
        try {
            JSONObject obj = new JSONObject(result);
            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {

                // ProfileJoiningDate.setText("Joining Date : "+obj.getString("joining_date"));

                //  ProfileDesignationTv.setText("Designation : "+obj.getString("designation"));
                ProfileUserNameTv.setText(obj.getString("user_name"));

                ProfileJoiningDate.setText(obj.getString("joining_date"));
                ProfileDesignationTv.setText(obj.getString("designation"));

                ProfileTargetTv.setText(obj.getString("totalTarget"));
                ProfieRemainingTv.setText(obj.getString("remaingTarget"));
                //status_txt.setText(obj.getString("name"));
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