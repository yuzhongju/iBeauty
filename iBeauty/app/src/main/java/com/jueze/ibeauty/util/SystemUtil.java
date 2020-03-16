package com.jueze.ibeauty.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.os.Build;

public class SystemUtil {

    /**
     * sgf
     * 强制隐藏软键盘
     *
     * @param activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0); //强制隐藏键盘
    }

    /**
     * 调用系统方法 强制隐藏软键盘
     *
     * @param activity
     */
    public static void hideSystemSoftKeyboard(Activity activity) {
        ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
	
	public static boolean isM(){
		return Build.VERSION.SDK_INT>=Build.VERSION_CODES.M;
	}
}
