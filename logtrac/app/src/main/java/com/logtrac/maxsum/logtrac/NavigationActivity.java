package com.logtrac.maxsum.logtrac;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import com.logtrac.maxsum.logtrac.model.Travel;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.min;

public class NavigationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Travel travel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //El Paso and houston
        //LatLng start = new LatLng(31.79, -106.46);
        //LatLng end = new LatLng(29.768, -95.365);
        //Jakarta Surabaya
        LatLng start = new LatLng(-6.104821, 106.900146);
        LatLng end = new LatLng(-6.104821, 106.900146);
//        LatLng end = new LatLng(-7.196474, 112.733568);

        try {
            Intent i = getIntent();
            this.travel = (Travel) i.getParcelableExtra("Travel_Object");
            start = new LatLng(Double.parseDouble(travel.departure_strlat), Double.parseDouble(travel.departure_strlong));
            end = new LatLng(Double.parseDouble(travel.arrival_strlat), Double.parseDouble(travel.arrival_strlong));
        }catch (Exception e) {

        }

        mMap.addMarker(new MarkerOptions().position(start).title("Departure"));
        mMap.addMarker(new MarkerOptions().position(end).title("Arrival"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(start));

        DateTime now = new DateTime();
        String info;
        try {
            DirectionsResult result = DirectionsApi.newRequest(getGeoContext())
                    .mode(TravelMode.DRIVING).origin(new com.google.maps.model.LatLng(start.latitude, start.longitude))
                    .destination(new com.google.maps.model.LatLng(end.latitude, end.longitude)).departureTime(now).await();
            this.addPolyline(result, mMap);
            this.addMarkersToMap(result, mMap);
            info=this.getEndLocationTitle(result);

            LatLngBounds areaBound = new LatLngBounds(
                    new LatLng(Math.min(start.latitude,end.latitude),Math.min(start.longitude,end.longitude)),
                    new LatLng(Math.max(start.latitude,end.latitude),Math.max(start.longitude,end.longitude)));

            mMap.setLatLngBoundsForCameraTarget(areaBound);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(areaBound.getCenter(), 6));


        }catch (Exception e) {
            Log.d("Direction error : ",e.toString());
        }
    }

    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext.setQueryRateLimit(3).setApiKey(getString(R.string.directionsApiKey))
                .setConnectTimeout(1, TimeUnit.SECONDS).setReadTimeout(1, TimeUnit.SECONDS)
                .setWriteTimeout(1, TimeUnit.SECONDS);
    }


    private void addMarkersToMap(DirectionsResult results, GoogleMap mMap) {
        mMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[0].legs[0]
                .startLocation.lat,results.routes[0].legs[0].startLocation.lng))
                .title(results.routes[0].legs[0].startAddress));
        mMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[0].legs[0]
                .endLocation.lat,results.routes[0].legs[0].endLocation.lng))
                .title(results.routes[0].legs[0].startAddress)
                .snippet(getEndLocationTitle(results)));
    }

    private String getEndLocationTitle(DirectionsResult results){
        return  "Time :"+ results.routes[0].legs[0].duration.humanReadable
                + " Distance :" + results.routes[0].legs[0].distance.humanReadable;
    }

    private void addPolyline(DirectionsResult results, GoogleMap mMap) {
        List<LatLng> decodedPath = PolyUtil.decode(results.routes[0].overviewPolyline.getEncodedPath());
        mMap.addPolyline(new PolylineOptions()
                .color(Color.rgb(255,0,128))
                .width(10)
                .addAll(decodedPath));
    }



}
