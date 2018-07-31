package com.zl.vo_.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zl.vo_.R;
import com.zl.vo_.main.activities.MyFrindEntivity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/11.
 */

public class AllContactsAdapter extends BaseAdapter {
    public Context mContext;
    public List<MyFrindEntivity> frindEntivities;
    private String groupId;


    public AllContactsAdapter(Context mContext, List<MyFrindEntivity> frindEntivities,  String groupId) {
        this.mContext = mContext;
        this.frindEntivities = frindEntivities;
        this.groupId=groupId;

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
            view= LayoutInflater.from(mContext).inflate(R.layout.lay_allcontacts_item,null);
            holder=new ViewHolder();
            holder.name=view.findViewById(R.id.allContacts_name);
            holder.checkBox=view.findViewById(R.id.allContacts_checbox);
            view.setTag(holder);
        }else {
            holder= (ViewHolder) view.getTag();
        }

        holder.name.setText(fri.getRemark());
        holder.checkBox.setChecked(fri.getMychecked());





        return view;
    }

    class ViewHolder{
        private TextView name;
        private CheckBox checkBox;
    }



}
