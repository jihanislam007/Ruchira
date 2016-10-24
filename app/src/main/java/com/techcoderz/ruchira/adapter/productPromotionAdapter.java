package com.techcoderz.ruchira.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.fragment.ProductPromotionDetailsFragment;
import com.techcoderz.ruchira.model.Promotion;
import com.techcoderz.ruchira.utills.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shahriar on 10/20/2016.
 */

public class ProductPromotionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Promotion> promotionList = new ArrayList<>();
    private Context context;
    Fragment toLaunchFragment = null;

    public ProductPromotionAdapter(Context context, List<Promotion> promotionList) {
        this.context = context;
        this.promotionList = promotionList;

        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(context, toLaunchFragment);
        }
    }

    @Override
    public ProductPromotionAdapter.RecyclerViewHoldersHomeAdapter onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_promotion, null);
        ProductPromotionAdapter.RecyclerViewHoldersHomeAdapter rcv = new ProductPromotionAdapter.RecyclerViewHoldersHomeAdapter(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ProductPromotionAdapter.RecyclerViewHoldersHomeAdapter) {
            ((ProductPromotionAdapter.RecyclerViewHoldersHomeAdapter) holder).product_name_txt.setText(promotionList.get(position).getProductName());
            ((ProductPromotionAdapter.RecyclerViewHoldersHomeAdapter) holder).promotion_title_txt.setText(promotionList.get(position).getTitle());
            ((ProductPromotionAdapter.RecyclerViewHoldersHomeAdapter) holder).end_date_txt.setText(promotionList.get(position).getPromotionEndDate());
            ((ProductPromotionAdapter.RecyclerViewHoldersHomeAdapter) holder).show_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openProductPromotionDetailsFragmentFragment();
                }
            });
        }
    }

    private void openProductPromotionDetailsFragmentFragment() {
        toLaunchFragment = new ProductPromotionDetailsFragment();
        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(context, toLaunchFragment);
            toLaunchFragment = null;
        }
    }

    @Override
    public int getItemCount() {
        return this.promotionList.size();
    }


    class RecyclerViewHoldersHomeAdapter extends RecyclerView.ViewHolder {

        public TextView product_name_txt,promotion_title_txt,end_date_txt,show_txt;
        public RecyclerViewHoldersHomeAdapter(View itemView) {
            super(itemView);
            product_name_txt = (TextView) itemView.findViewById(R.id.product_name_txt);
            promotion_title_txt = (TextView) itemView.findViewById(R.id.promotion_title_txt);
            end_date_txt = (TextView) itemView.findViewById(R.id.end_date_txt);
            show_txt = (TextView) itemView.findViewById(R.id.show_txt);
        }

    }
}