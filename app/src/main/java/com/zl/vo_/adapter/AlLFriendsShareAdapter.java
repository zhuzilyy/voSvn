package com.zl.vo_.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zl.vo_.R;
import com.zl.vo_.main.activities.MyFrindEntivity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/11.
 */

public class AlLFriendsShareAdapter extends BaseAdapter {
    public Context mContext;
    public List<MyFrindEntivity> frindEntivities;



    public AlLFriendsShareAdapter(Context mContext, List<MyFrindEntivity> frindEntivities) {
        this.mContext = mContext;
        this.frindEntivities = frindEntivities;


    }

    @Override
    public int getCount() {
        return frindEntivities.size();
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
        MyFrindEntivity fri = frindEntivities.get(i);

        ViewHolder holder=null;
        if(view==null){
            view= LayoutInflater.from(mContext).inflate(R.layout.lay_allfriends_share_item,null);
            holder=new ViewHolder();
            holder.name=view.findViewById(R.id.allContacts_name);
            holder.head=view.findViewById(R.id.allContacts_head);
            view.setTag(holder);
        }else {
            holder= (ViewHolder) view.getTag();

        }

        holder.name.setText(fri.getRemark());
        Glide.with(mContext).load(fri.getAvatar()).into(holder.head);
        return view;
    }

    class ViewHolder{
        private TextView name;
        private ImageView head;
    }



}
