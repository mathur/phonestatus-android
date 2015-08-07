package com.rmathur.phonestatus;

import com.parse.ParseUser;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.loopj.android.http.*;

import org.apache.http.Header;

public class Welcome extends Activity {

    // Declare Variable
    Button logout;
    Button update;
    EditText edtStatus;
    int batteryPercent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from singleitemview.xml
        setContentView(R.layout.welcome);

        // Retrieve current user from Parse.com
        ParseUser currentUser = ParseUser.getCurrentUser();
        registerReceiver(batteryStatusReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        // Convert currentUser into String
        final String struser = currentUser.getUsername().toString();

        edtStatus = (EditText) findViewById(R.id.edtNewStatus);
        update = (Button) findViewById(R.id.btnUpdate);
        update.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                String status = edtStatus.getText().toString();
                String url = "http://phonestatus.herokuapp.com/update" + "?username=" + struser + "&battery_percent=" + String.valueOf(batteryPercent) + "&status=" + status;
                AsyncHttpClient client = new AsyncHttpClient();
                client.get(url, new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        // called before request is started
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        // ayy
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    }

                    @Override
                    public void onRetry(int retryNo) {
                        // called when request is retried
                    }
                });
            }
        });

        // Locate Button in welcome.xml
        logout = (Button) findViewById(R.id.logout);

        // Logout Button Click Listener
        logout.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // Logout current user
                ParseUser.logOut();
                finish();
            }
        });
    }

    private BroadcastReceiver batteryStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            batteryPercent = intent.getIntExtra("level", 0);
        }
    };
}