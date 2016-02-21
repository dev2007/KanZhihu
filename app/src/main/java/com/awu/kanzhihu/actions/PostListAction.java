package com.awu.kanzhihu.actions;

/**
 * Created by yoyo on 2016/2/20.
 */
public class PostListAction extends Action<String> {
    public static final String ACTION_GET_POST_LIST = "action_get_post_list";
    public static final String ACTION_GET_OLD_POST_LIST = "action_get_old_post_list";
    public static final String ACTION_CHECK_NEW = "action_check_new";
    public static final String ACTION_LATEST_TIME = "action_latest_time";
    public static final String ACTION_OLDEST_TIME = "action_oldest_time";
    PostListAction(String type, String data) {
        super(type, data);
    }
}
