package com.awu.kanzhihu.bl;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by awu on 2015-12-28.
 */
public class RecyclerViewItemTouch implements RecyclerView.OnItemTouchListener {
    private static final String TAG = "RecyclerViewItemTouch";
    private float x1 = 0;
    private float x2 = 0;
    private float y1 = 0;
    private float y2 = 0;
    private Activity activity;

    public RecyclerViewItemTouch(Activity activity){
        this.activity = activity;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        Log.i(TAG, "item intercept touch");
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            x1 = e.getX();
            y1 = e.getY();
        }
        if(e.getAction() == MotionEvent.ACTION_MOVE){
            Log.i(TAG,"move:"+e.getX());
        }
        if (e.getAction() == MotionEvent.ACTION_UP) {
            x2 = e.getX();
            y2 = e.getY();
            Log.i(TAG, "down,up:" + x1 + "," + x2);
            if (x2 - x1 > 40 && Math.abs(y2 - y1) < 20) {
                Log.i(TAG, "more,touch");
                activity.onBackPressed();
                return true;
            } else {
                Log.i(TAG, "less,click");
                return false;
            }
        }
        Log.i(TAG, "others");
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
