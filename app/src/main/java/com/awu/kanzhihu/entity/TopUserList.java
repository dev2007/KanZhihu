package com.awu.kanzhihu.entity;

import java.util.ArrayList;

/**
 * Created by awu on 2015-12-30.
 */
public class TopUserList<T> {
    private String error;
    private int count;
    private ArrayList<T> topuser;

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

    public ArrayList<T> getTopuser() {
        return topuser;
    }

    public void setTopuser(ArrayList<T> topuser) {
        this.topuser = topuser;
    }
}
