package com.logtrac.maxsum.logtrac;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.logtrac.maxsum.logtrac.connection.CallmeConnection;
import com.logtrac.maxsum.logtrac.connection.MessageConnection;
import com.logtrac.maxsum.logtrac.model.CustomGridAdapter;

/**
 * Created by MuhammadAnwar on 10/23/2017.
 */
public class MainActivity extends AppCompatActivity {

    GridView grid;
    private String[] menuName = new String[]{"List Travel", "Navigation",
            "Message", "Call Me Now", "Operational", "Exit"};
    int[] menuImage = {
            R.drawable.listorder2,
            R.drawable.navigation2,
            R.drawable.message2,
            R.drawable.callme2,
            R.drawable.operational2,
            R.drawable.exit2
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");

        SharedPreferences pref = getApplicationContext().getSharedPreferences("SESSION", 0);
//        Toast.makeText(this, "SESSION : "+pref.getString("username",null)+"--"+pref.getString("id",null)+"--"+
//                pref.getString("session_id",null), Toast.LENGTH_LONG).show();

        CustomGridAdapter adapter = new CustomGridAdapter(MainActivity.this, menuName, menuImage);
        grid = (GridView) findViewById(R.id.menugrid);
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                Toast.makeText(MainActivity.this, "Go to " +
//                        menuName[+ position], Toast.LENGTH_SHORT).show();
                nextMenu(position);
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setTitle(R.string.app_name);
        setTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbarmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
//        if (id == R.id.action_refresh) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }


    private void nextMenu(int position) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SESSION", 0);
        Log.d("Next Menu : ", " Go to : " + position);
        Intent intent;
        try {
            switch (position) {
                case 0:
                    intent = new Intent(MainActivity.this, ListTravelActivity.class);
                    Log.d("Next Menu : ", " Go to : List Order");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 1:
                    intent = new Intent(MainActivity.this, NavigationActivity.class);
                    Log.d("Next Menu : ", " Go to : List Order");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 2:
//                    MessageConnection Message = new MessageConnection(pref.getString("session_id", null));
//                    Message.Message();
//                    while(!MessageConnection.ISMessageFINISHED){}
//                    String ms;
//                    if (MessageConnection.MessageMESSAGE.contains("error") || MessageConnection.MessageMESSAGE.contains("Not Found")) {
//                        ms="No message for you";
//                    } else {
//                       ms=MessageConnection.MessageMESSAGE;
//                    }
//                    MessageConnection.MessageMESSAGE = "";
//                    MessageConnection.ISMessageFINISHED = false;
//
//                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
//                    alertDialog.setTitle("Message:");
//                    alertDialog.setMessage(ms);
//                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            });
//                    alertDialog.show();
                    intent = new Intent(MainActivity.this, MessageListActivity.class);
                    Log.d("Show Message: ", " View Message ");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 3:
                    pref = getApplicationContext().getSharedPreferences("SESSION", 0);
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
                    break;
                case 4:
                    try {
                        String phone = "777000777";
                        intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                        startActivity(intent);
                    }catch (Exception e) {

                    }
                    break;
                case 5:
                    pref.edit().clear().commit();
                    super.finish();
                    break;
            }

        }catch (Exception e) {
            Toast.makeText(MainActivity.this, "Error : "+e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
