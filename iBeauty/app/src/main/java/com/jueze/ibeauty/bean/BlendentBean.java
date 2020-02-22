package com.jueze.ibeauty.bean;

public class BlendentBean {
    private String name;
    private int level;
    private String color;
    private String colors;
    
    public BlendentBean(){
        
    }
    
    public BlendentBean(String name, int level, String color, String colors){
        this.name = name;
        this.level = level;
        this.color = color;
        this.colors = colors;
    }
    
    public String getName(){
        return this.name;
    }
    
    public int getLevel(){
        return this.level;
    }
    
    public String getColor(){
        return this.color;
    }
    
    public String getColors(){
        return this.colors;
    }
}
