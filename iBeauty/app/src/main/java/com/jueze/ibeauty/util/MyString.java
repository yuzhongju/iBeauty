package com.jueze.ibeauty.util;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyString {
    public static String qc(String str, String regex){
        String res = null;
        try{
            Matcher m = Pattern.compile(regex).matcher(str);
            if(m.find()){
                res = m.group(1);
            }
        }catch(Exception e){}
        return res;
    }
    

    public static String qc(String str, String startStr, String endStr){
        String res =null;
        try{
            if(!str.contains(startStr) || !str.contains(endStr)) return null;
            int start = str.indexOf(startStr);
            str = str.substring(start+startStr.length());
            res = str.substring(0,str.indexOf(endStr));
        }catch(Exception e){}

        return res;
    }

	public static SpannableString highlightStr(int color, String str, String key) {
		SpannableString ss = new SpannableString(str);
		Matcher m = Pattern.compile(key).matcher(ss);
		while (m.find()) {
			int start = m.start();
			int end = m.end();
			ss.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return ss;
	}
}
