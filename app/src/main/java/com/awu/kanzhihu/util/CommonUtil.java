package com.awu.kanzhihu.util;

import  android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by awu on 2015-12-22.
 */
public class CommonUtil {
    private static final String TAG = "CommonUtil";
    /**
     * Convert 2015-01-23 to date format like 20150123
     * @param splitDate
     * @return
     */
    public static String convert8Date(String splitDate){
        return splitDate.replace("-","");
    }
}
