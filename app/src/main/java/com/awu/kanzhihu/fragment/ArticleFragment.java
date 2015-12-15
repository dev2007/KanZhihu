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
public class ArticleFragment extends Fragment {

    public ArticleFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_article,container,false);
    }
}
