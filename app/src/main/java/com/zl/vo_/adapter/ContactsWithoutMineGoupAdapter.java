package com.zl.vo_.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zl.vo_.R;
import com.zl.vo_.main.activities.MyFrindEntivity;

import java.util.List;

/**
 * Created by Administrator on 2017/11/28.
 */

public class ContactsWithoutMineGoupAdapter extends BaseAdapter {
    private Context mContext;
    private List<MyFrindEntivity> withoutGruopList;

    public ContactsWithoutMineGoupAdapter(Context mContext, List<MyFrindEntivity> withoutGruopList) {
        this.mContext = mContext;
        this.withoutGruopList=withoutGruopList;
    }

    @Override
    public int getCount() {
        return withoutGruopList.size();
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
        MyFrindEntivity itemdata=withoutGruopList.get(i);
        viewHolder holder=null;
        if(view==null){
            holder=new viewHolder();
            view= LayoutInflater.from(mContext).inflate(R.layout.lay_contacts_without_mine_group_item,null);
            holder.head=view.findViewById(R.id.group_item_head);
            holder.name=view.findViewById(R.id.group_item_name);
            view.setTag(holder);

        }else {
            holder= (viewHolder) view.getTag();
        }
        Picasso.with(mContext).load(itemdata.getAvatar()).into(holder.head);
        holder.name.setText(itemdata.getRemark());






        return view;
    }

    class viewHolder{

        private de.hdodenhof.circleimageview.CircleImageView head;
        private TextView name;

    }

}
