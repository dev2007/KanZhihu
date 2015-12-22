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
     * Key for post date.
     */
    public static final String KEY_DATE = "postdate";
    /**
     * Key for post name.
     */
    public static final String KEY_NAME = "postname";

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
