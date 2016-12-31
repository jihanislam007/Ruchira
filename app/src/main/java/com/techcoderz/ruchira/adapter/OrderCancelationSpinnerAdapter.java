package com.techcoderz.ruchira.adapter;

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
import com.techcoderz.ruchira.model.Beat;
import com.techcoderz.ruchira.model.OrderCancelation;

import java.util.List;

/**
 * Created by priom on 10/25/16.
 */
public class OrderCancelationSpinnerAdapter extends ArrayAdapter<OrderCancelation> {
    private final static String TAG = "OrderCancelationAdapter";
    private Context context;
    private List<OrderCancelation> cancelationList;

    public OrderCancelationSpinnerAdapter(Context context, int textViewResourceId, List<OrderCancelation> cancelationList) {
        super(context,textViewResourceId,cancelationList);
        this.context = context;
        this.cancelationList = cancelationList;
    }

    public int getCount() {
        return cancelationList.size();
    }

    public OrderCancelation getItem(int position) {
        return cancelationList.get(position);
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
            final OrderCancelation  beat = getItem(position);
            Log.e(TAG,"cancelationList:"+beat.getTitle());
            skillList.setText(beat.getTitle());
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        View row = convertView;
        if(row == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(android.R.layout.simple_spinner_item, parent,false);
        }
        TextView label = (TextView)row.findViewById(android.R.id.text1);
        label.setText(getItem(position).getTitle());
        label.setPadding(10, 15, 0, 15);
        label.setTextColor(Color.BLACK);
        ((TextView) row).setGravity(Gravity.CENTER);
        return row;
    }

}