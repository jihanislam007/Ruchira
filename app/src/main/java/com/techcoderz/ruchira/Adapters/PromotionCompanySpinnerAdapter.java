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
import com.techcoderz.ruchira.ModelClasses.Company;

import java.util.List;

/**
 * Created by Shahriar on 10/20/2016.
 */

public class PromotionCompanySpinnerAdapter extends ArrayAdapter<Company> {

    private Context context;
    private List<Company> companyList;

    public PromotionCompanySpinnerAdapter(Context context, int textViewResourceId, List<Company> companyList) {
        super(context,textViewResourceId,companyList);
        this.context = context;
        this.companyList = companyList;
    }

    public int getCount() {
        return companyList.size();
    }

    public Company getItem(int position) {
        return companyList.get(position);
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
            TextView companyName = (TextView) convertView.findViewById(R.id.beatTextView);
            final Company  company = getItem(position);
            Log.e("OrderBeatSpinnerAdapter","getBeatName:"+company.getCompanyName());
            companyName.setText(company.getCompanyName());
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        View row = convertView;
        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(android.R.layout.simple_spinner_item, parent,false);
        }

        TextView label = (TextView)row.findViewById(android.R.id.text1);
        label.setText(getItem(position).getCompanyName());
        label.setPadding(10, 15, 0, 15);

        label.setTextColor(Color.BLACK);

        ((TextView) row).setGravity(Gravity.CENTER);
        return row;
    }

}