package com.awu.kanzhihu.entity;

import java.util.ArrayList;

/**
 * Created by yoyo on 2016/1/12.
 */
public class SearchResult {
    private String error;
    private int count;
    private ArrayList<SearchUser> users;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<SearchUser> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<SearchUser> users) {
        this.users = users;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
