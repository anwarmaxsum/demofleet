package com.logtrac.maxsum.logtrac;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.logtrac.maxsum.logtrac.connection.LoginConnection;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity  {

    private AutoCompleteTextView mIDView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mIDView = (AutoCompleteTextView) findViewById(R.id.id);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


        Button logInButton = (Button) findViewById(R.id.log_in_button);
        logInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    private void attemptLogin() {
        String id = mIDView.getText().toString();
        String pass = mPasswordView.getText().toString();
        LoginConnection loginCon = new LoginConnection(id, pass);
        loginCon.login();

        while (!LoginConnection.ISLOGINFINISHED) {}

        if(LoginConnection.LOGINMESSAGE.startsWith("error")) {
            Toast.makeText(LoginActivity.this, LoginConnection.LOGINMESSAGE, Toast.LENGTH_LONG).show();
            LoginConnection.LOGINMESSAGE="";
            LoginConnection.ISLOGINFINISHED=false;
            mIDView.setText("");
            mPasswordView.setText("");

        }
        else {
            boolean isError=false;
            try {
                JSONObject jsonOb = new JSONObject(LoginConnection.LOGINMESSAGE);
                String usrName = jsonOb.getString("username");
                String usrId = jsonOb.getString("id");
                String sesId = jsonOb.getString("session_id");
                SharedPreferences pref = getApplicationContext().getSharedPreferences("SESSION", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("username", usrName);
                editor.putString("id", usrId);
                editor.putString("session_id", sesId);
                editor.commit();
            }catch (Exception e) {
                //Toast.makeText(LoginActivity.this, "Error : "+e.toString(), Toast.LENGTH_LONG).show();
                isError=true;
            }
            LoginConnection.LOGINMESSAGE="";
            LoginConnection.ISLOGINFINISHED=false;
            if(isError) {
                Toast.makeText(LoginActivity.this, "Error : "+"Invalid username or passworfd", Toast.LENGTH_LONG).show();
            }else {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Log.d("Toast", "Error #1: " + e);
                }
            }


        }
    }

}

