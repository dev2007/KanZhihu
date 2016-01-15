package com.awu.kanzhihu.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yoyo on 2016/1/14.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "kanzhihu.db";
    private static final int VERSION = 1;

    private static final String CREATE_SQL = "create table fav(_id INTEGER PRIMARY KEY " +
            "AUTOINCREMENT, url VARCHAR(200),name varchar(100))";

    public DbHelper(Context context){
        super(context,DB_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
