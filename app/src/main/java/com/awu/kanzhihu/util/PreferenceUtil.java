package com.awu.kanzhihu.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.awu.kanzhihu.app.KZHApp;

/**
 * SharedPreferences util.
 * Created by awu on 2016-01-20.
 */
public class PreferenceUtil {
    /**
     * The SharedPreferences name of the app.
     */
    private static final String SP_NAME = "com.awu.kanzhihu.sp";
    /**
     * SharedPreferences object.
     */
    private static SharedPreferences sp = null;

    /**
     * Initialize SahredPreference object if it's null.
     */
    private static void init() {
        if (sp == null)
            sp = KZHApp.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Write data into SharedPreferences.
     *
     * @param key   The name of the preference to modify
     * @param value The new value for the preference.
     */
    public static void write(String key, Object value) {
        init();
        SharedPreferences.Editor editor = sp.edit();
        if (value instanceof Integer)
            editor.putInt(key, (int) value);
        else if (value instanceof String)
            editor.putString(key, (String) value);
        else if (value instanceof Float)
            editor.putFloat(key, (float) value);
        else if (value instanceof Boolean)
            editor.putBoolean(key, (boolean) value);
        else if (value instanceof Long)
            editor.putLong(key, (long) value);

        editor.commit();
    }

    /**
     * Read data from SharedPreferences.
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if the preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue.
     */
    public static Object read(String key, Object defaultValue) {
        init();

        Object value = null;
        if (defaultValue instanceof Integer)
            value = sp.getInt(key, (int) defaultValue);
        else if (defaultValue instanceof String)
            value = sp.getString(key, (String) defaultValue);
        else if (defaultValue instanceof Float)
            value = sp.getFloat(key, (float) defaultValue);
        else if (defaultValue instanceof Boolean)
            value = sp.getBoolean(key, (boolean) defaultValue);
        else if (defaultValue instanceof Long)
            value = sp.getLong(key, (long) defaultValue);

        return value;
    }
}
