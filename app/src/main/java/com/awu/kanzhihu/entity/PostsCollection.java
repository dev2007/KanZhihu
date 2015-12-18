package com.awu.kanzhihu.entity;

import java.util.ArrayList;

/**
 * Created by awu on 2015-12-18.
 * Posts collection entity.
 */
public class PostsCollection {

    /**
     * error message.
     */
    private String error;

    /**
     * Posts count.
     */
    private int count;
    /**
     * posts list.
     */
    private ArrayList<Post> posts;

    public PostsCollection(){
        posts = new ArrayList<>();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
