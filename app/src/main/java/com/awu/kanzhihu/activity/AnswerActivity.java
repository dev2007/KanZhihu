package com.awu.kanzhihu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.util.Log;

import com.awu.kanzhihu.R;
import com.awu.kanzhihu.util.Define;

public class AnswerActivity extends AppCompatActivity {
    private static final String TAG = "AnswerActivity";
    private Toolbar mToolbar;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        initToolbar();
        initFlotingActionButton();
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

    private void initFlotingActionButton(){
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void initWebView(){
        mWebView = (WebView)findViewById(R.id.wv);
        Intent intent = getIntent();
        if(intent != null){
            String questionId = intent.getStringExtra(Define.KEY_QUESTIONID);
            String answerId = intent.getStringExtra(Define.KEY_ANSWERID);
            String url = String.format("%s/%s/answer/%s",Define.Url_Answer,questionId,answerId);
            Log.i(TAG,url);
            mWebView.loadUrl(url);
        }
    }

}
