package com.techcoderz.ruchira.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.fragment.ShopProfileFragment;
import com.techcoderz.ruchira.model.Outlet;
import com.techcoderz.ruchira.utills.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by priom on 10/5/16.
 */
public class OutletAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Outlet> outletList = new ArrayList<>();
    private Context context;
    Fragment toLaunchFragment = null;

    public OutletAdapter(Context context, List<Outlet> outletList) {
        this.outletList = outletList;
        this.context = context;

        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(context, toLaunchFragment);
        }
    }

    @Override
    public RecyclerViewSubHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_yet_to_visit, null);
        RecyclerViewSubHolders rcv = new RecyclerViewSubHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof RecyclerViewSubHolders) {
            if (outletList.size() > 0) {
                ((RecyclerViewSubHolders) holder).shop_name_txt.setText(outletList.get(position).getTitle());
                ((RecyclerViewSubHolders) holder).location_txt.setText(outletList.get(position).getGroup());


                ((RecyclerViewSubHolders) holder).wholeContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openOpenShopProfile(outletList.get(position).getOid());
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return this.outletList.size();
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

        public TextView shop_name_txt,location_txt;
        public LinearLayout wholeContent;
        Fragment toLaunchFragment = null;

        public RecyclerViewSubHolders(View itemView) {
            super(itemView);
            shop_name_txt = (TextView) itemView.findViewById(R.id.shop_name_txt);
            location_txt = (TextView) itemView.findViewById(R.id.location_txt);
            wholeContent = (LinearLayout) itemView.findViewById(R.id.linear_layout);
        }

    }
}