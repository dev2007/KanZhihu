package com.awu.kanzhihu.entity;

import java.util.ArrayList;

/**
 * Created by awu on 2015-12-21.
 */
public class AnswerCollection {
    /**
     * answers' count.
     */
    private int count;
    /**
     *
     */
    private ArrayList<Answer> answers;

    public AnswerCollection(){
        this.answers = new ArrayList<>();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }
}
