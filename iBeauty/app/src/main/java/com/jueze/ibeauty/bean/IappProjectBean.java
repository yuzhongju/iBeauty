package com.jueze.ibeauty.bean;

public class IappProjectBean {
    private String title;
    private String yuv;
    private String bm;
    private String remark;
    private String icon;
    private String path;
    
    public IappProjectBean(String title, String yuv, String bm, String remark, String icon,String path){
        this.title = title;
        this.yuv = yuv;
        this.bm = bm;
        this.remark = remark;
        this.icon = icon;
        this.path = path;
    }
    
    public String getTitle(){
        return this.title;
    }

    public String getYuv(){
        return this.yuv;
    }

    public String getBm(){
        return this.bm;
    }

    public String getRemark(){
        return this.remark;
    }

    public String getIcon(){
        return this.icon;
    }
    
    public String getPath(){
        return this.path;
    }
}
