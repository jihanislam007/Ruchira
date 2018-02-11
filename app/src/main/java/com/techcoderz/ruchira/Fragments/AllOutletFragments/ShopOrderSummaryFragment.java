package com.techcoderz.ruchira.Fragments.AllOutletFragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.Adapters.OrderSummaryAdapter;
import com.techcoderz.ruchira.Application.RuchiraApplication;
import com.techcoderz.ruchira.Fragments.OtherFragments.RuchiraFragment;
import com.techcoderz.ruchira.ModelClasses.OrderSummary;
import com.techcoderz.ruchira.Utils.AppConfig;
import com.techcoderz.ruchira.Utils.NetworkUtils;
import com.techcoderz.ruchira.Utils.TaskUtils;
import com.techcoderz.ruchira.Utils.UserPreferences;
import com.techcoderz.ruchira.Utils.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by priom on 9/19/16.
 */
public class ShopOrderSummaryFragment extends RuchiraFragment {
    private final static String TAG = "ShopOrderSummary";
    private List<OrderSummary> orderSummaryList;
    private RecyclerView report_rcview;
    private LinearLayoutManager manager;
    private OrderSummaryAdapter orderSummaryAdapter;
    private TextView name_txt, sale_date_txt, address_txt, date_txt, tvNoOrderFound;
    private ImageView ivNext, ivPrevious;
    private String stringMonth, stringYear, outletId, orderId;
    private int serverMonth, serverYear;
    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shop_order_summary, container, false);
        setupToolbar();
        initialize(rootView);
        action();
        return rootView;
    }

    private void setupToolbar() {
        mFragmentContext.getSupportActionBar().show();
        mFragmentContext.getSupportActionBar().setTitle("Shop Order Summary");
    }

    private void initialize(View rootView) {
        bundle = this.getArguments();
        orderSummaryList = new ArrayList<>();
        report_rcview = (RecyclerView) rootView.findViewById(R.id.report_rcview);

        outletId = bundle.getString("shopId");
        orderId = bundle.getString("orderId");

        name_txt = (TextView) rootView.findViewById(R.id.name_txt);
        sale_date_txt = (TextView) rootView.findViewById(R.id.sale_date_txt);
        address_txt = (TextView) rootView.findViewById(R.id.address_txt);
        date_txt = (TextView) rootView.findViewById(R.id.date_txt);
        tvNoOrderFound = (TextView) rootView.findViewById(R.id.no_order);

        ivNext = (ImageView) rootView.findViewById(R.id.iv_next_date);
        ivPrevious = (ImageView) rootView.findViewById(R.id.iv_prev_date);

        manager = new LinearLayoutManager(mFragmentContext);
        orderSummaryAdapter = new OrderSummaryAdapter(mFragmentContext, orderSummaryList, orderId);
        report_rcview.setAdapter(orderSummaryAdapter);
        report_rcview.setHasFixedSize(true);
        report_rcview.setLayoutManager(manager);
    }

    private void action() {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, +1);
        if (NetworkUtils.hasInternetConnection(mFragmentContext)) {
            serverMonth = calendar.get(Calendar.MONTH);
            stringMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
            stringYear = String.valueOf(calendar.get(Calendar.YEAR));

            if (serverMonth == 0)
                fetchDataFromServer(12, stringYear);
            else
                fetchDataFromServer(serverMonth, stringYear);

            ivNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calendar.roll(calendar.MONTH, true);
                    serverMonth = calendar.get(Calendar.MONTH);
                    stringMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
                    if (serverMonth == 1)
                        calendar.roll(calendar.YEAR, true);

                    stringYear = String.valueOf(calendar.get(Calendar.YEAR));

                    if (serverMonth == 0)
                        fetchDataFromServer(12, stringYear);
                    else
                        fetchDataFromServer(serverMonth, stringYear);
                }
            });

            ivPrevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calendar.roll(calendar.MONTH, false);
                    serverMonth = calendar.get(Calendar.MONTH);
                    stringMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
                    if (serverMonth == 0)
                        calendar.roll(calendar.YEAR, false);

                    stringYear = String.valueOf(calendar.get(Calendar.YEAR));

                    if (serverMonth == 0)
                        fetchDataFromServer(12, stringYear);
                    else
                        fetchDataFromServer(serverMonth, stringYear);
                }
            });
        }
    }

    private void fetchDataFromServer(final int month, final String year) {
        if (outletId != null) {
            ProgressDialog progressDialog = null;
            progressDialog = new ProgressDialog(mFragmentContext);
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();

            String tag_string_req = "req_memo";
            final ProgressDialog finalProgressDialog = progressDialog;

        } else {
            ViewUtils.alertUser(mFragmentContext, "No Memo Available");
        }

    }

    private void executeForMemo(String result) {
        Log.d(TAG, result.toString());
        orderSummaryList.clear();
        try {
            JSONObject obj = new JSONObject(result);
            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                name_txt.setText(obj.getString("outletName"));
                sale_date_txt.setText("Sale Start From : " + obj.getString("startDate"));
                address_txt.setText("Address : " + obj.getString("outletAddress"));
                date_txt.setText(obj.getString("orderDate"));
                orderSummaryList.addAll(TaskUtils.setOrderSummaryList(result));
                orderSummaryAdapter.notifyDataSetChanged();

                if (orderSummaryList.size() <= 0) {
                    tvNoOrderFound.setVisibility(View.VISIBLE);
                    report_rcview.setVisibility(View.GONE);
                } else {
                    tvNoOrderFound.setVisibility(View.GONE);
                    report_rcview.setVisibility(View.VISIBLE);
                }
            } else {
                ViewUtils.alertUser(mFragmentContext, "Server Error");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}