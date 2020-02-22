package com.jueze.ibeauty.util;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonFormat {
    public static String format(String data){
        try{
            JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(jsonObject);
        }catch(Exception e){}
        
        return data;
    }
}
