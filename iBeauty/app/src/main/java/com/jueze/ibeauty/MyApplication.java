package com.jueze.ibeauty;


import android.app.Application;
import android.content.Context;
import com.tencent.smtt.sdk.QbSdk;

public class MyApplication extends Application {

    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
		
		QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback(){

			@Override
			public void onCoreInitFinished() {
			}

			@Override
			public void onViewInitFinished(boolean p1) {
			}
		};
		//x5内核初始化
		QbSdk.initX5Environment(getApplicationContext(), cb);
		
    }
    
    public static Context getContext(){
        return mContext;
    }
    
}
