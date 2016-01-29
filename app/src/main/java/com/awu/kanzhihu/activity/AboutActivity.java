package com.awu.kanzhihu.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.awu.kanzhihu.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

/**
 * AboutActivity for setting and app information.
 */
public class AboutActivity extends BaseActivity {
    private TextView textViewVersion;
    private Button buttonUpdate;
    private Button buttonSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initToolbarNavigation();
        initData();
        initUpdate();
        initSetting();
        initGestureDetector(textViewVersion);
    }

    /**
     * Initialize display data.
     */
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

    /**
     * Initialize setting button.
     */
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

    /**
     * Get app's version name.
     * @return
     * @throws Exception
     */
    private String getVersionName() throws Exception
    {
        PackageManager packageManager = getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
        String version = packInfo.versionName;
        return version;
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
