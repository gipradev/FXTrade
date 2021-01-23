package com.example.fxcaptrade.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ConnectivityDetection extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String status = NetworkStatus.getConnectivityStatusString(context);
        Toast.makeText(context, status, Toast.LENGTH_LONG).show();

    }
}
