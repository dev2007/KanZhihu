package com.awu.kanzhihu.activity;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.awu.kanzhihu.R;
import com.awu.kanzhihu.adapter.SectionsPagerAdapter;
import com.awu.kanzhihu.util.CommonUtil;
import com.awu.kanzhihu.util.Define;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    //fragmentpageradapter object.
    private SectionsPagerAdapter mSectionsPagerAdapter;
    // tablayout's viewpager.
    private ViewPager mViewPager;
    private FloatingActionButton fab;
    private SearchView mSearchView;
    private MenuItem mSearchItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initFloatingActionButton();
        initTabWithAdapter();
        CommonUtil.initMetric(this);
    }

    /**
     * initialize toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * initialize TabLayout & ViewPager & ViewPager's FragmentPagerAdapter
     */
    private void initTabWithAdapter() {
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    fab.setVisibility(View.VISIBLE);
                    mSearchItem.setVisible(true);
                } else {
                    fab.setVisibility(View.INVISIBLE);
                    mSearchItem.setVisible(false);
                }
                mSearchItem.collapseActionView();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void initFloatingActionButton() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                        .setTitle(R.string.text_sorttitle)
                        .setItems(R.array.sortparam, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String[] listArray = getResources().getStringArray(R.array.sortparam);
                                String name = listArray[which];
                                Toast.makeText(view.getContext(), "选择了" + name, Toast.LENGTH_SHORT).show();
                                Define.ParamName param = Define.ParamName.getName(name);
                                mSectionsPagerAdapter.changeUserQuery(param);
                            }
                        })
                        .create();
                dialog.show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mSearchItem = menu.findItem(R.id.action_search);
        initSearchView();
        return true;
    }

    private void initSearchView() {
        mSearchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        ComponentName cn = new ComponentName(this, SearchResultActivity.class);
        SearchableInfo info = searchManager.getSearchableInfo(cn);
        mSearchView.setSearchableInfo(info);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Log.i(TAG, "click setting.");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
