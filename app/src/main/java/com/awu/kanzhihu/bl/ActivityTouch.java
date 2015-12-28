package com.awu.kanzhihu.bl;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by awu on 2015-12-28.
 */
public class ActivityTouch {
    private static final String TAG = "ActivityTouch";
    private static float x1 = 0;
    private static float x2 = 0;
    private static float y1 = 0;
    private static float y2 = 0;

    public static boolean parentOnTouch(Activity activity, View v, MotionEvent event) {
        Log.i(TAG,"touch");
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            x1 = event.getX();
            y1 = event.getY();
        }

        if(event.getAction() == MotionEvent.ACTION_MOVE){
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            x2 = event.getX();
            y2 = event.getY();
            if (x2 - x1 > 40 && Math.abs(y2 - y1) < 20) {
                activity.onBackPressed();
                return true;
            } else {
                return false;
            }
        }
        return true;
    }
}
