package com.zl.vo_.myindexbar;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zl.vo_.R;
import com.zl.vo_.db.InviteMessgeDao;
import com.zl.vo_.main.activities.GroupManagerActivityVo;
import com.zl.vo_.main.activities.MyFrindEntivity;
import com.zl.vo_.main.activities.NewFriendsMsgActivityVo;
import com.zl.vo_.main.activities.addFriendActivity_ContactsVo;
import com.zl.vo_.ui.GroupsActivity;

import java.util.List;


/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
    protected Context mContext;
    protected List<MyFrindEntivity> mDatas;
    protected LayoutInflater mInflater;
    protected int hasNewMsg;
    public myContactsClickListener listener;
    private InviteMessgeDao inviteMessgeDao=new InviteMessgeDao(mContext);

    public CityAdapter(Context mContext, List<MyFrindEntivity> mDatas, int hasNewMsg) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);
        this.hasNewMsg=hasNewMsg;
    }
    public List<MyFrindEntivity> getDatas() {
        return mDatas;
    }

    public CityAdapter setDatas(List<MyFrindEntivity> datas) {
        mDatas = datas;
        return this;
    }

    @Override
    public CityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_city, parent, false));
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
    public void onBindViewHolder(final CityAdapter.ViewHolder holder, final int position) {
        final MyFrindEntivity friend = mDatas.get(position);
        switch(position){
            case 0:
                if(1==hasNewMsg){
                    holder.newmsg_icon.setVisibility(View.VISIBLE);
                }else {
                    holder.newmsg_icon.setVisibility(View.GONE);
                }
                holder.avatar.setImageResource(R.mipmap.newfriend);
                //新的朋友
                holder.content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.startActivity(new Intent(mContext, NewFriendsMsgActivityVo.class));
                    }
                });

                break;
            case 1:
                holder.avatar.setImageResource(R.mipmap.contactsfriends);
                //通讯录好友
                holder.content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.startActivity(new Intent(mContext, addFriendActivity_ContactsVo.class));
                    }
                });
                break;
            case 2:
            //    holder.line.setVisibility(View.GONE);
                holder.avatar.setImageResource(R.mipmap.groupchat);
                //群聊
                holder.content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.startActivity(new Intent(mContext, GroupsActivity.class));
                    }
                });
                break;

            default:
                Glide.with(mContext).load(friend.getAvatar()).into(holder.avatar);
               // holder.line.setVisibility(View.GONE);
                holder.content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onMyContactItemClick(position);
                       /*Intent intent=new Intent(mContext, ChatActivity.class);
                        intent.putExtra("userId",mDatas.get(position).getHuanxinID());
                        intent.putExtra("account",mDatas.get(position).getAccount());
                        intent.putExtra("avatar", myUtils.readUser(mContext).getAvatar());
                        intent.putExtra("nick",myUtils.readUser(mContext).getNickname());
                        mContext.startActivity( intent);*/
                        return;
                    }
                });
                holder.content.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        listener.onMyContactItemLongClick(position);
                        return true;
                    }
                });


                break;
        }

        holder.tvCity.setText(friend.getRemark());


    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCity;
        de.hdodenhof.circleimageview.CircleImageView avatar;
        View content;
        RelativeLayout newmsg_icon;
        View line;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCity = (TextView) itemView.findViewById(R.id.tvCity);
            avatar = (de.hdodenhof.circleimageview.CircleImageView) itemView.findViewById(R.id.ivAvatar);
            content = itemView.findViewById(R.id.content);
            newmsg_icon=itemView.findViewById(R.id.newmsg_icon);
            line = itemView.findViewById(R.id.line);
        }
    }
}
