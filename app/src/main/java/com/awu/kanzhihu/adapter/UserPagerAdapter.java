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
import com.awu.kanzhihu.fragment.UserQXZFragment;
import com.awu.kanzhihu.fragment.UserRecentFragment;

/**
 * Created by awu on 2015-12-28.
 */
public class UserPagerAdapter extends FragmentPagerAdapter {
    private UserHighVoteFragment mUserHighVoteFragment;
    private UserDetailFragment mUserDetailFragment;
    private UserQXZFragment mUserQXZFragment;
    private UserDetails userDetails;

    public UserPagerAdapter(FragmentManager fm) {
        super(fm);
        mUserHighVoteFragment = new UserHighVoteFragment();
        mUserDetailFragment = new UserDetailFragment();
        mUserQXZFragment = new UserQXZFragment();
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
                return mUserQXZFragment;
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
                return KZHApp.appContext().getString(R.string.section_userhighvote);
            case 1:
                return KZHApp.appContext().getString(R.string.section_userdetail);
            case 2:
                return KZHApp.appContext().getString(R.string.section_userqxz);
        }
        return null;
    }
}
