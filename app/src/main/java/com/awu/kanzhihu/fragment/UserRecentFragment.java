package com.awu.kanzhihu.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.awu.kanzhihu.R;
import com.awu.kanzhihu.entity.UserDetails;

/**
 * Created by awu on 2015-12-28.
 */
public class UserRecentFragment extends Fragment implements IUserFragment {

    public UserRecentFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_userrecent,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void notifyDataChanged(UserDetails data) {

    }
}
