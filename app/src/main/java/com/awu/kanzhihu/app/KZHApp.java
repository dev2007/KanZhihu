package com.awu.kanzhihu.app;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.awu.kanzhihu.util.BitmapCache;

/**
 * Created by awu on 2015-12-15.
 */
public class KZHApp extends Application {
    private static Context mContext;
    private static BitmapCache cache = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    /**
     * Get application context.
     *
     * @return
     */
    public static Context appContext() {
        return mContext;
    }

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
