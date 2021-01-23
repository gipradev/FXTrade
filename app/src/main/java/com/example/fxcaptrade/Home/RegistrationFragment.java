package com.example.fxcaptrade.Home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
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
import com.example.fxcaptrade.Activities.EmailVerification;
import com.example.fxcaptrade.R;
import com.example.fxcaptrade.RecyclerAdaptor.CountryListAdaptor;
import com.example.fxcaptrade.Utils.WebServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationFragment extends Fragment implements CountryListAdaptor.CountryAdaptorListener {
    private static final String TAG = "RegistrationFragment";

    private RequestQueue requestQueue;

    private TextInputEditText name, displayName, mobile, sponsorID, countryCode;
    private TextInputEditText password0, password1, countryName;
    private Spinner countrySpinner;
    private TextView email;
    private View parentView;
    private FloatingActionButton regButton;
    private ProgressDialog dialog;
    private String countryID;
    private SharedPreferences user;
    private String sponsorUserId,installedDate;
    private String sponsorData;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;

    private RecyclerView recycleCountry;
    private NavigationView navigationView;
    private androidx.appcompat.widget.SearchView searchCountry;
    private CountryListAdaptor listAdaptor;
    private LinearLayoutManager linearLayoutManager;
    private ProgressBar progress;
    String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    Matcher matcher ;
    private ScrollView scrollView;


    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.test_file, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        user = getActivity().getSharedPreferences("User", getActivity().MODE_PRIVATE);
        sponsorUserId = user.getString("sponsor_id", "0");
        installedDate = user.getString("iDate","0");

        if (sponsorUserId.equals("0")) {

            sponsorData = "MAIN";

        } else {
            sponsorData = decodeBase64(sponsorUserId);
        }
        //changeNotificationShadeColor();
        declarations(view);
        // getCountry();

        mDrawer = (DrawerLayout) view.findViewById(R.id.drawer_registration);
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(), mDrawer, R.string.drawer_open, R.string.drawer_close);

        navigationView = (NavigationView) view.findViewById(R.id.navigation_search);

        View headerview = navigationView.getHeaderView(0);

        searchCountry = (androidx.appcompat.widget.SearchView) headerview.findViewById(R.id.searchView);
        recycleCountry = (RecyclerView) headerview.findViewById(R.id.countryList);
        progress = (ProgressBar) headerview.findViewById(R.id.progressSearch);


        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e(TAG, "here");
                validation();
            }
        });


        countryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.openDrawer(GravityCompat.END);
                getCountry();
            }
        });



        searchCountry.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e(TAG, "search " + newText);
                getCountryName(newText);
                return false;
            }
        });

        final Pattern pattern = Pattern.compile(regex);

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String stringEmail = s.toString();
                String [] split = stringEmail.split("@");

                validateEmail(s,email);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkCount(s.toString(),mobile);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void checkCount(String toString, TextInputEditText mobile) {
        if (toString.length()<10 || toString.length()>10){
            mobile.setError("Invalid");
        }
        else {
            mobile.setError(null);
        }
    }

    private void validateEmail(CharSequence email, TextView emailView) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+" +
                "(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";



        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        if(!matcher.matches()){
            emailView.setError("Invalid");
        }
    }

    private void changeNotificationShadeColor() {

        Window window = getActivity().getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.rgb(0, 172, 193));
        }
    }


    private void declarations(View view) {

        requestQueue = Volley.newRequestQueue(getContext());
        parentView = view;
        sponsorID = (TextInputEditText) view.findViewById(R.id.spID);
        name = (TextInputEditText) view.findViewById(R.id.name);
        displayName = (TextInputEditText) view.findViewById(R.id.displayName);
        mobile = (TextInputEditText) view.findViewById(R.id.mob);
        countryCode = (TextInputEditText) view.findViewById(R.id.countryCode);
        email = (TextInputEditText) view.findViewById(R.id.email);
        password0 = (TextInputEditText) view.findViewById(R.id.password0);
        password1 = (TextInputEditText) view.findViewById(R.id.password1);
        countryName = (TextInputEditText) view.findViewById(R.id.countryName);

        scrollView = (ScrollView) view.findViewById(R.id.scrollVIewReg);


        countryCode.setFocusable(false);

        countrySpinner = (Spinner) view.findViewById(R.id.country);
        regButton = (FloatingActionButton) view.findViewById(R.id.fabreg);


        if (sponsorUserId.equals("0")) {
            sponsorID.setText("MAIN");
        } else {
            sponsorID.setText(sponsorUserId);
        }

    }


    //********************************************************************************************************************************************************


    private void getCountryName(final String key) {
       progress.setVisibility(View.VISIBLE);
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getContext());
            Log.e(TAG, "Setting a new request queue");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "Country_list_name", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response" + response);
                try {

                    JSONObject object = new JSONObject(response.trim());
                    String status = object.getString("status");
                    if (status.equals("1")) {

                        progress.setVisibility(View.INVISIBLE);
                    //    dialog.dismiss();

                        JSONArray array = object.getJSONArray("data");

                        generateList(array);



                    } else {
                       // dialog.dismiss();

                        progress.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "Exception" + e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // dialog.dismiss();
                progress.setVisibility(View.INVISIBLE);
                Log.e(TAG, "VolleyError1" + error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("search_key", key);
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void generateList(JSONArray array) {

        listAdaptor = new CountryListAdaptor(getContext(), array,this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleCountry.setHasFixedSize(true);
        recycleCountry.setLayoutManager(linearLayoutManager);
        recycleCountry.setAdapter(listAdaptor);
    }


    //********************************************************************************************************************************************************


    private void getCountry() {
        progress.setVisibility(View.VISIBLE);
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getContext());
            Log.e(TAG, "Setting a new request queue");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "Country_list", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //    Log.e(TAG,"Response"+response);
                try {

                    JSONObject object = new JSONObject(response.trim());
                    String status = object.getString("status");
                    if (status.equals("1")) {

                        progress.setVisibility(View.INVISIBLE);

                        JSONArray array = object.getJSONArray("data");

                        generateList(array);




                    } else {

                        progress.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "Exception" + e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "Tre after sometimes...", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "VolleyError1" + error);

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


    private void getCountryCode(final String countryID) {

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getContext());
            Log.e(TAG, "Setting a new request queue");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "Country_code", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response" + response);
                try {

                    JSONObject object = new JSONObject(response.trim());
                    String status = object.getString("status");
                    if (status.equals("1")) {

                        //dialog.dismiss();

                        String code = object.getString("country_code");

                        countryCode.setText("+ "+code);
                        countryCode.setError(null);

                    } else {

                        Toast.makeText(getContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "Exception" + e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "VolleyError2" + error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("country_id", countryID);
                Log.e(TAG, map + "");
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }
    //********************************************************************************************************************************************************


    private void validation() {

        boolean stat = false;
        if (name.getText().toString().isEmpty()) {
            name.setError("Name is required");
            name.requestFocus();
            stat = true;
        }
        else if (displayName.getText().toString().isEmpty()) {
            displayName.setError("Display Name is required");
            displayName.requestFocus();
            stat = true;
        }

        else if (countryName.getText().toString().isEmpty()) {
            countryName.setError("Country is required");
            countryName.requestFocus();
            stat = true;
        }

        else if (countryCode.getText().toString().isEmpty()) {
            countryCode.setError("Country Code is required");
            countryCode.requestFocus();
            stat = true;
        }

        else if (mobile.getText().toString().isEmpty()) {
            mobile.setError("Mobile number is required");
            mobile.requestFocus();
            stat = true;
        }
        else if (mobile.getText().toString().length()<10 || mobile.getText().toString().length()>10) {
            mobile.setError("Invalid");
            mobile.requestFocus();
            stat = true;
        }

        else if (email.getText().toString().isEmpty()) {
            email.setError("Email is required");
            email.requestFocus();
            stat = true;
        }

        else if (password0.getText().toString().isEmpty()) {
            password0.setError("Required");
            password0.requestFocus();
            stat = true;
        }
        else if (password0.getText().toString().length() <8  ||  password0.getText().toString().length() >15) {
           password0.setError( "Password length must be 8 to 15 ");
            password0.requestFocus();
            stat = true;
        }
        else if (password1.getText().toString().isEmpty()) {
            password1.setError("Required");
            password1.requestFocus();
            stat = true;
        }
        else if (password1.getText().toString().length() <8  ||  password1.getText().toString().length() >15) {
            password1.setError( "Password length must be 8 to 15 ");
            password1.requestFocus();
            stat = true;
        }

        else if (!password0.getText().toString().equals(password1.getText().toString())){
            Toast.makeText(getContext(), "Password mismatch", Toast.LENGTH_LONG).show();
        }

        else if (!stat) {
            Log.e(TAG, "false");
            RegFunction(sponsorData, name.getText().toString(), displayName.getText().toString(),
                    mobile.getText().toString(), email.getText().toString(), countryCode.getText().toString(),
                    countryID, password0.getText().toString(), password1.getText().toString());
        }
        else if (stat){
            Toast.makeText(getContext(), "All fields are mandatory", Toast.LENGTH_LONG).show();
        }
    }

    private void RegFunction(final String sponsor_id, final String name, final String displayName, final String mobile_no, final String email,
                             final String countryCode, final String country, final String password, final String cPassword) {

        dialog = ProgressDialog.show(getContext(), "Loading", "Please wait...", true);


        Log.e(TAG, "REg");
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getContext());
            Log.e(TAG, "Setting a new request queue");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "Registration", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response" + response);
                try {

                    final JSONObject object = new JSONObject(response.trim());
                    String status = object.getString("status");
                    if (status.equals("1")) {

                        dialog.dismiss();

                        Toast.makeText(getContext(), object.getString("message"), Toast.LENGTH_SHORT).show();


                        SharedPreferences.Editor sp = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE).edit();
                        sp.clear();
                        sp.putString("user_id", object.getString("user_id"));
                        sp.commit();

                        generateUserToken(object.getString("user_id"));

                        getActivity().startActivity(new Intent(getActivity(), EmailVerification.class));


                    } else {
                        dialog.dismiss();

                        Toast.makeText(getContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "Exception" + e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getContext(), "Please try again later..", Toast.LENGTH_LONG).show();
                Log.e(TAG, "VolleyError3" + error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("sponsor_id", sponsor_id);
                map.put("name", name);
                map.put("display_name", displayName);
                map.put("email", email);
                map.put("mobile_no", mobile_no);
                map.put("c_country", country);
                map.put("install_date", installedDate);
                map.put("country_code", countryCode);
                map.put("C_PASSWORD", password);
                map.put("C_PASSWORD2", cPassword);


                Log.e(TAG, "Values" + map + "");
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }
    //********************************************************************************************************************************************************

    private void generateUserToken(final String user_id) {

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(getActivity(),
                new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        String token = instanceIdResult.getToken();
                        Log.e(TAG, "FCM Token\n" + token);


                        sendToServer(user_id, token);


                    }
                });
    }


    //********************************************************************************************************************************************************


    private void sendToServer(final String user_id, final String token) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "Save_Token", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response" + response);
                try {
                    JSONObject object = new JSONObject(response.trim());

                    if (object.getString("status").equals("1")) {


                        SharedPreferences.Editor fcnShared = getActivity().getSharedPreferences("FCM", Context.MODE_PRIVATE).edit();
                        fcnShared.clear();
                        fcnShared.putString("userToken_id", token);
                        fcnShared.commit();

                    }

                } catch (JSONException e) {
                    Log.e(TAG, "Exception" + e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "VolleyError4" + error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("login_id", user_id);
                map.put("token_id", token);
                return map;
            }

        };
        requestQueue.add(stringRequest);

    }

    private String decodeBase64(String coded) {
        byte[] valueDecoded = new byte[0];
        try {
            valueDecoded = Base64.decode(coded.getBytes("UTF-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
        }
        return new String(valueDecoded);
    }


    @Override
    public void onCountryClick(String Name, String id) {

        countryName.setText(Name);
        countryName.setError(null);
        countryID = id;
        countryName.setFocusable(false);
        countryName.setClickable(true);
        mDrawer.closeDrawer(GravityCompat.END);
        getCountryCode(id);
    }
}


