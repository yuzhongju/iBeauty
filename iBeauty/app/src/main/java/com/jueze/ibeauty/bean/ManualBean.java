package com.jueze.ibeauty.bean;

public class ManualBean {
    private String docTitle;
    private String docContent;
    
    public ManualBean(){
        
    }
    public ManualBean(String title, String content){
        this.docTitle = title;
        this.docContent = content;
    }
    public String getDocTitle() {
        return this.docTitle;
    }
    
    public String getDocContent() {
        return this.docContent;
    }
    
    public void setDocTitle(String title){
        this.docTitle = title;
    }
    
    public void setDocContent(String content){
        this.docContent = content;
    }
    
}
