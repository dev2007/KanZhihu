package com.awu.kanzhihu.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.Toast;

import com.awu.kanzhihu.app.KZHApp;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by awu on 2016-02-01.
 */
public class FileUtil {
    private final static String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/kanzhihu";

    public static void saveImage(final Bitmap bm, final String picName){
        new Thread(new Runnable() {
            @Override
            public void run() {
                saveImageThreadWork(bm,picName);
            }
        }).start();
    }

    private static void saveImageThreadWork(Bitmap bm, String picName) {
        File dirFile = new File(ALBUM_PATH);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(ALBUM_PATH + "/" + picName);
        BufferedOutputStream bos = null;

        try {
            bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();

        } catch (Exception e) {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static void saveBitmap(Bitmap bm, String picName) {
        FileOutputStream out = null;
        try {
            out = KZHApp.getContext().openFileOutput(picName, Context.MODE_PRIVATE);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Bitmap readBitmap(String picName) {
        FileInputStream inputStream = null;
        BufferedReader reader = null;
        Bitmap bm = null;
        try {
            inputStream = KZHApp.getContext().openFileInput(picName);
            bm = BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            return bm;
        }
    }

    public static void save(String content, String fileName) {
        FileOutputStream outputStream = null;
        BufferedWriter writer = null;
        try {
            outputStream = KZHApp.getContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String read(String fileName) {
        FileInputStream inputStream = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            inputStream = KZHApp.getContext().openFileInput(fileName);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            return content.toString();
        }
    }
}
