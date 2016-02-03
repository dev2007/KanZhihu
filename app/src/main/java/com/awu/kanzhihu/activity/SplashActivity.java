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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.awu.kanzhihu.R;
import com.awu.kanzhihu.app.KZHApp;
import com.awu.kanzhihu.util.Define;
import com.awu.kanzhihu.util.FileUtil;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

import awu.com.awutil.LogUtil;

public class SplashActivity extends AppCompatActivity {
    private RequestQueue mQueue;
    private ImageLoader mImageLoader;
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

        mQueue = Volley.newRequestQueue(this);
    }

    private void downLoad() {
        FileUtil.saveImage(bitmap, fileName);
    }

    private void requestData() {
        StringRequest stringRequest = new StringRequest(Define.Url_Bing_Text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                textViewDescription.setText(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mQueue.add(stringRequest);

        bitmap = readBitmap();
        if (bitmap != null) {
            LogUtil.i("Image", "read from storage");
            setImage();
            return;
        }

        ImageRequest imageRequest = new ImageRequest(Define.Url_Bing_Img, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                LogUtil.i("Image", "read from web");
                bitmap = response;
                saveBitmap();
                setImage();
            }
        }, 1920, 1080, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mQueue.add(imageRequest);
    }


    private void setImage() {
        imageView.setImageBitmap(bitmap);
        imageView.setAnimation(animation);
        imageButton.setVisibility(View.VISIBLE);
    }

    /**
     * save image to storage.
     */
    private void saveBitmap() {
        LogUtil.i("Image", "save cache");
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileUtil.saveBitmap(bitmap, bitmapName());
            }
        }).start();
    }

    /**
     * read image from storage.
     *
     * @return
     */
    private Bitmap readBitmap() {
        return FileUtil.readBitmap(bitmapName());
    }

    private String bitmapName() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        String name = java.lang.String.format("%d_%d_%d", year, month, day);
        fileName = name + ".png";
        return name + ".cache";
    }
}
