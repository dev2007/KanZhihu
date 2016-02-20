package com.awu.kanzhihu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.awu.kanzhihu.R;
import com.awu.kanzhihu.app.KZHApp;
import com.awu.kanzhihu.fragment.PostListFragment;
import com.awu.kanzhihu.fragment.UserFragment;
import com.awu.kanzhihu.util.Define;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private PostListFragment postListFragment;
    private UserFragment userFragment;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
        postListFragment = new PostListFragment();
        userFragment = new UserFragment();
    }

    public void changeUserQuery(Define.ParamName paramName){
        userFragment.requestParamData(paramName);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return postListFragment;
            case 1:
                return userFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return KZHApp.getContext().getString(R.string.section_article);
            case 1:
                return KZHApp.getContext().getString(R.string.section_user);
        }
        return null;
    }
}
