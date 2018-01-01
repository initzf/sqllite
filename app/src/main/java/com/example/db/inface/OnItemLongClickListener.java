package com.example.db.inface;

/**
 * Created by zhangfang on 2018/1/1.
 * recyclerView 点击事件
 */

public interface OnItemLongClickListener<T> {

    /**
     * 长按事件
     *
     * @param t 要操作的业务数据
     */
    void onItemLongClikcListener(T t);
}
