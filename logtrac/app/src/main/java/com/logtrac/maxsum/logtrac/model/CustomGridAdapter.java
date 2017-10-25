package com.logtrac.maxsum.logtrac.model;

/**
 * Created by MuhammadAnwar on 10/22/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.logtrac.maxsum.logtrac.R;

public class CustomGridAdapter extends BaseAdapter{
    private Context mContext;
    private final String[] nameID;
    private final int[] imageid;

    public CustomGridAdapter(Context c, String[] web, int[] Imageid ) {
        mContext = c;
        this.imageid = Imageid;
        this.nameID = web;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return nameID.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_box, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            textView.setText(nameID[position]);
            imageView.setImageResource(imageid[position]);
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}