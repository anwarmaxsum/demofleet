package com.logtrac.maxsum.logtrac;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.logtrac.maxsum.logtrac.connection.LoginConnection;
import com.logtrac.maxsum.logtrac.connection.TravelDispatchConnection;
import com.logtrac.maxsum.logtrac.model.Travel;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by MuhammadAnwar on 10/23/2017.
 */
public class PickupTravelActivity extends AppCompatActivity {


    Travel travel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_travel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        try {
            Intent i = getIntent();
            this.travel = (Travel) i.getParcelableExtra("Travel_Object");
            TextView tv_name = (TextView) findViewById(R.id.name_travel);

            TextView tv_waybill = (TextView) findViewById(R.id.waybill_travel);
            TextView tv_dep = (TextView) findViewById(R.id.departure_travel);
            TextView tv_arr = (TextView) findViewById(R.id.arrival_travel);
            TextView tv_date = (TextView) findViewById(R.id.date_travel);
            TextView tv_customer = (TextView) findViewById(R.id.customer_travel);

            tv_name.setText((String) travel.name);
            tv_waybill.setText("-");
            tv_dep.setText(travel.departure_name +" ("+travel.departure_strlat+","+travel.departure_strlong+")");
            tv_arr.setText(travel.arrival_name+" ("+travel.arrival_strlat+","+travel.arrival_strlong+")");
            tv_date.setText(travel.date);
            tv_customer.setText(travel.user_name);

        }catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }

        final Button acceptButton = (Button) findViewById(R.id.accept_button);
        final Button rejectButton = (Button) findViewById(R.id.reject_button);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your code here
                //Toast.makeText(PickupTravelActivity.this, "Button Accept", Toast.LENGTH_LONG).show();
                acceptTravel();
            }
        });
        rejectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your code here
                rejectTravel();
            }
        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void acceptTravel() {
        EditText fuel_editText = (EditText) findViewById(R.id.fuel);
        String fuel = fuel_editText.getText().toString();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("SESSION", 0);
        TravelDispatchConnection dispatch = new TravelDispatchConnection(travel.id, pref.getString("session_id",null), fuel);
        dispatch.dispatch();

        while (!TravelDispatchConnection.ISDISPATCHFINISHED) {}
        //Toast.makeText(PickupTravelActivity.this, TravelDispatchConnection.DISPATCHMESSAGE, Toast.LENGTH_LONG).show();

        if(TravelDispatchConnection.DISPATCHMESSAGE.contains("error")) {
            //Toast.makeText(PickupTravelActivity.this, TravelDispatchConnection.DISPATCHMESSAGE, Toast.LENGTH_LONG).show();
            String errorMessage = "error";
            try{
                JSONObject jsonOb = new JSONObject(TravelDispatchConnection.DISPATCHMESSAGE);
                errorMessage  = jsonOb.getString("error_message");
            }catch (Exception e){

            }
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            TravelDispatchConnection.DISPATCHMESSAGE="";
            TravelDispatchConnection.ISDISPATCHFINISHED=false;

        }
        else {
            TravelDispatchConnection.DISPATCHMESSAGE="";
            TravelDispatchConnection.ISDISPATCHFINISHED=false;
            //Toast.makeText(this, "Dispatch Travel : "+dispatch.getUrl()+"\nsession_id:"+pref.getString("session_id",null), Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Dispatch : "+travel.name, Toast.LENGTH_LONG).show();
            try {
                Intent intent = new Intent(PickupTravelActivity.this, ReserveTravelActivity.class);
                intent.putExtra("Travel_Object", this.travel);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void rejectTravel() {
        super.onBackPressed();
    }

}
