package com.jueze.ibeauty.util;
import android.content.Context;
import android.os.Environment;
import com.jueze.ibeauty.MyApplication;
import com.jueze.utils.FileUtil;

public class CacheUtil {
    public static void clear(){
        Context context = MyApplication.getContext();
        try{
            FileUtil.deleteDir(context.getCacheDir().getPath());
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                FileUtil.deleteDir(context.getExternalCacheDir().getPath());
            }
        }catch(Exception e){}   
    }
    
    public static void delete(String filename){
        Context context = MyApplication.getContext();
        try{
            FileUtil.deleteDir(context.getCacheDir()+"/"+filename);
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                FileUtil.deleteDir(context.getExternalCacheDir()+"/"+filename);
            }
        }catch(Exception e){}   
    }
}
