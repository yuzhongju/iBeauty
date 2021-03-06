package com.jueze.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

public class PermissionUtil {

	public static final int REQUEST_READ_EXTERNAL_STORAGE = 1;
    public static void requestReadPermission(Activity activity) {
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
			try {
				//检测是否有写的权限
				int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
				String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
				if (permission != PackageManager.PERMISSION_GRANTED) {
					// 没有写的权限，去申请写的权限，会弹出对话框
					ActivityCompat.requestPermissions(activity, permissions, REQUEST_READ_EXTERNAL_STORAGE);
				}
			} catch (Exception e) {}
		}
    }
    
}
