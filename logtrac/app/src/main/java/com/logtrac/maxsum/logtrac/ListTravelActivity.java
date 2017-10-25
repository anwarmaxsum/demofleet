package com.logtrac.maxsum.logtrac;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.logtrac.maxsum.logtrac.connection.RetrieveTravelConnection;
import com.logtrac.maxsum.logtrac.model.CustomListAdapter;
import com.logtrac.maxsum.logtrac.model.Employee;
import com.logtrac.maxsum.logtrac.model.PlaceID;
import com.logtrac.maxsum.logtrac.model.Travel;
import com.logtrac.maxsum.logtrac.model.UnitID;
import com.logtrac.maxsum.logtrac.model.User;
import com.logtrac.maxsum.logtrac.model.WayBill;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by MuhammadAnwar on 10/23/2017.
 */
public class ListTravelActivity extends ListActivity {
    ListView travelList;
    String[] travelNames;
    String[] travelDepartures;
    String[] travelArrivals;
    String[] travelStatus;
    Travel[] travelArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_travel);
        boolean isError = false;
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar();

        //Toast.makeText(this, "Cek 1", Toast.LENGTH_LONG).show();
        setTitle("Travel List");
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SESSION", 0);
        RetrieveTravelConnection retrieveTravel = new RetrieveTravelConnection(pref.getString("session_id", null));

        //Retrieve all travel
        retrieveTravel.retrieveAllTravel();
        while (!RetrieveTravelConnection.GET_TRAVELS_ISFINISHED) {
        }
        //Toast.makeText(this, RetrieveTravelConnection.GET_TRAVELS_MESSAGE, Toast.LENGTH_LONG).show();

        if (RetrieveTravelConnection.GET_TRAVELS_MESSAGE.startsWith("error")) {
            Toast.makeText(this, RetrieveTravelConnection.GET_TRAVELS_MESSAGE, Toast.LENGTH_LONG).show();
            RetrieveTravelConnection.GET_TRAVELS_MESSAGE = "";
            RetrieveTravelConnection.GET_TRAVELS_ISFINISHED = false;
        } else {
            try {
                JSONArray jsonArr = new JSONArray(RetrieveTravelConnection.GET_TRAVELS_MESSAGE);
                travelNames = new String[jsonArr.length()];
                travelDepartures = new String[jsonArr.length()];
                travelArrivals = new String[jsonArr.length()];
                travelStatus = new String[jsonArr.length()];
                travelArray = new Travel[jsonArr.length()];

                JSONObject employee, user_id, departure_id, arrival_id, waybill, unit_id;
                String date, state, name, id;

                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject jsonOb = jsonArr.getJSONObject(i);

                    date = jsonOb.getString("date");
                    state = jsonOb.getString("state");
                    name = jsonOb.getString("name");
                    id = jsonOb.getString("id");

                    employee = jsonOb.getJSONObject("employee_id");
                    Employee emp = new Employee(employee.getString("id"), employee.getString("name"));


                    user_id = jsonOb.getJSONObject("user_id");
                    User usr = new User(user_id.getString("id"), user_id.getString("name"));

                    departure_id = jsonOb.getJSONObject("departure_id");
                    PlaceID dep = new PlaceID(departure_id.getString("latitude"), departure_id.getString("longitude"),
                            departure_id.getString("id"), departure_id.getString("name"));

                    arrival_id = jsonOb.getJSONObject("arrival_id");
                    PlaceID arr = new PlaceID(arrival_id.getString("latitude"), arrival_id.getString("longitude"),
                            arrival_id.getString("id"), arrival_id.getString("name"));


                    unit_id = jsonOb.getJSONObject("unit_id");
                    UnitID ut = new UnitID(unit_id.getString("id"), unit_id.getString("name"));

                    Travel travel = new Travel(
                            date,
                            state,
                            name,

                            emp.id,
                            emp.name,

                            usr.id,
                            usr.name,

                            dep.id,
                            dep.name,
                            dep.strLat,
                            dep.strLong,

                            arr.id,
                            arr.name,
                            arr.strLat,
                            arr.strLong,

                            "",

                            id,
                            ut.id,
                            ut.name
                    );

                    travelArray[i] = travel;
                    travelNames[i] = travel.name;
                    travelDepartures[i] = dep.name;
                    travelArrivals[i] = arr.name;
                    travelStatus[i] = state;

                    Log.d("### Read All travel : ", travel.date + ":" + travel.state + ":" + travel.id + ":" + travel.name);
                    Log.d("### Read All Array : ", travelNames[i] + ":" + travelDepartures[i] + ":" + travelArrivals[i]);
                }

                RetrieveTravelConnection.GET_TRAVELS_MESSAGE = "";
                RetrieveTravelConnection.GET_TRAVELS_ISFINISHED = false;

                CustomListAdapter adapter = new CustomListAdapter(this, travelNames, travelDepartures, travelArrivals, travelStatus);
                travelList = (ListView) findViewById(android.R.id.list);
                travelList.setAdapter(adapter);

            } catch (Exception e) {
                Toast.makeText(this, "Error occures or You are not legitimate driver", Toast.LENGTH_LONG).show();
                Log.d("Error : ", e.toString());
            }
        }


        travelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem = travelNames[+position];
                //Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();
                nextActivity(position);
            }
        });

    }

    private void nextActivity(int position) {
        if(travelStatus[position].equals("draft")) {
            Intent intent = new Intent(ListTravelActivity.this, PickupTravelActivity.class);
            intent.putExtra("Travel_Object", this.travelArray[position]);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        if(travelStatus[position].equals("progress")) {
            Intent intent = new Intent(ListTravelActivity.this, ReserveTravelActivity.class);
            intent.putExtra("Travel_Object", this.travelArray[position]);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_list_travel);
        boolean isError = false;
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar();

        //Toast.makeText(this, "Cek 1", Toast.LENGTH_LONG).show();
        setTitle("Travel List");
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SESSION", 0);
        RetrieveTravelConnection retrieveTravel = new RetrieveTravelConnection(pref.getString("session_id", null));

        //Retrieve all travel
        retrieveTravel.retrieveAllTravel();
        while (!RetrieveTravelConnection.GET_TRAVELS_ISFINISHED) {
        }
        //Toast.makeText(this, RetrieveTravelConnection.GET_TRAVELS_MESSAGE, Toast.LENGTH_LONG).show();

        if (RetrieveTravelConnection.GET_TRAVELS_MESSAGE.startsWith("error")) {
            Toast.makeText(this, RetrieveTravelConnection.GET_TRAVELS_MESSAGE, Toast.LENGTH_LONG).show();
            RetrieveTravelConnection.GET_TRAVELS_MESSAGE = "";
            RetrieveTravelConnection.GET_TRAVELS_ISFINISHED = false;
        } else {
            try {
                JSONArray jsonArr = new JSONArray(RetrieveTravelConnection.GET_TRAVELS_MESSAGE);
                travelNames = new String[jsonArr.length()];
                travelDepartures = new String[jsonArr.length()];
                travelArrivals = new String[jsonArr.length()];
                travelStatus = new String[jsonArr.length()];
                travelArray = new Travel[jsonArr.length()];

                JSONObject employee, user_id, departure_id, arrival_id, waybill, unit_id;
                String date, state, name, id;

                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject jsonOb = jsonArr.getJSONObject(i);

                    date = jsonOb.getString("date");
                    state = jsonOb.getString("state");
                    name = jsonOb.getString("name");
                    id = jsonOb.getString("id");

                    employee = jsonOb.getJSONObject("employee_id");
                    Employee emp = new Employee(employee.getString("id"), employee.getString("name"));


                    user_id = jsonOb.getJSONObject("user_id");
                    User usr = new User(user_id.getString("id"), user_id.getString("name"));

                    departure_id = jsonOb.getJSONObject("departure_id");
                    PlaceID dep = new PlaceID(departure_id.getString("latitude"), departure_id.getString("longitude"),
                            departure_id.getString("id"), departure_id.getString("name"));

                    arrival_id = jsonOb.getJSONObject("arrival_id");
                    PlaceID arr = new PlaceID(arrival_id.getString("latitude"), arrival_id.getString("longitude"),
                            arrival_id.getString("id"), arrival_id.getString("name"));


                    unit_id = jsonOb.getJSONObject("unit_id");
                    UnitID ut = new UnitID(unit_id.getString("id"), unit_id.getString("name"));

                    Travel travel = new Travel(
                            date,
                            state,
                            name,

                            emp.id,
                            emp.name,

                            usr.id,
                            usr.name,

                            dep.id,
                            dep.name,
                            dep.strLat,
                            dep.strLong,

                            arr.id,
                            arr.name,
                            arr.strLat,
                            arr.strLong,

                            "",

                            id,
                            ut.id,
                            ut.name
                    );

                    travelArray[i] = travel;
                    travelNames[i] = travel.name;
                    travelDepartures[i] = dep.name;
                    travelArrivals[i] = arr.name;
                    travelStatus[i] = state;

                    Log.d("### Read All travel : ", travel.date + ":" + travel.state + ":" + travel.id + ":" + travel.name);
                    Log.d("### Read All Array : ", travelNames[i] + ":" + travelDepartures[i] + ":" + travelArrivals[i]);
                }

                RetrieveTravelConnection.GET_TRAVELS_MESSAGE = "";
                RetrieveTravelConnection.GET_TRAVELS_ISFINISHED = false;

                CustomListAdapter adapter = new CustomListAdapter(this, travelNames, travelDepartures, travelArrivals, travelStatus);
                travelList = (ListView) findViewById(android.R.id.list);
                travelList.setAdapter(adapter);

            } catch (Exception e) {
                Toast.makeText(this, "Error occures or You are not legitimate driver", Toast.LENGTH_LONG).show();
                Log.d("Error : ", e.toString());
            }
        }


        travelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem = travelNames[+position];
                //Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();
                nextActivity(position);
            }
        });
        Log.d("List Travel Activity :", "====================== On Resume =======================");
    }
}
