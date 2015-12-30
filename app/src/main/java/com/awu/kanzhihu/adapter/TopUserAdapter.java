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
import com.awu.kanzhihu.event.RecyclerViewClickListener;
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
    private ArrayList<TopUserAgree> mArrayList;
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
            TopUserAgree topUserAgree = mArrayList.get(position);
            TopUserViewHolder viewHolder = (TopUserViewHolder) holder;
            loadPicture(topUserAgree.getAvatar(), viewHolder.roundedImageViewAvatar);
            viewHolder.textViewName.setText(topUserAgree.getName());
            viewHolder.textViewAgree.setText("" + topUserAgree.getAgree());
            viewHolder.textViewSiganature.setText(topUserAgree.getSignature());
            viewHolder.textViewOrder.setText("" + (position + 1));
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
        mImageLoader.get(url, listener, 600, 300);
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    /**
     * attach data with adapter.
     *
     * @param collection
     * @param appendToEnd true,the new data.
     */
    public void bindData(ArrayList<TopUserAgree> collection, boolean appendToEnd) {
        if (appendToEnd) {
            append(mArrayList, collection, appendToEnd);
        } else {
            mArrayList = collection;
        }
    }

    private void append(ArrayList<TopUserAgree> sourceList, ArrayList<TopUserAgree> otherList, boolean appendToEnd) {
        //if append to end
        if (appendToEnd) {
            for (TopUserAgree topUserAgree : otherList) {
                sourceList.add(topUserAgree);
            }
        } else {
            for (TopUserAgree topUserAgree : sourceList) {
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
    public TopUserAgree getData(int position) {
        return mArrayList.get(position);
    }

    class TopUserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RecyclerViewClickListener mClickListener;
        private RoundedImageView roundedImageViewAvatar;
        private TextView textViewName;
        private TextView textViewAgree;
        private TextView textViewSiganature;
        private TextView textViewOrder;

        public TopUserViewHolder(View itemView, RecyclerViewClickListener clickListener) {
            super(itemView);
            Log.i(TAG, "create view holder");
            this.mClickListener = clickListener;
            itemView.setOnClickListener(this);
            roundedImageViewAvatar = (RoundedImageView) itemView.findViewById(R.id.iv_avatar);
            textViewName = (TextView) itemView.findViewById(R.id.tv_name);
            textViewAgree = (TextView) itemView.findViewById(R.id.tv_agree);
            textViewSiganature = (TextView) itemView.findViewById(R.id.tv_signature);
            textViewOrder = (TextView) itemView.findViewById(R.id.tv_order);
        }

        @Override
        public void onClick(View v) {
            Log.e(TAG,"click");
            if (mClickListener != null) {
                mClickListener.onItemClick(v, getPosition());
            }
        }
    }
}
