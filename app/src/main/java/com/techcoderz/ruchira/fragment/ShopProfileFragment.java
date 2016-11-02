package com.techcoderz.ruchira.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.adapter.PromotionAdapter;
import com.techcoderz.ruchira.application.RuchiraApplication;
import com.techcoderz.ruchira.model.Promotion;
import com.techcoderz.ruchira.utills.AppConfig;
import com.techcoderz.ruchira.utills.NetworkUtils;
import com.techcoderz.ruchira.utills.TaskUtils;
import com.techcoderz.ruchira.utills.UserPreferences;
import com.techcoderz.ruchira.utills.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Shahriar on 9/14/2016.
 */
public class ShopProfileFragment extends RuchiraFragment {
    private final static String TAG = "ShopProfileFragment";
    Fragment toLaunchFragment = null;
    private Button ok_btn,cancel_btn;
    private Bundle bundle;
    private String outletId = "";

    private List<Promotion> promotionList;

    private CircleImageView profile_image;
    private TextView address_txt, profile_name_txt, channel_txt, starting_date_txt;
    private RecyclerView promotion_rcview;
    private PromotionAdapter promotionAdapter;
    private String shopeProfileId="";

    public ShopProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shope_profile, container, false);
        setupToolbar();
        initialize(rootView);
        action();
        if(NetworkUtils.hasInternetConnection(mFragmentContext)) {
            fetchDataFromServer();
        }
        return rootView;
    }

    private void setupToolbar() {
        mFragmentContext.getSupportActionBar().show();
        mFragmentContext.getSupportActionBar().setTitle("Shop Profile");
    }

    private void initialize(View rootView) {
        bundle = this.getArguments();
        promotionList = new ArrayList<>();
        outletId = bundle.getString("getOid");
        ok_btn = (Button) rootView.findViewById(R.id.submit_btn);
        cancel_btn = (Button) rootView.findViewById(R.id.cancel_btn);
        promotion_rcview = (RecyclerView) rootView.findViewById(R.id.promotion_rcview);
        profile_name_txt = (TextView) rootView.findViewById(R.id.profile_name_txt);
        address_txt = (TextView) rootView.findViewById(R.id.address_txt);
        channel_txt = (TextView) rootView.findViewById(R.id.channel_txt);
        starting_date_txt = (TextView) rootView.findViewById(R.id.starting_date_txt);
        profile_image = (CircleImageView) rootView.findViewById(R.id.profile_image);
        promotionAdapter = new PromotionAdapter(mFragmentContext, promotionList);

        LinearLayoutManager manager = new LinearLayoutManager(mFragmentContext);
        promotion_rcview.setAdapter(promotionAdapter);
        promotion_rcview.setHasFixedSize(true);
        promotion_rcview.setLayoutManager(manager);
    }

    private void action() {
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddNewOrderFragment();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOrderCancelationFragment();
            }
        });
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            mFragmentContext.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
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
        String tag_string_req = "req_shope_profile";
        final ProgressDialog finalProgressDialog = progressDialog;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SHOPE_PROFILE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                finalProgressDialog.dismiss();
                execute(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                finalProgressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", UserPreferences.getId(mFragmentContext));
                params.put("tokenKey", UserPreferences.getToken(mFragmentContext));
                params.put("outletId", outletId);
                Log.d(TAG, outletId);
                return params;
            }

        };

        // Adding request to request queue
        RuchiraApplication.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void execute(String result) {
        Log.d(TAG, result.toString());
        promotionList.clear();
        try {
            JSONObject obj = new JSONObject(result);
            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                shopeProfileId = obj.getString("id");
//                UserPreferences.saveShopeProfileId(mFragmentContext, obj.getString("id"));
                profile_name_txt.setText(obj.getString("name"));
                address_txt.setText(obj.getString("address"));
                channel_txt.setText(obj.getString("channel"));
                Picasso.with(mFragmentContext)
                        .load(obj.getString("image"))
                        .into(profile_image);
                starting_date_txt.setText(obj.getString("startDate"));
                promotionList.addAll(TaskUtils.setPromotion(result));
                promotionAdapter.notifyDataSetChanged();

                return;

            } else {
                ViewUtils.alertUser(mFragmentContext, "Server Error");
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void openAddNewOrderFragment() {
        toLaunchFragment = new AddOrderFragment();
        if (toLaunchFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString("getShopeId",shopeProfileId);
            bundle.putString("getShopName", profile_name_txt.getText().toString());
            bundle.putString("getAddress", address_txt.getText().toString());
            toLaunchFragment.setArguments(bundle);
            ViewUtils.launchFragmentKeepingInBackStack(mFragmentContext, toLaunchFragment);
            toLaunchFragment = null;
        }
    }

    private void openOrderCancelationFragment() {
        toLaunchFragment = new OrderCancelationFragment();
        if (toLaunchFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString("getShopeName", profile_name_txt.getText().toString());
            bundle.putString("getAddress", address_txt.getText().toString());
            bundle.putString("shopeProfileId", shopeProfileId);
            toLaunchFragment.setArguments(bundle);
            ViewUtils.launchFragmentKeepingInBackStack(mFragmentContext, toLaunchFragment);
            toLaunchFragment = null;
        }
    }
}