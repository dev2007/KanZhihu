package com.awu.kanzhihu.util;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;

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
     *
     * @param splitDate
     * @return
     */
    public static String convert8Date(String splitDate) {
        return splitDate.replace("-", "");
    }

    /**
     * Convert half-angle character to full-angle character.
     *
     * @param input
     * @return
     */
    public static String toDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    private static DisplayMetrics metric = new DisplayMetrics();

    public static void initMetric(Activity activity) {
        if (metric.densityDpi == 0)
            activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
    }

    public static int convertImageSize(int basic160dpiSize) {
        if(metric.densityDpi == 0)
            return basic160dpiSize;

        return basic160dpiSize * metric.densityDpi / 160;
    }

    public static int convertImageSize(int basic160dpiSize, Activity activity) {
        initMetric(activity);
        return basic160dpiSize * metric.densityDpi / 160;
    }
}
