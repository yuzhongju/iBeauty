package com.jueze.ibeauty.bean;

public class MdIconBean {
    private String iconName;
    private String iconPath;
    private String bgColor;
    private String nameColor;

    public MdIconBean(String iconName, String iconPath, String bgColor, String nameColor){
        this.iconName = iconName;
        this.iconPath = iconPath;
        this.bgColor = bgColor;
        this.nameColor = nameColor;
    }
    
    public String getIconName(){
        return this.iconName;
    }
    
    public String getIconPath(){
        return this.iconPath;
    }
    public String getBgColor(){
        return this.bgColor;
    }
    
    public String getNameColor(){
        return this.nameColor;
    }
}
