package com.awu.kanzhihu.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.awu.kanzhihu.R;
import com.awu.kanzhihu.actions.ActionCreator;
import com.awu.kanzhihu.activity.ArticleDetailActivity;
import com.awu.kanzhihu.adapter.PostListAdapter;
import com.awu.kanzhihu.dispatcher.Dispatcher;
import com.awu.kanzhihu.entity.Post;
import com.awu.kanzhihu.entity.PostsCollection;
import com.awu.kanzhihu.event.RecyclerViewClickListener;
import com.awu.kanzhihu.stores.PostListStore;
import com.awu.kanzhihu.stores.Store;
import com.awu.kanzhihu.util.Define;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import awu.com.awutil.LogUtil;

/**
 * Created by awu on 2015-12-15.
 */
public class PostListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "PostListFragment";

    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private PostListAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private RequestQueue mQueue;
    private boolean isSwipeRefreshing = false;


    private PostListStore store;
    private ActionCreator actionCreator;

    public PostListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "create view");
        return inflater.inflate(R.layout.fragment_article, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "activity:" + getActivity().toString());
        if (savedInstanceState != null) {
            actionCreator.updatePostLatestTime(savedInstanceState.getInt(Define.KEY_PUBLISH_TIME));
            actionCreator.updatePostOlddestTime(savedInstanceState.getInt(Define.KEY_OLD_PUBLISH_TIME));
        }

        store = new PostListStore();
        Dispatcher.instance().register(store);
        actionCreator = ActionCreator.instance(Dispatcher.instance());

        initProgressBar();
        initSwipeRefreshLayout();
        initMQueue();
        initRecyclerView();
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.i(this, "resume");
        store.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.i(this, "pause");
        store.unregister(this);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.putInt(Define.KEY_PUBLISH_TIME, store.getLatestPublishTime());
            savedInstanceState.putInt(Define.KEY_OLD_PUBLISH_TIME, store.getOldestPublishTime());
        }
    }


    private void initProgressBar() {
        mProgressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && mLinearLayoutManager.findLastVisibleItemPosition() + 1
                        == mAdapter.getItemCount()) {
                    actionCreator.requestOldPostList();
                }
            }
        });

        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        if (mAdapter == null) {
            mAdapter = new PostListAdapter(mQueue, new RecyclerViewClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
                    Post post = mAdapter.getPost(position);
                    if (post == null) return;
                    intent.putExtra(Define.KEY_DATE, post.getDate());
                    intent.putExtra(Define.KEY_NAME, post.getName());
                    intent.putExtra(Define.KEY_PUBLISH_TIME, post.getPublishtime());
                    startActivity(intent);
                }
            });
        }

        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
//                DividerItemDecoration.VERTICAL_LIST));
    }

    private void initMQueue() {
        mQueue = Volley.newRequestQueue(getActivity());
    }

    protected void initData() {
        if (mAdapter.getItemCount() == 0) {
            Log.i(TAG, "first request data");
            actionCreator.requestPostList();
        } else {
            mProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        }
    }


    @Subscribe
    public void onStoreChange(Store.StoreChangeEvent event) {
        LogUtil.i(this, "store change");
        if (event instanceof PostListStore.RequestEvent) {
            PostListStore.RequestEvent requestEvent = (PostListStore.RequestEvent) event;
            switch (requestEvent.getState()) {
                case PostListStore.RequestEvent.OK:
                    eventOk();
                    break;
                case PostListStore.RequestEvent.ERROR_RESPONSE:
                case PostListStore.RequestEvent.ERROR_DATA:
                    eventError();
                    break;
                case PostListStore.RequestEvent.CHECK_NEW_FAIL:
                    eventCheckNewFail();
                    break;
                case PostListStore.RequestEvent.OK_OLD:
                    eventOKOld();
                    break;
                case PostListStore.RequestEvent.ERROR_RESPONSE_OLD:
                case PostListStore.RequestEvent.ERROR_DATA_OLD:
                    eventErrorOld();
                    break;
                default:
                    break;
            }
        }
    }

    private void eventOk() {
        mAdapter.bindData(store.getCollection(), true);
        mAdapter.notifyDataSetChanged();
        stopRefresh();
    }

    private void eventError() {
        stopRefresh();

        Toast.makeText(getActivity(), R.string.hint_refresh, Toast.LENGTH_LONG).show();
    }

    private void eventCheckNewFail() {
        Toast.makeText(getActivity(), R.string.hint_nonew, Toast.LENGTH_LONG).show();
        stopRefresh();
    }

    private void eventOKOld() {
        mAdapter.bindData(store.getCollection(), false);
        mAdapter.notifyDataSetChanged();
        stopRefresh();
    }

    private void eventErrorOld() {
        stopRefresh();
        Toast.makeText(getActivity(), R.string.hint_pushrefresh, Toast.LENGTH_SHORT).show();
    }

    /**
     * swiperefreshlayout refresh function.
     */
    @Override
    public void onRefresh() {
        if (!isSwipeRefreshing) {
            isSwipeRefreshing = true;
            actionCreator.requestNewPost();
            Log.i(TAG, "start refresh");
        } else {
            Log.i(TAG, "is refreshing...");
        }
    }


    private void stopRefresh() {
        if (mSwipeRefreshLayout.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        }

        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
            isSwipeRefreshing = false;
        }
    }
}
