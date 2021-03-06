package com.awu.kanzhihu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.awu.kanzhihu.R;
import com.awu.kanzhihu.app.KZHApp;
import com.awu.kanzhihu.fragment.PostListFragment;
import com.awu.kanzhihu.fragment.SettingFragment;
import com.awu.kanzhihu.fragment.UserFragment;
import com.awu.kanzhihu.util.Define;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "SectionPagerAdataper";

    private PostListFragment postListFragment;
    private UserFragment userFragment;
    private SettingFragment settingFragment;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
        postListFragment = new PostListFragment();
        userFragment = new UserFragment();
        settingFragment = new SettingFragment();
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
//            case 2:
//                return settingFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return KZHApp.getContext().getString(R.string.section_article);
            case 1:
                return KZHApp.getContext().getString(R.string.section_user);
//            case 2:
//                return KZHApp.appContext().getString(R.string.section_setting);
        }
        return null;
    }
}
