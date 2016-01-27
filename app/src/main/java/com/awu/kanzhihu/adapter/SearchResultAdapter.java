package com.awu.kanzhihu.adapter;

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
import com.awu.kanzhihu.entity.SearchResult;
import com.awu.kanzhihu.entity.SearchUser;
import com.awu.kanzhihu.event.RecyclerViewClickListener;
import com.awu.kanzhihu.util.CommonUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

/**
 * Created by yoyo on 2016/1/12.
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultHolder> {
    private static final String TAG = "SearchResultAdapter";
    private ArrayList<SearchUser> mData;
    private RecyclerViewClickListener mClickListener;
    private RequestQueue mQueue;
    private ImageLoader mImageLoader;

    public SearchResultAdapter(RequestQueue queue, RecyclerViewClickListener clickListener) {
        this.mData = new ArrayList<>();
        this.mClickListener = clickListener;
        this.mQueue = queue;
        this.mImageLoader = new ImageLoader(mQueue, KZHApp.bitmapCacheInstance());
    }

    @Override
    public SearchResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SearchResultHolder holder = new SearchResultHolder(
                LayoutInflater.from(KZHApp.getContext()).inflate(R.layout.item_searchresult,
                        parent, false), mClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(SearchResultHolder holder, int position) {
        SearchUser user = mData.get(position);
        loadPicture(user.getAvatar(), holder.imageView);
        holder.textViewName.setText(user.getName() + "(" + user.getId() + ")");
        holder.textViewSignature.setText(user.getSignature());
        holder.textViewAgree.setText(KZHApp.getContext().getString(R.string.text_agree) + user.getAgree());
        holder.textViewAnswer.setText(KZHApp.getContext().getString(R.string.text_answer) + user.getAnswer());
        holder.textViewFollower.setText(KZHApp.getContext().getString(R.string.text_follower) + user.getFollower());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public SearchUser getData(int position) {
        if (mData.size() != 0)
            return mData.get(position);
        else
            return null;
    }

    public void bindData(ArrayList<SearchUser> data) {
        this.mData = data;
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
        mImageLoader.get(url, listener, CommonUtil.convertImageSize(30), CommonUtil.convertImageSize(30));

    }

    class SearchResultHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RecyclerViewClickListener mClickListener;
        private RoundedImageView imageView;
        private TextView textViewName;
        private TextView textViewSignature;
        private TextView textViewAnswer;
        private TextView textViewAgree;
        private TextView textViewFollower;

        public SearchResultHolder(View itemView, RecyclerViewClickListener clickListener) {
            super(itemView);
            this.mClickListener = clickListener;
            itemView.setOnClickListener(this);
            this.imageView = (RoundedImageView) itemView.findViewById(R.id.iv_avatar);
            this.textViewName = (TextView) itemView.findViewById(R.id.tv_username);
            this.textViewSignature = (TextView) itemView.findViewById(R.id.tv_signature);
            this.textViewAnswer = (TextView) itemView.findViewById(R.id.tv_useranswer);
            this.textViewAgree = (TextView) itemView.findViewById(R.id.tv_useragree);
            this.textViewFollower = (TextView) itemView.findViewById(R.id.tv_userfollower);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                mClickListener.onItemClick(v, getPosition());
            }
        }
    }
}
