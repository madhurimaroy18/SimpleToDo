package com.roy.simpletodo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.roy.simpletodo.com.roy.simpletodo.utils.DateTime;

import java.util.ArrayList;

/**
 * Custom adapter for Item listview
 *
 * Created by roy on 2/19/2017.
 */

public class CustomItemAdapter extends ArrayAdapter<Item> {

    public CustomItemAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Item item = getItem(position);
        View v = convertView;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_item, parent, false);
        }

        // Lookup view for data population
    //    TextView tvCheck = (TextView)convertView.findViewById(R.id.cbItem);
        TextView tvName = (TextView)convertView.findViewById(R.id.tvItem);
        TextView tvDue = (TextView)convertView.findViewById(R.id.tvDue);
        ImageView ivPriority = (ImageView)convertView.findViewById(R.id.ivPriority);

        // Populate the data into the template view using the data object
        tvName.setText(item.getName());
        String fDate = DateTime.dateToString(item.getDate(), "MMM-dd");
        tvDue.setText(fDate);

        switch(item.getIntPriority()){
            case 0: ivPriority.setImageResource(R.drawable.priority_high); break;
            case 1: ivPriority.setImageResource(R.drawable.priority_medium); break;
            case 2: ivPriority.setImageResource(R.drawable.priority_low); break;
            default: break;
        }


        // Return the completed view to render on screen
        return convertView;
    }

    public void applyThemeToDrawable(Drawable image, Item.PRIORITY pr) {
        //red high orange medium blue low

        if (image != null) {
            PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(Color.BLUE,
                    PorterDuff.Mode.SRC_ATOP);

            image.setColorFilter(porterDuffColorFilter);
        }
    }

}

