package com.logtrac.maxsum.logtrac.connection;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by MuhammadAnwar on 10/24/2017.
 */

public class TravelCancelConnection {
    private String cancelUrl="https://track.primeforcindo.com/api/travels/";
    public static String CancelMESSAGE="";
    public static boolean ISCancelFINISHED=false;

    private String id;
    private String session_id;

    public TravelCancelConnection(String  id, String sesid) {
        this.id=id;
        this.session_id=sesid;
        cancelUrl="https://track.primeforcindo.com/api/travels/"+id+"/cancel?";
    }


    public void Cancel() {
        new TravelCancelConnection.CancelAsyncTask().execute();
    }
    public String getUrl() {
        return this.cancelUrl;
    }

    private class CancelAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            urls.getClass();
            return postCancelData(session_id);
        }
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        }
    }

    private String postCancelData(String sid){
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(cancelUrl+"session_id="+sid);
            HttpResponse httpResponse = httpclient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            if(entity != null) {
                result = EntityUtils.toString(entity);
            }
            else
                result = "error";
        } catch (Exception e) {
            result = "error : "+e.toString();
        }

        TravelCancelConnection.CancelMESSAGE=result;
        TravelCancelConnection.ISCancelFINISHED=true;
        return result;
    }
}
