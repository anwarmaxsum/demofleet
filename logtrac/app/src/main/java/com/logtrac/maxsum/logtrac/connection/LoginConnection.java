package com.logtrac.maxsum.logtrac.connection;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.logtrac.maxsum.logtrac.LoginActivity;
import com.logtrac.maxsum.logtrac.MainActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.InputStream;


/**
 * Created by MuhammadAnwar on 10/23/2017.
 */

public class LoginConnection {
    public static String LOGINURL="https://track.primeforcindo.com/api/login";
    //public static String LOGINURL="http://jsonapi.org/examples/posts/1";
    public static String LOGINMESSAGE="";
    public static boolean ISLOGINFINISHED=false;

    private String userid;
    private String password;

    public LoginConnection(String  id, String pass) {
        this.userid=id;
        this.password=pass;
    }

    public void login() {
        new LoginAsyncTask().execute();
    }

    private class LoginAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            urls.getClass();
            return PostLoginData(userid,password);
        }
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        }
    }


    public static String PostLoginData(String id, String password){
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(LoginConnection.LOGINURL);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("login", id);
            jsonObject.put("password", password);
            StringEntity se = new StringEntity(jsonObject.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/plain"));
            httpPost.setEntity(se);

            Log.d("PostLoginData","prepare");
            Log.d("PostLoginData","userid : "+id);
            Log.d("PostLoginData","password : "+password);
            HttpResponse httpResponse = httpclient.execute(httpPost);
            Log.d("PostLoginData","execute post");
            HttpEntity entity = httpResponse.getEntity();
            Log.d("PostLoginData","get content");
            //inputStream = httpResponse.getEntity().getContent();
            // if(inputStream != null)
            if(entity != null) {
                //result = inputStream.toString();
                result = EntityUtils.toString(entity);
            }
            else
                result = "error";
        } catch (Exception e) {
            result = "error : "+e.toString();
        }
        Log.d("PostLoginData","return result : " +result);
        LoginConnection.LOGINMESSAGE=result;
        LoginConnection.ISLOGINFINISHED=true;
        return result;
    }
}
