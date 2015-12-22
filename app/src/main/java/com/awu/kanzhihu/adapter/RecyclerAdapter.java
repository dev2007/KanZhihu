package com.awu.kanzhihu.adapter;

import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.awu.kanzhihu.R;
import com.awu.kanzhihu.app.KZHApp;
import com.awu.kanzhihu.entity.Post;
import com.awu.kanzhihu.entity.PostsCollection;
import com.awu.kanzhihu.event.RecyclerViewClickListener;
import com.awu.kanzhihu.util.BitmapCache;
import com.awu.kanzhihu.util.Define;

import java.util.ArrayList;

/**
 * Created by awu on 2015-12-17.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    private static final String TAG = "RecyclerAdapter";

    private ArrayList<Post> postList;
    private RequestQueue mQueue;
    private ImageLoader mImageLoader;

    private RecyclerViewClickListener mClickListener;

    public RecyclerAdapter(RequestQueue rQueue) {
        this.postList = new ArrayList<>();
        this.mQueue = rQueue;
        mImageLoader = new ImageLoader(mQueue, new BitmapCache());
    }

    public RecyclerAdapter(RequestQueue rQueue,RecyclerViewClickListener clickListener){
        this(rQueue);
        this.mClickListener = clickListener;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewHolder holder = new RecyclerViewHolder(
                LayoutInflater.from(KZHApp.appContext()).inflate(R.layout.item_home, parent, false),mClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Post post = postList.get(position);
        Log.i(TAG, "load row,date:" + post.getDate() + " name:" + post.getName() + " position:" + position);
        loadPicture(post.getPic(), holder.imageViewPic);
        holder.textViewDate.setText(post.getDate());

        String name = Define.PostName.getDisplay(post.getName());
        holder.textViewName.setText(name);

        String countStr = String.format("%d%s", post.getCount(), KZHApp.appContext().getString(R.string.text_articlecount));
        holder.textViewCount.setText(countStr);
    }

    /**
     * load picture
     *
     * @param url
     * @param imageView
     */
    private void loadPicture(String url, final ImageView imageView) {
        Log.i(TAG, "load list pic");

        imageView.setTag(url);
        ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    String tag = (String)imageView.getTag();
                    if(tag.equals(response.getRequestUrl()))
                        imageView.setImageBitmap(response.getBitmap());
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };
         mImageLoader.get(url, listener, 600, 300);

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    /**
     * attach data with adapter.
     *
     * @param collection
     */
    public void bindData(PostsCollection collection) {
        if (postList.size() == 0)
            postList = collection.getPosts();
    }

    /**
     * get the post data by the position.
     * @param position
     * @return if get,return the post object,else return null.
     */
    public Post getPost(int position){
        if(postList.size() != 0){
            return postList.get(position);
        }else{
            return null;
        }
    }

    /**
     * Set RecyclerView's click Listener.
     * @param clickListener
     */
    public void setClickListener(RecyclerViewClickListener clickListener){
        this.mClickListener = clickListener;
    }


    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RecyclerViewClickListener mClickListener;
        private ImageView imageViewPic;
        private TextView textViewDate;
        private TextView textViewName;
        private TextView textViewCount;

        public RecyclerViewHolder(View view,RecyclerViewClickListener clickListener) {
            super(view);
            this.mClickListener = clickListener;
            imageViewPic = (ImageView) view.findViewById(R.id.iv_pic);
            textViewDate = (TextView) view.findViewById(R.id.tv_date);
            textViewName = (TextView) view.findViewById(R.id.tv_name);
            textViewCount = (TextView) view.findViewById(R.id.tv_count);
            view.setOnClickListener(this);
        }


        /**
         * Click event.
         * @param v
         */
        @Override
        public void onClick(View v) {
            if(mClickListener != null){
                mClickListener.onItemClick(v,getPosition());
            }
        }
    }
}
