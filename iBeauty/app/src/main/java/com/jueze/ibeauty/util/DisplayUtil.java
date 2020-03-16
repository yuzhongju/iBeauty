package com.jueze.ibeauty.util;
import com.jueze.ibeauty.MyApplication;
import android.content.Context;

public class DisplayUtil {
	
	private static Context getContext(){
		return MyApplication.getContext();
	}
	
    public static int dip2px(float value){
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int)(value * scale + 0.5f);
    }
    
	
}
