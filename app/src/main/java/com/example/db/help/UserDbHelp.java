package com.example.db.help;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by zhangfang on 2017/12/30.
 */

public class UserDbHelp extends SQLiteOpenHelper implements OnDbControlListener {
    private static final String TAG = "UserDbHelp";


    //数据库名
    public static final String DB_NAME = "users.db";

    //数据库版本号
    public static final int DB_VERSION = 1;

    //表名
    public static final String TABLE_NAME = "users";

    //列名
    public static final String ROW_ID = "id";
    public static final String ROW_NAME = "name";
    public static final String ROW_AGE = "age";


    public UserDbHelp(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public UserDbHelp(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "create table " + TABLE_NAME + "( " + ROW_ID + " integer primary key autoincrement , " + ROW_NAME + " TEXT , " + ROW_AGE + " integer )";

        Log.i(TAG, "onCreate: " + sql);

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public long onInsert(String tableName, ContentValues contentValues) {
        long id = getReadableDatabase().insert(tableName, null, contentValues);
        return id;
    }

    @Override
    public int onDelete(String tableName, String whereClause, String[] whereArgs) {
        return getReadableDatabase().delete(tableName, whereClause, whereArgs);
    }

    @Override
    public Cursor onQuery(String tableName, String selection, String[] selectionArgs) {
        return getReadableDatabase().query(tableName, null, selection, selectionArgs, null, null, null);
    }
}
