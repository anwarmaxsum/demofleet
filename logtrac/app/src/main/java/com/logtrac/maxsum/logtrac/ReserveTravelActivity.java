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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.logtrac.maxsum.logtrac.connection.CallmeConnection;
import com.logtrac.maxsum.logtrac.connection.MessageConnection;
import com.logtrac.maxsum.logtrac.connection.TravelCancelConnection;
import com.logtrac.maxsum.logtrac.connection.TravelFinishConnection;
import com.logtrac.maxsum.logtrac.model.Travel;

/**
 * Created by MuhammadAnwar on 10/23/2017.
 */
public class ReserveTravelActivity extends AppCompatActivity {

    Travel travel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_travel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                showMessage();
            }
        });


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

        final Button navButton = (Button) findViewById(R.id.navigation_button);
        final Button callmeButton = (Button) findViewById(R.id.callme_button);
        final Button cancelButton = (Button) findViewById(R.id.button_cancel);
        final Button finishButton = (Button) findViewById(R.id.button_finish);


        navButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your code here
                //Toast.makeText(PickupTravelActivity.this, "Button Accept", Toast.LENGTH_LONG).show();
                navigationPlease();
            }
        });
        callmeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your code here
                callmePlease();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your code here
                //Toast.makeText(PickupTravelActivity.this, "Button Accept", Toast.LENGTH_LONG).show();
                cancelTravel();
            }
        });
        finishButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your code here
                //Toast.makeText(PickupTravelActivity.this, "Button Accept", Toast.LENGTH_LONG).show();
                finishTravel();
            }
        });

    }

    private void navigationPlease() {
        Intent intent = new Intent(ReserveTravelActivity.this, NavigationActivity.class);
        intent.putExtra("Travel_Object", this.travel);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void callmePlease() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SESSION", 0);
        CallmeConnection callme = new CallmeConnection(pref.getString("session_id",null));
        callme.Callme();

        while (!CallmeConnection.ISCallmeFINISHED) {}
        //Toast.makeText(ReserveActivity.this, CallmeConnection.CallmeMESSAGE, Toast.LENGTH_LONG).show();
        //Toast.makeText(this, "Call request ", Toast.LENGTH_LONG).show();
        if(CallmeConnection.CallmeMESSAGE.contains("error")) {
            Toast.makeText(this, "Connection error : "+CallmeConnection.CallmeMESSAGE, Toast.LENGTH_LONG).show();
            CallmeConnection.CallmeMESSAGE="";
            CallmeConnection.ISCallmeFINISHED=false;
        }
        else {
            Toast.makeText(this, "Request was sent to center : "+CallmeConnection.CallmeMESSAGE,
                    Toast.LENGTH_LONG).show();
            CallmeConnection.CallmeMESSAGE="";
            CallmeConnection.ISCallmeFINISHED=false;

        }
    }

    private void cancelTravel() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SESSION", 0);
        TravelCancelConnection cancel = new TravelCancelConnection(travel.id, pref.getString("session_id",null));
        cancel.Cancel();

        while (!TravelCancelConnection.ISCancelFINISHED) {}
        //Toast.makeText(ReserveTravelActivity.this, TravelCancelConnection.CancelMESSAGE, Toast.LENGTH_LONG).show();

        if(TravelCancelConnection.CancelMESSAGE.contains("error")) {
            Toast.makeText(ReserveTravelActivity.this, TravelCancelConnection.CancelMESSAGE, Toast.LENGTH_LONG).show();
            TravelCancelConnection.CancelMESSAGE="";
            TravelCancelConnection.ISCancelFINISHED=false;
        }
        else {
            TravelCancelConnection.CancelMESSAGE="";
            TravelCancelConnection.ISCancelFINISHED=false;
            //Toast.makeText(this, "Cancel Travel : "+cancel.getUrl()+"\nsession_id:"+pref.getString("session_id",null), Toast.LENGTH_LONG).show();
            try {
                Intent intent = new Intent(ReserveTravelActivity.this, MainActivity.class);
                intent.putExtra("Travel_Object", this.travel);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void finishTravel() {
        try {
            Intent intent = new Intent(ReserveTravelActivity.this, FinishTravelActivity.class);
            intent.putExtra("Travel_Object", this.travel);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }

//        SharedPreferences pref = getApplicationContext().getSharedPreferences("SESSION", 0);
//        TravelFinishConnection finish = new TravelFinishConnection(travel.id, pref.getString("session_id",null));
//        finish.Finish();
//
//        while (!TravelFinishConnection.ISFinishFINISHED) {}
//
//        if(TravelFinishConnection.FinishMESSAGE.startsWith("error")) {
//            Toast.makeText(ReserveTravelActivity.this, TravelFinishConnection.FinishMESSAGE, Toast.LENGTH_LONG).show();
//            TravelFinishConnection.FinishMESSAGE="";
//            TravelFinishConnection.ISFinishFINISHED=false;
//        }
//        else {
//            TravelFinishConnection.FinishMESSAGE="";
//            TravelFinishConnection.ISFinishFINISHED=false;
//            Toast.makeText(this, "Finish Travel : "+finish.getUrl(), Toast.LENGTH_LONG).show();
//            try {
//                Intent intent = new Intent(ReserveTravelActivity.this, FinishTravelActivity.class);
//                intent.putExtra("Travel_Object", this.travel);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }catch (Exception e) {
//                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
//            }
//        }
    }


    private void showMessage() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SESSION", 0);
        MessageConnection Message = new MessageConnection(pref.getString("session_id", null));
        Message.Message();
        while(!MessageConnection.ISMessageFINISHED){}
        String ms;
        if (MessageConnection.MessageMESSAGE.contains("error") || MessageConnection.MessageMESSAGE.contains("Not Found")) {
            ms="No message for you";
        } else {
            ms=MessageConnection.MessageMESSAGE;
        }
        MessageConnection.MessageMESSAGE = "";
        MessageConnection.ISMessageFINISHED = false;

        AlertDialog alertDialog = new AlertDialog.Builder(ReserveTravelActivity.this).create();
        alertDialog.setTitle("Message:");
        alertDialog.setMessage(ms);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
