package com.awu.kanzhihu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.awu.kanzhihu.R;
import com.awu.kanzhihu.entity.Fav;
import com.awu.kanzhihu.event.RecyclerViewClickListener;
import com.awu.kanzhihu.util.DbUtil;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by yoyo on 2016/1/15.
 */
public class MyFavAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>implements ItemTouchHelperAdapter {
    private static final String TAG = "MyFavAdapter";

    private ArrayList<Fav> arrayList;
    private RecyclerViewClickListener mClickListener;

    public MyFavAdapter(ArrayList<Fav> data,RecyclerViewClickListener clickListener){
        this.mClickListener = clickListener;
        arrayList = data;
    }

    public MyFavAdapter(RecyclerViewClickListener clickListener) {
        this.mClickListener = clickListener;
        arrayList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FavViewHolder holder = new FavViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fav, parent, false)
                , mClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FavViewHolder viewHolder = (FavViewHolder)holder;
        viewHolder.textViewTitle.setText(arrayList.get(position).getName());
    }

    public void bindData(ArrayList<Fav> data){
        arrayList = data;
    }

    public Fav getFav(int position){
        if(arrayList.size() > 0)
            return arrayList.get(position);
        else
            return null;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(arrayList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);

    }

    @Override
    public void onItemDismiss(int position) {
        final String url = arrayList.get(position).getUrl();
        arrayList.remove(position);
        notifyItemRemoved(position);
        new Thread(new Runnable() {
            @Override
            public void run() {
                DbUtil.deleteFav(url);
            }
        }).start();
    }

    class FavViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RecyclerViewClickListener mClickListener;
        private TextView textViewTitle;

        public FavViewHolder(View itemView, RecyclerViewClickListener clickListener) {
            super(itemView);
            this.mClickListener = clickListener;
            itemView.setOnClickListener(this);
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
