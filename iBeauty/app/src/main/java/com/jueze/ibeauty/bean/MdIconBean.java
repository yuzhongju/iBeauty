package com.jueze.ibeauty.bean;

import java.io.File;

public class MdIconBean {
    
	private File file;
	
    public MdIconBean(File file){
        this.file=file;
    }
	
	public String getIconPath(){
		return file.toString();
	}
	
	public String getIconName(){
		return file.getName();
	}
	
	public String getSuffix(){
		return getIconName().substring(getIconName().lastIndexOf("."));
	}
	
	public String replace(String v){
		return getIconName().replace(getSuffix(),"_"+v+getSuffix());
	}
}
