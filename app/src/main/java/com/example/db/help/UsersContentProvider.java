package com.example.db.help;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by zhangfang on 2017/12/30.
 */

public class UsersContentProvider extends ContentProvider {
    private static final String TAG = "UsersContentProvider";
    public static final String AUTHORITY = "com.example.db.help.Provider";

    public static final Uri uri = Uri.parse("content://" + AUTHORITY + "/users");

    private UserDbHelp mUserDbHelp;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);


    static {
        URI_MATCHER.addURI(AUTHORITY, UserDbHelp.TABLE_NAME, 0);
    }

    @Override
    public boolean onCreate() {
        mUserDbHelp = new UserDbHelp(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        String url = getType(uri);
        if (TextUtils.isEmpty(url)) {
            return null;
        }

        return mUserDbHelp.onQuery(url.substring(1, url.length()), selection, selectionArgs);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        int code = URI_MATCHER.match(uri);

        if (code != 0) {
            Log.e(TAG, "query: uri 不匹配");
            return null;
        }

        String url = uri.getPath();

        if (TextUtils.isEmpty(url)) {
            Log.e(TAG, "query: authority 为空");
            return null;
        }
        return url;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        String url = getType(uri);
        if (TextUtils.isEmpty(url)) {
            return null;
        }

        long id = mUserDbHelp.onInsert(url.substring(1, url.length()), values);

        String str = "插入成功";

        if (id == -1) {
            str = "插入失败";
        }

        return ContentUris.withAppendedId(Uri.parse(str), id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        String url = getType(uri);
        if (TextUtils.isEmpty(url)) {
            return 0;
        }

        return mUserDbHelp.onDelete(url.substring(1, url.length()), selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
