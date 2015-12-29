package com.awu.kanzhihu.entity;

/**
 * Created by yoyo on 2015/12/28.
 */
public class UserStar {
    private int answerrank;
    private int agreerank;
    private int ratiorank;
    private int followerrank;
    private int favrank;
    private int count1000rank;
    private int count100rank;

    public int getFavrank() {
        return favrank;
    }

    public void setFavrank(int favrank) {
        this.favrank = favrank;
    }

    public int getCount1000rank() {
        return count1000rank;
    }

    public void setCount1000rank(int count1000rank) {
        this.count1000rank = count1000rank;
    }

    public int getCount100rank() {
        return count100rank;
    }

    public void setCount100rank(int count100rank) {
        this.count100rank = count100rank;
    }

    public int getFollowerrank() {
        return followerrank;
    }

    public void setFollowerrank(int followerrank) {
        this.followerrank = followerrank;
    }

    public int getAnswerrank() {
        return answerrank;
    }

    public void setAnswerrank(int answerrank) {
        this.answerrank = answerrank;
    }

    public int getAgreerank() {
        return agreerank;
    }

    public void setAgreerank(int agreerank) {
        this.agreerank = agreerank;
    }

    public int getRatiorank() {
        return ratiorank;
    }

    public void setRatiorank(int ratiorank) {
        this.ratiorank = ratiorank;
    }
}
