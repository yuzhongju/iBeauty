package com.jueze.ibeauty.util;
import android.content.Context;
import android.content.Intent;
import com.jueze.ibeauty.MyApplication;

public class MyIntent {
    private static Context mContext;
    public static void go(Class<?> cls){
        mContext = MyApplication.getContext();
        mContext.startActivity(new Intent(mContext, cls));
    }
}
