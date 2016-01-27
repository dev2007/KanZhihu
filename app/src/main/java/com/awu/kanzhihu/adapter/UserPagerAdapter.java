package com.awu.kanzhihu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.awu.kanzhihu.R;
import com.awu.kanzhihu.app.KZHApp;
import com.awu.kanzhihu.entity.UserDetails;
import com.awu.kanzhihu.fragment.IUserFragment;
import com.awu.kanzhihu.fragment.UserDetailFragment;
import com.awu.kanzhihu.fragment.UserHighVoteFragment;
import com.awu.kanzhihu.fragment.UserRankFragment;

/**
 * Created by awu on 2015-12-28.
 */
public class UserPagerAdapter extends FragmentPagerAdapter {
    private UserHighVoteFragment mUserHighVoteFragment;
    private UserDetailFragment mUserDetailFragment;
    private UserRankFragment mUserRankFragment;
    private UserDetails userDetails;

    public UserPagerAdapter(FragmentManager fm) {
        super(fm);
        mUserHighVoteFragment = new UserHighVoteFragment();
        mUserDetailFragment = new UserDetailFragment();
        mUserRankFragment = new UserRankFragment();
    }

    /**
     * bind the user's data to show on fragment.
     * @param userDetails
     */
    public void bindData(UserDetails userDetails){
        this.userDetails = userDetails;
        for (int i = 0;i < getCount();i++){
            ((IUserFragment)getItem(i)).notifyDataChanged(userDetails);
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return mUserHighVoteFragment;
            case 1:
                return mUserDetailFragment;
            case 2:
                return mUserRankFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return KZHApp.getContext().getString(R.string.section_userhighvote);
            case 1:
                return KZHApp.getContext().getString(R.string.section_userdetail);
            case 2:
                return KZHApp.getContext().getString(R.string.section_userqxz);
        }
        return null;
    }
}
