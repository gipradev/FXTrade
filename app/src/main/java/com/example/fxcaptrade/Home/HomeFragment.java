package com.example.fxcaptrade.Home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fxcaptrade.Activities.EmailVerification;
import com.example.fxcaptrade.R;
import com.example.fxcaptrade.Utils.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private SharedPreferences user;
    private String user_id;
    private RequestQueue requestQueue;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requestQueue = Volley.newRequestQueue(getContext());
        getSharedPreference();


        if (!user_id.equals("0")){
            registrationStatus();
        }else {

        }

    }

    private void registrationStatus() {

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getContext());
            Log.e(TAG, "Setting a new request queue");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "Otp_verification_checking", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG,"Response"+response);
                try {

                    JSONObject object = new JSONObject(response.trim());
                    String status = object.getString("status");

                    if (status.equals("1")) {


                        // Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();



                    } else {

                        alertDialogue();


                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
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
                map.put("user_id", user_id);

                return map;
            }
        };
        requestQueue.add(stringRequest);

    }


    private void getSharedPreference() {

        user = getActivity().getSharedPreferences("User", getActivity().MODE_PRIVATE);
        user_id = user.getString("user_id", "0");
        Log.e(TAG, user_id);

    }


    private void alertDialogue() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm dialog");
        builder.setMessage("Do you what to Complete your Registration ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                startActivity(new Intent(getContext(), EmailVerification.class));


            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }




}
