package com.awu.kanzhihu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.awu.kanzhihu.R;
import com.awu.kanzhihu.app.KZHApp;
import com.awu.kanzhihu.entity.UserTopAnswer;
import com.awu.kanzhihu.event.RecyclerViewClickListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by awu on 2015-12-29.
 */
public class UserHighVoteAdapter extends RecyclerView.Adapter<UserHighVoteAdapter.UserHighVoteHolder>{
    private static final String TAG = "UserHighVoteAdapter";
    private ArrayList<UserTopAnswer> topAnswerArrayList;
    private RecyclerViewClickListener mClickListener;

    public UserHighVoteAdapter(RecyclerViewClickListener clickListener){
        this.topAnswerArrayList = new ArrayList<>();
        this.mClickListener = clickListener;
    }

    @Override
    public UserHighVoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UserHighVoteHolder holder = new UserHighVoteHolder(
                LayoutInflater.from(KZHApp.appContext()).inflate(R.layout.item_userhighvote, parent, false),
                mClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(UserHighVoteHolder holder, int position) {
        UserTopAnswer topAnswer = topAnswerArrayList.get(position);

        holder.textViewVote.setText(""+topAnswer.getAgree());
        String dateStr = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date date = format.parse(topAnswer.getDate());
            dateStr = format.format(date);
        }catch (Exception e){

        }

        holder.textViewDate.setText(dateStr);
        holder.textViewTitle.setText(topAnswer.getTitle());
    }

    @Override
    public int getItemCount() {
        return topAnswerArrayList.size();
    }

    /**
     * get the row data by position.
     * @param position
     * @return
     */
    public UserTopAnswer getTopAnswer(int position){
        if(topAnswerArrayList.size() != 0){
            return topAnswerArrayList.get(position);
        }
        return null;
    }

    /**
     * bind data.
     * @param data
     */
    public void bindData(ArrayList<UserTopAnswer> data){
        if(topAnswerArrayList.size() == 0)
            topAnswerArrayList = data;
    }

    class UserHighVoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RecyclerViewClickListener mClickListener;
        private TextView textViewVote;
        private TextView textViewDate;
        private TextView textViewTitle;

        public UserHighVoteHolder(View itemView, RecyclerViewClickListener clickListener) {
            super(itemView);
            this.mClickListener = clickListener;
            itemView.setOnClickListener(this);
            textViewVote = (TextView) itemView.findViewById(R.id.tv_vote);
            textViewDate = (TextView) itemView.findViewById(R.id.tv_date);
            textViewTitle = (TextView) itemView.findViewById(R.id.tv_title);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                mClickListener.onItemClick(v, getPosition());
            }
        }
    }
}
