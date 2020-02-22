package com.jueze.ibeauty.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.app.Activity;

public class MyShare
{
    public static void share(Context context, String file)
    {
        try{
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(file));         
            context.startActivity(Intent.createChooser(intent, "分享到"));
        }catch(Exception e){}
    }

    public static void shareText(Context context, String text)
    {
        try{
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, text);
            context.startActivity(Intent.createChooser(intent, "分享到"));
        }catch(Exception e){}
    }
}
