package com.zl.vo_.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zl.vo_.R;
import com.zl.vo_.main.Entity.MyFriendGroupEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/11/17.
 */

public class GroupManagerAdapter extends BaseAdapter {

    private Context context;
    private List<MyFriendGroupEntity> entities;

    public GroupManagerAdapter(Context context, List<MyFriendGroupEntity> entities) {
        this.context = context;
        this.entities = entities;
    }

    @Override
    public int getCount() {
        return entities.size();
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

        view= LayoutInflater.from(context).inflate(R.layout.lay_group_managet_item,null);
        TextView name=view.findViewById(R.id.group_item_name);
        name.setText(entities.get(i).getGroupName());
        return view;
    }
}
