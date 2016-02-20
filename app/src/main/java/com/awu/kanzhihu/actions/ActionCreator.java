package com.awu.kanzhihu.actions;

import com.awu.kanzhihu.dispatcher.Dispatcher;

/**
 * Created by yoyo on 2016/2/18.
 */
public class ActionCreator {
    private static ActionCreator _instance;
    final Dispatcher dispatcher;

    public static ActionCreator instance(Dispatcher dispatcher){
        if(_instance == null)
            _instance = new ActionCreator(dispatcher);
        return _instance;
    }

    ActionCreator(Dispatcher dispatcher){
        this.dispatcher = dispatcher;
    }

    public void requestSplash(){
        dispatcher.dispatch(new SplashAction(SplashAction.ACTION_NAME,""));
    }
}
