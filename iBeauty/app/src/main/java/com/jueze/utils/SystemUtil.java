package com.jueze.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.inputmethod.InputMethodManager;

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

	public static boolean isM(){
		return Build.VERSION.SDK_INT>=Build.VERSION_CODES.M;
	}
}
