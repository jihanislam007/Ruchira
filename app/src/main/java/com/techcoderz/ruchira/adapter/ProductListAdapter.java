package com.techcoderz.ruchira.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.fragment.OrderSubmitFragment;
import com.techcoderz.ruchira.model.ProductList;
import com.techcoderz.ruchira.utills.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shahriar on 10/16/2016.
 */

public class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String TAG = "ProductListAdapter";
    private List<ProductList> productList = new ArrayList<>();
    private Context context;
    private Fragment toLaunchFragment = null;
    private String shopeId, orderId;

    public ProductListAdapter(Context context, List<ProductList> productList,String shopeId, String orderId) {
        this.productList = productList;
        this.context = context;
        this.shopeId = shopeId;
        this.orderId = orderId;
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
                        openOrderSubmitFragment(position);
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return this.productList.size();
    }

    private void openOrderSubmitFragment(int position) {
        toLaunchFragment = new OrderSubmitFragment();
        if (toLaunchFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString("getShopeId",shopeId);
            bundle.putString("getproductId", productList.get(position).getProductId());
            bundle.putString("getpromotionId", productList.get(position).getPromotionId());
            bundle.putInt("getPricePerCarton", productList.get(position).getPricePerCarton());
            bundle.putInt("getPricePerPiece", productList.get(position).getPricePerPiece());
            bundle.putString("getProductSku", productList.get(position).getProductSku());
            bundle.putString("getOrderId", orderId);

            Log.d(TAG, "getproductId  " + productList.get(position).getProductId());
            Log.d(TAG, "getpromotionId  " + productList.get(position).getPromotionId());
            Log.d(TAG, "getPricePerCarton  " + productList.get(position).getPricePerCarton());
            Log.d(TAG, "getPricePerPiece  " + productList.get(position).getPricePerPiece());
            Log.d(TAG, "getProductSku  " + productList.get(position).getProductSku());

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
            wholeContent = (CardView) itemView.findViewById(R.id.card_view_ordered);
        }

    }
}

