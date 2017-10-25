package com.logtrac.maxsum.logtrac.model;

/**
 * Created by MuhammadAnwar on 10/23/2017.
 */
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.logtrac.maxsum.logtrac.R;

public class CustomListAdapter extends ArrayAdapter<String> {
    private Activity context;
    private String[] itemname;
    private String[] itemdeparture;
    private String[] itemarrival;
    private String[] itemstatus;

    public CustomListAdapter(Activity context, String[] itemname, String[] itemdep, String[] itemarr, String[] itemstat) {
        super(context,R.layout.one_order, itemname);
        // TODO Auto-generated constructor stub
        this.context=context;
        this.itemname=itemname;
        this.itemdeparture=itemdep;
        this.itemarrival=itemarr;
        this.itemstatus=itemstat;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.one_order, null,true);

        TextView txtName = (TextView) rowView.findViewById(R.id.travel_name);
        //ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView txtDep = (TextView) rowView.findViewById(R.id.travel_departure);
        TextView txtArr = (TextView) rowView.findViewById(R.id.travel_arrival);
        TextView txtStat = (TextView) rowView.findViewById(R.id.travel_status);

        txtName.setText("Travel : "+itemname[position]);
        txtDep.setText("Departure : "+itemdeparture[position]);
        txtArr.setText("Arrival : "+ itemarrival[position]);
        txtStat.setText(itemstatus[position]);



        if(itemstatus[position].equals("cancel")) {
            txtStat.setBackgroundColor(rowView.getResources().getColor(R.color.colorRed));
        } else if (itemstatus[position].equals("done")) {
            txtStat.setBackgroundColor(rowView.getResources().getColor(R.color.colorPrimary));
        } else if (itemstatus[position].equals("draft")) {
            txtStat.setBackgroundColor(rowView.getResources().getColor(R.color.colorAccent));
        } else if (itemstatus[position].contains("Progress") || itemstatus[position].contains("progress") ||
                itemstatus[position].contains("PROGRESS") || itemstatus[position].contains("running") ) {
            txtStat.setBackgroundColor(rowView.getResources().getColor(R.color.colorGreen));
        }

        return rowView;

    };
}