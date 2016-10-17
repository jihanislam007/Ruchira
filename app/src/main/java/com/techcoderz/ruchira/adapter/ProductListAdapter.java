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
import com.techcoderz.ruchira.fragment.ShopProfileFragment;
import com.techcoderz.ruchira.model.ProductList;
import com.techcoderz.ruchira.utills.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shahriar on 10/16/2016.
 */

public class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ProductList> productList = new ArrayList<>();
    private Context context;
    Fragment toLaunchFragment = null;
    private String shopeId;

    public ProductListAdapter(Context context, List<ProductList> productList,String shopeId) {

        this.productList = productList;
        this.context = context;
        this.shopeId = shopeId;
        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(context, toLaunchFragment);
        }
    }

    @Override
    public ProductListAdapter.RecyclerViewSubHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_add_new_order, null);
        ProductListAdapter.RecyclerViewSubHolders rcv = new ProductListAdapter.RecyclerViewSubHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ProductListAdapter.RecyclerViewSubHolders) {
            if (productList.size() > 0) {

                ((RecyclerViewSubHolders) holder).item_btn.setText(productList.get(position).getProductName());


                ((ProductListAdapter.RecyclerViewSubHolders) holder).wholeContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openOrderDetailsFragment(position);
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return this.productList.size();
    }

    private void openOrderDetailsFragment(int position) {
        toLaunchFragment = new OrderDetailsFragment();
        if (toLaunchFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString("getShopeId",shopeId);
            bundle.putString("getproductId",productList.get(position).getProductId());
            bundle.putString("getpromotionId",productList.get(position).getPromotionId());
            toLaunchFragment.setArguments(bundle);
            ViewUtils.launchFragmentKeepingInBackStack(context, toLaunchFragment);
            toLaunchFragment = null;
        }
    }


    class RecyclerViewSubHolders extends RecyclerView.ViewHolder {

        public TextView item_btn;
        public CardView wholeContent;

        public RecyclerViewSubHolders(View itemView) {
            super(itemView);
            item_btn = (TextView) itemView.findViewById(R.id.item_btn);
            wholeContent = (CardView) itemView.findViewById(R.id.card_view2);
        }

    }
}

