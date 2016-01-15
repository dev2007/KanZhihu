package com.awu.kanzhihu.entity;

/**
 * Fav entity for database table  fav.
 * Created by yoyo on 2016/1/15.
 */
public class Fav {
    private String url;
    private String name;

    public Fav(){
        this.url = "";
        this.name = "";
    }

    public Fav(String url,String name){
        this.url = url;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
