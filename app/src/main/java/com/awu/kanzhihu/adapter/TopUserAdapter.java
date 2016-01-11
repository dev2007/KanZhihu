package com.awu.kanzhihu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.awu.kanzhihu.R;
import com.awu.kanzhihu.app.KZHApp;
import com.awu.kanzhihu.entity.TopUserAgree;
import com.awu.kanzhihu.entity.TopUserAnswer;
import com.awu.kanzhihu.entity.TopUserAsk;
import com.awu.kanzhihu.entity.TopUserFav;
import com.awu.kanzhihu.entity.TopUserFollower;
import com.awu.kanzhihu.entity.TopUserThanks;
import com.awu.kanzhihu.event.RecyclerViewClickListener;
import com.awu.kanzhihu.util.Define;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

/**
 * Created by awu on 2015-12-30.
 */
public class TopUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "TopUserAdapter";
    private RequestQueue mQueue;
    private RecyclerViewClickListener mClickListener;
    private ImageLoader mImageLoader;
    private ArrayList mArrayList;
    private final int TYPE_ITEM = 0;
    private final int TYPE_FOOTER = 1;

    public TopUserAdapter(RequestQueue rQueue, RecyclerViewClickListener clickListener) {
        this.mArrayList = new ArrayList<>();
        this.mQueue = rQueue;
        this.mImageLoader = new ImageLoader(mQueue, KZHApp.bitmapCacheInstance());
        this.mClickListener = clickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            FooterViewHolder holder = new FooterViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.footerview, parent, false));
            return holder;
        } else {
            TopUserViewHolder holder = new TopUserViewHolder(
                    LayoutInflater.from(KZHApp.appContext()).inflate(R.layout.item_topuser, parent, false),
                    mClickListener);
            return holder;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TopUserViewHolder) {
            Log.i(TAG, "load row data,position:" + position);
            Object topUser = mArrayList.get(position);
            TopUserViewHolder viewHolder = (TopUserViewHolder) holder;
            if (topUser instanceof TopUserAgree) {
                TopUserAgree topUserAgree = (TopUserAgree) topUser;
                loadPicture(topUserAgree.getAvatar(), viewHolder.roundedImageViewAvatar);
                viewHolder.textViewName.setText(topUserAgree.getName());
                viewHolder.textViewAgree.setText("" + topUserAgree.getAgree());
                viewHolder.textViewSignature.setText(topUserAgree.getSignature());
                viewHolder.textViewOrder.setText("" + (position + 1));
                viewHolder.textViewTopName.setText(Define.ParamName.Agree.getShowName());
            } else if (topUser instanceof TopUserAsk) {
                TopUserAsk topUserAsk = (TopUserAsk) topUser;
                loadPicture(topUserAsk.getAvatar(), viewHolder.roundedImageViewAvatar);
                viewHolder.textViewName.setText(topUserAsk.getName());
                viewHolder.textViewAgree.setText("" + topUserAsk.getAsk());
                viewHolder.textViewSignature.setText(topUserAsk.getSignature());
                viewHolder.textViewOrder.setText("" + (position + 1));
                viewHolder.textViewTopName.setText(Define.ParamName.Ask.getShowName());
            } else if (topUser instanceof TopUserAnswer) {
                TopUserAnswer topUserAnswer = (TopUserAnswer) topUser;
                loadPicture(topUserAnswer.getAvatar(), viewHolder.roundedImageViewAvatar);
                viewHolder.textViewName.setText(topUserAnswer.getName());
                viewHolder.textViewAgree.setText("" + topUserAnswer.getAnswer());
                viewHolder.textViewSignature.setText(topUserAnswer.getSignature());
                viewHolder.textViewOrder.setText("" + (position + 1));
                viewHolder.textViewTopName.setText(Define.ParamName.Answer.getShowName());
            } else if (topUser instanceof TopUserFollower) {
                TopUserFollower topUserFollower = (TopUserFollower) topUser;
                loadPicture(topUserFollower.getAvatar(), viewHolder.roundedImageViewAvatar);
                viewHolder.textViewName.setText(topUserFollower.getName());
                viewHolder.textViewAgree.setText("" + topUserFollower.getFollower());
                viewHolder.textViewSignature.setText(topUserFollower.getSignature());
                viewHolder.textViewOrder.setText("" + (position + 1));
                viewHolder.textViewTopName.setText(Define.ParamName.Follower.getShowName());
            } else if (topUser instanceof TopUserThanks) {
                TopUserThanks topUserThanks = (TopUserThanks) topUser;
                loadPicture(topUserThanks.getAvatar(), viewHolder.roundedImageViewAvatar);
                viewHolder.textViewName.setText(topUserThanks.getName());
                viewHolder.textViewAgree.setText("" + topUserThanks.getThanks());
                viewHolder.textViewSignature.setText(topUserThanks.getSignature());
                viewHolder.textViewOrder.setText("" + (position + 1));
                viewHolder.textViewTopName.setText(Define.ParamName.Thanks.getShowName());
            } else if (topUser instanceof TopUserFav) {
                TopUserFav topUserFav = (TopUserFav) topUser;
                loadPicture(topUserFav.getAvatar(), viewHolder.roundedImageViewAvatar);
                viewHolder.textViewName.setText(topUserFav.getName());
                viewHolder.textViewAgree.setText("" + topUserFav.getFav());
                viewHolder.textViewSignature.setText(topUserFav.getSignature());
                viewHolder.textViewOrder.setText("" + (position + 1));
                viewHolder.textViewTopName.setText(Define.ParamName.Fav.getShowName());
            }
        }
    }

    private void loadPicture(String url, final RoundedImageView iv) {
        iv.setTag(url);
        ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    String tag = (String) iv.getTag();
                    if (tag.equals(response.getRequestUrl()))
                        iv.setImageBitmap(response.getBitmap());
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };
        mImageLoader.get(url, listener, 100, 100);
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    /**
     * attach data with adapter.
     *
     * @param collection  the source data need to be added.
     * @param appendToEnd true,the new data.
     */
    public void bindData(ArrayList collection, boolean appendToEnd) {
        if (appendToEnd) {
            append(mArrayList, collection, appendToEnd);
        } else {
            mArrayList = collection;
        }
    }

    public void clearData(){
        mArrayList = new ArrayList();
    }

    public ArrayList getData(){
        return (ArrayList)mArrayList.clone();
    }

    private <T> void append(ArrayList<T> sourceList, ArrayList<T> otherList, boolean appendToEnd) {
        //if append to end
        if (appendToEnd) {
            for (T topUserAgree : otherList) {
                sourceList.add(topUserAgree);
            }
        } else {
            for (T topUserAgree : sourceList) {
                otherList.add(topUserAgree);
            }
            sourceList = otherList;
        }
    }

    /**
     * get data by position.
     *
     * @param position
     * @return
     */
    public Object getData(int position) {
        return mArrayList.get(position);
    }

    class TopUserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RecyclerViewClickListener mClickListener;
        private RoundedImageView roundedImageViewAvatar;
        private TextView textViewName;
        private TextView textViewAgree;
        private TextView textViewSignature;
        private TextView textViewOrder;
        private TextView textViewTopName;

        public TopUserViewHolder(View itemView, RecyclerViewClickListener clickListener) {
            super(itemView);
            Log.i(TAG, "create view holder");
            this.mClickListener = clickListener;
            itemView.setOnClickListener(this);
            roundedImageViewAvatar = (RoundedImageView) itemView.findViewById(R.id.iv_avatar);
            textViewName = (TextView) itemView.findViewById(R.id.tv_name);
            textViewAgree = (TextView) itemView.findViewById(R.id.tv_agree);
            textViewSignature = (TextView) itemView.findViewById(R.id.tv_signature);
            textViewOrder = (TextView) itemView.findViewById(R.id.tv_order);
            textViewTopName = (TextView)itemView.findViewById(R.id.tv_topname);
        }

        @Override
        public void onClick(View v) {
            Log.e(TAG, "click");
            if (mClickListener != null) {
                mClickListener.onItemClick(v, getPosition());
            }
        }
    }
}
