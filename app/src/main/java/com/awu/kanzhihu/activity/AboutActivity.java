package com.awu.kanzhihu.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.awu.kanzhihu.R;
import com.umeng.update.UmengUpdateAgent;

public class AboutActivity extends AppCompatActivity {
    private static final String TAG = "AboutActivity";
    private Toolbar mToolbar;
    private TextView textViewVersion;
    private Button buttonUpdate;
    private Button buttonSetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initToolbar();
        initData();
        initUpdate();
        initSetting();
    }

    private void initToolbar(){
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

    private void initData(){
        textViewVersion = (TextView)findViewById(R.id.tv_version);
        try {
            textViewVersion.setText(getString(R.string.text_version) + getVersionName());
        }catch (Exception e){
            textViewVersion.setText(R.string.text_noget_version);
        }
    }

    private void initUpdate(){
        buttonUpdate = (Button)findViewById(R.id.btn_update);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UmengUpdateAgent.forceUpdate(getApplicationContext());
            }
        });
    }

    private void initSetting(){
        buttonSetting = (Button)findViewById(R.id.btn_setting);
        buttonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    private String getVersionName() throws Exception
    {
        PackageManager packageManager = getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
        String version = packInfo.versionName;
        return version;
    }

}
