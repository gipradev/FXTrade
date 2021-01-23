package com.example.fxcaptrade;

import android.app.Application;
import android.util.Log;

import com.example.fxcaptrade.Utils.ConnectivityReceiver;

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        Log.e(TAG,"muApplication");

    }
    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

}
