package com.jueze.utils;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.text.TextUtils;

public class AppUtil {
	
    public static boolean isInstalled(Context context,String packageName){
		if(TextUtils.isEmpty(packageName)) return false;
		PackageInfo packageInfo = null;
		try{
			packageInfo =context.getPackageManager().getPackageInfo(packageName,0);
		}catch(Exception e){}
		if(packageInfo==null){
			return false;
		}else{
			return true;
		}
	}
}
