package com.awu.kanzhihu.stores;

import android.graphics.Bitmap;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.awu.kanzhihu.actions.Action;
import com.awu.kanzhihu.actions.SplashAction;
import com.awu.kanzhihu.util.Define;
import com.awu.kanzhihu.util.FileUtil;

import java.util.Calendar;

import awu.com.awutil.LogUtil;

/**
 * Created by yoyo on 2016/2/18.
 */
public class SplashStore extends Store {
    private Bitmap bitmap;
    private String fileName;
    private String text;
    private int requestFlag = 0;
    private final int ALL_OK = 2;

    public SplashStore(){
        super();
    }

    @Override
    public StoreChangeEvent changeEvent() {
        return new StoreChangeEvent();
    }

    public Bitmap getImage(){
        return bitmap;
    }

    public String getFileName(){
        return fileName;
    }

    public String getText(){
        return text;
    }

    @Override
    public void onAction(Action action) {
        LogUtil.d(this, "onAction");
        requestFlag = 0;
        switch (action.getType()){
            case SplashAction.ACTION_NAME:
                requestText();
                requestImage();
                break;
            default:
                break;
        }
    }

    private void requestText(){
        StringRequest stringRequest = new StringRequest(Define.Url_Bing_Text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                text = response;
                emitChange();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.i(this,"requestText error");
            }
        });
        mQueue.add(stringRequest);
    }

    private void requestImage(){
        bitmap = readBitmap();
        if (bitmap != null) {
            LogUtil.i("Image", "read from storage");
            emitChange();
            return;
        }

        ImageRequest imageRequest = new ImageRequest(Define.Url_Bing_Img, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                LogUtil.i("Image", "read from web");
                bitmap = response;
                saveBitmap();
                emitChange();
            }
        }, 1920, 1080, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.i(this,"requestImage error");
            }
        });
        mQueue.add(imageRequest);
    }

    private synchronized void emitChange(){
        requestFlag++;
        if(requestFlag == ALL_OK)
            emitStoreChange();
    }

    /**
     * read image from storage.
     *
     * @return
     */
    private Bitmap readBitmap() {
        return FileUtil.readBitmap(bitmapName());
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

    private String bitmapName() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        String name = java.lang.String.format("%d_%d_%d", year, month, day);
        fileName = name + ".png";
        return name + ".cache";
    }
}
