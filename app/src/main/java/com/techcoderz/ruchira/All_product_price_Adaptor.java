package com.techcoderz.ruchira;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by jihan on 12/11/17.
 */

public class All_product_price_Adaptor extends ArrayAdapter<String> {

    private Context context;
    private final String[] Name;
    private final String[] Price;

    public All_product_price_Adaptor(Context context, String[] Name , String[] Price) {

        super(context, R.layout.costom_product_price, Name);
        this.context = context;
        this.Name = Name;
        this.Price = Price;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater =  (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        View rowView= inflater.inflate(R.layout.costom_product_price, null, true);

        TextView txtName = (TextView) rowView.findViewById(R.id.tv_product_name_adapter);
        TextView txtPrice = (TextView) rowView.findViewById(R.id.tv_product_price_adapter);


        txtName.setText(Name[position]);
        txtPrice.setText(Price[position]);

        return rowView;
    }
}