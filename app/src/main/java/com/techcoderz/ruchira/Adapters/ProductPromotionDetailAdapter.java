package com.techcoderz.ruchira.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.ModelClasses.Promotion;
import com.techcoderz.ruchira.Utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shahriar on 10/24/2016.
 */

public class ProductPromotionDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Promotion> promotionList = new ArrayList<>();
    private Context context;
    Fragment toLaunchFragment = null;

    public ProductPromotionDetailAdapter(Context context, List<Promotion> promotionList) {
        this.context = context;
        this.promotionList = promotionList;

        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(context, toLaunchFragment);
        }
    }

    @Override
    public ProductPromotionDetailAdapter.RecyclerViewHoldersHomeAdapter onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_promotion_detail, null);
        ProductPromotionDetailAdapter.RecyclerViewHoldersHomeAdapter rcv = new ProductPromotionDetailAdapter.RecyclerViewHoldersHomeAdapter(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ProductPromotionDetailAdapter.RecyclerViewHoldersHomeAdapter) {
            ((ProductPromotionDetailAdapter.RecyclerViewHoldersHomeAdapter) holder).promotion_title_txt.setText(promotionList.get(position).getTitle());
            ((ProductPromotionDetailAdapter.RecyclerViewHoldersHomeAdapter) holder).promotion_start_date_txt.setText(promotionList.get(position).getPromotionStartDate());
            ((ProductPromotionDetailAdapter.RecyclerViewHoldersHomeAdapter) holder).promotion_end_date_txt.setText(promotionList.get(position).getPromotionEndDate());
        }
    }

    @Override
    public int getItemCount() {
        return this.promotionList.size();
    }


    class RecyclerViewHoldersHomeAdapter extends RecyclerView.ViewHolder {

        public TextView promotion_title_txt, promotion_start_date_txt, promotion_end_date_txt;

        public RecyclerViewHoldersHomeAdapter(View itemView) {
            super(itemView);
            promotion_title_txt = (TextView) itemView.findViewById(R.id.promotion_title_txt);
            promotion_start_date_txt = (TextView) itemView.findViewById(R.id.promotion_start_date_txt);
            promotion_end_date_txt = (TextView) itemView.findViewById(R.id.promotion_end_date_txt);
        }

    }
}