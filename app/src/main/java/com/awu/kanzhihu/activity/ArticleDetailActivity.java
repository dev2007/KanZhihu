package com.awu.kanzhihu.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.awu.kanzhihu.R;
import com.awu.kanzhihu.adapter.AnswerListAdapter;
import com.awu.kanzhihu.bl.ActivityTouch;
import com.awu.kanzhihu.bl.RecyclerViewItemTouch;
import com.awu.kanzhihu.entity.Answer;
import com.awu.kanzhihu.entity.AnswerCollection;
import com.awu.kanzhihu.event.RecyclerViewClickListener;
import com.awu.kanzhihu.util.CommonUtil;
import com.awu.kanzhihu.util.Define;
import com.awu.kanzhihu.util.PreferenceUtil;
import com.awu.kanzhihu.view.DividerItemDecoration;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import awu.com.awutil.LogUtil;

public class ArticleDetailActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener,
        Response.Listener<String>,
        Response.ErrorListener{
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private AnswerListAdapter mAdapter;
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
        initToolbarNavigation();
        setTitle(intent);
        initQueue();
        initProgressBar();
        initSwipeRefreshLayout();
        initRecyclerView();
        requestData(date, name);
        initGestureDetectorRecyclerView(mRecyclerView);
    }

    private void setTitle(Intent intent) {
        if (intent != null) {
            String disDate = intent.getStringExtra(Define.KEY_DATE);
            date = CommonUtil.convert8Date(disDate);
            name = intent.getStringExtra(Define.KEY_NAME);
            String disName = Define.PostName.getDisplay(name);
            getSupportActionBar().setTitle(disName + " " + disDate);
        } else {
            getSupportActionBar().setTitle(getString(R.string.title_activity_article_detail));
        }
    }

    private void initProgressBar() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar_detail);
    }

    /**
     * create Volley RequestQueue object.
     */
    private void initQueue() {
        mQueue = Volley.newRequestQueue(this);
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout_detail);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }


    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_detail);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AnswerListAdapter(mQueue, new RecyclerViewClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Answer answer = mAdapter.getAnswer(position);
                if (view instanceof ImageView) {
                    Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                    intent.putExtra(Define.KEY_USER_HASH, answer.getAuthorhash());
                    intent.putExtra(Define.KEY_USER_AVATAR, answer.getAvatar());
                    intent.putExtra(Define.KEY_USER_NAME, answer.getAuthorname());
                    startActivity(intent);
                } else {
                    if (answer == null) return;
                    LogUtil.d(this, "" + PreferenceUtil.read(Define.KEY_USEAPPWEB, false));
                    if ((boolean) PreferenceUtil.read(Define.KEY_USEAPPWEB, false)) {
                        Intent intent = new Intent(getApplicationContext(), AnswerActivity.class);
                        intent.putExtra(Define.KEY_QUESTIONID, answer.getQuestionid());
                        intent.putExtra(Define.KEY_ANSWERID, answer.getAnswerid());
                        intent.putExtra(Define.KEY_ANSWER_TITLE, answer.getTitle());
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        String url = String.format("%s/%s/answer/%s", Define.Url_Answer,
                                answer.getQuestionid(), answer.getAnswerid());
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
//                DividerItemDecoration.VERTICAL_LIST));
    }


    /**
     * SwipeRefreshLayout refresh event.
     */
    @Override
    public void onRefresh() {
        if (!isRefreshing) {
            isRefreshing = true;
            requestData(date, name);
        } else {
        }
    }

    private void requestData(String publishTime, String name) {
        String url = String.format("%s/%s/%s", Define.Url_AnswerList, publishTime, name);
        LogUtil.d(this, url);
        StringRequest stringRequest = new StringRequest(url, this, this);
        mQueue.add(stringRequest);
    }

    @Override
    public void onResponse(String response) {
        LogUtil.d(this, response);
        Gson gson = new Gson();
        AnswerCollection collection = gson.fromJson(response, AnswerCollection.class);
        if (!collection.getError().equals("")) {
            LogUtil.d(this, "Response Error:" + collection.getError());
            return;
        }
        mAdapter.bindData(collection);
        mAdapter.notifyDataSetChanged();
        setNoRefresh();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (error != null && error.getMessage() != null)
            LogUtil.d(this, error.getMessage());
        else
            LogUtil.d(this, "i don't know what happen.");
        Toast.makeText(this, R.string.hint_refresh, Toast.LENGTH_LONG).show();
        setNoRefresh();
    }

    private void setNoRefresh() {
        if (mSwipeRefreshLayout.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        }

        if (mSwipeRefreshLayout.isRefreshing()) {
            isRefreshing = false;
            mSwipeRefreshLayout.setRefreshing(false);
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
