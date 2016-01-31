package com.awu.kanzhihu.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;
import com.awu.kanzhihu.R;
import com.awu.kanzhihu.adapter.MyFavAdapter;
import com.awu.kanzhihu.adapter.PostListAdapter;
import com.awu.kanzhihu.adapter.SimpleItemTouchHelperCallback;
import com.awu.kanzhihu.entity.Fav;
import com.awu.kanzhihu.entity.Post;
import com.awu.kanzhihu.event.RecyclerViewClickListener;
import com.awu.kanzhihu.util.DbUtil;
import com.awu.kanzhihu.util.Define;
import com.awu.kanzhihu.util.PreferenceUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import awu.com.awutil.LogUtil;

/**
 * MyFavActivity.
 */
public class MyFavActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private MyFavAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private boolean isSwipeRefreshing = false;
    private ItemTouchHelper itemTouchHelper;
    private ArrayList<Fav> mData = new ArrayList<>();
    private static final int MSG_OK = 1;
    private Handler noticeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_OK:
                    LogUtil.d(this, "get ok");
                    stopRefresh();
                    mAdapter.bindData(mData);
                    mAdapter.notifyDataSetChanged();
                    if (mData.size() == 0) {
                        Toast.makeText(getApplicationContext(), R.string.hint_nofav, Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fav);
        initToolbarNavigation();
        initProgressBar();
        initSwipeRefreshLayout();
        initRecyclerView();
        initData();
        initGestureDetectorRecyclerView(mRecyclerView);
    }

    private void initProgressBar() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar_fav);
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout_fav);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_fav);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        if (mAdapter == null) {
            mAdapter = new MyFavAdapter(mData, new RecyclerViewClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Fav fav = mAdapter.getFav(position);
                    if (fav != null) {
                        if ((boolean) PreferenceUtil.read(Define.KEY_USEAPPWEB, false)) {
                            Intent intent = new Intent(getApplicationContext(), AnswerActivity.class);
                            intent.putExtra(Define.KEY_ANSWER_TITLE, fav.getName());
                            intent.putExtra(Define.KEY_FAV_URL, fav.getUrl());
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(fav.getUrl()));
                            startActivity(intent);
                        }
                    }
                }
            });
        }
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(mAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }


    protected void initData() {
        if (mAdapter.getItemCount() == 0) {
            LogUtil.d(this, "first request data");
            requestData();
        } else {
            mProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        }
    }

    private void requestData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mData = DbUtil.queryAllFav();
                noticeHandler.sendEmptyMessage(MSG_OK);
            }
        }).start();
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
            isSwipeRefreshing = true;
            requestData();
            LogUtil.d(this, "start refresh");
        } else {
            LogUtil.d(this, "is refreshing...");
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
