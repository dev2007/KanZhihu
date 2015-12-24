package com.awu.kanzhihu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.util.Log;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.awu.kanzhihu.R;
import com.awu.kanzhihu.util.Define;

public class AnswerActivity extends AppCompatActivity {
    private static final String TAG = "AnswerActivity";
    private Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private WebView mWebView;
    private String mUrl;
    private String mAnswerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        initToolbar();
        initFlotingActionButton();
        initProgressBar();
        initWebView();
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

    private void initProgressBar() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar_answer);
    }

    private void initFlotingActionButton() {
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void initWebView() {
        mWebView = (WebView) findViewById(R.id.wv);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            String questionId = intent.getStringExtra(Define.KEY_QUESTIONID);
            String answerId = intent.getStringExtra(Define.KEY_ANSWERID);
            mUrl = String.format("%s/%s/answer/%s", Define.Url_Answer, questionId, answerId);
            Log.i(TAG, mUrl);
            mWebView.loadUrl(mUrl);
            mWebView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
                    Log.i(TAG, "title is " + title);
                    mAnswerTitle = title;
                }

                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    if (newProgress == 100) {
                        if (mProgressBar.getVisibility() == View.VISIBLE) {
                            mProgressBar.setVisibility(View.GONE);
                            mWebView.setVisibility(View.VISIBLE);
                        }
                    }else{
                        Log.i(TAG,"progress is:"+newProgress);
                    }
                }
            });
        }
    }
}
