package com.example.db;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.db.adapter.UsersAdapter;
import com.example.db.bean.Users;
import com.example.db.databinding.ActivityMainBinding;
import com.example.db.help.UserDbHelp;
import com.example.db.help.UsersContentProvider;
import com.example.db.inface.OnItemLongClickListener;
import com.example.db.inface.UserDbHelpListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements UserDbHelpListener, OnItemLongClickListener<Users> {
    private static final String TAG = "MainActivity";

    private ActivityMainBinding mainBinding;
    private UserDbHelp mDbHelp;
    private RecyclerView mRecyclerView;
    private UsersAdapter mUsersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainBinding.setUserDbHelpListener(this);

        if (mDbHelp == null) {
            mDbHelp = new UserDbHelp(this);
        }

        mRecyclerView = mainBinding.recyclerView;


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mRecyclerView.setAdapter(mUsersAdapter = new UsersAdapter(this, this));

    }

    @Override
    public void onQuery() {

        String inputName = mainBinding.edtName.getText().toString();

        String where = null;
        String[] args = null;

        if (!TextUtils.isEmpty(inputName)) {
            where = "name=?";
            args = new String[]{inputName};
        }

        //使用 ContentProvider 方式做查询 可用于模块化app 数据解耦合
        Cursor cursor = getContentResolver().query(UsersContentProvider.uri, null, where, args, null);

        //使用sqlite api 做查询
        //Cursor cursor = mDbHelp.onQuery(UserDbHelp.TABLE_NAME, where, args);

        if (cursor == null) {
            return;
        }

        List<Users> usersList = new ArrayList<>();

        if (cursor.getCount() <= 0) {
            mUsersAdapter.setUsers(usersList);
            Toast.makeText(this, "暂无数据", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.i(TAG, "onQuery: " + cursor.getCount());


        //使用游标 查询之前 一定要调用 moveToNext() 方法

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(UserDbHelp.ROW_ID));
            int age = cursor.getInt(cursor.getColumnIndex(UserDbHelp.ROW_AGE));
            String name = cursor.getString(cursor.getColumnIndex(UserDbHelp.ROW_NAME));

            Users users = new Users(id, name, age);
            usersList.add(users);
        }

        cursor.close();

        mUsersAdapter.setUsers(usersList);
    }

    @Override
    public void onInsert() {

        String name = mainBinding.edtName.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "清输入姓名", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(UserDbHelp.ROW_NAME, name);
        values.put(UserDbHelp.ROW_AGE, new Random().nextInt(30));

        long id = mDbHelp.onInsert(UserDbHelp.TABLE_NAME, values);

        mainBinding.edtName.setText(null);

        if (id == -1) {
            Toast.makeText(this, "插入失败", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "插入成功", Toast.LENGTH_SHORT).show();
            onQuery();
        }
    }

    @Override
    public void onDelete() {
        int count = mDbHelp.onDelete(UserDbHelp.TABLE_NAME, null, null);

        Log.i(TAG, "onDelete: 已经删除" + count + "条数据");


        onQuery();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDbHelp != null) {
            mDbHelp.close();
        }
    }

    @Override
    public void onItemLongClikcListener(final Users users) {

        if (users == null) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("确定删除此条数据?").setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                if (isFinishing()) {
                    return;
                }

                String where = " id = ? ";

                String[] args = new String[]{String.valueOf(users.getId())};


                if (mDbHelp == null) {
                    mDbHelp = new UserDbHelp(MainActivity.this);
                }

                int count = mDbHelp.onDelete(UserDbHelp.TABLE_NAME, where, args);

                if (count <= 0) {
                    Toast.makeText(MainActivity.this, "未删除成功", Toast.LENGTH_SHORT).show();
                } else {
                    onQuery();
                }
            }
        }).setPositiveButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();

    }
}
