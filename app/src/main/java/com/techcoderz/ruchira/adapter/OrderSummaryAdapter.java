package com.techcoderz.ruchira.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.fragment.CustomerOrderDetailsFragment;
import com.techcoderz.ruchira.model.OrderSummary;
import com.techcoderz.ruchira.utills.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shahriar on 10/17/2016.
 */

public class OrderSummaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String TAG = "OrderSummaryAdapter";
    private List<OrderSummary> orderSummaryList = new ArrayList<>();
    private Context context;
    private Fragment toLaunchFragment = null;
    private String orderId;

    public OrderSummaryAdapter(Context context, List<OrderSummary> orderSummaryList, String oId) {
        this.orderSummaryList = orderSummaryList;
        this.context = context;
        this.orderId = oId;
        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(context, toLaunchFragment);
        }
    }

    @Override
    public OrderSummaryAdapter.RecyclerViewSubHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_yearlytotal_sale, null);
        OrderSummaryAdapter.RecyclerViewSubHolders rcv = new OrderSummaryAdapter.RecyclerViewSubHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OrderSummaryAdapter.RecyclerViewSubHolders) {
            if (orderSummaryList.size() > 0) {
                ((OrderSummaryAdapter.RecyclerViewSubHolders) holder).name_txt.setText(orderSummaryList.get(position).getOrderDate());
                ((OrderSummaryAdapter.RecyclerViewSubHolders) holder).quantity_txt.setText("#"+orderSummaryList.get(position).getInvoiceNo());
                ((OrderSummaryAdapter.RecyclerViewSubHolders) holder).ammount_txt.setText(orderSummaryList.get(position).getAmount()+" BDT");
                ((OrderSummaryAdapter.RecyclerViewSubHolders) holder).wholeContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openCustomerOrderDetailsFragment(orderId);
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return this.orderSummaryList.size();
    }

    private void openCustomerOrderDetailsFragment(String orderId) {
        toLaunchFragment = new CustomerOrderDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("orderId", orderId);
        Log.d(TAG, "order id: " + orderId);
        toLaunchFragment.setArguments(bundle);
        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(context, toLaunchFragment);
            toLaunchFragment = null;
        }
    }

    class RecyclerViewSubHolders extends RecyclerView.ViewHolder {
        public TextView name_txt, quantity_txt, ammount_txt;
        public RelativeLayout wholeContent;
        public RecyclerViewSubHolders(View itemView) {
            super(itemView);
            name_txt = (TextView) itemView.findViewById(R.id.name_txt);
            quantity_txt = (TextView) itemView.findViewById(R.id.quantity_txt);
            ammount_txt = (TextView) itemView.findViewById(R.id.ammount_txt);
            wholeContent = (RelativeLayout) itemView.findViewById(R.id.wholeContent);
        }
    }
}
