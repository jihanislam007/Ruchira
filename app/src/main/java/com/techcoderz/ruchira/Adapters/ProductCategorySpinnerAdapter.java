package com.techcoderz.ruchira.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.ModelClasses.ProductCategory;

import java.util.List;

/**
 * Created by Shahriar on 10/16/2016.
 */

public class ProductCategorySpinnerAdapter extends ArrayAdapter<ProductCategory> {
    private Context context;
    private List<ProductCategory> productCategoryList;

    public ProductCategorySpinnerAdapter(Context context, int textViewResourceId, List<ProductCategory> productCategoryList) {
        super(context, textViewResourceId, productCategoryList);
        this.context = context;
        this.productCategoryList = productCategoryList;
    }

    public int getCount() {
        return productCategoryList.size();
    }

    public ProductCategory getItem(int position) {
        return productCategoryList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.beat_list, null);
            TextView skillList = (TextView) convertView.findViewById(R.id.beatTextView);
            final ProductCategory productCategory = getItem(position);
            Log.e("OrderBeatSpinnerAdapter", "getBeatName:" + productCategory.getName());
            skillList.setText(productCategory.getName());
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }
        TextView label = (TextView) row.findViewById(android.R.id.text1);
        label.setText(getItem(position).getName());
        label.setPadding(10, 15, 0, 15);
        label.setTextColor(Color.BLACK);
        ((TextView) row).setGravity(Gravity.CENTER);
        return row;
    }

}