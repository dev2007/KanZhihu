package com.awu.kanzhihu.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.awu.kanzhihu.entity.Answer;
import com.awu.kanzhihu.entity.AnswerCollection;
import com.awu.kanzhihu.event.RecyclerViewClickListener;
import com.awu.kanzhihu.util.BitmapCache;
import com.awu.kanzhihu.util.CommonUtil;

import java.util.ArrayList;

/**
 * Created by awu on 2015-12-23.
 */
public class AnswerListAdapter extends RecyclerView.Adapter<AnswerListAdapter.AnswerListHolder> {
    private static final String TAG = "AnswerListAdapter";
    private ArrayList<Answer> answerList;
    private RequestQueue mQueue;
    private ImageLoader mImageLoader;
    private RecyclerViewClickListener mClickListener;

    public AnswerListAdapter(RequestQueue rQueue, RecyclerViewClickListener clickListener) {
        this.answerList = new ArrayList<>();
        this.mQueue = rQueue;
        this.mImageLoader = new ImageLoader(mQueue, KZHApp.bitmapCacheInstance());
        this.mClickListener = clickListener;
    }

    /**
     * bind data with adapter.
     * @param collection
     */
    public void bindData(AnswerCollection collection){
        if(answerList.size() == 0){
            answerList = collection.getAnswers();
        }
    }

    /**
     * get answer by position.
     * @param position
     * @return
     */
    public Answer getAnswer(int position){
        if(answerList.size() != 0){
            return answerList.get(position);
        }else {

            return null;
        }
    }

    @Override
    public AnswerListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AnswerListHolder holder = new AnswerListHolder(
                LayoutInflater.from(KZHApp.appContext()).inflate(R.layout.item_answer, parent, false),
                mClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(AnswerListHolder holder, int position) {
        Answer answer = answerList.get(position);
        loadPicture(answer.getAvatar(),holder.imageViewAvtar);

        holder.textViewTitle.setText(CommonUtil.toDBC(answer.getTitle()));
        holder.textViewSummary.setText(answer.getSummary());
        holder.textViewAuthorName.setText(answer.getAuthorname());
        holder.textViewVote.setText("" + answer.getVote());
    }

    @Override
    public int getItemCount() {
        return answerList.size();
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
                    String tag = (String)imageView.getTag();
                    if(tag.equals(response.getRequestUrl()))
                        imageView.setImageBitmap(response.getBitmap());
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };
        mImageLoader.get(url, listener, CommonUtil.convertImageSize(30), CommonUtil.convertImageSize(30));

    }

    class AnswerListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RecyclerViewClickListener mClickListener;
        private ImageView imageViewAvtar;
        private TextView textViewTitle;
        private TextView textViewSummary;
        private TextView textViewAuthorName;
        private TextView textViewVote;

        public AnswerListHolder(View itemView, RecyclerViewClickListener clickListener) {
            super(itemView);
            this.mClickListener = clickListener;
            itemView.setOnClickListener(this);
            textViewTitle = (TextView)itemView.findViewById(R.id.tv_title);
            textViewSummary = (TextView)itemView.findViewById(R.id.tv_summary);
            imageViewAvtar = (ImageView)itemView.findViewById(R.id.iv_avatar);
            textViewAuthorName = (TextView)itemView.findViewById(R.id.tv_authorname);
            textViewVote = (TextView)itemView.findViewById(R.id.tv_vote);
            imageViewAvtar.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                mClickListener.onItemClick(v, getPosition());
            }
        }
    }
}
