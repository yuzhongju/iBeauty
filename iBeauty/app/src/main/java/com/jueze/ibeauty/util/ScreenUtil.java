package com.jueze.ibeauty.util;
import android.app.Activity;
import com.jueze.ibeauty.MyApplication;
import android.view.WindowManager;
import android.util.DisplayMetrics;
import android.content.Context;

public class ScreenUtil {
    public static int getHeight(){
        return getDm().heightPixels;
    }
    
    public static int getWidth(){
        return getDm().widthPixels;
    }
    
    private static Context getContext(){
        return MyApplication.getContext();
    }
    
    private static DisplayMetrics getDm(){
        return getContext().getResources().getDisplayMetrics();
    }
}
