package com.jueze.ibeauty.util;
import android.content.ClipboardManager;
import com.jueze.ibeauty.MyApplication;

public class MyClipBoard {
    public static void write(CharSequence str){
        ClipboardManager cm = (ClipboardManager) MyApplication.getContext().getSystemService(MyApplication.getContext().CLIPBOARD_SERVICE);
        cm.setText(str);
    }
}
