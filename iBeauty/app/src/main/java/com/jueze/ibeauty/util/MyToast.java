package com.jueze.ibeauty.util;
import android.widget.Toast;
import com.jueze.ibeauty.MyApplication;

public class MyToast {
    
    
    public static void ts(Object str){
        Toast.makeText(MyApplication.getContext(), String.valueOf(str), Toast.LENGTH_SHORT).show();
    }
   
}
