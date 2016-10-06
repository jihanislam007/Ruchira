package com.techcoderz.ruchira.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.model.Promotion;
import com.techcoderz.ruchira.utills.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shahriar on 10/6/2016.
 */

public class PromotionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Promotion> promotionList = new ArrayList<>();
    private Context context;
    Fragment toLaunchFragment = null;

    public PromotionAdapter(Context context, List<Promotion> promotionList) {
        this.context = context;
        this.promotionList = promotionList;

        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(context, toLaunchFragment);
        }
    }

    @Override
    public RecyclerViewHoldersHomeAdapter onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_shop_profile, null);
        RecyclerViewHoldersHomeAdapter rcv = new RecyclerViewHoldersHomeAdapter(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof RecyclerViewHoldersHomeAdapter) {
            ((RecyclerViewHoldersHomeAdapter) holder).promotion_title_txt.setText(promotionList.get(position).getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return this.promotionList.size();
    }


    class RecyclerViewHoldersHomeAdapter extends RecyclerView.ViewHolder {

        public TextView promotion_title_txt;
        public LinearLayout wholeContent;

        public RecyclerViewHoldersHomeAdapter(View itemView) {
            super(itemView);
            promotion_title_txt = (TextView) itemView.findViewById(R.id.promotion_title_txt);
            wholeContent = (LinearLayout) itemView.findViewById(R.id.wholeContent);
        }

    }
}