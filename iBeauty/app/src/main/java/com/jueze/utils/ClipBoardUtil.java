package com.jueze.utils;
import android.content.ClipboardManager;
import android.content.Context;

public class ClipBoardUtil {
    public static void write(Context context,Object str){
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(String.valueOf(str));
    }
}
