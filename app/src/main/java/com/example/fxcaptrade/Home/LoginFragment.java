package com.example.fxcaptrade.Home;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fxcaptrade.Activities.ForgotPassword;
import com.example.fxcaptrade.DashBoard.DashBoardActivity;
import com.example.fxcaptrade.R;
import com.example.fxcaptrade.Utils.WebServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "LoginFragment";

    private RequestQueue requestQueue;
    private TextInputEditText userName,password;
    private FloatingActionButton loginButton;
    private TextView toLogin,forgot;
    private SharedPreferences user;
    private String username;
    private ProgressDialog dialog;
    private String user_id;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //changeNotificationShadeColor();
        declarations(view);
        getSharedPreference();

        loginButton.setOnClickListener( this);
        toLogin.setOnClickListener(this);
        forgot.setOnClickListener(this);
       // userName.setText(username);
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    private void changeNotificationShadeColor() {


        Window window = getActivity().getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.rgb(188, 204, 48));
        }
    }

    private void declarations(View view) {

        requestQueue = Volley.newRequestQueue(getActivity());
        userName = (TextInputEditText) view.findViewById(R.id.email);
        password = (TextInputEditText) view.findViewById(R.id.pass);
        loginButton = (FloatingActionButton) view.findViewById(R.id.floating_action_button);
        toLogin = (TextView) view.findViewById(R.id.toReg);
        forgot = (TextView) view.findViewById(R.id.forgot);


    }

    private void loginFunction(final String username, final String password) {
        dialog = ProgressDialog.show(getContext(), "Loading", "Please wait...", true);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getActivity());
            Log.e(TAG, "Setting a new request queue");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "Login", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               Log.e(TAG,"Response"+response);
                try {

                    JSONObject object = new JSONObject(response.trim());
                    String status = object.getString("status");

                    if (status.equals("1")) {

                        dialog.dismiss();

                      //  Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor sp = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE).edit();
                        sp.clear();
                        sp.putString("user_id", object.getString("user_id"));
                        sp.commit();

                        generateToken(object.getString("user_id"));

                        Log.e(TAG,"here");

                        getActivity().startActivity(new Intent(getActivity(),DashBoardActivity.class));
                        getActivity().finish();


                    } else {
                        dialog.dismiss();

                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "Exception" + e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();
                Toast.makeText(getActivity(), "Please try after some time!.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "VolleyError" + error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("username", username);
                map.put("password",password);
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.floating_action_button:
                validate();
                break;
            case R.id.toReg:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, new RegistrationFragment()).addToBackStack(null).commit();
                break;
            case R.id.forgot:

              CustomDialogClass cdd=new CustomDialogClass(getActivity());
              cdd.show();
              //  setOtp(usernameForgot.getText().toString());

                break;
        }
    }


    private void validate() {
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


    private void generateToken(final String userid) {

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(getActivity(),
                new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                Log.e(TAG, "FCM Token\n" + token);

                if (!token.equals("")) {
                    sendToServer(userid, token);
                }

                SharedPreferences.Editor fcnShared = getActivity().getSharedPreferences("FCM", Context.MODE_PRIVATE).edit();
                fcnShared.clear();
                fcnShared.putString("kitchenToken", token);
                fcnShared.commit();

            }
        });

    }

    private void sendToServer(final String userid, final String token) {

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getActivity());
            Log.e(TAG, "Setting a new request queue");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "Save_Token", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.e(TAG,"Response"+response);
                try {

                    JSONObject object = new JSONObject(response.trim());
                    String status = object.getString("status");

                    if (status.equals("1")) {



                    } else {

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
                map.put("login_id", userid);
                map.put("token_id", token);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void getSharedPreference() {

        user = getContext().getSharedPreferences("User", getContext().MODE_PRIVATE);
        user_id = user.getString("user_id","0");


    }



    public class CustomDialogClass extends Dialog  {

        public Activity activity;
        public Dialog d;
        public Button yes, no;
        private TextView message;
        private TextInputEditText usernameForgot;
        private RelativeLayout sendForgot;
        private ProgressBar progressBar;
        private TextView textSend;

        public CustomDialogClass(Activity activity) {
            super(activity);
            // TODO Auto-generated constructor stub
            this.activity = activity;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.popup_package_list);

            usernameForgot = (TextInputEditText) findViewById(R.id.userNameForgot);
            sendForgot = (RelativeLayout) findViewById(R.id.fabSend);

            progressBar = (ProgressBar) findViewById(R.id.progressLoad);
            textSend = (TextView) findViewById(R.id.textSend);

            sendForgot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String username = usernameForgot.getText().toString();

                    setOtp(username);
                }
            });

        }






        private void setOtp(final String usernameForgot) {

            textSend.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(getActivity());
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

                            progressBar.setVisibility(View.GONE);

                            dismiss();

                            Intent intent = new Intent(getActivity(),ForgotPassword.class);
                            intent.putExtra("username",usernameForgot);
                            startActivity(intent);
                          //  getContext().startActivity(new Intent(getContext(), ForgotPassword.class));


                        } else {

                            progressBar.setVisibility(View.GONE);
                            textSend.setVisibility(View.VISIBLE);
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

                    progressBar.setVisibility(View.GONE);
                    textSend.setVisibility(View.VISIBLE);

                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("user_name", usernameForgot);
                    Log.e(TAG,"Map   "+map);
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


}
