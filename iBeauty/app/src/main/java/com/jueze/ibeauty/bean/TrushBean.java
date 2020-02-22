package com.jueze.ibeauty.bean;
import java.util.List;

public class TrushBean {
    private String msg;
    private List<Data> data;
    
    public String getMsg(){
        return this.msg;
    }
    public List<Data> getData(){
        return this.data;
    }
    
    public static class Data{
        private String name;
        private String category;
        private String remark;
        
        public String getName(){
            return this.name;
        }
        public String getCategory(){
            return this.category;
        }
        public String getRemark(){
            return this.remark;
        }
    }
}
