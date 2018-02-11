package com.techcoderz.ruchira.Fragments.AllOutletFragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
public class OutletsFragment extends RuchiraFragment {
    private final static String TAG = "OutletsFragment";
    private List<Beat> beatList;
    private List<Outlet> outletList;
    private AppCompatSpinner beat_spinner;
    private SwipeRefreshLayout swipeRefreshLayout;
    private orderBeatSpinnerAdapter orderBeatSpinnerAdapter;
    private String location;
    private RecyclerView outlet_rcview;
    private OutletAdapter outletAdapter;
    private GridLayoutManager gridLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_outlets, container, false);
        setupToolbar();
//        initialize(rootView);
 //       action();
        if(NetworkUtils.hasInternetConnection(mFragmentContext)) {
            fetchDataFromServer();
        }
        return rootView;
    }

    private void setupToolbar() {
        mFragmentContext.getSupportActionBar().show();
        mFragmentContext.getSupportActionBar().setTitle("All Outlets");
    }

    private void initialize(View rootView) {
        beat_spinner = (AppCompatSpinner) rootView.findViewById(R.id.beat_spinner);
        outlet_rcview = (RecyclerView) rootView.findViewById(R.id.outlet_rcview);
        beatList = new ArrayList<>();
        outletList = new ArrayList<>();
       /* swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_outlet_list);
        orderBeatSpinnerAdapter = new orderBeatSpinnerAdapter(mFragmentContext, R.layout.beat_list, beatList);
        orderBeatSpinnerAdapter.setDropDownViewResource(R.layout.beat_list);*/

        gridLayoutManager = new GridLayoutManager(mFragmentContext, 3);
        outletAdapter = new OutletAdapter(mFragmentContext, outletList, 1);

      /*  outlet_rcview.setAdapter(outletAdapter);
        outlet_rcview.setHasFixedSize(true);
        outlet_rcview.setLayoutManager(gridLayoutManager);*/
    }

    private void action() {
        beat_spinner.setAdapter(orderBeatSpinnerAdapter);
        beat_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                location = beatList.get(position).getTitle();
                if(NetworkUtils.hasInternetConnection(mFragmentContext)) {
                    fetchDataFromServerForOutlet(beatList.get(position).getId());
                }
                Log.e(TAG, "beatList.get(position).getUserId() : " + beatList.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void fetchDataFromServerForOutlet(final String id) {
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(mFragmentContext);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String tag_string_req = "req_order";
        final ProgressDialog finalProgressDialog = progressDialog;

    }

    private void fetchDataFromServer() {
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(mFragmentContext);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String tag_string_req = "req_order";
        final ProgressDialog finalProgressDialog = progressDialog;


    }

    private void executeForbeat(String result) {
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
                orderBeatSpinnerAdapter.notifyDataSetChanged();

                return;

            } else {
                ViewUtils.alertUser(mFragmentContext, "Server Error");
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
                ViewUtils.alertUser(mFragmentContext, "Server Error");
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}