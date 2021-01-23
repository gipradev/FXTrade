package com.example.fxcaptrade.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.fxcaptrade.Home.HomeActivity;
import com.example.fxcaptrade.R;
import com.wang.avi.AVLoadingIndicatorView;
import java.util.Date;
import java.util.List;


public class SplashScreen extends AppCompatActivity {
    private static final String TAG = "SplashScreen";

    private static int SPLASH_TIME = 5000;
    private SharedPreferences user;
    private String username,installedDate;
    private RequestQueue requestQueue;
    private AVLoadingIndicatorView avi;
    private ImageView image;
    private TextView headerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


       // setStatusBarGradient(this);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.rgb(33, 24, 81));
        }

        if (AppCompatDelegate.getDefaultNightMode()== AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.AppTheme);
        }else {

        }


        Uri uri = getIntent().getData();
        if (uri != null) {
            List<String> params = uri.getPathSegments();
            Log.e(TAG, "" + params);
            if (params.size() > 0) {
                String id = params.get(params.size() - 1);
                Log.e(TAG, "ID" + id);

                SharedPreferences.Editor sp = getApplicationContext().getSharedPreferences("User", Context.MODE_PRIVATE).edit();
                sp.putString("sponsor_id",id);
                sp.commit();
                Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
            }

        }






        user = getApplicationContext().getSharedPreferences("User", getApplicationContext().MODE_PRIVATE);
        username = user.getString("user_id","0");
        installedDate = user.getString("iDate","0");
        Log.e(TAG,username);

        if (installedDate.equals("0")){
            getInstalledDate(getApplicationContext());

        }
        Log.e(TAG,installedDate);

        avi = (AVLoadingIndicatorView) findViewById(R.id.loader);
        image = (ImageView) findViewById(R.id.imageView);
        headerText = (TextView) findViewById(R.id.header);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layoutPowered);

        Animation lToRAnime = AnimationUtils.loadAnimation(this,R.anim.side_anim);
        Animation rToLAnime = AnimationUtils.loadAnimation(this,R.anim.opposite_side);
        Animation bAnime = AnimationUtils.loadAnimation(this,R.anim.bottom_anim);

        image.setAnimation(lToRAnime);
        headerText.setAnimation(rToLAnime);
        layout.setAnimation(bAnime);

        startAnim();


        requestQueue = Volley.newRequestQueue(getApplicationContext());
        new Handler().postDelayed(new Runnable() {
            @Override

            public void run() {

                Intent intent  = new Intent(getApplicationContext(), HomeActivity.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View,String> (image,"imageLogo");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this,pairs);
                    startActivity(intent,options.toBundle());
                }
                finish();
                //loginCheck(username);
            }
        },SPLASH_TIME);
    }

    private void getInstalledDate(Context applicationContext) {
        long installedTime = 0;
        try {
            installedTime = getApplicationContext().getPackageManager()
                    .getPackageInfo(getApplicationContext().getPackageName(),0)
                    .firstInstallTime;


            // Format the date time
            String dateString = DateFormat
                    .format("yyy/MM/dd", new Date(installedTime))
                    .toString();

            // Get the time difference between now and app first install time
            String timeDifference = getTimeDifference(new Date(installedTime));


            SharedPreferences.Editor sp = getApplicationContext().
                    getSharedPreferences("User", Context.MODE_PRIVATE).edit();
            sp.putString("iDate",dateString);
            sp.commit();
//
//            // Display the app install date time on text view
          Log.e(TAG,"First Install Time\n"+dateString);
//
//            // Display app first install time ago
//            Log.e(TAG,"\n\n Time ago\n"+timeDifference);

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG,"Exc "+e);
        }
    }



//    public static void setStatusBarGradient(Activity activity) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = activity.getWindow();
//            Drawable background = activity.getResources().getDrawable(R.drawable.ic_fx_factory_bg);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
//            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
//            window.setBackgroundDrawable(background);
//        }
//    }

    protected static int getSecondsDifference(Date then){
        Date now = new Date(System.currentTimeMillis());
        long dateDiff = now.getTime() - then.getTime();
        return (int) dateDiff/1000; // 1000 milliseconds = 1 second
    }

    // Custom method to get seconds to readable time
    protected static String getReadableTime(int seconds){
        String readableTime = "";

        int hours = (int) seconds / 3600;
        int remainder = (int) seconds - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        if(hours>0){
            readableTime = hours + " hour ";
        }
        if(mins>0){
            readableTime += mins + " min ";
        }
        if (secs>0){
            readableTime += secs + " sec";
        }
        return readableTime;
    }

    // Custom method to get readable time difference between two date objects
    protected static String getTimeDifference(Date then){
        return getReadableTime(getSecondsDifference(then));
    }



    void startAnim(){
        // avi.show();
        avi.smoothToShow();
    }

    void stopAnim(){
        //avi.hide();
        avi.smoothToHide();
    }
}
