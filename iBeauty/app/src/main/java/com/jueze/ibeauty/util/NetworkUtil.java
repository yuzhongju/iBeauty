package com.jueze.ibeauty.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.jueze.ibeauty.MyApplication;

public class NetworkUtil {
    
    public static int state(){

        int type = -1;
        NetworkInfo info = ((ConnectivityManager) MyApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
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
