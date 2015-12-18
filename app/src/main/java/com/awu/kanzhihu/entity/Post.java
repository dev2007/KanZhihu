package com.awu.kanzhihu.entity;

/**
 * Created by awu on 2015-12-18.
 * Post entity.
 */
public class Post {
    /**
     * publish time.
     */
    private String date;
    /**
     * title name.yesterday, recent, archive
     */
    private String name;
    /**
     * picture.
     */
    private String pic;
    /**
     * publish time stamp.
     */
    private int publishtime;
    /**
     * answers count.
     */
    private int count;
    /**
     * excerpt
     */
    private String excerpt;

    public Post(){

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(int publishtime) {
        this.publishtime = publishtime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }
}
