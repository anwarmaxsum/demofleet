package com.logtrac.maxsum.logtrac;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.logtrac.maxsum.logtrac.connection.MessageConnection;
import com.logtrac.maxsum.logtrac.connection.RetrieveTravelConnection;
import com.logtrac.maxsum.logtrac.model.CustomListAdapter;
import com.logtrac.maxsum.logtrac.model.Employee;
import com.logtrac.maxsum.logtrac.model.MessageListAdapter;
import com.logtrac.maxsum.logtrac.model.PlaceID;
import com.logtrac.maxsum.logtrac.model.Travel;
import com.logtrac.maxsum.logtrac.model.UnitID;
import com.logtrac.maxsum.logtrac.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

public class MessageListActivity extends ListActivity {

    private String[] messageName;
    private String[] messageBody;
    private String[] messageTime;

    ListView messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        setTitle("Messages");
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SESSION", 0);
        RetrieveTravelConnection retrieveTravel = new RetrieveTravelConnection(pref.getString("session_id", null));

        //Retrieve all messages
        MessageConnection Message = new MessageConnection(pref.getString("session_id", null));
        Message.Message();
        while(!MessageConnection.ISMessageFINISHED){}
        String ms;

        if (MessageConnection.MessageMESSAGE.contains("error") || MessageConnection.MessageMESSAGE.contains("Not Found")) {
            ms="No message for you";
            Toast.makeText(this, RetrieveTravelConnection.GET_TRAVELS_MESSAGE, Toast.LENGTH_LONG).show();
            MessageConnection.MessageMESSAGE = "";
            MessageConnection.ISMessageFINISHED = false;
        } else {
            try {
                JSONArray jsonArr = new JSONArray(MessageConnection.MessageMESSAGE);
                messageName = new String[jsonArr.length()];
                messageBody = new String[jsonArr.length()];
                messageTime = new String[jsonArr.length()];

                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject jsonOb = jsonArr.getJSONObject(i);

                    String date = jsonOb.getString("create_date");
                    String body = jsonOb.getString("body");
                    JSONObject aut = jsonOb.getJSONObject("author_id");
                    String name  = aut.getString("name");

                    messageName[i] = name;
                    messageBody[i] = body;
                    messageTime[i] = date;
                }

//
//                    Log.d("### Read All travel : ", travel.date + ":" + travel.state + ":" + travel.id + ":" + travel.name);
//                    Log.d("### Read All Array : ", travelNames[i] + ":" + travelDepartures[i] + ":" + travelArrivals[i]);

                MessageConnection.MessageMESSAGE = "";
                MessageConnection.ISMessageFINISHED = false;

                MessageListAdapter adapter = new MessageListAdapter(this, messageName, messageBody, messageTime);
                messageList = (ListView) findViewById(android.R.id.list);
                messageList.setAdapter(adapter);

            } catch (Exception e) {
                Toast.makeText(this, "Error occures : You are not legitimate driver", Toast.LENGTH_LONG).show();
                Log.d("Error : ", e.toString());
            }
        }

    }

}
