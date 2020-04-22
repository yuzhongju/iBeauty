package com.jueze.utils;
import android.content.Context;
import android.util.DisplayMetrics;

public class ScreenUtil {
    public static int getHeight(Context context){
        return getDm(context).heightPixels;
    }
    
    public static int getWidth(Context context){
        return getDm(context).widthPixels;
    }
    
    private static DisplayMetrics getDm(Context context){
        return context.getResources().getDisplayMetrics();
    }
}
