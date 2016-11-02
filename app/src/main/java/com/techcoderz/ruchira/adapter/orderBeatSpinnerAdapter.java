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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shahriar on 10/5/2016.
 */

public class orderBeatSpinnerAdapter extends ArrayAdapter<Beat> {
    private Context context;
    private List<Beat> beatList;

    public orderBeatSpinnerAdapter(Context context, int textViewResourceId, List<Beat> beatList) {
        super(context,textViewResourceId,beatList);
        this.context = context;
        this.beatList = beatList;
    }

    public int getCount() {
        return beatList.size();
    }

    public Beat getItem(int position) {
        return beatList.get(position);
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
            final Beat  beat = getItem(position);
            skillList.setText(beat.getTitle());
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
        label.setText(getItem(position).getTitle());
        label.setPadding(10, 15, 0, 15);

        label.setTextColor(Color.BLACK);

        ((TextView) row).setGravity(Gravity.CENTER);
        return row;
    }

}