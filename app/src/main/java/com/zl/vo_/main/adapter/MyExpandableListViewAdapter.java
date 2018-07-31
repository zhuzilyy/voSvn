package com.zl.vo_.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zl.vo_.R;
import com.zl.vo_.main.Entity.BigMineGroupList;
import com.zl.vo_.main.activities.MyFrindEntivity;
import com.zl.vo_.main.activities.UserDetailsActivityVo;
import com.zl.vo_.main.activities.allContactsListActivityVo;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/9.
 */

public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Map<String, List<MyFrindEntivity>> dataset = new HashMap<>();
    private Context mContext;
    private List<String> fatherNames;
    private  List<BigMineGroupList> bigGroupLists;


    public MyExpandableListViewAdapter(Map<String, List<MyFrindEntivity>> dataset, Context mContext, List<String> fatherNames, List<BigMineGroupList> bigGroupLists) {

        this.dataset = dataset;
        this.mContext = mContext;
        this.fatherNames=fatherNames;
        this.bigGroupLists=bigGroupLists;
        Log.i("tt","构造");

    }

    @Override
    public int getGroupCount() {
        return dataset.size();
    }
    //  获得某个父项的子项数目
    @Override
    public int getChildrenCount(int parentPos) {
        return dataset.get(fatherNames.get(parentPos)).size();
    }

    //  获得某个父项
    @Override
    public Object getGroup(int parentPos) {
        return dataset.get(parentPos);
    }


    //  获得某个父项的某个子项
    @Override
    public Object getChild(int parentPos, int childPos) {
        return dataset.get(parentPos).get(childPos);
    }
    //  获得某个父项的id
    @Override
    public long getGroupId(int parentPos) {
        return parentPos;
    }


    //  获得某个父项的某个子项的id
    @Override
    public long getChildId(int parentPos, int childPos) {
        return childPos;
    }

    //  按函数的名字来理解应该是是否具有稳定的id，这个方法目前一直都是返回false，没有去改动过
    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int parentPos, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.parent_item, null);
            }
            view.setTag(R.layout.parent_item, parentPos);
            view.setTag(R.layout.child_item, -1);
            TextView text = (TextView) view.findViewById(R.id.parent_title);
            ImageView jiajian_icon=view.findViewById(R.id.jiajian_icon);
            text.setText(fatherNames.get(parentPos));
            jiajian_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,allContactsListActivityVo.class);

                intent.putExtra("groupId",bigGroupLists.get(parentPos).getGroupId());
                mContext.startActivity(intent );
            }
        });

            return view;
    }

    @Override
    public View getChildView(final int parentPos, final int childPos, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {

                view = LayoutInflater.from(mContext).inflate(R.layout.child_item, null);
            }

            view.setTag(R.layout.parent_item, parentPos);
            view.setTag(R.layout.child_item, childPos);
            TextView text = (TextView) view.findViewById(R.id.child_title);
            de.hdodenhof.circleimageview.CircleImageView head=view.findViewById(R.id.contactHead);
            text.setText(dataset.get(fatherNames.get(parentPos)).get(childPos).getRemark());
            Picasso.with(mContext).load(dataset.get(fatherNames.get(parentPos)).get(childPos).getAvatar()).placeholder(R.mipmap.logo).into(head);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(mContext,UserDetailsActivityVo.class);
                    intent.putExtra("HXid",dataset.get(fatherNames.get(parentPos)).get(childPos).getHuanxinID());
                    //账号
                    intent.putExtra("account",dataset.get(fatherNames.get(parentPos)).get(childPos).getAccount());
                    intent.putExtra("remark",dataset.get(fatherNames.get(parentPos)).get(childPos).getRemark());
                    mContext.startActivity(intent);

                }
            });
            return view;
    }
    //  子项是否可选中，如果需要设置子项的点击事件，需要返回true
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }




}
