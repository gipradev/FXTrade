package com.example.fxcaptrade.DashBoard;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import com.example.fxcaptrade.Activities.TestimonyActivity;
import com.example.fxcaptrade.Home.HomeActivity;

import com.example.fxcaptrade.R;
import com.example.fxcaptrade.Utils.ConnectivityReceiver;
import com.example.fxcaptrade.Utils.WebServices;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

public class    DashBoardActivity extends AppCompatActivity  implements
        ConnectivityReceiver.ConnectivityReceiverListener {
    private static final String TAG = "DashBoardActivity";

    private final static int ID_DASHBOARD = 1;
    private final static int ID_INVESTMENT = 2;
    private final static int ID_TEAM = 3;
    private final static int ID_PROFILE = 4;
    private final static int ID_WALLET = 5;
    private static final int REQUSTCODE = 500;
    private MeowBottomNavigation bottomNavigation;
    private SharedPreferences user;
    private String user_id;
    private RequestQueue requestQueue;
    private ProgressDialog dialog;
    private String android_id;



    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = findViewById(R.id.toolbarDashBord);
        setSupportActionBar(toolbar);


        if (AppCompatDelegate.getDefaultNightMode()== AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.AppTheme);
        }else {

        }



        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.rgb(33, 24, 81));
        }

        toolbar.setNavigationIcon(R.drawable.ic_logo_icon);
        getSupportActionBar().setTitle("Dashboard");

        bottomNavigation = (MeowBottomNavigation) findViewById(R.id.bottomNavigation);

        bottomNavigation.add(new MeowBottomNavigation.Model(ID_PROFILE, R.drawable.ic_user));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_INVESTMENT, R.drawable.ic_investment));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_DASHBOARD, R.drawable.ic_dashboard));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_TEAM, R.drawable.ic_team));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_WALLET, R.drawable.ic_wallet));

        bottomNavigation.show(ID_DASHBOARD, true);

        user = getApplicationContext().getSharedPreferences("User", getApplicationContext().MODE_PRIVATE);
        user_id = user.getString("user_id", "0");
        requestQueue = Volley.newRequestQueue(getApplicationContext());


        viewFragment(new DashboardFragment(), "HOME");
        getSupportActionBar().setTitle("Dashboard");


        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getIcon()) {

                    case R.drawable.ic_dashboard:

                        viewFragment(new DashboardFragment(), "HOME");
                        getSupportActionBar().setTitle("Dashboard");
                        break;

                    case R.drawable.ic_investment:

                        getSupportActionBar().setTitle("Investment");
                        viewFragment(new InvestmentFragment(), "OTHER");
                        break;

                    case R.drawable.ic_team:

                        viewFragment(new MyTeamFragment(), "OTHER");
                        getSupportActionBar().setTitle("My Team");
                        break;

                    case R.drawable.ic_user:

                        viewFragment(new ProfileFragment(), "OTHER");
                        getSupportActionBar().setTitle("Profile");
                        break;

                    case R.drawable.ic_wallet:

                        viewFragment(new WalletFragment(), "OTHER");
                        getSupportActionBar().setTitle("My Wallet");
                        break;

                }

                return null;
            }
        });

        bottomNavigation.setOnReselectListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                Log.e("Reselect", "");
                return null;
            }
        });

        bottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                Log.e("On Show", "");

                return null;
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                alertDialogue(getApplicationContext());

                return true;
            case R.id.notification:
                // showHelp();
                return true;
            case R.id.testimony:
                startActivity(new Intent(getApplicationContext(), TestimonyActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void functionLogout(final String user_id) {
        dialog = ProgressDialog.show(this, "Loading", "Please wait...", true);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            Log.e(TAG, "Setting a new request queue");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "Logout", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response" + response);

                try {

                    JSONObject object = new JSONObject(response.trim());
                    String status = object.getString("status");
                    if (status.equals("1")) {

                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();

                    } else {

                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
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

    private void viewFragment(Fragment fragment, final String name) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerFragment, fragment);
        // 1. Know how many fragments there are in the stack
        final int count = fragmentManager.getBackStackEntryCount();
        // 2. If the fragment is **not** "home type", save it to the stack
        if (name.equals("OTHER")) {
            fragmentTransaction.addToBackStack(name);
        } else {
            // Log.e(TAG, "Else");
        }
        // Commit !
        fragmentTransaction.commit();
        // 3. After the commit, if the fragment is not an "home type" the back stack is changed, triggering the
        // OnBackStackChanged callback
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                // If the stack decreases it means I clicked the back button
                if (fragmentManager.getBackStackEntryCount() <= count) {
                    // pop all the fragment and remove the listener
                    fragmentManager.popBackStack("OTHER", POP_BACK_STACK_INCLUSIVE);
                    fragmentManager.removeOnBackStackChangedListener(this);
                    // set the home button selected
                    bottomNavigation.show(ID_DASHBOARD, true);
                    //Set App bar title
                    getSupportActionBar().setTitle("Dashboard");

                }
            }
        });
    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        Log.e(TAG, "OnResume Called");
//        // register connection status listener
//        MyApplication.getInstance().setConnectivityListener(this);
//
//    }




    @Override
    public void onNetworkConnectionChanged(Boolean isConnected) {
        Log.e(TAG,"Interface Called");
        //showSnack(isConnected);
    }


    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {

        Log.e(TAG,"snackBar Called");
        String message;
        int color;
        if (isConnected) {
            message = "You are in Online";
            color = Color.WHITE;
        } else {
            message = "You are in Offline";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);

//        View sbView = snackbar.getView();
//        TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
//        textView.setTextColor(color);
        snackbar.show();
    }

    private void checkConnection() {
        Log.e(TAG,"called Check");
        boolean isConnected = ConnectivityReceiver.isConnected();
        //showSnack(isConnected);
    }

    private void alertDialogue(Context applicationContext) {

        AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardActivity.this);
        builder.setTitle("Confirm dialog");
        builder.setMessage("Do you what to Logout  ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                functionLogout(user_id);

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
