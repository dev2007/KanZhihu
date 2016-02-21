package com.awu.kanzhihu.stores;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.awu.kanzhihu.R;
import com.awu.kanzhihu.actions.Action;
import com.awu.kanzhihu.actions.PostListAction;
import com.awu.kanzhihu.entity.Post;
import com.awu.kanzhihu.entity.PostsCollection;
import com.awu.kanzhihu.util.Define;
import com.google.gson.Gson;

import awu.com.awutil.LogUtil;

/**
 * Created by yoyo on 2016/2/20.
 */
public class PostListStore extends Store {
    private int latestPublishTime = 0;
    private int oldestPublishTime = 0;
    private StoreChangeEvent event = null;
    private PostsCollection collection = null;
    private boolean loadNew = true;

    public PostListStore() {
        super();
        event = new RequestEvent(RequestEvent.DEFAULT);
    }

    public int getLatestPublishTime(){
        return latestPublishTime;
    }

    public int getOldestPublishTime(){
        return oldestPublishTime;
    }

    public PostsCollection getCollection() {
        return collection;
    }

    @Override
    public StoreChangeEvent changeEvent() {
        LogUtil.d(this,"storeChangeEvent");
        return event;
    }

    @Override
    public void onAction(Action action) {
        LogUtil.d(this, "onAction");
        switch (action.getType()) {
            case PostListAction.ACTION_CHECK_NEW:
                requestCheckNew();
                break;
            case PostListAction.ACTION_GET_POST_LIST:
                requestData();
                break;
            case PostListAction.ACTION_GET_OLD_POST_LIST:
                requestOldData();
                break;
            case PostListAction.ACTION_LATEST_TIME:
                updateLatestTime(Integer.parseInt((String)action.getData()));
                break;
            case PostListAction.ACTION_OLDEST_TIME:
                updateOldestTime(Integer.parseInt((String)action.getData()));
                break;
            default:
                break;
        }
    }

    private void requestOldData() {
        String url = String.format("%s/%d", Define.Url_PostList, oldestPublishTime);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                collection = gson.fromJson(response, PostsCollection.class);
                if (!collection.getError().equals("")) {
                    LogUtil.i(this, "Push Response Error:" + collection.getError());
                    event = new RequestEvent(RequestEvent.ERROR_DATA_OLD);
                    emitStoreChange();
                    return;
                }
                getTimeStamp(collection);
                event = new RequestEvent(RequestEvent.OK_OLD);
                emitStoreChange();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null && error.getMessage() != null) {
                    event = new RequestEvent(RequestEvent.ERROR_RESPONSE_OLD,error.getMessage());
                    LogUtil.i(this, "push:" + error.getMessage());
                }
                else {
                    event = new RequestEvent(RequestEvent.ERROR_RESPONSE_OLD,"not knowed");
                    LogUtil.i(this, "push:i don't know what happen.");
                }
                emitStoreChange();
            }
        });
        mQueue.add(stringRequest);
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
                    event = new RequestEvent(RequestEvent.CHECK_NEW_FAIL);
                    emitStoreChange();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                event = new RequestEvent(RequestEvent.CHECK_NEW_FAIL);
                emitStoreChange();
            }
        });
        mQueue.add(stringRequest);
    }

    private void requestData() {
        StringRequest stringRequest = new StringRequest(Define.Url_PostList, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                collection = gson.fromJson(response, PostsCollection.class);
                if (!collection.getError().equals("")) {
                    loadNew = false;
                    LogUtil.i(this, "Response Error:" + collection.getError());
                    event = new RequestEvent(RequestEvent.ERROR_DATA);
                    emitStoreChange();
                    return;
                }
                LogUtil.i(this,"response ok");
                getTimeStamp(collection);
                event = new RequestEvent(RequestEvent.OK);
                emitStoreChange();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null && error.getMessage() != null) {
                    event = new RequestEvent(RequestEvent.ERROR_RESPONSE, error.getMessage());
                    LogUtil.i(this, error.getMessage());
                } else {
                    event = new RequestEvent(RequestEvent.ERROR_RESPONSE, "not knowed");
                    LogUtil.i(this, "i don't know what happen.");
                }
                loadNew = false;
                emitStoreChange();
            }
        });
        LogUtil.i(this, "url:" + Define.Url_PostList);
        mQueue.add(stringRequest);
    }

    private void updateLatestTime(int latestPublishTime){
        this.latestPublishTime = latestPublishTime;
    }

    private void updateOldestTime(int oldestPublishTime){
        this.oldestPublishTime = oldestPublishTime;
    }

    /**
     * get latest publish-time & oldest publish-time for old update.
     *
     * @param collection
     */
    private void getTimeStamp(PostsCollection collection) {
        LogUtil.i(this, "bind time stamp");
        if (loadNew) {
            LogUtil.i(this, "bind latest publish time");
            Post latestPost = collection.getPosts().get(0);
            latestPublishTime = latestPost.getPublishtime();
            loadNew = false;
        }
        Post oldestPost = collection.getPosts().get(collection.getCount() - 1);
        oldestPublishTime = oldestPost.getPublishtime();
    }

    public class RequestEvent extends StoreChangeEvent {
        private int state = 0;
        private String message = "";
        public static final int DEFAULT = 0;
        public static final int OK = 1;
        public static final int ERROR_RESPONSE = -1;
        public static final int ERROR_DATA = -2;
        public static final int CHECK_NEW_FAIL = -3;
        public static final int OK_OLD = 2;
        public static final int ERROR_RESPONSE_OLD = -4;
        public static final int ERROR_DATA_OLD = -5;

        public RequestEvent(int state) {
            this(state, "");
        }

        public RequestEvent(int state, String message) {
            super();
            this.state = state;
            this.message = message;
        }

        public int getState() {
            return state;
        }

        public String getMessage() {
            return message;
        }
    }
}
