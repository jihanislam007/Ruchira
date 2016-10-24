package com.techcoderz.ruchira.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.fragment.OrderDetailsFragment;
import com.techcoderz.ruchira.fragment.ProductWithPriceDetailsFragment;
import com.techcoderz.ruchira.model.ProductList;
import com.techcoderz.ruchira.utills.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shahriar on 10/24/2016.
 */

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ProductList> productList = new ArrayList<>();
    private Context context;
    Fragment toLaunchFragment = null;

    public ProductAdapter(Context context, List<ProductList> productList) {

        this.productList = productList;
        this.context = context;
        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(context, toLaunchFragment);
        }
    }

    @Override
    public ProductAdapter.RecyclerViewSubHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_promotion, null);
        ProductAdapter.RecyclerViewSubHolders rcv = new ProductAdapter.RecyclerViewSubHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ProductAdapter.RecyclerViewSubHolders) {
            if (productList.size() > 0) {

                ((ProductAdapter.RecyclerViewSubHolders) holder).product_name_txt.setText(productList.get(position).getProductName());
                ((ProductAdapter.RecyclerViewSubHolders) holder).promotion_title_txt.setText(productList.get(position).getProductId());
                ((ProductAdapter.RecyclerViewSubHolders) holder).end_date_txt.setText(productList.get(position).getPricePerCarton());
                ((ProductAdapter.RecyclerViewSubHolders) holder).show_txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openProductWithPriceDetailsFragment(position);
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return this.productList.size();
    }

    private void openProductWithPriceDetailsFragment(int position) {
        toLaunchFragment = new ProductWithPriceDetailsFragment();
        if (toLaunchFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString("getProductId", productList.get(position).getProductId());
            toLaunchFragment.setArguments(bundle);
            ViewUtils.launchFragmentKeepingInBackStack(context, toLaunchFragment);
            toLaunchFragment = null;
        }
    }


    class RecyclerViewSubHolders extends RecyclerView.ViewHolder {

        public TextView product_name_txt,promotion_title_txt,end_date_txt,show_txt;

        public RecyclerViewSubHolders(View itemView) {
            super(itemView);
            product_name_txt = (TextView) itemView.findViewById(R.id.product_name_txt);
            promotion_title_txt = (TextView) itemView.findViewById(R.id.promotion_title_txt);
            end_date_txt = (TextView) itemView.findViewById(R.id.end_date_txt);
            show_txt = (TextView) itemView.findViewById(R.id.show_txt);
        }

    }
}
