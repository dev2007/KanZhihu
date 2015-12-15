package com.awu.kanzhihu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.awu.kanzhihu.R;
import com.awu.kanzhihu.app.KZHApp;
import com.awu.kanzhihu.fragment.ArticleFragment;
import com.awu.kanzhihu.fragment.SettingFragment;
import com.awu.kanzhihu.fragment.UserFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private ArticleFragment articleFragment;
    private UserFragment userFragment;
    private SettingFragment settingFragment;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
        articleFragment = new ArticleFragment();
        userFragment = new UserFragment();
        settingFragment = new SettingFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return articleFragment;
            case 1:
                return userFragment;
            case 2:
                return settingFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return KZHApp.appContext().getString(R.string.section_article);
            case 1:
                return KZHApp.appContext().getString(R.string.section_user);
            case 2:
                return KZHApp.appContext().getString(R.string.section_setting);
        }
        return null;
    }
}
