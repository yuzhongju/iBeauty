package com.jueze.utils;
import org.json.JSONObject;

public class JsonFormat {
    public static String format(String data){
        try{
			JSONObject jsonObj=new JSONObject(data);
			return jsonObj.toString(1);
        }catch(Exception e){}
        return data;
    }
}
