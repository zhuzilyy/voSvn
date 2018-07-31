package com.zl.vo_.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zl.vo_.R;
import com.zl.vo_.main.activities.MyFrindEntivity;

import java.util.List;

/**
 * Created by Administrator on 2017/11/25.
 */

public class PrivacyPwd_ContactAdapter extends BaseAdapter {
    private Context context;
    private List<MyFrindEntivity> pwdEntivityList;
    private String passid_;

    public PrivacyPwd_ContactAdapter(Context context, List<MyFrindEntivity> pwdEntivityList, String passid_) {
        this.context = context;
        this.pwdEntivityList=pwdEntivityList;
        this.passid_=passid_;
    }

    @Override
    public int getCount() {
        return pwdEntivityList.size();
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
        MyFrindEntivity en = pwdEntivityList.get(i);
        viewHolder holder=new viewHolder();
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.lay_privacy_pwd_contact_item,null);
            holder.head=view.findViewById(R.id.avatar);
            holder.name=view.findViewById(R.id.name);
            holder.chex=view.findViewById(R.id.chex);
            view.setTag(holder);


        }else {
            holder= (viewHolder) view.getTag();
        }

        Picasso.with(context).load(en.getAvatar()).placeholder(R.mipmap.girl2).into(holder.head);
        holder.name.setText(en.getRemark());
        holder.chex.setChecked(en.getMychecked());

        return view;
    }

    public class viewHolder{
        private ImageView head;
        private TextView name;
        private CheckBox chex;

    }


}
