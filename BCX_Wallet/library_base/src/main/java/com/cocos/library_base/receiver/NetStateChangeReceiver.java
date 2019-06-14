package com.cocos.library_base.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class NetStateChangeReceiver extends BroadcastReceiver {

    public NetStateChangeObserver netStateChangeObserver;

    public void setNetStateChangeObserver(NetStateChangeObserver netStateChangeObserver) {
        this.netStateChangeObserver = netStateChangeObserver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            NetworkType networkType = NetworkUtil.getNetworkType(context);
            if (netStateChangeObserver == null) {
                return;
            }
            if (networkType == NetworkType.NETWORK_NO) {
                netStateChangeObserver.onNetDisconnected();
            } else {
                netStateChangeObserver.onNetConnected();
            }
        }
    }

}