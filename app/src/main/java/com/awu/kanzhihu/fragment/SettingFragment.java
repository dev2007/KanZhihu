package com.awu.kanzhihu.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.awu.kanzhihu.R;

/**
 * Created by awu on 2015-12-15.
 */
public class SettingFragment extends Fragment {

    public SettingFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstaceState){
        return inflater.inflate(R.layout.fragment_setting,container,false);
    }
}
