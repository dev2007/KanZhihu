package com.awu.kanzhihu.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.util.Log;

import com.awu.kanzhihu.app.KZHApp;
import com.awu.kanzhihu.db.DbHelper;
import com.awu.kanzhihu.entity.Fav;

import java.util.ArrayList;

/**
 * Created by yoyo on 2016/1/14.
 */
public class DbUtil {
    private static final String TAG = "DbUtil";
    private static DbHelper dbHelper = null;
    private static SQLiteDatabase db = null;
    private static final String DT_FAV = "fav";
    private static final String CN_URL = "url";
    private static final String CN_NAME = "name";

    private static void init(){
        if(dbHelper == null){
            dbHelper = new DbHelper(KZHApp.getContext());
            db = dbHelper.getReadableDatabase();
        }
    }

    public static void closeDB(){
        if(db != null){
            db.close();
        }
        if(dbHelper != null){
            dbHelper.close();
        }
    }

    /**
     * insert a fav with name and url.
     * @param url
     * @param name
     * @return
     */
    public static boolean insertFav(String url,String name){
        init();
        long returnValue = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(CN_URL, url);
            values.put(CN_NAME, name);
            returnValue = db.insert(DT_FAV, null, values);
        }catch (Exception e){
            Log.e(TAG,"insertFav ex:"+e.getMessage());
            returnValue = -1;
        }finally {
            return returnValue > 0;
        }
    }

    /**
     * delete fav by url.
     * @param url
     * @return
     */
    public static boolean deleteFav(String url){
        init();
        int returnValue = 0;
        try {
            String whereClause = String.format("%s=?",CN_URL);
            String[] whereArgs = {url};
            returnValue = db.delete(DT_FAV, whereClause, whereArgs);
        }catch (Exception e){
            returnValue = 0;
        }finally {
            return returnValue != 0;
        }
    }

    /**
     * get all fav.
     * @return
     */
    public static ArrayList<Fav> queryAllFav(){
        init();
        ArrayList<Fav> arrayList = new ArrayList<>();
        Cursor c = db.query(DT_FAV,null,null,null,null,null,null);
        if(c.moveToFirst()){
            Log.i(TAG,"count:" + c.getCount());
            do{
                String url = c.getString(c.getColumnIndex(CN_URL));
                String name = c.getString(c.getColumnIndex(CN_NAME));
                Fav fav = new Fav(url,name);
                arrayList.add(fav);
            }while (c.moveToNext());
        }
        return arrayList;
    }

    /**
     * check if the url is in fav table.
     * @param url
     * @return
     */
    public static boolean isFav(String url){
        init();
        Cursor c = db.query(DT_FAV,null,"url=?",new String[]{url},null,null,null);
        if(c.moveToFirst()){
            return true;
        }else
            return false;
    }
}
