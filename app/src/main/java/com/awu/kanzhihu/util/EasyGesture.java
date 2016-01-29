package com.awu.kanzhihu.util;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.awu.kanzhihu.activity.BaseActivity;

import awu.com.awutil.LogUtil;

/**
 * Easy GestureDetector listener.
 * Provide fling back
 * Created by awu on 2016-01-29.
 */
public class EasyGesture extends GestureDetector.SimpleOnGestureListener {
    private final int verticalMinDistance = 20;
    private final int minVelocity = 0;
    private BaseActivity activity;

    public EasyGesture(BaseActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        if (e1.getX() - e2.getX() > verticalMinDistance
                && Math.abs(velocityX) > minVelocity) {
            LogUtil.d(this, "fling left");
            return true;
        } else if (e2.getX() - e1.getX() > verticalMinDistance
                && Math.abs(velocityX) > minVelocity) {
            LogUtil.d(this, "fling right");
            activity.onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        LogUtil.d(this, "Down");
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2,
                            float distanceX, float distanceY) {
        LogUtil.d(this, "scroll");
        return false;
    }
}
