package com.zl.vo_.main.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zl.vo_.Constant;
import com.zl.vo_.R;
import com.zl.vo_.main.Entity.friendHintEntivity;
import com.zl.vo_.main.activities.UserDetailsActivityVo;
import com.zl.vo_.widget.RoundImageView;

import java.util.List;

/**
 * Created by Administrator on 2017/11/21.
 */

public class AddFriendInvatitionAdapter extends BaseAdapter {
    private Context context;
    private List<friendHintEntivity.friendHintInfo.friend_hintArr> bigList;
    private Activity activity;

    public AddFriendInvatitionAdapter(Context context, List<friendHintEntivity.friendHintInfo.friend_hintArr> bigList, Activity addFriendActivity_contacts) {
        this.context = context;
        this.bigList = bigList;
        this.activity=addFriendActivity_contacts;
    }

    @Override
    public int getCount() {
        Log.i("size","大小="+bigList.size());
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        friendHintEntivity.friendHintInfo.friend_hintArr itemData= bigList.get(i);
        viewHolder holder=null;
        if(view==null){
            holder=new viewHolder();
            view= LayoutInflater.from(context).inflate(R.layout.lay_addfriend_item,null);
            holder.head=view.findViewById(R.id.addressHead);
            holder.title=view.findViewById(R.id.addressName);
            holder.phone=view.findViewById(R.id.addressPhone);
            holder.btn=view.findViewById(R.id.addressBtn);
            view.setTag(holder);

        }else {
            holder= (viewHolder) view.getTag();
        }
        Picasso.with(context).load(itemData.getAvatar()).placeholder(R.mipmap.girl2).into(holder.head);
        holder.title.setText(itemData.getNickname());
        holder.phone.setText(itemData.getMobile());

        if(itemData.getTag().equals("1")){
            holder.btn.setText("添加");
            holder.btn.setBackground(context.getResources().getDrawable(R.drawable.selector_accept2));

        }else {
            holder.btn.setText("邀请");
            holder.btn.setBackground(context.getResources().getDrawable(R.drawable.selector_accept));
        }

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("邀请".equals(((Button)view).getText().toString().trim())){
                  //  Toast.makeText(context, "发链接", Toast.LENGTH_SHORT).show();
                    //发送短信
                    sendSMSwithLink(bigList.get(i).getMobile());



                }else {
                   // Toast.makeText(context, "进入个人详情界面", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context, UserDetailsActivityVo.class);
                    intent.putExtra("HXid",bigList.get(i).getHuanxin_account());
                    intent.putExtra("way","apply_mobile");
                    context.startActivity(intent);


                }
            }
        });



        return view;
    }

    /****
     * 发送短信邀请
     * @param mobile
     */
    private void sendSMSwithLink(final String mobile) {


        final Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.lay_dia_sms);
        dialog.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    SmsManager smsManager = SmsManager.getDefault();
                    PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0, new Intent(), 0);
                    smsManager.sendTextMessage(mobile, null, "快来下载VO，和好友一起畅享VO娱乐"+ Constant.DOWN_LOAD_URL, sentIntent, null);
                    Toast.makeText(context, "短信已发送", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(context, "短信发送失败", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
        dialog.show();







    }

    class viewHolder{

        private RoundImageView head;
        private TextView title;
        private TextView phone;
        private Button btn;

    }
}
