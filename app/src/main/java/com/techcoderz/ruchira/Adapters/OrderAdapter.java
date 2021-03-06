package com.techcoderz.ruchira.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.ModelClasses.Order;
import com.techcoderz.ruchira.Utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shahriar on 10/17/2016.
 */

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Order> orderList = new ArrayList<>();
    private Context context;
    Fragment toLaunchFragment = null;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.orderList = orderList;
        this.context = context;
        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(context, toLaunchFragment);
        }
    }

    @Override
    public OrderAdapter.RecyclerViewSubHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_yearlytotal_sale, null);
        OrderAdapter.RecyclerViewSubHolders rcv = new OrderAdapter.RecyclerViewSubHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OrderAdapter.RecyclerViewSubHolders) {
            if (orderList.size() > 0) {
                ((RecyclerViewSubHolders) holder).name_txt.setText(orderList.get(position).getProductName());
                ((RecyclerViewSubHolders) holder).quantity_txt.setText(orderList.get(position).getQuantity());
                ((RecyclerViewSubHolders) holder).ammount_txt.setText(orderList.get(position).getCost()+" BDT");
            }
        }

    }

    @Override
    public int getItemCount() {
        return this.orderList.size();
    }

    class RecyclerViewSubHolders extends RecyclerView.ViewHolder {
        public TextView name_txt, quantity_txt, ammount_txt;
        public LinearLayout wholeContent;

        public RecyclerViewSubHolders(View itemView) {
            super(itemView);
            name_txt = (TextView) itemView.findViewById(R.id.name_txt);
            quantity_txt = (TextView) itemView.findViewById(R.id.quantity_txt);
            ammount_txt = (TextView) itemView.findViewById(R.id.ammount_txt);
            wholeContent = (LinearLayout) itemView.findViewById(R.id.linear_layout);
        }

    }
}