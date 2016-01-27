package com.awu.kanzhihu.activity;

import android.content.Intent;
import android.net.Uri;
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
import com.umeng.analytics.MobclickAgent;

import awu.com.awutil.LogUtil;

public class AnswerActivity extends BaseActivity {
    private ProgressBar mProgressBar;
    private FloatingActionButton mFloatingActionButton;
    private WebView mWebView;
    private String mUrl;
    private String mAnswerTitle;
    private MenuItem menuItemFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        Intent intent = getIntent();
        initToolbarNavigation();
        setTitle(intent);
        initFloatingActionButton();
        initProgressBar();
        initWebView(intent);
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
            favHint();
            return true;
        } else if (id == R.id.action_view) {
            Uri uri = Uri.parse(mUrl);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Set toolbar title.
     * @param intent
     */
    private void setTitle(Intent intent) {
        if (intent != null) {
            mAnswerTitle = intent.getStringExtra(Define.KEY_ANSWER_TITLE);
            getSupportActionBar().setTitle(mAnswerTitle);
        }
    }

    /**
     * Initialize progressbar.
     */
    private void initProgressBar() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar_answer);
    }

    /**
     * If current answer is user's favorite.
     * @return True,if it is.
     */
    private boolean isFav() {
        boolean flag = false;
        if (!TextUtils.isEmpty(mUrl))
            flag = DbUtil.isFav(mUrl);
        return flag;
    }

    /**
     * Switch fav icon displaying.
     * @param flag
     */
    private void switchFavIcon(boolean flag) {
        if (flag) {
            if (menuItemFav != null)
                menuItemFav.setIcon(R.mipmap.ic_star_white);
        } else {
            if (menuItemFav != null)
                menuItemFav.setIcon(R.mipmap.ic_star_border);
        }
    }

    /**
     * Hint for click fav icon.
     */
    private void favHint() {
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
     * Initialize FloatingActionButton.
     */
    private void initFloatingActionButton() {
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra("android.intent.extra.TEXT", String.format("「%s」%s", mAnswerTitle, mUrl));
                startActivity(Intent.createChooser(intent, getString(R.string.text_shareto)));
            }
        });
    }

    /**
     * Initialize WebView.
     * @param intent
     */
    private void initWebView(Intent intent) {
        mWebView = (WebView) findViewById(R.id.wv);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        if (intent != null) {
            String shortUrl = intent.getStringExtra(Define.KEY_QUESTION_ANSWER);
            String favUrl = intent.getStringExtra(Define.KEY_FAV_URL);
            //is full link.
            if (shortUrl != null) {
                boolean isPost = intent.getBooleanExtra(Define.KEY_ISPOST, false);
                if (isPost) {
                    mUrl = String.format("%s%s", Define.Url_Zhihu_ZhuanLan, shortUrl);
                } else
                    mUrl = String.format("%s%s", Define.Url_Zhihu, shortUrl);
            } else if (favUrl != null) {//is fav.
                mUrl = favUrl;
            } else {//is separate url.
                String questionId = intent.getStringExtra(Define.KEY_QUESTIONID);
                String answerId = intent.getStringExtra(Define.KEY_ANSWERID);
                mUrl = String.format("%s/%s/answer/%s", Define.Url_Answer, questionId, answerId);
            }
            switchFavIcon(isFav());
            LogUtil.d(this,mUrl);
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
                        LogUtil.d(this,"url load ok");
                        if (mProgressBar.getVisibility() == View.VISIBLE) {
                            mProgressBar.setVisibility(View.GONE);
                            mWebView.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        MobclickAgent.onPause(this);
    }
}
