package com.awu.kanzhihu.entity;

import java.util.Date;

/**
 * Created by awu on 2015-12-21.
 */
public class Answer {
    /**
     * title id.
     */
    private String title;
    /**
     * publish time.
     */
    private String time;
    /**
     * answer summary.
     */
    private String summary;
    /**
     * question id,8 digits number.
     */
    private String questionid;
    /**
     * answer id.8 digits number.
     */
    private String answerid;
    /**
     * author name
     */
    private String authorname;
    /**
     * author hash.
     */
    private String authorhash;
    /**
     * author avatar url.
     */
    private String avatar;
    /**
     * vote number.
     */
    private int vote;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getQuestionid() {
        return questionid;
    }

    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }

    public String getAnswerid() {
        return answerid;
    }

    public void setAnswerid(String answerid) {
        this.answerid = answerid;
    }

    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }

    public String getAuthorhash() {
        return authorhash;
    }

    public void setAuthorhash(String authorhash) {
        this.authorhash = authorhash;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }
}
