package com.awu.kanzhihu.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;

import com.awu.kanzhihu.R;
import com.awu.kanzhihu.activity.AnswerActivity;
import com.awu.kanzhihu.adapter.UserHighVoteAdapter;
import com.awu.kanzhihu.entity.UserDetails;
import com.awu.kanzhihu.entity.UserTopAnswer;
import com.awu.kanzhihu.event.RecyclerViewClickListener;
import com.awu.kanzhihu.util.Define;
import com.awu.kanzhihu.util.PreferenceUtil;
import com.awu.kanzhihu.view.DividerItemDecoration;

import java.util.ArrayList;

/**
 * Created by awu on 2015-12-28.
 */
public class UserHighVoteFragment extends Fragment implements IUserFragment {
    private static final String TAG = "UserHighVoteFragment";
    private ArrayList<UserTopAnswer> topAnswerArrayList;
    private RecyclerView mRecyclerView;
    private UserHighVoteAdapter mAdapter;

    public UserHighVoteFragment() {
        topAnswerArrayList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_userhighvote, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.rv_topvote);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (mAdapter == null)
            mAdapter = new UserHighVoteAdapter(new RecyclerViewClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    UserTopAnswer answer = mAdapter.getTopAnswer(position);
                    if (answer != null) {
                        if ((boolean) PreferenceUtil.read(Define.KEY_USEAPPWEB, false)) {
                            Intent intent = new Intent(getActivity(), AnswerActivity.class);
                            intent.putExtra(Define.KEY_ANSWER_TITLE, answer.getTitle());
                            intent.putExtra(Define.KEY_QUESTION_ANSWER, answer.getLink());
                            intent.putExtra(Define.KEY_ISPOST, answer.ispost());
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            String url;
                            if (answer.ispost()) {
                                url = String.format("%s%s", Define.Url_Zhihu_ZhuanLan, answer.getLink());
                            } else
                                url = String.format("%s%s", Define.Url_Zhihu, answer.getLink());
                            intent.setData(Uri.parse(url));
                            startActivity(intent);
                        }
                    }
                }
            });
        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
//                DividerItemDecoration.VERTICAL_LIST));
        if (topAnswerArrayList.size() != 0) {
            Log.i(TAG, "re bind data");
            mAdapter.bindData(topAnswerArrayList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void notifyDataChanged(UserDetails data) {
        Log.i(TAG, "notify data");
        topAnswerArrayList = data.getTopanswers();
        if (mAdapter != null) {
            Log.i(TAG, "bind data");
            mAdapter.bindData(topAnswerArrayList);
            mAdapter.notifyDataSetChanged();
        }
    }
}
