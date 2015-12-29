package com.awu.kanzhihu.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.awu.kanzhihu.R;
import com.awu.kanzhihu.adapter.UserPagerAdapter;
import com.awu.kanzhihu.app.KZHApp;
import com.awu.kanzhihu.entity.UserDetails;
import com.awu.kanzhihu.util.BitmapCache;
import com.awu.kanzhihu.util.Define;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

public class UserActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {
    private static final String TAG = "UserActivity";
    private Toolbar mToolbar;
    //fragmentpageradapter object.
    private UserPagerAdapter mUserPagerAdapter;
    // tablayout's viewpager.
    private ViewPager mViewPager;
    private RequestQueue mQueue;
    private ProgressBar mProgressBar;
    private String userName;
    private String avatarUrl;
    private String userHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getIntentData();
        initToolbar();
        initQueue();
        updateToolbar();
        initTabWithAdapter();
        initProgressBar();
        requestData();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            userName = intent.getStringExtra(Define.KEY_USER_NAME);
            avatarUrl = intent.getStringExtra(Define.KEY_USER_AVATAR);
            userHash = intent.getStringExtra(Define.KEY_USER_HASH);
        }
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
        if (!userName.equals(""))
            ((TextView) findViewById(R.id.toolbar_tv)).setText(userName);
        if (!avatarUrl.equals("")) {
            ImageLoader mImageLoader = new ImageLoader(mQueue, KZHApp.bitmapCacheInstance());
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
            mImageLoader.get(avatarUrl, listener, 600, 600);
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

    private void initProgressBar() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar_user);
    }

    private void requestData() {
        String url = String.format("%s/%s", Define.Url_UserDetail, userHash);
        Log.i(TAG, url);
        StringRequest stringRequest = new StringRequest(url, this, this);
        mQueue.add(stringRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (error != null && error.getMessage() != null)
            Log.i(TAG, error.getMessage());
        else
            Log.i(TAG, "i don't know what happen.");
        Toast.makeText(this, getString(R.string.hint_loaderror), Toast.LENGTH_SHORT).show();
        stopRefresh();
    }

    @Override
    public void onResponse(String response) {
        if (!response.equals("")) {
            Gson gson = new Gson();
            UserDetails userDetails = gson.fromJson(response, UserDetails.class);
            if (!userDetails.getError().equals("")) {
                Log.i(TAG, "response had error msg:" + userDetails.getError());
                Toast.makeText(this, getString(R.string.hint_loaderror), Toast.LENGTH_SHORT).show();
            }else{
                Log.i(TAG,"response ok");
                mUserPagerAdapter.bindData(userDetails);
            }
        } else {
            Log.i(TAG, "response error");
            Toast.makeText(this, getString(R.string.hint_loaderror), Toast.LENGTH_SHORT).show();
        }
        stopRefresh();
    }

    private void stopRefresh() {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.GONE);
            mViewPager.setVisibility(View.VISIBLE);
        }
    }
}
