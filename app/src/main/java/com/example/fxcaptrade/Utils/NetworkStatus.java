package com.example.fxcaptrade.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetworkStatus {
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;


    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = NetworkStatus.getConnectivityStatus(context);
        String status = null;
        if (conn == NetworkStatus.TYPE_WIFI) {
            status = "Wifiis detected";
        } else if (conn == NetworkStatus.TYPE_MOBILE) {
            status = "Mobile data connection is detected";
        } else if (conn == NetworkStatus.TYPE_NOT_CONNECTED) {
            status = "Not connected to Internetconnection";
        }
        return status;
    }
}
