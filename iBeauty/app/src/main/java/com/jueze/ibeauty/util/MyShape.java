package com.jueze.ibeauty.util;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

public class MyShape
{

    public static void set(Drawable dw, String color){
        try{
            dw.setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_ATOP);
        }
        catch(Exception e){}

    }

    //无边框
    public static void set(View view, int c1, int c2, int c3, int c4, String color){
        try{
            GradientDrawable gd = new GradientDrawable();
            gd.setShape(GradientDrawable.RECTANGLE);
            gd.setCornerRadii(new float[]{c1, c1, c2, c2, c3, c3, c4, c4});
            gd.setColor(Color.parseColor(color));
            gd.setStroke(0, Color.parseColor("#ffffff"));
            view.setBackgroundDrawable(gd);          
        }
        catch(Exception e){}
    }

    //有边框
    public static void set(View view, int c1, int c2, int c3, int c4, int stroke, String color, String strokeColor){
        try{
            GradientDrawable gd = new GradientDrawable();
            gd.setShape(GradientDrawable.RECTANGLE);
            gd.setCornerRadii(new float[]{c1, c1, c2, c2, c3, c3, c4, c4});
            gd.setColor(Color.parseColor(color));
            gd.setStroke(stroke, Color.parseColor(strokeColor));
            view.setBackgroundDrawable(gd);
            
        }
        catch(Exception e){}
    }
}

