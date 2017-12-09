package com.techcoderz.ruchira;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by jihan on 12/4/17.
 */

public class Profile_listview_adapter extends ArrayAdapter<String> {

    private Context context;
    private final String[] Name;
    //   private final Integer[] imageId;

    public Profile_listview_adapter(Context context, String[] Name) {

        super(context, R.layout.costom_listview_profile, Name);

        this.context = context;
        this.Name = Name;
        //  this.imageId = imageId;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater =  (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        View rowView= inflater.inflate(R.layout.costom_listview_profile, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.BeatIDTextView);


        //   ImageView imageView = (ImageView) rowView.findViewById(R.id.img);

        txtTitle.setText(Name[position]);
        //    imageView.setImageResource(imageId[position]);

        return rowView;
    }
}
