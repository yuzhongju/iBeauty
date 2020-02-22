package com.jueze.ibeauty.bean;

import java.util.List;

public class BookMarkBean {
    private List<BookMark> bookmark;

    public BookMarkBean(){

    }

    public List<BookMark> getBookMark(){
        return this.bookmark;
    }
    public static class BookMark{
        private String name;
        private String url;

        public BookMark(){

        }

        public String getName(){
            return this.name;
        }

        public String getUrl(){
            return this.url;
        }
    }
}
