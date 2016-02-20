package com.awu.kanzhihu.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.awu.kanzhihu.R;
import com.awu.kanzhihu.actions.ActionCreator;
import com.awu.kanzhihu.dispatcher.Dispatcher;
import com.awu.kanzhihu.stores.SplashStore;
import com.awu.kanzhihu.stores.Store;
import com.awu.kanzhihu.util.Define;
import com.awu.kanzhihu.util.FileUtil;
import com.squareup.otto.Subscribe;

public class SplashActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView textViewDescription;
    private Animation animation;
    private ImageButton imageButton;
    private Bitmap bitmap = null;
    private String fileName;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    jumpToMain();
                    break;
                default:
                    break;
            }
        }
    };

    private SplashStore imageStore;
    private ActionCreator actionCreator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_acvitity);
        init();
        requestData();
        timerJump();
    }

    private void jumpToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void timerJump() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    mHandler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void init() {
        imageView = (ImageView) findViewById(R.id.iv);
        textViewDescription = (TextView) findViewById(R.id.tv_description);
        animation = AnimationUtils.loadAnimation(this, R.anim.enlarge);
        animation.setFillAfter(true);
        imageButton = (ImageButton) findViewById(R.id.ib_download);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), R.string.text_download, Toast.LENGTH_LONG).show();
                downLoad();
            }
        });

        imageStore = new SplashStore();
        Dispatcher.instance().register(imageStore);
        actionCreator = ActionCreator.instance(Dispatcher.instance());
    }

    @Override
    public void onResume() {
        super.onResume();
        imageStore.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        imageStore.unregister(this);
    }

    @Subscribe
    public void onStoreChange(Store.StoreChangeEvent event) {
        render();
    }

    private void render() {
        textViewDescription.setText(imageStore.getText());
        bitmap = imageStore.getImage();
        if (bitmap != null)
            setImage();
        fileName = imageStore.getFileName();
    }

    private void downLoad() {
        FileUtil.saveImage(bitmap, fileName);
    }

    private void requestData() {
        actionCreator.requestSplash();
    }


    private void setImage() {
        imageView.setImageBitmap(bitmap);
        imageView.setAnimation(animation);
        imageButton.setVisibility(View.VISIBLE);
    }
}
