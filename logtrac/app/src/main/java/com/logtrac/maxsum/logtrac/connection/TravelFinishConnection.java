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

public class TravelFinishConnection {
    private String finishUrl="https://track.primeforcindo.com/api/travels/";
    public static String FinishMESSAGE="";
    public static boolean ISFinishFINISHED=false;

    private String id;
    private String session_id;
    private String fuel;

    public TravelFinishConnection(String  id, String sesid, String fu) {
        this.id=id;
        this.session_id=sesid;
        this.fuel=fu;
        finishUrl="https://track.primeforcindo.com/api/travels/"+id+"/finish?";
    }


    public void Finish() {
        new TravelFinishConnection.FinishAsyncTask().execute();
    }

    public String getUrl() {
        return this.finishUrl;
    }

    private class FinishAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            urls.getClass();
            return postFinishData(session_id, fuel);
        }
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        }
    }

    private String postFinishData(String sid, String fu){
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(finishUrl+"session_id="+sid);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("fuel", fu);
            StringEntity se = new StringEntity(jsonObject.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/plain"));
            httpPost.setEntity(se);

            HttpResponse httpResponse = httpclient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();

            Log.d("PostFinishData","url: "+finishUrl+"session_id="+sid+"&fuel="+fu);
            Log.d("PostFinishData","session_id: "+sid);
            if(entity != null) {
                result = EntityUtils.toString(entity);
            }
            else
                result = "error";
        } catch (Exception e) {
            result = "error : "+e.toString();
        }

        TravelFinishConnection.FinishMESSAGE=result;
        TravelFinishConnection.ISFinishFINISHED=true;
        return result;
    }
}
