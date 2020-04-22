package com.jueze.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {
    
    public static int state(Context context){
        int type = -1;
        NetworkInfo info = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info == null) {
        	type = 0;
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            type = 1;
        } else if(info.getType() == ConnectivityManager.TYPE_WIFI){
            type = 2;
        }
        return type;
    }
}
