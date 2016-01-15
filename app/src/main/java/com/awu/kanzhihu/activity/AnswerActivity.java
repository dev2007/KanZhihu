package com.awu.kanzhihu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.util.Log;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.awu.kanzhihu.R;
import com.awu.kanzhihu.bl.ActivityTouch;
import com.awu.kanzhihu.util.DbUtil;
import com.awu.kanzhihu.util.Define;

public class AnswerActivity extends AppCompatActivity implements View.OnTouchListener {
    private static final String TAG = "AnswerActivity";
    private Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private WebView mWebView;
    private String mUrl;
    private String mAnswerTitle;
    private MenuItem menuItemFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        Intent intent = getIntent();
        initToolbar();
        setTitle(intent);
        initFloatingActionButton();
        initProgressBar();
        initWebView(intent);
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, android.R.anim.slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_answer, menu);
        menuItemFav = menu.findItem(R.id.action_fav);
        switchFavIcon(isFav());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_fav) {
            fav();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setTitle(Intent intent) {
        if (intent != null) {
            mAnswerTitle = intent.getStringExtra(Define.KEY_ANSWER_TITLE);
            getSupportActionBar().setTitle(mAnswerTitle);
        }
    }

    private void initProgressBar() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar_answer);
    }

    private boolean isFav() {
        boolean flag = false;
        if (!TextUtils.isEmpty(mUrl))
            flag = DbUtil.isFav(mUrl);
        return flag;
    }

    private void switchFavIcon(boolean flag) {
        if (flag) {
            if (menuItemFav != null)
                menuItemFav.setIcon(R.mipmap.ic_star_white);
        } else {
            if (menuItemFav != null)
                menuItemFav.setIcon(R.mipmap.ic_star_border);
        }
    }

    private void fav() {
        if (isFav()) {
            DbUtil.deleteFav(mUrl);
            switchFavIcon(false);
            Toast.makeText(this, R.string.text_cacelfav, Toast.LENGTH_SHORT).show();
        } else {
            DbUtil.insertFav(mUrl, mAnswerTitle);
            switchFavIcon(true);
            Toast.makeText(this, R.string.text_isfav, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * TODO:next version,now NOT.
     */
    private void initFloatingActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initWebView(Intent intent) {
        mWebView = (WebView) findViewById(R.id.wv);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        if (intent != null) {
            String shortUrl = intent.getStringExtra(Define.KEY_QUESTION_ANSWER);
            if (shortUrl != null) {
                boolean isPost = intent.getBooleanExtra(Define.KEY_ISPOST, false);
                if (isPost) {
                    mUrl = String.format("%s%s", Define.Url_Zhihu_ZhuanLan, shortUrl);
                } else
                    mUrl = String.format("%s%s", Define.Url_Zhihu, shortUrl);
            } else {
                String questionId = intent.getStringExtra(Define.KEY_QUESTIONID);
                String answerId = intent.getStringExtra(Define.KEY_ANSWERID);
                mUrl = String.format("%s/%s/answer/%s", Define.Url_Answer, questionId, answerId);
            }
            switchFavIcon(isFav());
            Log.i(TAG, mUrl);
            mWebView.loadUrl(mUrl);
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });

            mWebView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    if (newProgress == 100) {
                        Log.i(TAG, "url load ok");
                        if (mProgressBar.getVisibility() == View.VISIBLE) {
                            mProgressBar.setVisibility(View.GONE);
                            mWebView.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
//            mWebView.setOnTouchListener(this);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return ActivityTouch.parentOnTouch(this, v, event);
    }
}
