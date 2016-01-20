package com.awu.kanzhihu.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.awu.kanzhihu.R;
import com.awu.kanzhihu.util.Define;
import com.awu.kanzhihu.util.PreferenceUtil;

public class SettingActivity extends AppCompatActivity {
    private static final String TAG = "SettingActivity";
    private Toolbar mToolbar;
    private CheckBox checkBoxAppWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initToolbar();
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

    private void initSetting(){
        checkBoxAppWeb = (CheckBox)findViewById(R.id.cb_appweb);
        checkBoxAppWeb.setChecked((boolean)PreferenceUtil.read(Define.KEY_USEAPPWEB,false));

        checkBoxAppWeb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceUtil.write(Define.KEY_USEAPPWEB,isChecked);
            }
        });
    }

}
