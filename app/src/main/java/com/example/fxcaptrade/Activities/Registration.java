package com.example.fxcaptrade.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {
    private static final String TAG = "Registration";

    private RequestQueue requestQueue;
    private TextView sponsorID,name,mobile,address;
    private TextInputEditText password0,password1;
    private Spinner countrySpinner;
    private TextView email;
    private View parentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        declarations();
        getCountry();
    }


    //********************************************************************************************************************************************************
    private void declarations() {

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        parentView = findViewById(android.R.id.content);
        sponsorID = (TextView) findViewById(R.id.spID);
        name = (TextView) findViewById(R.id.name);
        mobile = (TextView) findViewById(R.id.mob);
        email = (TextView) findViewById(R.id.email);
        address = (TextView) findViewById(R.id.address);
        password0 = (TextInputEditText) findViewById(R.id.password0);
        password1 = (TextInputEditText) findViewById(R.id.password1);
        countrySpinner = (Spinner) findViewById(R.id.country);
    }
    //********************************************************************************************************************************************************

    //********************************************************************************************************************************************************
    private void getCountry() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            Log.e(TAG, "Setting a new request queue");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "Country_list", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.e(TAG,"Response"+response);
                try {

                    JSONObject object = new JSONObject(response.trim());
                    String status = object.getString("status");
                    if (status.equals("1")) {

                        JSONArray array = object.getJSONArray("data");

                        Log.e(TAG, array.length() + "");
                        ArrayList<String> list = new ArrayList<>();
                        list.add("SELECT COUNTRY");

                        for (int i = 0; i < array.length(); i++) {
                            try {
                                String state = array.getJSONObject(i).getString("country_name");
                                list.add(state);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.e(TAG, list + "");

                        setCountrySpinner(list, array);


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
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<>();
//
//                return map;
//            }
        };
        requestQueue.add(stringRequest);

    }

    private void setCountrySpinner(ArrayList<String> countryArray, final JSONArray jsonArray) {

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, countryArray);
        countrySpinner.setAdapter(itemsAdapter);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!countrySpinner.getSelectedItem().toString().equalsIgnoreCase("Select Country")){
                    try {
                        String countryID = jsonArray.getJSONObject(position).getString("country_code");
                        Log.e(TAG, countryID);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

    }
    //********************************************************************************************************************************************************

    //********************************************************************************************************************************************************
    public void submitButton(View view) {
        boolean stat = false;
        if (sponsorID.getText().toString().isEmpty()) {
            sponsorID.setError("required");
            stat = true;
        }
        if (name.getText().toString().isEmpty()) {
            name.setError("required");
            stat = true;
        }
        if (address.getText().toString().isEmpty()) {
            address.setError("required");
            stat = true;
        }
        if (email.getText().toString().isEmpty()) {
            email.setError("required");
            stat = true;
        }
        if (mobile.getText().toString().isEmpty()) {
            mobile.setError("required");
            stat = true;
        }

        if (password0.getText().toString().isEmpty()) {
            password0.setError("required");
            stat = true;
        }
        if (password1.getText().toString().isEmpty()) {
            password1.setError("required");
            stat = true;
        }
        if (!password1.getText().toString().equals(password0.getText().toString())) {
            Toast.makeText(this, "Password miss match", Toast.LENGTH_SHORT).show();
            stat = true;
        }
        if (!stat) {
            RegFunction(sponsorID.getText().toString(),name.getText().toString(),
                    mobile.getText().toString(),email.getText().toString(),address.getText().toString(),
                   countrySpinner.getSelectedItem().toString(),password0.getText().toString(),password1.getText().toString());
        }
    }

    private void RegFunction(final String sponsor_id, final String name, final String mobile_no, final String email,
                             final String address, final String country, final String password, final String cPassword) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            Log.e(TAG, "Setting a new request queue");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "registration", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.e(TAG,"Response"+response);
                try {

                    final JSONObject object = new JSONObject(response.trim());
                    String status = object.getString("status");
                    if (status.equals("1")) {

                      //  Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();


                        SharedPreferences.Editor sp = getSharedPreferences("User", Context.MODE_PRIVATE).edit();
                        sp.clear();
                        sp.putString("user_id", object.getString("c_username"));
                        sp.commit();

                        final String username = object.getString("c_username");
                        Snackbar snackbar = Snackbar
                                .make(parentView, object.getString("message")+"\n"+
                                        "Username is "+object.getString("c_username"), Snackbar.LENGTH_LONG)
                                .setAction("COPY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
//                                        Snackbar snackbar1 = Snackbar.make(parentView, "Message is restored!", Snackbar.LENGTH_SHORT);
//                                        snackbar1.show();
                                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                        ClipData clip = ClipData.newPlainText("UserName", username);
                                        clipboard.setPrimaryClip(clip);

                                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                        finish();
                                    }
                                });

                        snackbar.show();




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
                map.put("sponsor_id", sponsor_id);
                map.put("name",name);
                map.put("email",email);
                map.put("address",address);
                map.put("mobile_no",mobile_no);
                map.put("c_country",country);
                map.put("C_PASSWORD",password);
                map.put("C_PASSWORD_CONFIRM",cPassword);
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }
    //********************************************************************************************************************************************************
    //********************************************************************************************************************************************************
}
