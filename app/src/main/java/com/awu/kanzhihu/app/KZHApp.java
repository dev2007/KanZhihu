package com.awu.kanzhihu.app;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.awu.kanzhihu.actions.ActionCreator;
import com.awu.kanzhihu.dispatcher.Dispatcher;
import com.awu.kanzhihu.util.BitmapCache;

import awu.com.awutil.context.EasyApplication;

/**
 * Created by awu on 2015-12-15.
 */
public class KZHApp extends EasyApplication {
    private static BitmapCache cache = null;

    /**
     * Global ImageCache
     * @return
     */
    public static BitmapCache bitmapCacheInstance() {
        if (cache == null)
            cache = new BitmapCache();

        return cache;
    }
}
