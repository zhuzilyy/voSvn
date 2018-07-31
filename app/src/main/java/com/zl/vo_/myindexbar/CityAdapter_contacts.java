package com.zl.vo_.myindexbar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zl.vo_.R;
import com.zl.vo_.db.InviteMessgeDao;
import com.zl.vo_.main.Entity.PhoneContactsEntity;

import java.util.List;


/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */

public class CityAdapter_contacts extends RecyclerView.Adapter<CityAdapter_contacts.ViewHolder> {
    protected Context mContext;
    protected List<PhoneContactsEntity> mDatas;
    protected LayoutInflater mInflater;
    public myContactsClickListener listener;
    private InviteMessgeDao inviteMessgeDao=new InviteMessgeDao(mContext);

    public CityAdapter_contacts(Context mContext, List<PhoneContactsEntity> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);
    }

    public List<PhoneContactsEntity> getDatas() {
        return mDatas;
    }

    public CityAdapter_contacts setDatas(List<PhoneContactsEntity> datas) {
        mDatas = datas;
        return this;
    }

    @Override
    public CityAdapter_contacts.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_city_contacts, parent, false));
    }
    //******************************************
    public interface myContactsClickListener{

        void onMyContactItemClick(int positon);
        void onMyContactItemLongClick(int position);

    }

    public void setMyContactClickListener(myContactsClickListener l){
        this.listener=l;
    }


    //******************************************
    @Override
    public void onBindViewHolder(final CityAdapter_contacts.ViewHolder holder, final int position) {
        final PhoneContactsEntity friend = mDatas.get(position);
                Glide.with(mContext).load(friend.getAvatar()).into(holder.avatar);
                holder.tvCity.setText(mDatas.get(position).getNickname());
        if(friend.getTag().equals("1")){
            holder.addressBtn.setText("添加");
            holder.addressBtn.setBackground(mContext.getResources().getDrawable(R.drawable.selector_accept2));

        }else {
            holder.addressBtn.setText("邀请");
            holder.addressBtn.setBackground(mContext.getResources().getDrawable(R.drawable.selector_accept));
        }


                holder.addressBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onMyContactItemClick(position);

                    }
                });
                holder.addressBtn.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        listener.onMyContactItemLongClick(position);
                        return true;
                    }
                });



    }

    @Override
    public int getItemCount() {
        int  u=mDatas.size();
        return mDatas != null ? mDatas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCity;
        de.hdodenhof.circleimageview.CircleImageView avatar;
        View content;
        RelativeLayout newmsg_icon;
        Button addressBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCity = (TextView) itemView.findViewById(R.id.tvCity);
            avatar = (de.hdodenhof.circleimageview.CircleImageView) itemView.findViewById(R.id.ivAvatar);
            addressBtn=itemView.findViewById(R.id.addressBtn);

        }
    }
}
