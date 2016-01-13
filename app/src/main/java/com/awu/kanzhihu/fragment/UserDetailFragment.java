package com.awu.kanzhihu.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.awu.kanzhihu.R;
import com.awu.kanzhihu.entity.UserDetail;
import com.awu.kanzhihu.entity.UserDetails;

/**
 * Created by awu on 2015-12-28.
 */
public class UserDetailFragment extends Fragment implements IUserFragment {
    private String signature;
    private String description;
    private UserDetail detail;

    private TextView textViewSignature;
    private TextView textViewDescription;
    private TextView textViewAgree;
    private TextView textViewThanks;
    private TextView textViewFollower;
    private TextView textViewAsk;
    private TextView textViewAnswer;
    private TextView textViewPost;
    private TextView textViewFollowee;
    private TextView textViewFav;
    private TextView textViewLogs;

    public UserDetailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_userdetail, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textViewSignature = (TextView) getActivity().findViewById(R.id.tv_signature);
        textViewDescription = (TextView) getActivity().findViewById(R.id.tv_description);
        textViewAgree = (TextView) getActivity().findViewById(R.id.tv_agree);
        textViewThanks = (TextView) getActivity().findViewById(R.id.tv_thanks);
        textViewFollower = (TextView) getActivity().findViewById(R.id.tv_follower);
        textViewAsk = (TextView) getActivity().findViewById(R.id.tv_ask);
        textViewAnswer = (TextView) getActivity().findViewById(R.id.tv_answer);
        textViewPost = (TextView) getActivity().findViewById(R.id.tv_post);
        textViewFollowee = (TextView) getActivity().findViewById(R.id.tv_followee);
        textViewFav = (TextView) getActivity().findViewById(R.id.tv_fav);
        textViewLogs = (TextView) getActivity().findViewById(R.id.tv_logs);
        if (detail != null) {
            updateData();
        }
    }

    @Override
    public void notifyDataChanged(UserDetails data) {
        signature = data.getSignature();
        description = data.getDescription();
        detail = data.getDetail();
        if (textViewSignature != null)
            updateData();
    }

    private void updateData() {
        textViewSignature.setText(signature);
        textViewDescription.setText(description);
        textViewAgree.setText(" " + detail.getAgree());
        textViewThanks.setText(" " + detail.getThanks());
        textViewFollower.setText(" " + detail.getFollower());
        textViewAsk.setText(" " + detail.getAsk());
        textViewAnswer.setText(" " + detail.getAnswer());
        textViewPost.setText(" " + detail.getPost());
        textViewFollowee.setText(" " + detail.getFollowee());
        textViewFav.setText(" " + detail.getFav());
        textViewLogs.setText(" " + detail.getLogs());
    }
}
