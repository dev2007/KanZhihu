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
import android.widget.ProgressBar;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.awu.kanzhihu.R;
import com.awu.kanzhihu.activity.ArticleDetailActivity;
import com.awu.kanzhihu.adapter.RecyclerAdapter;
import com.awu.kanzhihu.adapter.TopUserAdapter;
import com.awu.kanzhihu.entity.Post;
import com.awu.kanzhihu.entity.TopUserAgree;
import com.awu.kanzhihu.entity.TopUserAnswer;
import com.awu.kanzhihu.entity.TopUserAsk;
import com.awu.kanzhihu.entity.TopUserFav;
import com.awu.kanzhihu.entity.TopUserFollower;
import com.awu.kanzhihu.entity.TopUserList;
import com.awu.kanzhihu.entity.TopUserThanks;
import com.awu.kanzhihu.event.RecyclerViewClickListener;
import com.awu.kanzhihu.util.Define;
import com.awu.kanzhihu.view.DividerItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by awu on 2015-12-15.
 */
public class UserFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "UserFragment";
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isSwipeRefreshing = false;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private RequestQueue mQueue;
    private TopUserAdapter mAdapter;
    private int currentPage = 1;
    private Define.ParamName mParamName = Define.ParamName.Agree;
    private ArrayList mUserListCache;

    public UserFragment() {
        mUserListCache = new ArrayList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "activity:" + getActivity().toString());
        initProgressBar();
        initSwipeRefreshLayout();
        initMQueue();
        initRecyclerView();
        requestData();
    }

    private void initProgressBar() {
        mProgressBar = (ProgressBar) getView().findViewById(R.id.progressBar_user);
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swiperefreshlayout_user);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recyclerview_user);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && mLinearLayoutManager.findLastVisibleItemPosition() + 1
                        == mAdapter.getItemCount()) {
                    requestData();
                }
            }
        });

        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        if (mAdapter == null) {
            mAdapter = new TopUserAdapter(mQueue, new RecyclerViewClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    //no action.
                }
            });
        }

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
    }

    private void initMQueue() {
        mQueue = Volley.newRequestQueue(getActivity());
    }

    public void requestParamData(Define.ParamName newParamName) {
        mSwipeRefreshLayout.setRefreshing(true);
        this.mParamName = newParamName;
        this.currentPage = 1;
        requestData();
    }

    public void searchExpand(){
        Log.i(TAG,"expand");
        mAdapter.clearData();
        mAdapter.notifyDataSetChanged();
    }

    public void searchCollapse(){
        Log.i(TAG,"collapse");
        mAdapter.bindData(mUserListCache,false);
        mAdapter.notifyDataSetChanged();
    }

    public void search(String query){
        Log.i(TAG,"search");
    }

    private void requestData() {
        String url = String.format("%s/%s/%s", Define.Url_TopUser, mParamName.getName(), currentPage);
        Log.i(TAG, url);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                TopUserList topUserList = null;
                switch (mParamName) {
                    case Agree:
                        topUserList = gson.fromJson(response, new TypeToken<TopUserList<TopUserAgree>>() {
                        }.getType());
                        break;
                    case Ask:
                        topUserList = gson.fromJson(response, new TypeToken<TopUserList<TopUserAsk>>() {
                        }.getType());
                        break;
                    case Answer:
                        topUserList = gson.fromJson(response, new TypeToken<TopUserList<TopUserAnswer>>() {
                        }.getType());
                        break;
                    case Follower:
                        topUserList = gson.fromJson(response, new TypeToken<TopUserList<TopUserFollower>>() {
                        }.getType());
                        break;
                    case Thanks:
                        topUserList = gson.fromJson(response, new TypeToken<TopUserList<TopUserThanks>>() {
                        }.getType());
                        break;
                    case Fav:
                        topUserList = gson.fromJson(response, new TypeToken<TopUserList<TopUserFav>>() {
                        }.getType());
                        break;
                    default:
                        topUserList = null;
                }
                if (topUserList != null) {
                    if (!topUserList.getError().equals("")) {
                        Log.i(TAG, "response has error:" + topUserList.getError());
                    } else {
                        mUserListCache = topUserList.getTopuser();
                        mAdapter.bindData(topUserList.getTopuser(), currentPage != 1);
                        mAdapter.notifyDataSetChanged();
                        currentPage++;
                    }
                }
                stopRefresh();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null) {
                    Log.i(TAG, "I don't know why.");
                } else {
                    Log.i(TAG, "error response " + error.getMessage());
                }

                stopRefresh();
            }
        });
        mQueue.add(request);
    }

    @Override
    public void onRefresh() {
        if (!isSwipeRefreshing) {
            isSwipeRefreshing = true;
            currentPage = 1;
            requestData();
        }
    }

    private void stopRefresh() {
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
        isSwipeRefreshing = false;
    }
}
