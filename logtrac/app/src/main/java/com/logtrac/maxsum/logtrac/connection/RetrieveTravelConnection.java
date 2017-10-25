package com.logtrac.maxsum.logtrac.connection;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by MuhammadAnwar on 10/23/2017.
 */

public class RetrieveTravelConnection {
    public static String GET_TRAVELS_URL="https://track.primeforcindo.com/api/travels";
    public static String GET_TRAVELS_MESSAGE="";
    public static boolean GET_TRAVELS_ISFINISHED=false;

    private String session_id;
    public RetrieveTravelConnection(String  sid) {
        this.session_id=sid;
    }

    public void retrieveAllTravel() {
        new RetrieveTravelConnection.RetriveTravelAsyncTask().execute();
    }

    private class RetriveTravelAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            urls.getClass();
            return getAllTravel(session_id);
        }
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        }
    }

    public static String getAllTravel(String sid){
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(GET_TRAVELS_URL + "?" +"session_id="+sid);
            Log.d("Get All Travels: ","session_id : "+sid);
            HttpResponse httpResponse = httpclient.execute(httpGet);
            Log.d("Get All Travels: ","execute get");
            HttpEntity entity = httpResponse.getEntity();
            Log.d("Get All Travels: ","get content");
            if(entity != null) {
                result = EntityUtils.toString(entity);
            }
            else
                result = "error";
        } catch (Exception e) {
            result = "error : "+e.toString();
        }
        Log.d("Get All Travels: ","return : " +result);
        RetrieveTravelConnection.GET_TRAVELS_MESSAGE=result;
        RetrieveTravelConnection.GET_TRAVELS_ISFINISHED=true;
        return result;
    }
}
