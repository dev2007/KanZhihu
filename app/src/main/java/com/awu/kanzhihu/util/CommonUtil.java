package com.awu.kanzhihu.util;

import android.app.Activity;
import android.util.Base64;
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

    /**
     * initialize screen's dpi.
     * @param activity
     */
    public static void initMetric(Activity activity) {
        if (metric.densityDpi == 0)
            activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
    }

    /**
     * convert size for image by basic size.
     * @param basic160dpiSize the basic pixels size of 160dpi.
     * @return the size for current screen's dpi.
     */
    public static int convertImageSize(int basic160dpiSize) {
        if (metric.densityDpi == 0)
            return basic160dpiSize;

        return basic160dpiSize * metric.densityDpi / 160;
    }

    /**
     * convert size and initialize screen's dpi.
     * @param basic160dpiSize basic pixels size of 160dpi.
     * @param activity activity object.
     * @return the size of current screen's dpi.
     */
    public static int convertImageSize(int basic160dpiSize, Activity activity) {
        initMetric(activity);
        return basic160dpiSize * metric.densityDpi / 160;
    }

    /**
     * encrypt string to base64.
     * @param source
     * @return
     */
    public static String EnBase64(String source) {
        return Base64.encodeToString(source.getBytes(), Base64.DEFAULT);
    }

    /**
     * de-encrypt base64 string to original string.
     * @param base64
     * @return
     */
    public static String DeBase64(String base64) {
        return new String(Base64.decode(base64, Base64.DEFAULT));
    }

}
