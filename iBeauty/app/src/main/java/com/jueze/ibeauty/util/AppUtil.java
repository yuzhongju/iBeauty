package com.jueze.ibeauty.util;
import android.text.TextUtils;
import android.content.pm.PackageInfo;
import com.jueze.ibeauty.MyApplication;

public class AppUtil {
	
    public static boolean isInstalled(String packageName){
		if(TextUtils.isEmpty(packageName)) return false;
		PackageInfo packageInfo = null;
		try{
			packageInfo = MyApplication.getContext().getPackageManager().getPackageInfo(packageName,0);
		}catch(Exception e){}
		if(packageInfo==null){
			return false;
		}else{
			return true;
		}
	}
}
