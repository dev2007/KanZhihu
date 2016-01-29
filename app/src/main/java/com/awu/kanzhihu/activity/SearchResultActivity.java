package com.awu.kanzhihu.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.awu.kanzhihu.R;
import com.awu.kanzhihu.adapter.SearchResultAdapter;
import com.awu.kanzhihu.entity.PostsCollection;
import com.awu.kanzhihu.entity.SearchResult;
import com.awu.kanzhihu.entity.SearchUser;
import com.awu.kanzhihu.event.RecyclerViewClickListener;
import com.awu.kanzhihu.util.CommonUtil;
import com.awu.kanzhihu.util.Define;
import com.awu.kanzhihu.view.DividerItemDecoration;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import awu.com.awutil.LogUtil;

/**
 * SearchResultActivity.
 */
public class SearchResultActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener, Response.Listener<String>,
        Response.ErrorListener {
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private SearchResultAdapter mAdapter;
    private RequestQueue mQueue;
    private boolean isSwipeRefreshing = false;
    private String mQueryStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        initToolbarNavigation();
        initQueue();
        initProgressBar();
        initSwipeRefreshLayout();
        initRecyclerView();
        handleIntent(getIntent());
        initGestureDetector(mSwipeRefreshLayout);
    }

    private void initProgressBar() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar_search);
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout_search);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_search);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SearchResultAdapter(mQueue, new RecyclerViewClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SearchUser user = mAdapter.getData(position);
                Intent intent = new Intent(view.getContext(), UserActivity.class);
                intent.putExtra(Define.KEY_USER_HASH, user.getHash());
                intent.putExtra(Define.KEY_USER_AVATAR, user.getAvatar());
                intent.putExtra(Define.KEY_USER_NAME, user.getName());
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
//                DividerItemDecoration.VERTICAL_LIST));
    }


    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void initQueue() {
        mQueue = Volley.newRequestQueue(this);
    }

    private void requestData() {
        String url = String.format("%s/%s", Define.Url_Search, CommonUtil.UrlEncode(mQueryStr));
        StringRequest stringRequest = new StringRequest(url, this, this);
        LogUtil.d(this, "url:" + url);
        mQueue.add(stringRequest);
    }


    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            LogUtil.d(this, "query:" + query);
            mQueryStr = query;
            getSupportActionBar().setTitle(getString(R.string.text_search) + ":" + query);
            requestData();
        }
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        if (error != null && error.getMessage() != null)
            LogUtil.d(this, error.getMessage());
        else
            LogUtil.d(this, "i don't know what happen.");
        stopRefresh();
        Toast.makeText(this, R.string.hint_refresh, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(String response) {
        Gson gson = new Gson();
        SearchResult collection = gson.fromJson(response, SearchResult.class);
        if (!collection.getError().equals("")) {
            LogUtil.d(this, "Response Error:" + collection.getError());
            return;
        }
        mAdapter.bindData(collection.getUsers());
        mAdapter.notifyDataSetChanged();
        stopRefresh();
    }

    private void stopRefresh() {
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
        isSwipeRefreshing = false;
    }

    @Override
    public void onRefresh() {
        if (!isSwipeRefreshing) {
            LogUtil.d(this, "refresh");
            isSwipeRefreshing = true;
            requestData();
        } else {
            LogUtil.d(this, "repeat refresh");
        }
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
