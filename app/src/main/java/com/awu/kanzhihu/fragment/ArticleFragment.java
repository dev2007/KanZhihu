package com.awu.kanzhihu.fragment;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.awu.kanzhihu.R;
import com.awu.kanzhihu.adapter.RecyclerAdapter;
import com.awu.kanzhihu.entity.PostsCollection;
import com.awu.kanzhihu.util.Define;
import com.google.gson.Gson;

/**
 * Created by awu on 2015-12-15.
 */
public class ArticleFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        Response.Listener<String>,
        Response.ErrorListener{
    private static final String TAG = "ArticleFragment";

    private ProgressDialog progressDialog;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private PostsCollection mPostsCollection;
    private RecyclerAdapter mAdapter;
    private RequestQueue mQueue;
    private boolean isSwipeRefreshing =false;

    public ArticleFragment() {
        mPostsCollection = new PostsCollection();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initProgressDialog();
        initSwipeRefreshLayout();
        initRecyclerView();
    }

    private void initProgressDialog(){
        progressDialog = new ProgressDialog(getActivity());
    }

    private void initSwipeRefreshLayout(){
        mSwipeRefreshLayout = (SwipeRefreshLayout)getActivity().findViewById(R.id.swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeColors(R.color.colorRed,R.color.colorGreen,R.color.colorBlue,R.color.colorRed);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initRecyclerView() {
        initData();
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter = new RecyclerAdapter(mQueue));
    }

    protected void initData() {
        progressDialog.show();
        mQueue = Volley.newRequestQueue(getActivity());
        requestData();
    }

    private void requestData(){
        StringRequest stringRequest = new StringRequest(Define.Url_PostList,this,this);
        mQueue.add(stringRequest);
    }

    /**
     * swiperefreshlayout refresh function.
     */
    @Override
    public void onRefresh() {
        if(!isSwipeRefreshing) {
            isSwipeRefreshing = true;
            requestData();
            Log.i(TAG,"start refresh");
        }else{
            Log.i(TAG,"is refreshing...");
        }
    }

    /**
     * request ok.
     * @param response
     */
    @Override
    public void onResponse(String response) {
        Log.i(TAG, response);
        Gson gson = new Gson();
        PostsCollection collection = gson.fromJson(response, PostsCollection.class);
        if(!collection.getError().equals("")){
            Log.i(TAG,"Response Error:"+collection.getError());
            return;
        }
        Log.i(TAG, "Count:" + collection.getCount());
        mAdapter.bindData(collection);
        Log.i(TAG,"notify data changed");
        mAdapter.notifyDataSetChanged();

        if(progressDialog.isShowing()) {
            Log.i(TAG,"ok progressdialog stop show");
            progressDialog.dismiss();
        }
        if(mSwipeRefreshLayout.isRefreshing()) {
            Log.i(TAG, "ok swiperefreshlayout stop refresh");
            mSwipeRefreshLayout.setRefreshing(false);
            isSwipeRefreshing = false;
        }
    }

    /**
     * request error.
     * @param error
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        if(error != null && error.getMessage() != null)
            Log.i(TAG,error.getMessage());
        else
            Log.i(TAG,"i don't know what happen.");

        if(progressDialog.isShowing()) {
            Log.i(TAG,"error progressdialog stop show");
            progressDialog.dismiss();
        }
        if(mSwipeRefreshLayout.isRefreshing()) {
            Log.i(TAG, "error swiperefreshlayout stop refresh");
            mSwipeRefreshLayout.setRefreshing(false);
            isSwipeRefreshing = false;
        }
        Toast.makeText(getActivity(),R.string.hint_refresh,Toast.LENGTH_LONG).show();
    }
}
