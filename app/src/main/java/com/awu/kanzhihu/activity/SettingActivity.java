package com.awu.kanzhihu.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.awu.kanzhihu.R;
import com.awu.kanzhihu.util.Define;
import com.awu.kanzhihu.util.PreferenceUtil;
import com.umeng.analytics.MobclickAgent;

import awu.com.awutil.LogUtil;

/**
 * SettingActivity.
 */
public class SettingActivity extends BaseActivity{
    private CheckBox checkBoxAppWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initToolbarNavigation();
        initSetting();
        initGestureDetector(checkBoxAppWeb);
    }

    private void initSetting() {
        checkBoxAppWeb = (CheckBox) findViewById(R.id.cb_appweb);
        checkBoxAppWeb.setChecked((boolean) PreferenceUtil.read(Define.KEY_USEAPPWEB, false));

        checkBoxAppWeb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceUtil.write(Define.KEY_USEAPPWEB, isChecked);
            }
        });
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
