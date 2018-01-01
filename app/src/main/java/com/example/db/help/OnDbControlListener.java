package com.example.db.help;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by zhangfang on 2017/12/31.
 * <p>
 * 数据库的操作
 */

public interface OnDbControlListener {

    /**
     * 数据的插入
     *
     * @param tableName     表名
     * @param contentValues 插入表的数据
     * @return id  -1 表示没有插入成功
     */
    long onInsert(String tableName, ContentValues contentValues);


    /**
     * 数据的删除
     *
     * @param tableName   表名
     * @param whereClause 条件  如 name=?
     * @param whereArgs   条件的参数 如 new String[]{"name"};
     * @return 返回影响的行数 为0表示未删除成功
     */
    int onDelete(String tableName, String whereClause, String[] whereArgs);


    /**
     * 数据的查询
     *
     * @param tableName     表面
     * @param selection     查询条件
     * @param selectionArgs 查询条件的参数
     * @return 游标
     */
    Cursor onQuery(String tableName, String selection, String[] selectionArgs);
}
