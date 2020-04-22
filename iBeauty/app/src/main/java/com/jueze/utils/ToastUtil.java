package com.jueze.utils;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtil {

    public static void show(Context context,Object str) {
		
		int scale=(int)context.getResources().getDisplayMetrics().density;
		GradientDrawable gd =new GradientDrawable();
		gd.setCornerRadius(10);
		gd.setColor(Color.parseColor("#ffffff"));
		
		TextView tv = new TextView(context);
		tv.setBackground(gd);
		int padding = 8*scale;
		tv.setPadding(padding, padding, padding, padding);
		tv.setTextColor(Color.parseColor("#000000"));
		tv.setText(String.valueOf(str));
		tv.setTextSize(12);
		tv.setElevation(2*scale);
		
		Toast toast = new Toast(context);
		toast.setView(tv);
		toast.show();
    }

}
