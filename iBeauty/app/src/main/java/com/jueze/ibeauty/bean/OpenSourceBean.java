package com.jueze.ibeauty.bean;

public class OpenSourceBean {
    private String title;
    private String note;
    
    private String url;
    public OpenSourceBean(String title, String note, String url){
        this.title = title;
        this.note = note;
        this.url = url;
    }
    
    public String getTitle(){
        return this.title;
    }
    
    public String getNote(){
        return this.note;
    }
    
    public String getUrl(){
        return this.url;
    }
}
