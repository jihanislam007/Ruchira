package com.techcoderz.ruchira.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.Fragments.OrderFragments.ShopProfileFragment;
import com.techcoderz.ruchira.ModelClasses.Report;
import com.techcoderz.ruchira.Utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shahriar on 10/6/2016.
 */

public class ReportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Report> reportList = new ArrayList<>();
    private Context context;
    Fragment toLaunchFragment = null;
    private int type;

    public ReportAdapter(Context context, List<Report> reportList) {
        this.reportList = reportList;
        this.context = context;
        this.type = type;

        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(context, toLaunchFragment);
        }
    }

    @Override
    public ReportAdapter.RecyclerViewSubHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_yearlytotal_sale, null);
        ReportAdapter.RecyclerViewSubHolders rcv = new ReportAdapter.RecyclerViewSubHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ReportAdapter.RecyclerViewSubHolders) {
            if (reportList.size() > 0) {
                ((ReportAdapter.RecyclerViewSubHolders) holder).name_txt.setText(reportList.get(position).getMonth());
                ((ReportAdapter.RecyclerViewSubHolders) holder).quantity_txt.setText(reportList.get(position).getOrrder());
                ((ReportAdapter.RecyclerViewSubHolders) holder).ammount_txt.setText(reportList.get(position).getAmmount()+" BDT");


//                ((OutletAdapter.RecyclerViewSubHolders) holder).wholeContent.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (type == 0) {
//                            openOpenShopProfile(reportList.get(position).getOid());
//                        } else {
//                            openShopOrderSummaryFragment();
//                        }
//                    }
//                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return this.reportList.size();
    }

    private void openOpenShopProfile(String oId) {
        toLaunchFragment = new ShopProfileFragment();
        if (toLaunchFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString("getOid", oId);
            toLaunchFragment.setArguments(bundle);
            ViewUtils.launchFragmentKeepingInBackStack(context, toLaunchFragment);
            toLaunchFragment = null;
        }
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