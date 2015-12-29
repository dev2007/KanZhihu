package com.awu.kanzhihu.fragment;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.util.Log;

import com.awu.kanzhihu.R;
import com.awu.kanzhihu.entity.UserDetails;
import com.awu.kanzhihu.entity.UserStar;

/**
 * Created by awu on 2015-12-28.
 */
public class UserRankFragment extends Fragment implements IUserFragment {
    private static final String TAG = "UserRankFragment";
    private UserStar star;
    private TextView textViewAnswerRank;
    private TextView textViewAgreeRank;
    private TextView textViewRatioRank;
    private TextView textViewFollowerRank;
    private TextView textViewFavRank;
    private TextView textViewCount1000Rank;
    private TextView textViewCount100Rank;

    public UserRankFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_userrank, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textViewAnswerRank = (TextView) getActivity().findViewById(R.id.tv_answerrank);
        textViewAgreeRank = (TextView) getActivity().findViewById(R.id.tv_agreerank);
        textViewRatioRank = (TextView) getActivity().findViewById(R.id.tv_ratiorank);
        textViewFollowerRank = (TextView) getActivity().findViewById(R.id.tv_followerrank);
        textViewFavRank = (TextView) getActivity().findViewById(R.id.tv_favrank);
        textViewCount1000Rank = (TextView) getActivity().findViewById(R.id.tv_count1000rank);
        textViewCount100Rank = (TextView) getActivity().findViewById(R.id.tv_count100rank);
        if (star != null) {
            updateData();
        }
    }

    @Override
    public void notifyDataChanged(UserDetails data) {
        if (data != null)
            star = data.getStar();
        if (textViewAnswerRank != null)
            updateData();
    }

    private void updateData() {
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);

        int standardWidth = (int) ((metric.densityDpi - 16) * 0.75 * metric.widthPixels / metric.densityDpi);
        Log.i(TAG, "width:" + standardWidth);
        textViewAnswerRank.getLayoutParams().width = calcColorWidth(star.getAnswerrank(), standardWidth);
        if (star.getAnswerrank() <= 1000)
            textViewAnswerRank.setText("" + star.getAnswerrank());
        textViewAgreeRank.getLayoutParams().width = calcColorWidth(star.getAgreerank(), standardWidth);
        if (star.getAgreerank() <= 1000)
            textViewAgreeRank.setText("" + star.getAgreerank());
        textViewRatioRank.getLayoutParams().width = calcColorWidth(star.getRatiorank(), standardWidth);
        if (star.getRatiorank() <= 1000)
            textViewRatioRank.setText("" + star.getRatiorank());
        textViewFollowerRank.getLayoutParams().width = calcColorWidth(star.getFollowerrank(), standardWidth);
        if (star.getFollowerrank() <= 1000)
            textViewFollowerRank.setText("" + star.getFollowerrank());
        textViewFavRank.getLayoutParams().width = calcColorWidth(star.getFavrank(), standardWidth);
        if (star.getFavrank() <= 1000)
            textViewFavRank.setText("" + star.getFavrank());
        textViewCount1000Rank.getLayoutParams().width = calcColorWidth(star.getCount1000rank(), standardWidth);
        if (star.getCount1000rank() <= 1000)
            textViewCount1000Rank.setText("" + star.getCount1000rank());
        textViewCount100Rank.getLayoutParams().width = calcColorWidth(star.getCount100rank(), standardWidth);
        if (star.getCount100rank() <= 100)
            textViewCount100Rank.setText("" + star.getCount100rank());
    }

    private int calcColorWidth(int rankValue, int standardWidth) {
        if (rankValue <= 1000) {
            return standardWidth;
        } else {
            int temp = standardWidth / rankValue * 1000;
            if (temp < 10)
                return 10;
            else
                return temp;
        }
    }
}
