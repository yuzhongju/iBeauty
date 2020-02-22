package com.jueze.ibeauty.bean;

public class ColorsBean {
    private String level;
    private String color;
    
    public ColorsBean(String level, String color){
        this.level = level;
        this.color = color;
    }
    
    public String getLevel(){
        return this.level;
    }
    
    public String getColor(){
        return this.color;
    }
}
