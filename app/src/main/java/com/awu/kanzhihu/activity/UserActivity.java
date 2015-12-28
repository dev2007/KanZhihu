package com.awu.kanzhihu.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.awu.kanzhihu.R;
import com.awu.kanzhihu.adapter.UserPagerAdapter;
import com.awu.kanzhihu.app.KZHApp;
import com.awu.kanzhihu.util.BitmapCache;
import com.awu.kanzhihu.util.Define;
import com.makeramen.roundedimageview.RoundedImageView;

public class UserActivity extends AppCompatActivity {
    private static final String TAG = "UserActivity";
    private Toolbar mToolbar;
    //fragmentpageradapter object.
    private UserPagerAdapter mUserPagerAdapter;
    // tablayout's viewpager.
    private ViewPager mViewPager;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initToolbar();
        initQueue();
        updateToolbar();
        initTabWithAdapter();
    }

    private void initQueue() {
        mQueue = Volley.newRequestQueue(this);
    }

    /**
     * initialize toolbar
     */
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void updateToolbar() {
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra(Define.KEY_USER_NAME);
            ((TextView) findViewById(R.id.toolbar_tv)).setText(title);

            ImageLoader mImageLoader = new ImageLoader(mQueue, KZHApp.bitmapCacheInstance(mQueue));
            ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if (response.getBitmap() != null) {
                        ((RoundedImageView) findViewById(R.id.toolbar_iv)).setImageDrawable(new BitmapDrawable(response.getBitmap()));
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            };
            mImageLoader.get(intent.getStringExtra(Define.KEY_USER_AVATAR), listener, 600, 600);
        }
    }

    /**
     * initialize TabLayout & ViewPager & ViewPager's FragmentPagerAdapter
     */
    private void initTabWithAdapter() {
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mUserPagerAdapter = new UserPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container_user);
        mViewPager.setAdapter(mUserPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_user);
        tabLayout.setupWithViewPager(mViewPager);
    }

}
