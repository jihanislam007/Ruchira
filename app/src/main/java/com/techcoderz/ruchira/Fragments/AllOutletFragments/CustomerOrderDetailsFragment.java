package com.techcoderz.ruchira.Fragments.AllOutletFragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.Adapters.OrderAdapter;
import com.techcoderz.ruchira.Application.RuchiraApplication;
import com.techcoderz.ruchira.Fragments.OtherFragments.RuchiraFragment;
import com.techcoderz.ruchira.ModelClasses.Billing;
import com.techcoderz.ruchira.ModelClasses.Order;
import com.techcoderz.ruchira.ModelClasses.Shipping;
import com.techcoderz.ruchira.Utils.AppConfig;
import com.techcoderz.ruchira.Utils.NetworkUtils;
import com.techcoderz.ruchira.Utils.TaskUtils;
import com.techcoderz.ruchira.Utils.UserPreferences;
import com.techcoderz.ruchira.Utils.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by priom on 9/19/16.
 */
public class CustomerOrderDetailsFragment extends RuchiraFragment {
    private final static String TAG = "CustomerOrderDetails";
    private List<Order> orderList;
    private List<Billing> billingList;
    private List<Shipping> shippingList;
    private RecyclerView report_rcview;
    private TextView shop_name_txt, order_id_txt, name_txt, cell_no_txt, customer_id_txt, email_txt, status_txt;
    private TextView billing_to_txt, billing_address_txt, billing_city_post_code_txt, billing_mail_txt, billing_cell_txt,
            shipping_to_txt, shipping_address_txt, shipping_city_post_code_txt, shipping_cell_txt, shipping_mail_txt;
    private TextView date_txt, total_sale_txt;
    private LinearLayoutManager manager;
    private OrderAdapter orderAdapter;
    private String orderDate, orderId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_customer_order_details, container, false);
        setupToolbar();
        initialize(rootView);
        action();
        return rootView;
    }

    private void setupToolbar() {
        mFragmentContext.getSupportActionBar().show();
        mFragmentContext.getSupportActionBar().setTitle("Customer Order Details");
    }

    private void initialize(View rootView) {
        report_rcview = (RecyclerView) rootView.findViewById(R.id.report_rcview);
        shop_name_txt = (TextView) rootView.findViewById(R.id.shope_name_txt);
        order_id_txt = (TextView) rootView.findViewById(R.id.order_id_txt);
        name_txt = (TextView) rootView.findViewById(R.id.name_txt);
        cell_no_txt = (TextView) rootView.findViewById(R.id.cell_no_txt);
        customer_id_txt = (TextView) rootView.findViewById(R.id.customer_id_txt);
        email_txt = (TextView) rootView.findViewById(R.id.email_txt);
        status_txt = (TextView) rootView.findViewById(R.id.status_txt);
        billing_to_txt = (TextView) rootView.findViewById(R.id.billing_to_txt);
        billing_address_txt = (TextView) rootView.findViewById(R.id.billing_address_txt);
        billing_city_post_code_txt = (TextView) rootView.findViewById(R.id.billing_city_post_code_txt);
        billing_mail_txt = (TextView) rootView.findViewById(R.id.billing_mail_txt);
        billing_cell_txt = (TextView) rootView.findViewById(R.id.billing_cell_txt);
        shipping_to_txt = (TextView) rootView.findViewById(R.id.shipping_to_txt);
        shipping_address_txt = (TextView) rootView.findViewById(R.id.shipping_address_txt);
        shipping_city_post_code_txt = (TextView) rootView.findViewById(R.id.shipping_city_post_code_txt);
        shipping_cell_txt = (TextView) rootView.findViewById(R.id.shipping_cell_txt);
        shipping_mail_txt = (TextView) rootView.findViewById(R.id.shipping_mail_txt);
        date_txt = (TextView) rootView.findViewById(R.id.date_txt);
        total_sale_txt = (TextView) rootView.findViewById(R.id.total_sale_txt);

        orderList = new ArrayList<>();
        shippingList = new ArrayList<>();
        billingList = new ArrayList<>();

        manager = new LinearLayoutManager(mFragmentContext);
        orderAdapter = new OrderAdapter(mFragmentContext, orderList);
    }

    private void action() {
        Bundle bundle = this.getArguments();
        orderDate = bundle.getString("orderDate");
        orderId = bundle.getString("orderId");
        report_rcview.setAdapter(orderAdapter);
        report_rcview.setHasFixedSize(true);
        report_rcview.setLayoutManager(manager);
        if (NetworkUtils.hasInternetConnection(mFragmentContext)) {
            fetchDataFromServer(changeDateFormate(orderDate));
        }
    }

    private String changeDateFormate(String orderDate) {
        String inputPattern = "dd/mm/yyyy";
        String outputPattern = "yyyy-mm-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date;
        String str = null;
        try {
            date = inputFormat.parse(orderDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    private void fetchDataFromServer(final String date) {
        if (orderDate != null) {
            ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(mFragmentContext);
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();

            String tag_string_req = "customer_order_details_req";
            final ProgressDialog finalProgressDialog = progressDialog;

        } else {
            ViewUtils.alertUser(mFragmentContext, "No Memo Available");
        }

    }

    private void executeForMemo(String result) {
        Log.d(TAG, result.toString());
        orderList.clear();
        shippingList.clear();
        billingList.clear();
        try {
            JSONObject obj = new JSONObject(result);
            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                orderList.addAll(TaskUtils.setOrderList(result));
                shippingList.addAll(TaskUtils.setShippingList(result));
                billingList.addAll(TaskUtils.setBillingList(result));
                orderAdapter.notifyDataSetChanged();
                shop_name_txt.setText("Shop Name: " + obj.getString("outletName"));
                order_id_txt.setText("Order Id: #" + orderId);
                name_txt.setText("Owner Name: " + obj.getString("ownerName"));
                cell_no_txt.setText("Cell: " + obj.getString("ownerPhone"));
                customer_id_txt.setText("Customer ID : " + obj.getString("ownerId"));
                email_txt.setText("Email: " + obj.getString("ownerEmail"));
                status_txt.setText("Status: " + obj.getString("status"));

                billing_to_txt.setText(billingList.get(0).getTo());
                billing_address_txt.setText(billingList.get(0).getAddress());
                billing_city_post_code_txt.setText(billingList.get(0).getCity() + " - " + billingList.get(0).getPostcode());
                billing_mail_txt.setText(billingList.get(0).getEmail());
                billing_cell_txt.setText(billingList.get(0).getPhone());

                shipping_to_txt.setText(shippingList.get(0).getTo());
                shipping_address_txt.setText(shippingList.get(0).getAddress());
                shipping_city_post_code_txt.setText(shippingList.get(0).getCity() + " - " + shippingList.get(0).getPostcode());
                shipping_cell_txt.setText(shippingList.get(0).getPhone());
                shipping_mail_txt.setText(shippingList.get(0).getEmail());

                date_txt.setText(obj.getString("orderDate"));
                total_sale_txt.setText(obj.getString("total") + " BDT");
            } else {
                ViewUtils.alertUser(mFragmentContext, "Server Error");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}