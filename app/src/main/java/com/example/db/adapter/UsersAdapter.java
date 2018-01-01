package com.example.db.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.db.R;
import com.example.db.bean.Users;
import com.example.db.databinding.ItemUsersLayoutBinding;
import com.example.db.inface.OnItemLongClickListener;

import java.util.List;

/**
 * Created by zhangfang on 2017/12/30.
 */

public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "UsersAdapter";
    private List<Users> mUsers;
    private LayoutInflater mLayoutInflater;

    private static final int TYPE_TITLE = 1;

    private OnItemLongClickListener mLongClickListener;

    public UsersAdapter(Context mContext, OnItemLongClickListener mLongClickListener) {
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mLongClickListener = mLongClickListener;
    }


    /**
     * 设置数据
     *
     * @param users 用户表数据
     */
    public void setUsers(final List<Users> users) {
        if (mUsers == null) {
            mUsers = users;
            notifyItemRangeInserted(0, users.size());
        } else {

            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mUsers.size();
                }

                @Override
                public int getNewListSize() {
                    return users.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {

                    Users oldUsers = mUsers.get(oldItemPosition);

                    Users newUsers = users.get(newItemPosition);

                    return oldUsers.getId() == newUsers.getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Users oldUsers = mUsers.get(oldItemPosition);

                    Users newUsers = users.get(newItemPosition);

                    return oldUsers.getId() == newUsers.getId() && TextUtils.equals(oldUsers.getName(), newUsers.getName());
                }
            });

            this.mUsers = users;
            diffResult.dispatchUpdatesTo(this);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_TITLE) {
            return new TitleHolder(mLayoutInflater.inflate(R.layout.item_title_layout, parent, false));
        }
        ItemUsersLayoutBinding binding = DataBindingUtil.bind(mLayoutInflater.inflate(R.layout.item_users_layout, parent, false));
        return new UsersHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (getItemViewType(position) != TYPE_TITLE) {

            final UsersHolder usersHolder = (UsersHolder) holder;

            int pos = position - 1;

            if (pos < 0 || pos >= mUsers.size()) {
                return;
            }

            final Users users = mUsers.get(pos);

            usersHolder.bind(users);

            View mViewItem = usersHolder.mBinding.getRoot();

            mViewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(usersHolder.mBinding.getRoot().getContext(), users.getName(), Toast.LENGTH_SHORT).show();
                }
            });


            mViewItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mLongClickListener != null) {
                        mLongClickListener.onItemLongClikcListener(users);
                    }
                    return true;//阻止事件的继续传递
                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return mUsers == null ? 0 : mUsers.size() + 1;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_TITLE;
        }
        return super.getItemViewType(position);
    }

    public static class UsersHolder extends RecyclerView.ViewHolder {

        public ItemUsersLayoutBinding mBinding;

        public UsersHolder(ItemUsersLayoutBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        /**
         * 设置数据
         *
         * @param users 用户信息
         */
        public void bind(Users users) {
            mBinding.setUsers(users);
        }

    }

    public static class TitleHolder extends RecyclerView.ViewHolder {

        public TitleHolder(View itemView) {
            super(itemView);
        }
    }

}
