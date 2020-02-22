package com.jueze.ibeauty.dialog;

import android.app.ProgressDialog;
import android.content.Context;

public class MyProgressDialog extends ProgressDialog {
    
    private Context mContext;
    public MyProgressDialog(Context context){
        super(context);
        setMessage("加载中...");
        setCancelable(false);
    }
}
