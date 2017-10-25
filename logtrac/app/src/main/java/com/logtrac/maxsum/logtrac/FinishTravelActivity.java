package com.logtrac.maxsum.logtrac;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.logtrac.maxsum.logtrac.connection.MessageConnection;
import com.logtrac.maxsum.logtrac.connection.TravelFinishConnection;
import com.logtrac.maxsum.logtrac.model.Travel;

public class FinishTravelActivity extends AppCompatActivity {

    Travel travel;
    String fuel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_travel);
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

        final Button finishButtonm = (Button) findViewById(R.id.button_finish_travel);
        finishButtonm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your code here
                //Toast.makeText(PickupTravelActivity.this, "Button Accept", Toast.LENGTH_LONG).show();
                finishTravel();
            }
        });
    }


    private void finishTravel() {
        EditText fuel_editText = (EditText) findViewById(R.id.fuel);
        fuel = fuel_editText.getText().toString();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("SESSION", 0);
        TravelFinishConnection finish = new TravelFinishConnection(travel.id, pref.getString("session_id",null),fuel);
        finish.Finish();
        while (!TravelFinishConnection.ISFinishFINISHED) {}

        //Toast.makeText(FinishTravelActivity.this, TravelFinishConnection.FinishMESSAGE, Toast.LENGTH_LONG).show();

        if(TravelFinishConnection.FinishMESSAGE.startsWith("error")) {
            Toast.makeText(FinishTravelActivity.this, TravelFinishConnection.FinishMESSAGE, Toast.LENGTH_LONG).show();
            TravelFinishConnection.FinishMESSAGE="";
            TravelFinishConnection.ISFinishFINISHED=false;
        }
        else {
            TravelFinishConnection.FinishMESSAGE="";
            TravelFinishConnection.ISFinishFINISHED=false;
            //Toast.makeText(this, "Finish Travel : "+finish.getUrl()+"\nsession_id:"+pref.getString("session_id",null), Toast.LENGTH_LONG).show();
            try {
                Intent intent = new Intent(FinishTravelActivity.this, FinishTravelActivity.class);
                intent.putExtra("Travel_Object", this.travel);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }

        Intent intent = new Intent(FinishTravelActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }



}
