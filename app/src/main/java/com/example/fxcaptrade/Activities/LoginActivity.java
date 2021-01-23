package com.example.fxcaptrade.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fxcaptrade.R;
import com.example.fxcaptrade.Utils.WebServices;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private TextView userName;
    private TextInputEditText password;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        declarations();
    }

    private void declarations() {

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        userName = (TextView) findViewById(R.id.email);
        password = (TextInputEditText) findViewById(R.id.pass);
    }

    public void login(View view) {
        boolean stat = false;
        if (userName.getText().toString().isEmpty()) {
            userName.setError("required");
            stat = true;
        }
        if (password.getText().toString().isEmpty()) {
            password.setError("required");
            stat = true;
        }
        if (!stat) {
            loginFunction(userName.getText().toString(),password.getText().toString());
        }

    }

    private void loginFunction(final String user, final String pass) {

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            Log.e(TAG, "Setting a new request queue");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "Login", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.e(TAG,"Response"+response);
                try {

                    JSONObject object = new JSONObject(response.trim());
                    String status = object.getString("status");
                    if (status.equals("1")) {

                        Toast.makeText(LoginActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor sp = getSharedPreferences("User", Context.MODE_PRIVATE).edit();
                        sp.clear();
                        sp.putString("user_id", object.getString("userid"));

                        sp.commit();

//                        startActivity(new Intent(getApplicationContext(),Home.class));
//                        finish();

                    } else {

                        Toast.makeText(LoginActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
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
                map.put("username", user);
                map.put("password",pass);
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    public void forgotPassword(View view) {
    }

    public void registration(View view) {
        startActivity(new Intent(getApplicationContext(),Registration.class));
    }
}
