package com.jueze.ibeauty.util;
import com.jueze.ibeauty.MyApplication;
import android.content.Context;

public class dip2px {
    public static int to(float value){
        Context context = MyApplication.getContext();
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(value * scale + 0.5f);
    }
    
}
