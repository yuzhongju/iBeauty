package com.jueze.utils;
import android.content.Context;

public class DisplayUtil {
    public static int dip2px(Context context,float value){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(value * scale);
    }
}
