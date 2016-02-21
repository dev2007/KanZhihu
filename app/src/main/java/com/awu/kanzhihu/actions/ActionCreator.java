package com.awu.kanzhihu.actions;

import com.awu.kanzhihu.dispatcher.Dispatcher;

/**
 * Created by yoyo on 2016/2/18.
 */
public class ActionCreator {
    private static ActionCreator _instance;
    final Dispatcher dispatcher;

    public static ActionCreator instance(Dispatcher dispatcher) {
        if (_instance == null)
            _instance = new ActionCreator(dispatcher);
        return _instance;
    }

    ActionCreator(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void requestSplash() {
        dispatcher.dispatch(new SplashAction(SplashAction.ACTION_NAME, ""));
    }

    public void requestPostList() {
        dispatcher.dispatch(new PostListAction(PostListAction.ACTION_GET_POST_LIST, ""));
    }

    public void requestOldPostList(){
        dispatcher.dispatch(new PostListAction(PostListAction.ACTION_GET_OLD_POST_LIST,""));
    }

    public void requestNewPost() {
        dispatcher.dispatch(new PostListAction(PostListAction.ACTION_CHECK_NEW, ""));
    }

    public void updatePostLatestTime(int latestTime) {
        dispatcher.dispatch(new PostListAction(PostListAction.ACTION_LATEST_TIME, "" + latestTime));
    }

    public void updatePostOlddestTime(int oldestTime) {
        dispatcher.dispatch(new PostListAction(PostListAction.ACTION_OLDEST_TIME, "" + oldestTime));
    }
}
