package com.techcoderz.ruchira.Fragments.OrderFragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.Application.RuchiraApplication;
import com.techcoderz.ruchira.Fragments.OtherFragments.RuchiraFragment;
import com.techcoderz.ruchira.Utils.AppConfig;
import com.techcoderz.ruchira.Utils.NetworkUtils;
import com.techcoderz.ruchira.Utils.TaskUtils;
import com.techcoderz.ruchira.Utils.UserPreferences;
import com.techcoderz.ruchira.Utils.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shahriar on 9/18/2016.
 */
public class OrderSubmitFragment extends RuchiraFragment {
    private final static String TAG = "OrderSubmitFragment";
    private Fragment toLaunchFragment = null;
    private Button submit_btn, cancel_btn;
    private Bundle bundle;
    private String shopeId, productId, promotionId, sku, orderId, prevCtn, prevPiece;
    private int pricePerCarton, pricePerPiece;
    private EditText ctn_et, pcs_et;
    private TextView value_et, sku_txt, price_txt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order_submit, container, false);
        setupToolbar();
        initialize(rootView);
        action();
        return rootView;
    }

    private void setupToolbar() {
        mFragmentContext.getSupportActionBar().show();
        mFragmentContext.getSupportActionBar().setTitle("Add New Order");
    }

    private void initialize(View rootView) {
        bundle = getArguments();
        submit_btn = (Button) rootView.findViewById(R.id.submit_btn);
        cancel_btn = (Button) rootView.findViewById(R.id.cancel_btn);
        shopeId = bundle.getString("getShopeId");
        productId = bundle.getString("getproductId");
        promotionId = bundle.getString("getpromotionId");
        sku = bundle.getString("getProductSku");
        pricePerCarton = bundle.getInt("getPricePerCarton");
        pricePerPiece = bundle.getInt("getPricePerPiece");
        orderId = bundle.getString("getOrderId");
        prevCtn = bundle.getString("prevCtn");
        prevPiece = bundle.getString("prevPiece");

        ctn_et = (EditText) rootView.findViewById(R.id.ctn_et);
        pcs_et = (EditText) rootView.findViewById(R.id.pcs_et);
        value_et = (TextView) rootView.findViewById(R.id.value_et);
        sku_txt = (TextView) rootView.findViewById(R.id.sku_txt);
        price_txt = (TextView) rootView.findViewById(R.id.price_txt);

    }

    private void action() {
        if (prevCtn.matches("0")) {
            ctn_et.setHint(prevCtn);
        } else ctn_et.setText(prevCtn);

        if (prevPiece.matches("0")) {
            pcs_et.setHint(prevPiece);
        } else pcs_et.setText(prevPiece);

        value_et.setText("" + ((Integer.parseInt(prevCtn.toString()) * pricePerCarton) +
                (Integer.parseInt(prevPiece.toString()) * pricePerPiece)));

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtils.hasInternetConnection(mFragmentContext)) {
                    fetchDataFromServerOrderItemSubmit();
                }
            }
        });

        sku_txt.setText(sku);
        price_txt.setText("Price : " + pricePerCarton + " per ctn,   " + pricePerPiece + " per pcs");

        ctn_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!ctn_et.getText().toString().equals("")) {
                    if (pcs_et.getText().toString().equals("")) {
                        value_et.setText((Integer.parseInt(ctn_et.getText().toString()) * pricePerCarton) + "");
                    } else {
                        value_et.setText(((Integer.parseInt(ctn_et.getText().toString()) * pricePerCarton) +
                                ((Integer.parseInt(pcs_et.getText().toString())) * pricePerPiece)) + "");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pcs_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!pcs_et.getText().toString().equals("")) {
                    if (ctn_et.getText().toString().equals("")) {
                        value_et.setText((Integer.parseInt(pcs_et.getText().toString()) * pricePerPiece) + "");
                    } else {
                        value_et.setText((((Integer.parseInt(ctn_et.getText().toString()) * pricePerCarton)) +
                                ((Integer.parseInt(pcs_et.getText().toString()) * pricePerPiece))) + "");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtils.hasInternetConnection(mFragmentContext)) {
                    fetchDataFromServerOrderCancelation();
                }
            }
        });
    }

    private String getTodaysDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    private void fetchDataFromServerOrderItemSubmit() {
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(mFragmentContext);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String tag_string_req = "req_submit_order";
        final ProgressDialog finalProgressDialog = progressDialog;

    }

    private void fetchDataFromServerOrderCancelation() {
        ProgressDialog progressDialog = null;
        progressDialog = new ProgressDialog(mFragmentContext);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String tag_string_req = "req_cancel_order";
        final ProgressDialog finalProgressDialog = progressDialog;

    }

    private void executeForOrderCancelation(String result) {
        Log.d(TAG, result.toString());
        try {
            JSONObject obj = new JSONObject(result);
            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                TaskUtils.clearOrderId(mFragmentContext);
                openAddNewOrderFragmentForCalcelOrder();
            } else {
                ViewUtils.alertUser(mFragmentContext, "Server Error");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void executeForOrderItemSubmit(String result) {
        Log.d(TAG, result.toString());
        try {
            JSONObject obj = new JSONObject(result);
            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                ViewUtils.alertUser(mFragmentContext, "Order Submitted Successfully!!!");
                openAddNewOrderFragmentForSubmitOrder();
            } else {
                ViewUtils.alertUser(mFragmentContext, "Server Error");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void openAddNewOrderFragmentForSubmitOrder() {
        toLaunchFragment = new AddOrderFragment();
        if (toLaunchFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString("getShopeId", shopeId);
            toLaunchFragment.setArguments(bundle);
            mFragmentContext.getSupportFragmentManager().popBackStack();
            toLaunchFragment = null;
        }
    }

    private void openAddNewOrderFragmentForCalcelOrder() {
        toLaunchFragment = new AddOrderFragment();
        if (toLaunchFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString("getShopeId", shopeId);
            toLaunchFragment.setArguments(bundle);
            mFragmentContext.getSupportFragmentManager().popBackStack();
            toLaunchFragment = null;
        }
    }
}