package com.techcoderz.ruchira.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.Fragments.AllOutletFragments.ShopOrderSummaryFragment;
import com.techcoderz.ruchira.Fragments.OrderFragments.ShopProfileFragment;
import com.techcoderz.ruchira.ModelClasses.Outlet;
import com.techcoderz.ruchira.Utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by priom on 10/5/16.
 */
public class OutletAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Outlet> outletList = new ArrayList<>();
    private Context context;
    private String TAG = "OutletAdapter";
    Fragment toLaunchFragment = null;
    private int type;

    public OutletAdapter(Context context, List<Outlet> outletList, int type) {
        this.outletList = outletList;
        this.context = context;
        this.type = type;
        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(context, toLaunchFragment);
        }
    }

    @Override
    public RecyclerViewSubHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.outlet_adapter_view, null);
        RecyclerViewSubHolders rcv = new RecyclerViewSubHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof RecyclerViewSubHolders) {
            if (outletList.size() > 0) {
                ((RecyclerViewSubHolders) holder).shop_name_txt.setText(outletList.get(position).getTitle());
                ((RecyclerViewSubHolders) holder).group_txt.setText(outletList.get(position).getGroup());
                ((RecyclerViewSubHolders) holder).wholeContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (type == 0) {
                            if (outletList.get(position).getFlag().equals("2")) {
                                final AlertDialog alertDialog = new AlertDialog.Builder(context)
                                        .setTitle("Not Ordered!!!")
                                        .setMessage(outletList.get(position).getReason())
                                        .setCancelable(false)
                                        .setPositiveButton("OK", null)
                                        .setNegativeButton("Order Again",  new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                openShopProfile(outletList.get(position).getOutletId(), outletList.get(position).getOid());
                                            }
                                        })
                                        .create();
                                alertDialog.show();
                            } else {
                                openShopProfile(outletList.get(position).getOutletId(), outletList.get(position).getOid());
                            }
                        } else {
                            openShopOrderSummaryFragment(outletList.get(position).getOutletId(), outletList.get(position).getOid());
                        }
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return this.outletList.size();
    }

    private void openShopProfile(String outletId, String orderId) {
        toLaunchFragment = new ShopProfileFragment();
        if (toLaunchFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString("getOutletId", outletId);
            bundle.putString("orderId", orderId);
            Log.d(TAG, orderId);
            toLaunchFragment.setArguments(bundle);
            ViewUtils.launchFragmentKeepingInBackStack(context, toLaunchFragment);
            toLaunchFragment = null;
        }
    }

    private void openShopOrderSummaryFragment(String shopId, String orderId) {
        toLaunchFragment = new ShopOrderSummaryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("shopId", shopId);
        bundle.putString("orderId", orderId);
        toLaunchFragment.setArguments(bundle);
        ViewUtils.launchFragmentKeepingInBackStack(context, toLaunchFragment);
        toLaunchFragment = null;
    }

    class RecyclerViewSubHolders extends RecyclerView.ViewHolder {
        public TextView shop_name_txt, group_txt;
        public LinearLayout wholeContent;

        public RecyclerViewSubHolders(View itemView) {
            super(itemView);
            shop_name_txt = (TextView) itemView.findViewById(R.id.shop_name_txt);
            group_txt = (TextView) itemView.findViewById(R.id.group_txt);
            wholeContent = (LinearLayout) itemView.findViewById(R.id.linear_layout);
        }

    }
}