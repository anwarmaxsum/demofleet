package com.logtrac.maxsum.logtrac.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by MuhammadAnwar on 10/23/2017.
 */

public class Travel implements Parcelable {
    public String date;
    public String state;
    public String name;

    public String employee_id;
    public String employee_name;

    public String user_id;
    public String user_name;

    public String departure_id;
    public String departure_name;
    public String departure_strlat;
    public String departure_strlong;

    public String arrival_id;
    public String arrival_name;
    public String arrival_strlat;
    public String arrival_strlong;

    public String waybill;

    public String id;
    public String unit_id;
    public String unit_name;

//    public Travel() {
//        super();
//
//    }

    public Travel(String date,
                  String state,
                  String name,

                  String employee_id,
                  String employee_name,

                  String user_id,
                  String user_name,

                  String departure_id,
                  String departure_name,
                  String departure_strlat,
                  String departure_strlong,

                  String arrival_id,
                  String arrival_name,
                  String arrival_strlat,
                  String arrival_strlong,

                  String waybill,

                  String id,
                  String unit_id,
                  String unit_name) {

        super();

        this.date = date;
        this.state = state;
        this.name = name;

        this.employee_id = employee_id;
        this.employee_name = employee_name;

        this.user_id = user_id;
        this.user_name = user_name;

        this.departure_id = departure_id;
        this.departure_name = departure_name;
        this.departure_strlat = departure_strlat;
        this.departure_strlong = departure_strlong;

        this.arrival_id = arrival_id;
        this.arrival_name = arrival_name;
        this.arrival_strlat = arrival_strlat;
        this.arrival_strlong = arrival_strlong;

        this.waybill = waybill;

        this.id = id;
        this.unit_id = unit_id;
        this.unit_name = unit_name;
    }

    protected Travel(Parcel in) {
        date = in.readString();
        state = in.readString();
        name = in.readString();
        employee_id = in.readString();
        employee_name = in.readString();
        user_id = in.readString();
        user_name = in.readString();
        departure_id = in.readString();
        departure_name = in.readString();
        departure_strlat = in.readString();
        departure_strlong = in.readString();
        arrival_id = in.readString();
        arrival_name = in.readString();
        arrival_strlat = in.readString();
        arrival_strlong = in.readString();
        waybill = in.readString();
        id = in.readString();
        unit_id = in.readString();
        unit_name = in.readString();
    }

    public static final Creator<Travel> CREATOR = new Creator<Travel>() {
        @Override
        public Travel createFromParcel(Parcel in) {
            return new Travel(in);
        }

        @Override
        public Travel[] newArray(int size) {
            return new Travel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(state);
        dest.writeString(name);

        dest.writeString(employee_id);
        dest.writeString(employee_name);

        dest.writeString(user_id);
        dest.writeString(user_name);

        dest.writeString(departure_id);
        dest.writeString(departure_name);
        dest.writeString(departure_strlat);
        dest.writeString(departure_strlong);

        dest.writeString(arrival_id);
        dest.writeString(arrival_name);
        dest.writeString(arrival_strlat);
        dest.writeString(arrival_strlong);

        dest.writeString(waybill);

        dest.writeString(id);
        dest.writeString(unit_id);
        dest.writeString(unit_name);
    }
}
