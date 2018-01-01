package com.example.db.inface;

/**
 * Created by zhangfang on 2017/12/30.
 */

public interface UserDbHelpListener {

    void onQuery();

    void onInsert();

    void onDelete();
}
