package com.techcoderz.ruchira.Fragments.OrderFragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.Adapters.orderBeatSpinnerAdapter;
import com.techcoderz.ruchira.Adapters.OutletAdapter;
import com.techcoderz.ruchira.Application.RuchiraApplication;
import com.techcoderz.ruchira.Fragments.OtherFragments.RuchiraFragment;
import com.techcoderz.ruchira.ModelClasses.Beat;
import com.techcoderz.ruchira.ModelClasses.Outlet;
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

/**
 * Created by Shahriar on 9/14/2016.
 */
public class OrderFragment extends RuchiraFragment {
    private final static String TAG = "OrderFragment";
    private TextView tv_yet_to_visit, tv_ordered, tv_not_ordered, tvTotalSale;
    private CardView cvYetToVisit, cvOrdered, cvNotOrdered;
    private List<Beat> beatList;
    private List<Outlet> outletList;
    private AppCompatSpinner beat_spinner;
    private orderBeatSpinnerAdapter orderBeatSpinnerAdapter;
    private RecyclerView outlet_rcview;
    private OutletAdapter outletAdapter;
    private GridLayoutManager gridLayoutManager;
    private int position2 = 0;
    private int selected = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order, container, false);
        initialize(rootView);
        action();
        if (NetworkUtils.hasInternetConnection(mFragmentContext)) {
            fetchDataFromServerForBeat();
        }
        return rootView;
    }

    private void initialize(View rootView) {
        tvTotalSale = (TextView) rootView.findViewById(R.id.tv_total_sale);
        tv_yet_to_visit = (TextView) rootView.findViewById(R.id.yet_to_visit_btn);
        tv_ordered = (TextView) rootView.findViewById(R.id.ordered_btn);
        tv_not_ordered = (TextView) rootView.findViewById(R.id.not_ordered_btn);
        beat_spinner = (AppCompatSpinner) rootView.findViewById(R.id.beat_spinner);
        cvYetToVisit = (CardView) rootView.findViewById(R.id.card_view_yet_to_visit);
        cvOrdered = (CardView) rootView.findViewById(R.id.card_view_ordered);
        cvNotOrdered = (CardView) rootView.findViewById(R.id.card_view_not_order);
        outlet_rcview = (RecyclerView) rootView.findViewById(R.id.outlet_rcview);
        beatList = new ArrayList<>();
        outletList = new ArrayList<>();
        orderBeatSpinnerAdapter = new orderBeatSpinnerAdapter(mFragmentContext, R.layout.beat_list, beatList);
        orderBeatSpinnerAdapter.setDropDownViewResource(R.layout.beat_list);
        gridLayoutManager = new GridLayoutManager(mFragmentContext, 3);
        outletAdapter = new OutletAdapter(mFragmentContext, outletList, 0);
        outlet_rcview.setAdapter(outletAdapter);
        outlet_rcview.setHasFixedSize(true);
        outlet_rcview.setLayoutManager(gridLayoutManager);
    }

    private void action() {
        mFragmentContext.getSupportActionBar().setTitle("Order");
        if (selected == 0){
            tv_yet_to_visit.setBackgroundDrawable(getResources().getDrawable(R.color.colorDarkBlue));
            cvYetToVisit.setCardBackgroundColor(getResources().getColor(R.color.colorDarkBlue));
            tv_yet_to_visit.setTextColor(getResources().getColor(R.color.white));
        } else if (selected == 1){
            tv_ordered.setBackgroundDrawable(getResources().getDrawable(R.color.colorDarkBlue));
            cvOrdered.setCardBackgroundColor(getResources().getColor(R.color.colorDarkBlue));
            tv_ordered.setTextColor(getResources().getColor(R.color.white));
            tv_yet_to_visit.setBackgroundDrawable(getResources().getDrawable(R.color.white));
            tv_yet_to_visit.setTextColor(getResources().getColor(R.color.colorDarkBlue));
            tv_not_ordered.setBackgroundDrawable(getResources().getDrawable(R.color.white));
            tv_not_ordered.setTextColor(getResources().getColor(R.color.colorDarkBlue));
            cvNotOrdered.setCardBackgroundColor(getResources().getColor(R.color.white));
            cvYetToVisit.setCardBackgroundColor(getResources().getColor(R.color.white));
        } else if (selected == 2){
            tv_not_ordered.setBackgroundDrawable(getResources().getDrawable(R.color.colorDarkBlue));
            cvNotOrdered.setCardBackgroundColor(getResources().getColor(R.color.colorDarkBlue));
            tv_not_ordered.setTextColor(getResources().getColor(R.color.white));
            tv_yet_to_visit.setBackgroundDrawable(getResources().getDrawable(R.color.white));
            tv_yet_to_visit.setTextColor(getResources().getColor(R.color.colorDarkBlue));
            tv_ordered.setBackgroundDrawable(getResources().getDrawable(R.color.white));
            tv_ordered.setTextColor(getResources().getColor(R.color.colorDarkBlue));
            cvOrdered.setCardBackgroundColor(getResources().getColor(R.color.white));
            cvYetToVisit.setCardBackgroundColor(getResources().getColor(R.color.white));
        } else {}

        cvYetToVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected == 1 || selected == 2) {
                    tv_yet_to_visit.setBackgroundDrawable(getResources().getDrawable(R.color.colorDarkBlue));
                    tv_yet_to_visit.setTextColor(getResources().getColor(R.color.white));
                    cvYetToVisit.setCardBackgroundColor(getResources().getColor(R.color.colorDarkBlue));
                    tv_ordered.setBackgroundDrawable(getResources().getDrawable(R.color.white));
                    tv_ordered.setTextColor(getResources().getColor(R.color.colorDarkBlue));
                    tv_not_ordered.setBackgroundDrawable(getResources().getDrawable(R.color.white));
                    tv_not_ordered.setTextColor(getResources().getColor(R.color.colorDarkBlue));
                    cvOrdered.setCardBackgroundColor(getResources().getColor(R.color.white));
                    cvNotOrdered.setCardBackgroundColor(getResources().getColor(R.color.white));
                    selected = 0;
                }
                if (NetworkUtils.hasInternetConnection(mFragmentContext)) {
                    fetchDataFromServerForOutlet(beatList.get(position2).getId(), "0");
                }
            }
        });

        cvOrdered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected == 0 || selected == 2) {
                    tv_ordered.setBackgroundDrawable(getResources().getDrawable(R.color.colorDarkBlue));
                    cvOrdered.setCardBackgroundColor(getResources().getColor(R.color.colorDarkBlue));
                    tv_ordered.setTextColor(getResources().getColor(R.color.white));
                    tv_yet_to_visit.setBackgroundDrawable(getResources().getDrawable(R.color.white));
                    tv_yet_to_visit.setTextColor(getResources().getColor(R.color.colorDarkBlue));
                    tv_not_ordered.setBackgroundDrawable(getResources().getDrawable(R.color.white));
                    tv_not_ordered.setTextColor(getResources().getColor(R.color.colorDarkBlue));
                    cvNotOrdered.setCardBackgroundColor(getResources().getColor(R.color.white));
                    cvYetToVisit.setCardBackgroundColor(getResources().getColor(R.color.white));
                    selected = 1;
                }
                if(NetworkUtils.hasInternetConnection(mFragmentContext)) {
                    fetchDataFromServerForOutlet(beatList.get(position2).getId(), "1");
                }
            }
        });

        cvNotOrdered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected == 0 || selected == 1) {
                    tv_not_ordered.setBackgroundDrawable(getResources().getDrawable(R.color.colorDarkBlue));
                    cvNotOrdered.setCardBackgroundColor(getResources().getColor(R.color.colorDarkBlue));
                    tv_not_ordered.setTextColor(getResources().getColor(R.color.white));
                    tv_yet_to_visit.setBackgroundDrawable(getResources().getDrawable(R.color.white));
                    tv_yet_to_visit.setTextColor(getResources().getColor(R.color.colorDarkBlue));
                    tv_ordered.setBackgroundDrawable(getResources().getDrawable(R.color.white));
                    tv_ordered.setTextColor(getResources().getColor(R.color.colorDarkBlue));
                    cvOrdered.setCardBackgroundColor(getResources().getColor(R.color.white));
                    cvYetToVisit.setCardBackgroundColor(getResources().getColor(R.color.white));
                    selected = 2;
                }
                if(NetworkUtils.hasInternetConnection(mFragmentContext)) {
                    fetchDataFromServerForOutlet(beatList.get(position2).getId(), "2");
                }
            }
        });

        beat_spinner.setAdapter(orderBeatSpinnerAdapter);

        beat_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                position2 = position;
                if(NetworkUtils.hasInternetConnection(mFragmentContext) &&
                        !beatList.get(position).getTitle().matches("Select Beat")) {
                    fetchDataFromServerForOutlet(beatList.get(position).getId(), String.valueOf(selected));
                }
                Log.e(TAG, "beat id : " + beatList.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void fetchDataFromServerForOutlet(final String bearId, final String option) {
        ProgressDialog progressDialog = null;
        progressDialog = new ProgressDialog(mFragmentContext);
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
                processResultForOutlet(response, option);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "order Error: " + error.getMessage());
                finalProgressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", UserPreferences.getUserId(mFragmentContext));
                params.put("tokenKey", UserPreferences.getToken(mFragmentContext));
                params.put("beatId", bearId);
                return params;
            }
        };

        // Adding request to request queue
        RuchiraApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void fetchDataFromServerForBeat() {
        ProgressDialog progressDialog = null;
        progressDialog = new ProgressDialog(mFragmentContext);
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
                executeForBeat(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "order Error: " + error.getMessage());
                finalProgressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.d(TAG, "User ID: " + UserPreferences.getUserId(mFragmentContext));
                params.put("userId", UserPreferences.getUserId(mFragmentContext));
                params.put("tokenKey", UserPreferences.getToken(mFragmentContext));
                return params;
            }
        };

        // Adding request to request queue
        RuchiraApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void executeForBeat(String result) {
        Log.d(TAG, result.toString());
        beatList.clear();
        Beat beat = new Beat();
        beat.setTitle("Select Beat");
        beatList.add(beat);
        try {
            JSONObject obj = new JSONObject(result);
            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                beatList.addAll(TaskUtils.setBeat(result));
                tvTotalSale.setText("Order: " + obj.getString("todaySale") + " ৳");
                orderBeatSpinnerAdapter.notifyDataSetChanged();
            } else {
                ViewUtils.alertUser(mFragmentContext, "Server Error");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void processResultForOutlet(String result, String option) {
        Log.d(TAG, result.toString());
        outletList.clear();
        try {
            JSONObject obj = new JSONObject(result);
            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                List<Outlet> tempOutletList = new ArrayList<>();
                tempOutletList.addAll(TaskUtils.setOutlet(result));
                for (int i = 0; i < tempOutletList.size(); i++) {
                    if (tempOutletList.get(i).getFlag().equals(option)) {
                        outletList.add(tempOutletList.get(i));
                    }
                }
                outletAdapter.notifyDataSetChanged();
            } else {
                ViewUtils.alertUser(mFragmentContext, "Server Error");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}