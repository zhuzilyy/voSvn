package com.zl.vo_.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zl.vo_.R;
import com.zl.vo_.main.Entity.BlackListEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/18.
 */

public class BlackListAdapter extends BaseAdapter {
    public Context mContext;
    public List<BlackListEntity.BlackInfo.blackListCell> bigList;

    public BlackListAdapter(Context mContext, List<BlackListEntity.BlackInfo.blackListCell> bigList) {
        this.mContext = mContext;
        this.bigList = bigList;
    }

    @Override
    public int getCount() {
        return bigList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        BlackListEntity.BlackInfo.blackListCell cell=bigList.get(i);
        view= LayoutInflater.from(mContext).inflate(R.layout.lay_blacklist_item,null);
        de.hdodenhof.circleimageview.CircleImageView head= view.findViewById(R.id.black_item_avatar);
        TextView name=view.findViewById(R.id.black_item_name);

        Glide.with(mContext).load(cell.getAvatar()).into(head);
        name.setText(cell.getRemark());


        return view;
    }
}
