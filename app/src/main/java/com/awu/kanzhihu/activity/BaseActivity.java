package com.awu.kanzhihu.activity;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;

import com.awu.kanzhihu.R;
import com.awu.kanzhihu.util.EasyGesture;

import awu.com.awutil.LogUtil;

/**
 * Created by awu on 2016-01-27.
 */
public class BaseActivity extends AppCompatActivity implements View.OnTouchListener {
    protected Toolbar mToolbar;
    protected GestureDetectorCompat mDetector;
    private EasyGesture mEasyGesture;

    protected void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    protected void initToolbarNavigation() {
        initToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     *
     */
    protected void initGestureDetector(View subView) {
        mDetector = new GestureDetectorCompat(this, new EasyGesture(this));
        subView.getRootView().setOnTouchListener(this);
    }

    protected void initGestureDetectorRecyclerView(final RecyclerView view) {
        mEasyGesture = new EasyGesture(this);
        mDetector = new GestureDetectorCompat(this, mEasyGesture);
        view.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                LogUtil.d("Base RecyclerView Event", "Touch");
                mDetector.onTouchEvent(e);
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    /**
     * Set back animation.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, android.R.anim.slide_out_right);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mDetector != null) {
            LogUtil.d(this, "onTouch");
            return mDetector.onTouchEvent(event);
        } else {
            LogUtil.d(this, "no detector,onTouch false");
            return false;
        }
    }
}
