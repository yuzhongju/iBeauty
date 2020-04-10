package com.jueze.ibeauty.util;
import android.text.TextUtils;
import android.content.pm.PackageInfo;
import com.jueze.ibeauty.MyApplication;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

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
	
	public static int getVersionCode(){
		int version = 0;
		PackageManager pm = MyApplication.getContext().getPackageManager();
		try {
			PackageInfo info=pm.getPackageInfo(MyApplication.getContext().getPackageName(), 0);
			version= info.versionCode;
		} catch (PackageManager.NameNotFoundException e) {}

		return version;
	}
}
