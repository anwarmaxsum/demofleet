package com.logtrac.maxsum.logtrac.model;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.logtrac.maxsum.logtrac.R;

/**
 * Created by MuhammadAnwar on 10/25/2017.
 */

public class MessageListAdapter extends ArrayAdapter<String> {
    private Activity context;
    private String[] messageName;
    private String[] messageBody;
    private String[] messageTime;

    public MessageListAdapter(Activity context, String[] name, String[] body, String[] time) {
        super(context, R.layout.item_message_received, name);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.messageName = name;
        this.messageBody = body;
        this.messageTime = time;
    }


    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.item_message_received, null,true);

        TextView txtName = (TextView) rowView.findViewById(R.id.text_message_name);
        TextView txtBody = (TextView) rowView.findViewById(R.id.text_message_body);
        //ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView txtTime = (TextView) rowView.findViewById(R.id.text_message_time);

        txtName.setText(messageName[position]);
        txtBody.setText(messageBody[position]);
        txtTime.setText(messageTime[position]);

        return rowView;

    };
}
