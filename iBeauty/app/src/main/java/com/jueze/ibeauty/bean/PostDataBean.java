package com.jueze.ibeauty.bean;

public class PostDataBean {
    private String key;
    private String value;
    
    public PostDataBean(String key, String value){
        this.key = key;
        this.value = value;
    }
    
    public String getKey(){
        return this.key;
    }
    
    public String getValue(){
        return this.value;
    }
}
