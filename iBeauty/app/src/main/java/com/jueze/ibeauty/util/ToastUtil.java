package com.jueze.ibeauty.util;
import android.widget.Toast;
import com.jueze.ibeauty.MyApplication;
import android.content.Context;
import android.widget.TextView;
import android.graphics.Color;

public class ToastUtil {
    
    private static Context getContext(){
		return MyApplication.getContext();
	}
	
    public static void show(Object str){
		TextView tv = new TextView(getContext());
		int padding = DisplayUtil.dip2px(8);
		tv.setPadding(padding,padding,padding,padding);
		tv.setTextColor(Color.parseColor("#000000"));
		ShapeUtil.set(tv,10,10,10,10,"#ffffff");
		tv.setText(String.valueOf(str));
		tv.setTextSize(12);
		tv.setElevation(DisplayUtil.dip2px(2));
		
		Toast toast = new Toast(getContext());
		toast.setView(tv);
		toast.show();
    }
   
}
