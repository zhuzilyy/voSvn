package com.zl.vo_.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.easeui.domain.EaseUser;
import com.squareup.picasso.Picasso;
import com.zl.vo_.R;
import com.zl.vo_.main.activities.MyFrindEntivity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/7.
 */

public class MyContactsListAdapter extends BaseAdapter {
    public Context mContext;
    public List<MyFrindEntivity> contactList;

    public MyContactsListAdapter(Context mContext, List<MyFrindEntivity> contactList) {
        this.mContext = mContext;
        this.contactList=contactList;
    }

    @Override
    public int getCount() {
        return contactList.size();
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


        MyFrindEntivity user=contactList.get(i);




        ViewHolder holder=null;
        if(view==null){
            holder=new ViewHolder();
            view= LayoutInflater.from(mContext).inflate(R.layout.lay_mycontact_item,null);
            holder.head= view.findViewById(R.id.contact_head);
            holder.name=view.findViewById(R.id.contact_name);
            view.setTag(holder);
        }else {
            holder= (ViewHolder) view.getTag();
        }
        Picasso.with(mContext).load(user.getAvatar()).placeholder(R.drawable.ease_default_avatar).into(holder.head);
        holder.name.setText(user.getRemark());




        return view;
    }

    class ViewHolder{
        private ImageView head;
        private TextView name;

    }
}
