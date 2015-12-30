package com.awu.kanzhihu.entity;

import java.util.ArrayList;

/**
 * Created by awu on 2015-12-30.
 */
public class TopUserList {
    private String error;
    private int count;
    private ArrayList<TopUserAgree> topuser;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<TopUserAgree> getTopuser() {
        return topuser;
    }

    public void setTopuser(ArrayList<TopUserAgree> topuser) {
        this.topuser = topuser;
    }
}
