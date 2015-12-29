package com.awu.kanzhihu.util;

import com.awu.kanzhihu.R;
import com.awu.kanzhihu.app.KZHApp;
import com.awu.kanzhihu.entity.Post;

/**
 * Created by awu on 2015-12-18.
 */
public class Define {
    /**
     * url for getposts.
     */
    public static final String Url_PostList = "http://api.kanzhihu.com/getposts";

    /**
     * url for getpostanswers
     */
    public static final String Url_AnswerList = "http://api.kanzhihu.com/getpostanswers";

    /**
     * url for Zhihu.com
     */
    public static final String Url_Zhihu = "http://www.zhihu.com";
    /**
     * url for get answer.
     */
    public static final String Url_Answer = "http://www.zhihu.com/question";

    /**
     * url for check new post.
     */
    public static final String Url_CheckNew = "http://api.kanzhihu.com/checknew";

    /**
     * url for user detail.
     */
    public static final String Url_UserDetail = "http://api.kanzhihu.com/userdetail2";
    /**
     * Key for post date.
     */
    public static final String KEY_DATE = "postdate";
    /**
     * Key for post name.
     */
    public static final String KEY_NAME = "postname";

    /**
     * Key for question id.
     */
    public static final String KEY_QUESTIONID = "questionid";

    /**
     * Key for answer id.
     */
    public static final String KEY_ANSWERID = "answerid";

    /**
     * Key for question with answer.
     */
    public static final String KEY_QUESTION_ANSWER = "question_answer";

    /**
     * Key for answer title.
     */
    public static final String KEY_ANSWER_TITLE = "answertitle";

    /**
     * Key for publish time.
     */
    public static final String KEY_PUBLISH_TIME ="publishtime";

    /**
     * Key for old publish time.
     */
    public static final String KEY_OLD_PUBLISH_TIME = "oldpublishtime";

    /**
     * Key for user hash.
     */
    public static final String KEY_USER_HASH = "userhash";

    /**
     * Key for user name.
     */
    public static final String KEY_USER_NAME = "username";

    /**
     * Key for user avatar.
     */
    public static final String KEY_USER_AVATAR = "useravatar";

    public enum PostName{
        Yesterday("yesterday", KZHApp.appContext().getString(R.string.text_yesterday)),
        Recent("recent",KZHApp.appContext().getString(R.string.text_recent)),
        Archive("archive",KZHApp.appContext().getString(R.string.text_archive));

        private String mName;
        private String mDisName;
        private PostName(String name,String disName){
            mName = name;
            mDisName = disName;
        }

        public static String getDisplay(String name){
            switch (name) {
                case "recent":
                    return Recent.mDisName;
                case "yesterday":
                    return Yesterday.mDisName;
                case "archive":
                    return Archive.mDisName;
                default:
                    return "";

            }
        }
    }
}
