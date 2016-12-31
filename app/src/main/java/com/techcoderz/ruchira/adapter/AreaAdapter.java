package com.techcoderz.ruchira.adapter;

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
import com.techcoderz.ruchira.fragment.ShopProfileFragment;
import com.techcoderz.ruchira.model.Area;
import com.techcoderz.ruchira.model.Report;
import com.techcoderz.ruchira.utills.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shahriar on 10/19/2016.
 */

public class AreaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Area> areaList = new ArrayList<>();
    private Context context;
    Fragment toLaunchFragment = null;
    private int type;
    public AreaAdapter(Context context, List<Area> areaList) {
        this.areaList = areaList;
        this.context = context;
        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(context, toLaunchFragment);
        }
    }

    @Override
    public AreaAdapter.RecyclerViewSubHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_profile_area, null);
        AreaAdapter.RecyclerViewSubHolders rcv = new AreaAdapter.RecyclerViewSubHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof AreaAdapter.RecyclerViewSubHolders) {
            if (areaList.size() > 0) {
                ((AreaAdapter.RecyclerViewSubHolders) holder).beat_name_txt.setText(areaList.get(position).getBeatName());
            }
        }

    }

    @Override
    public int getItemCount() {
        return this.areaList.size();
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
        public TextView beat_name_txt;
        public RecyclerViewSubHolders(View itemView) {
            super(itemView);
            beat_name_txt = (TextView) itemView.findViewById(R.id.beat_name_txt);
        }

    }
}