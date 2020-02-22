package com.jueze.ibeauty.util;
import android.content.Context;
import android.os.Environment;
import java.io.File;
import com.jueze.ibeauty.MyApplication;

public class CacheClean {
    public static void clear(){
        Context context = MyApplication.getContext();
        try{
            FileHelper.deleteDir(context.getCacheDir().getPath());
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                FileHelper.deleteDir(context.getExternalCacheDir().getPath());
            }
        }catch(Exception e){}   
    }
    
    public static void clear(String filename){
        Context context = MyApplication.getContext();
        try{
            FileHelper.deleteDir(context.getCacheDir()+"/"+filename);
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                FileHelper.deleteDir(context.getExternalCacheDir()+"/"+filename);
            }
        }catch(Exception e){}   
    }
}
