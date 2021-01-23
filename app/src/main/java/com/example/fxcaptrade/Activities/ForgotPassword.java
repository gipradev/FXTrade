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
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPassword extends AppCompatActivity {
    private static final String TAG = "ForgotPassword";

    private RequestQueue requestQueue;
    private EditText otpEditText;
    private SharedPreferences user;
    private String username;
    private TextInputEditText newPass;
    private TextInputEditText repeatPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Forgot Password");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        changeNotificationShadeColor(33,24,81);


        declarations();

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        Log.e(TAG,"username "+username);

    }

    private void declarations() {
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        // fabVerify = (ExtendedFloatingActionButton) findViewById(R.id.fabVerify);
        newPass = (TextInputEditText) findViewById(R.id.password0);
        repeatPass = (TextInputEditText) findViewById(R.id.password1);
        otpEditText = (EditText) findViewById(R.id.otpValue);

    }


    public void verifyOTP(View view) {

        validate();


    }

    private void validate() {
        boolean stat = false;
        if (otpEditText.getText().toString().isEmpty()) {
            otpEditText.setError("required");
            stat = true;
        }
        if (newPass.getText().toString().isEmpty()) {
            newPass.setError("required");
            stat = true;
        }

        if (repeatPass.getText().toString().isEmpty()) {
            repeatPass.setError("required");
            stat = true;
        }
        if (newPass.getText().toString().length() <8  ||  newPass.getText().toString().length() >15) {
            Toast.makeText(getApplicationContext(), "Password length must be 8 to 15 ", Toast.LENGTH_SHORT).show();
            stat = true;
        }
        if (repeatPass.getText().toString().length() <8  ||  repeatPass.getText().toString().length() >15) {
            Toast.makeText(getApplicationContext(), "Password length must be 8 to 15 ", Toast.LENGTH_SHORT).show();
            stat = true;
        }
        if (!newPass.getText().toString().equals(repeatPass.getText().toString())) {
            Toast.makeText(this, "Password miss match", Toast.LENGTH_SHORT).show();
            stat = true;
        }


        if (!stat) {
            sendToServer();
        }
    }

    private void sendToServer() {

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            Log.e(TAG, "Setting a new request queue");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "New_password", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG,"New pass"+response);
                try {

                    JSONObject object = new JSONObject(response.trim());
                    String status = object.getString("status");

                    if (status.equals("1")) {


                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();

                    } else {

                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
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
                map.put("user_name", username);
                map.put("otp", otpEditText.getText().toString());
                map.put("C_PASSWORD", newPass.getText().toString());
                map.put("C_PASSWORD2", repeatPass.getText().toString());

                Log.e(TAG,map+"");
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }
    private void changeNotificationShadeColor(int red,int green,int blue) {

        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.rgb(red, green, blue));
        }
    }



    public void onClickResent(View view) {
        final ProgressDialog dialog = ProgressDialog.show(this, "Loading", "Please wait...", true);
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            Log.e(TAG, "Setting a new request queue");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "Forgot_password", new Response.Listener<String>() {
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
                Log.e(TAG, "VolleyError" + error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_name", username);
                Log.e(TAG,map+"");
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
