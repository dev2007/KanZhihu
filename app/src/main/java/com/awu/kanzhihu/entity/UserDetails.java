package com.awu.kanzhihu.entity;

import java.util.ArrayList;

/**
 * Created by yoyo on 2015/12/28.
 */
public class UserDetails {
    private String name;
    private String avatar;
    private String signature;
    private String description;
    private UserDetail detail;
    private UserStar star;
    private ArrayList<UserTrend> trend;

    public ArrayList<UserTrend> getTrend() {
        return trend;
    }

    public void setTrend(ArrayList<UserTrend> trend) {
        this.trend = trend;
    }

    public ArrayList<UserTopAnswer> getTopanswers() {
        return topanswers;
    }

    public void setTopanswers(ArrayList<UserTopAnswer> topanswers) {
        this.topanswers = topanswers;
    }

    private ArrayList<UserTopAnswer> topanswers;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserDetail getDetail() {
        return detail;
    }

    public void setDetail(UserDetail detail) {
        this.detail = detail;
    }

    public UserStar getStar() {
        return star;
    }

    public void setStar(UserStar star) {
        this.star = star;
    }
}
