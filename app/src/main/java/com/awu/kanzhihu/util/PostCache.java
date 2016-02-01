package com.awu.kanzhihu.util;

import com.awu.kanzhihu.entity.AnswerCollection;
import com.awu.kanzhihu.entity.Post;
import com.awu.kanzhihu.entity.PostsCollection;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import awu.com.awutil.LogUtil;

/**
 * Created by awu on 2016-02-01.
 */
public class PostCache {
    private static final String TAG = "PostCache";
    private static Map<Integer, AnswerCollection> _container = new HashMap<>();
    private static final String FILE_NAME = "PostCache";

    /**
     * check if has cache.
     * @param publishTime
     * @return
     */
    public static boolean hasCache(int publishTime){
        return _container.containsKey(publishTime);
    }

    /**
     * add cache content.
     * @param publishTime
     * @param collection
     */
    public static void add(int publishTime, AnswerCollection collection) {
        if (!_container.containsKey(publishTime))
            _container.put(publishTime, collection);
    }

    /**
     * get cache content.
     * @param publishTime the post's publishtime.
     * @return the post's answers list.
     */
    public static AnswerCollection get(int publishTime) {
        if (_container.containsKey(publishTime)) {
            return _container.get(publishTime);
        }

        return null;
    }
}
