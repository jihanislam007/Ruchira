package com.techcoderz.ruchira.fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.adapter.OrderBeatSpinnerAdapter;
import com.techcoderz.ruchira.adapter.OutletAdapter;
import com.techcoderz.ruchira.application.RuchiraApplication;
import com.techcoderz.ruchira.model.Beat;
import com.techcoderz.ruchira.model.Outlet;
import com.techcoderz.ruchira.utills.AppConfig;
import com.techcoderz.ruchira.utills.TaskUtils;
import com.techcoderz.ruchira.utills.UserPreferences;
import com.techcoderz.ruchira.utills.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shahriar on 9/14/2016.
 */
public class OrderFragment extends RuchiraFragment {
    private final static String TAG = "OrderFragment";
    String url = "http://gear-go.com/ruchira/index.php/home/orderapi";
    Fragment toLaunchFragment = null;
    private TextView yet_to_visit_btn, ordered_btn, not_ordered_btn;

    private List<Beat> beatList;
    private List<Outlet> outletList;

    private AppCompatSpinner beat_spinner;
    private OrderBeatSpinnerAdapter orderBeatSpinnerAdapter;

    private String location;

    private RecyclerView outlet_rcview;
    private OutletAdapter outletAdapter;
    private GridLayoutManager gridLayoutManager;



    public OrderFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_yet_to_visit, container, false);

        initialize(rootView);
        action();
        fetchDataFromServer();
        return rootView;
    }

    private void initialize(View rootView) {
        yet_to_visit_btn = (TextView) rootView.findViewById(R.id.yet_to_visit_btn);
        ordered_btn = (TextView) rootView.findViewById(R.id.ordered_btn);
        not_ordered_btn = (TextView) rootView.findViewById(R.id.not_ordered_btn);
        beat_spinner = (AppCompatSpinner) rootView.findViewById(R.id.beat_spinner);

        outlet_rcview = (RecyclerView) rootView.findViewById(R.id.outlet_rcview);

        beatList = new ArrayList<>();
        outletList = new ArrayList<>();
        orderBeatSpinnerAdapter = new OrderBeatSpinnerAdapter(ownerActivity, R.layout.beat_list, beatList);
        orderBeatSpinnerAdapter.setDropDownViewResource(R.layout.beat_list);

        gridLayoutManager = new GridLayoutManager(ownerActivity, 3);
        outletAdapter = new OutletAdapter(ownerActivity,outletList,0);

        outlet_rcview.setAdapter(outletAdapter);
        outlet_rcview.setHasFixedSize(true);
        outlet_rcview.setLayoutManager(gridLayoutManager);

    }

    private void action() {
//        yet_to_visit_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                third_layout.setVisibility(View.VISIBLE);
//                third_layout.setBackgroundColor(Color.WHITE);
//                second_layout.setBackgroundColor(Color.WHITE);
//                second_layout.setVisibility(View.VISIBLE);
//            }
//        });
//        ordered_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                third_layout.setVisibility(View.GONE);
//            }
//        });
//        not_ordered_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                third_layout.setVisibility(View.GONE);
//                second_layout.setVisibility(View.GONE);
//                first_layout.setBackgroundColor(Color.WHITE);
//            }
//        });

        beat_spinner.setAdapter(orderBeatSpinnerAdapter);

        beat_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                location = beatList.get(position).getTitle();
                fetchDataFromServerForOutlet(beatList.get(position).getId());
                Log.e(TAG, "beatList.get(position).getId() : " + beatList.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    private void fetchDataFromServerForOutlet(final String id) {

        ProgressDialog progressDialog = null;

        progressDialog = new ProgressDialog(ownerActivity);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String tag_string_req = "req_order";
        final ProgressDialog finalProgressDialog = progressDialog;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ORDER_OUTLET, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "order Response: " + response.toString());
                finalProgressDialog.dismiss();
                processResultForOutlet(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "order Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", UserPreferences.getId(ownerActivity));
                params.put("tokenKey", UserPreferences.getToken(ownerActivity));
                params.put("beatId",id);
                return params;
            }

        };

        // Adding request to request queue
        RuchiraApplication.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void fetchDataFromServer() {

        ProgressDialog progressDialog = null;

        progressDialog = new ProgressDialog(ownerActivity);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String tag_string_req = "req_order";
        final ProgressDialog finalProgressDialog = progressDialog;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ORDER_BEAT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "order Response: " + response.toString());
                finalProgressDialog.dismiss();
                executeForbeat(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "order Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", UserPreferences.getId(ownerActivity));
                params.put("tokenKey", UserPreferences.getToken(ownerActivity));
                return params;
            }

        };

        // Adding request to request queue
        RuchiraApplication.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void executeForbeat(String result) {
        Log.d(TAG, result.toString());
        beatList.clear();

        try {

            JSONObject obj = new JSONObject(result);

            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                beatList.addAll(TaskUtils.setBeat(result));
                orderBeatSpinnerAdapter.notifyDataSetChanged();

                return;

            } else {
                ViewUtils.alertUser(ownerActivity, "Server Error");
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void processResultForOutlet(String result) {
        Log.d(TAG, result.toString());
        outletList.clear();

        try {

            JSONObject obj = new JSONObject(result);

            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                outletList.addAll(TaskUtils.setOutlet(result));
                outletAdapter.notifyDataSetChanged();

                return;

            } else {
                ViewUtils.alertUser(ownerActivity, "Server Error");
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void processResult() {

    }


    private void openOpenShopProfile() {
        toLaunchFragment = new ShopProfileFragment();
        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(ownerActivity, toLaunchFragment);
            toLaunchFragment = null;
        }
    }
}