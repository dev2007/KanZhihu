package com.awu.kanzhihu.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.awu.kanzhihu.R;
import com.awu.kanzhihu.app.KZHApp;
import com.awu.kanzhihu.entity.Post;
import com.awu.kanzhihu.entity.PostsCollection;
import com.awu.kanzhihu.event.RecyclerViewClickListener;
import com.awu.kanzhihu.util.Define;

import java.util.ArrayList;

/**
 * Created by awu on 2015-12-17.
 */
public class PostListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "PostListAdapter";

    private ArrayList<Post> postList;
    private RequestQueue mQueue;
    private ImageLoader mImageLoader;

    private RecyclerViewClickListener mClickListener;

    private final int TYPE_ITEM = 0;
    private final int TYPE_FOOTER = 1;
    private final int TYPE_ITEM_DATE = 2;

    public PostListAdapter(RequestQueue rQueue) {
        this.postList = new ArrayList<>();
        this.mQueue = rQueue;
        mImageLoader = new ImageLoader(mQueue, KZHApp.bitmapCacheInstance());
    }

    public PostListAdapter(RequestQueue rQueue, RecyclerViewClickListener clickListener) {
        this(rQueue);
        this.mClickListener = clickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            if(position == 0)
                return TYPE_ITEM_DATE;
            else {
                if(!getPost(position).getDate().equals(getPost(position - 1).getDate()))
                    return TYPE_ITEM_DATE;
                return TYPE_ITEM;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            FooterViewHolder holder = new FooterViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.footerview, parent, false));
            return holder;
        } else if(viewType == TYPE_ITEM){
            NormalAnswerHolder holder = new NormalAnswerHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false), mClickListener);
            return holder;
        }else{
            NormalAnswerWithDateHolder holder = new NormalAnswerWithDateHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_homewithdate, parent, false), mClickListener);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalAnswerHolder) {
            NormalAnswerHolder viewHolder = ((NormalAnswerHolder) holder);
            Post post = postList.get(position);

            String name = Define.PostName.getDisplay(post.getName());
            viewHolder.textViewName.setText(name);

            String countStr = String.format("%d%s", post.getCount(), KZHApp.getContext().getString(R.string.text_articlecount));
            viewHolder.textViewCount.setText(countStr);
            viewHolder.textViewExcerpt.setText(post.getExcerpt());
        }else if(holder instanceof  NormalAnswerWithDateHolder){
            NormalAnswerWithDateHolder viewHolder = ((NormalAnswerWithDateHolder) holder);
            Post post = postList.get(position);

            viewHolder.textViewDate.setText(post.getDate());

            String name = Define.PostName.getDisplay(post.getName());
            viewHolder.textViewName.setText(name);

            String countStr = String.format("%d%s", post.getCount(), KZHApp.getContext().getString(R.string.text_articlecount));
            viewHolder.textViewCount.setText(countStr);
            viewHolder.textViewExcerpt.setText(post.getExcerpt());
        }
    }

    /**
     * load picture
     *
     * @param url
     * @param imageView
     */
    private void loadPicture(String url, final ImageView imageView) {
        imageView.setTag(url);
        ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    String tag = (String) imageView.getTag();
                    if (tag.equals(response.getRequestUrl()))
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
     * @param isNew      true,the new data.
     */
    public void bindData(PostsCollection collection, boolean isNew) {
        if (isNew) {
            if (postList.size() == 0)
                postList = collection.getPosts();
            else {
                append(postList, collection.getPosts(), false);
            }
        } else {
            append(postList, collection.getPosts(), true);
        }
    }

    /**
     * merge data.
     *
     * @param sourceList  first list.
     * @param otherList   other list.
     * @param appendToEnd true,append other list to first list.false,append other list to the first list head.
     */
    private void append(ArrayList<Post> sourceList, ArrayList<Post> otherList, boolean appendToEnd) {
        //if append to end
        if (appendToEnd) {
            for (Post post : otherList) {
                sourceList.add(post);
            }
        } else {
            for (Post post : sourceList) {
                otherList.add(post);
            }
            sourceList = otherList;
        }
    }

    /**
     * get the post data by the position.
     *
     * @param position
     * @return if get,return the post object,else return null.
     */
    public Post getPost(int position) {
        if (postList.size() != 0) {
            return postList.get(position);
        } else {
            return null;
        }
    }

    /**
     * Set RecyclerView's click Listener.
     *
     * @param clickListener
     */
    public void setClickListener(RecyclerViewClickListener clickListener) {
        this.mClickListener = clickListener;
    }


    class NormalAnswerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RecyclerViewClickListener mClickListener;
        private TextView textViewName;
        private TextView textViewCount;
        private TextView textViewExcerpt;

        public NormalAnswerHolder(View view, RecyclerViewClickListener clickListener) {
            super(view);
            this.mClickListener = clickListener;
            textViewName = (TextView) view.findViewById(R.id.tv_name);
            textViewCount = (TextView) view.findViewById(R.id.tv_count);
            textViewExcerpt = (TextView)view.findViewById(R.id.tv_excerpt);

            itemView.setOnClickListener(this);
        }


        /**
         * Click event.
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                mClickListener.onItemClick(v, getPosition());
            }
        }
    }

    class NormalAnswerWithDateHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RecyclerViewClickListener mClickListener;
        private TextView textViewDate;
        private TextView textViewName;
        private TextView textViewCount;
        private TextView textViewExcerpt;
        private CardView cardView;

        public NormalAnswerWithDateHolder(View view, RecyclerViewClickListener clickListener) {
            super(view);
            this.mClickListener = clickListener;
            textViewDate = (TextView) view.findViewById(R.id.tv_date);
            textViewName = (TextView) view.findViewById(R.id.tv_name);
            textViewCount = (TextView) view.findViewById(R.id.tv_count);
            textViewExcerpt = (TextView)view.findViewById(R.id.tv_excerpt);
            cardView = (CardView)view.findViewById(R.id.cv_post);

            cardView.setOnClickListener(this);
        }


        /**
         * Click event.
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                mClickListener.onItemClick(v, getPosition());
            }
        }
    }
}
