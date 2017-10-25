package com.logtrac.maxsum.logtrac.connection;

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
 * Created by MuhammadAnwar on 10/24/2017.
 */

public class MessageConnection {
    private String messageUrl="https://track.primeforcindo.com/api/messages";
    public static String MessageMESSAGE="";
    public static boolean ISMessageFINISHED=false;

    private String session_id;

    public MessageConnection(String sesid) {
        this.session_id=sesid;
    }

    public void Message() {
        new MessageConnection.MessageAsyncTask().execute();
    }
    public String getUrl() {
        return this.messageUrl;
    }

    private class MessageAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            urls.getClass();
            return postMessageData(session_id);
        }
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        }
    }

    private String postMessageData(String sid){
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(messageUrl+"?"+"session_id="+sid);
            HttpResponse httpResponse = httpclient.execute(httpGet);
            //HttpPost httpPost = new HttpPost(messageUrl+"session_id="+sid);
            //HttpResponse httpResponse = httpclient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            if(entity != null) {
                result = EntityUtils.toString(entity);
            }
            else
                result = "error";
        } catch (Exception e) {
            result = "error : "+e.toString();
        }

        MessageConnection.MessageMESSAGE=result;
        MessageConnection.ISMessageFINISHED=true;
        return result;
    }
}
