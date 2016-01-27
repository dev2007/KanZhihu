package com.awu.kanzhihu.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TableLayout;
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
import com.awu.kanzhihu.util.CommonUtil;
import com.awu.kanzhihu.util.Define;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.umeng.analytics.MobclickAgent;

import awu.com.awutil.LogUtil;

public class UserActivity extends BaseActivity
        implements Response.Listener<String>, Response.ErrorListener {
    private TabLayout mTabLayout;
    //fragmentpageradapter object.
    private UserPagerAdapter mUserPagerAdapter;
    // tablayout's viewpager.
    private ViewPager mViewPager;
    private RequestQueue mQueue;
    private ProgressBar mProgressBar;
    private String userName;
    private String avatarUrl;
    private String userHash;
    private String mUserUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getIntentData();
        initToolbarNavigation();
        initQueue();
        updateToolbar();
        initTabLayout();
        initTabWithAdapter();
        initProgressBar();
        requestData();
    }

    private void initTabLayout() {
        mTabLayout = (TabLayout) findViewById(R.id.tabs_user);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            userName = intent.getStringExtra(Define.KEY_USER_NAME);
            avatarUrl = intent.getStringExtra(Define.KEY_USER_AVATAR);
            userHash = intent.getStringExtra(Define.KEY_USER_HASH);
            mUserUrl = String.format("%s/%s",Define.Url_User,userHash);
        }
    }

    private void initQueue() {
        mQueue = Volley.newRequestQueue(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_view) {
            Uri uri = Uri.parse(mUserUrl);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
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
            mImageLoader.get(avatarUrl, listener, CommonUtil.convertImageSize(30), CommonUtil.convertImageSize(30));
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
        LogUtil.d(this, url);
        StringRequest stringRequest = new StringRequest(url, this, this);
        mQueue.add(stringRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (error != null && error.getMessage() != null)
            LogUtil.i(this, error.getMessage());
        else
            LogUtil.i(this, "i don't know what happen.");
        Toast.makeText(this, getString(R.string.hint_loaderror), Toast.LENGTH_SHORT).show();
        stopRefresh();
    }

    @Override
    public void onResponse(String response) {
        if (!response.equals("")) {
            Gson gson = new Gson();
            UserDetails userDetails = gson.fromJson(response, UserDetails.class);
            if (!userDetails.getError().equals("")) {
                LogUtil.i(this, "response had error msg:" + userDetails.getError());
                Toast.makeText(this, getString(R.string.hint_loaderror), Toast.LENGTH_SHORT).show();
            } else {
                LogUtil.i(this, "response ok");
                mUserPagerAdapter.bindData(userDetails);
            }
        } else {
            LogUtil.i(this, "response error");
            Toast.makeText(this, getString(R.string.hint_loaderror), Toast.LENGTH_SHORT).show();
        }
        stopRefresh();
    }

    private void stopRefresh() {
        mProgressBar.setVisibility(View.GONE);
        mTabLayout.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        MobclickAgent.onPause(this);
    }
}
