package com.awu.kanzhihu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.awu.kanzhihu.R;
import com.awu.kanzhihu.util.CommonUtil;
import com.awu.kanzhihu.util.Define;

import java.util.Date;

public class ArticleDetailActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener,
        Response.Listener<String>,
        Response.ErrorListener {
    private static final String TAG = "ArticleDetailActivity";
    private Toolbar mToolbar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isRefreshing = false;
    private RequestQueue mQueue;
    private String date;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        Intent intent = getIntent();
        initToolbar();
        setTitle(intent);
        initQueue();
        initFloatingActionButton();
        initSwipeRefreshLayout();
        initRecyclerView();
    }

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

    private void setTitle(Intent intent) {
        if (intent != null) {
            String disDate = intent.getStringExtra(Define.KEY_DATE);
            date = CommonUtil.convert8Date(disDate);
            name = intent.getStringExtra(Define.KEY_NAME);
            String disName = Define.PostName.getDisplay(name);
            this.setTitle(disName + " " + disDate);
        } else {
            this.setTitle(getString(R.string.title_activity_article_detail));
        }
    }

    /**
     * create Volley RequestQueue object.
     */
    private void initQueue() {
        mQueue = Volley.newRequestQueue(this);
    }

    private void initFloatingActionButton() {
        //        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout_detail);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void initRecyclerView() {

    }

    /**
     * SwipeRefreshLayout refresh event.
     */
    @Override
    public void onRefresh() {
        if (!isRefreshing) {
            isRefreshing = true;
            Log.e(TAG, "refresh");
            Log.e(TAG,"is refresh?" + mSwipeRefreshLayout.isRefreshing());
//            requestData(date,name);
        } else {
            Log.e(TAG, "is refreshing...");
        }
    }

    private void requestData(String publishTime, String name) {
        String url = String.format("%s/%s/%s", Define.Url_AnswerList, publishTime, name);
        Log.i(TAG,url);
        StringRequest stringRequest = new StringRequest(url, this, this);
        mQueue.add(stringRequest);
    }

    @Override
    public void onResponse(String response) {
        Log.i(TAG, response);

        setNoRefresh();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (error != null && error.getMessage() != null)
            Log.i(TAG, error.getMessage());
        else
            Log.i(TAG, "i don't know what happen.");

        setNoRefresh();
    }

    private void setNoRefresh(){
        Log.i(TAG,"stop refresh");
        if(mSwipeRefreshLayout.isRefreshing()) {
            isRefreshing = false;
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

}
