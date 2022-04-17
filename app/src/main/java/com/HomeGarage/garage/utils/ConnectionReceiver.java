package com.HomeGarage.garage.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ConnectionReceiver extends BroadcastReceiver {
    public static ReceiverListener Listener;

    public interface ReceiverListener {
        void onNetworkChange(boolean isConnected);
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        String status = NetworkUtil.getConnectivityStatusString(context);
        if(status == null || status.equals("0"))
            Listener.onNetworkChange(false);
        else Listener.onNetworkChange(true);
    }
}