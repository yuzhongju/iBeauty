package com.jueze.ibeauty.bean;

public class PackSourceBean {

    private String title;
    private String size;
    private String path;
    public PackSourceBean(String title, String size, String path) {
        this.title = title;
        this.size = size;
        this.path = path;
    }

    public String getTitle() {
        return this.title;
    }

    public String getSize() {
        return this.size;
    }
    public String getPath(){
        return this.path;
    }
}
