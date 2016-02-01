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
import com.awu.kanzhihu.activity.ArticleDetailActivity;
import com.awu.kanzhihu.adapter.PostListAdapter;
import com.awu.kanzhihu.entity.Post;
import com.awu.kanzhihu.entity.PostsCollection;
import com.awu.kanzhihu.event.RecyclerViewClickListener;
import com.awu.kanzhihu.util.Define;
import com.google.gson.Gson;

/**
 * Created by awu on 2015-12-15.
 */
public class PostListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        Response.Listener<String>,
        Response.ErrorListener {
    private static final String TAG = "PostListFragment";

    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private PostListAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private RequestQueue mQueue;
    private boolean isSwipeRefreshing = false;
    private int latestPublishTime = 0;
    private int oldestPublishTime = 0;
    private boolean loadNew = true;

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
            latestPublishTime = savedInstanceState.getInt(Define.KEY_PUBLISH_TIME);
            oldestPublishTime = savedInstanceState.getInt(Define.KEY_OLD_PUBLISH_TIME);
        }
        initProgressBar();
        initSwipeRefreshLayout();
        initMQueue();
        initRecyclerView();
        initData();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.putInt(Define.KEY_PUBLISH_TIME, latestPublishTime);
            savedInstanceState.putInt(Define.KEY_OLD_PUBLISH_TIME, oldestPublishTime);
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
                    requestOldData();
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
            requestData();
        } else {
            mProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        }
    }

    private void requestCheckNew() {
        String url = String.format("%s/%d", Define.Url_CheckNew, latestPublishTime);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (response.contains("true")) {
                    loadNew = true;
                    requestData();
                } else {
                    Toast.makeText(getActivity(), R.string.hint_nonew, Toast.LENGTH_LONG).show();
                    stopRefresh();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), R.string.hint_refresh, Toast.LENGTH_LONG).show();
                stopRefresh();
            }
        });
        mQueue.add(stringRequest);
    }

    private void requestData() {
        StringRequest stringRequest = new StringRequest(Define.Url_PostList, this, this);
        Log.i(TAG, "url:" + Define.Url_PostList);
        mQueue.add(stringRequest);
    }

    private void requestOldData() {
        String url = String.format("%s/%d", Define.Url_PostList, oldestPublishTime);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                PostsCollection collection = gson.fromJson(response, PostsCollection.class);
                if (!collection.getError().equals("")) {
                    Log.i(TAG, "Push Response Error:" + collection.getError());
                    return;
                }
                mAdapter.bindData(collection, false);
                mAdapter.notifyDataSetChanged();
                getTimeStamp(collection);
                stopRefresh();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null && error.getMessage() != null)
                    Log.i(TAG, "push:" + error.getMessage());
                else
                    Log.i(TAG, "push:i don't know what happen.");
                stopRefresh();
                Toast.makeText(getActivity(), R.string.hint_pushrefresh, Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(stringRequest);
    }

    /**
     * swiperefreshlayout refresh function.
     */
    @Override
    public void onRefresh() {
        if (!isSwipeRefreshing) {
            isSwipeRefreshing = true;
            requestCheckNew();
            Log.i(TAG, "start refresh");
        } else {
            Log.i(TAG, "is refreshing...");
        }
    }

    /**
     * request ok.
     *
     * @param response
     */
    @Override
    public void onResponse(String response) {
        Gson gson = new Gson();
        PostsCollection collection = gson.fromJson(response, PostsCollection.class);
        if (!collection.getError().equals("")) {
            Log.i(TAG, "Response Error:" + collection.getError());
            return;
        }
        Log.i(TAG, "Count:" + collection.getCount());
        mAdapter.bindData(collection, true);
        Log.i(TAG, "notify data changed");
        mAdapter.notifyDataSetChanged();
        getTimeStamp(collection);
        stopRefresh();
    }

    /**
     * get latest publish-time & oldest publish-time for old update.
     *
     * @param collection
     */
    private void getTimeStamp(PostsCollection collection) {
        Log.i(TAG, "bind time stamp");
        if (loadNew) {
            Log.i(TAG, "bind latest publish time");
            Post latestPost = collection.getPosts().get(0);
            latestPublishTime = latestPost.getPublishtime();
            loadNew = false;
        }
        Post oldestPost = collection.getPosts().get(collection.getCount() - 1);
        oldestPublishTime = oldestPost.getPublishtime();
    }

    /**
     * request error.
     *
     * @param error
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        if (error != null && error.getMessage() != null)
            Log.i(TAG, error.getMessage());
        else
            Log.i(TAG, "i don't know what happen.");
        stopRefresh();
        loadNew = false;
        Toast.makeText(getActivity(), R.string.hint_refresh, Toast.LENGTH_LONG).show();
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
