package com.example.fxcaptrade.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fxcaptrade.Home.HomeActivity;
import com.example.fxcaptrade.R;
import com.example.fxcaptrade.Utils.WebServices;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EmailVerification extends AppCompatActivity {
    private static final String TAG = "EmailVerification";

    private SharedPreferences user;
    private String username;
    private ExtendedFloatingActionButton fabVerify;
    private TextView resendOtp;
    private EditText otpEditText;
    private RequestQueue requestQueue;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_varification);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Email Verification");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        changeNotificationShadeColor(33,24,81);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                finish();
            }
        });


        declarations();
        getSharedPreference();

    }

    private void declarations() {
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        // fabVerify = (ExtendedFloatingActionButton) findViewById(R.id.fabVerify);
        otpEditText = (EditText) findViewById(R.id.otpValue);

    }


    public void verifyOTP(View view) {
        dialog = ProgressDialog.show(this, "Loading", "Please wait...", true);
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            Log.e(TAG, "Setting a new request queue");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "Email_verification", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG,"Response"+response);
                try {

                    JSONObject object = new JSONObject(response.trim());
                    String status = object.getString("status");

                    if (status.equals("1")) {


                        dialog.dismiss();
                        Toast toast = Toast.makeText(getApplicationContext(), object.getString("message"),
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                      //  Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();

                    } else {

                        dialog.dismiss();
                        Toast toast = Toast.makeText(getApplicationContext(), object.getString("message"),
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                       // Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "Exception" + e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "VolleyError" + error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", username);
                map.put("otp", otpEditText.getText().toString());
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void getSharedPreference() {

        user = getApplicationContext().getSharedPreferences("User", getApplicationContext().MODE_PRIVATE);
        username = user.getString("user_id", "0");
        Log.e(TAG, username);

    }

    private void changeNotificationShadeColor(int red,int green,int blue) {

        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.rgb(red, green, blue));
        }
    }


    public void resendOTP(View view) {

        final ProgressDialog dialog = ProgressDialog.show(this, "Loading", "Please wait...", true);
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            Log.e(TAG, "Setting a new request queue");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "Resend_otp", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG,"Response"+response);
                try {

                    JSONObject object = new JSONObject(response.trim());
                    String status = object.getString("status");

                    if (status.equals("1")) {


                        dialog.dismiss();

                        Toast toast = Toast.makeText(getApplicationContext(), object.getString("message"),
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        ///  Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();

                    } else {

                        dialog.dismiss();
                        Toast toast = Toast.makeText(getApplicationContext(), object.getString("message"),
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        // Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "Exception" + e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
             Toast.makeText(getApplicationContext(),"Try after some time", Toast.LENGTH_SHORT).show();

                Log.e(TAG, "VolleyError" + error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", username);
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);
    }
}
