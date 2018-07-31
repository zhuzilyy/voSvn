package com.zl.vo_.main.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zl.vo_.R;
import com.zl.vo_.main.Entity.LoginData;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.widget.RoundImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/11/22.
 */

public class CurrentUserInfoActivityVo extends VoBaseActivity implements View.OnClickListener{
   @BindView(R.id.re_avatar)
    public RelativeLayout re_avatar;
    @BindView(R.id.re_name)
    public RelativeLayout re_name;
    @BindView(R.id.re_void)
    public RelativeLayout re_void;
    @BindView(R.id.re_qrcode)
    public RelativeLayout re_qrcode;
    @BindView(R.id.re_more)
    public RelativeLayout re_more;

    //----------------------------
    @BindView(R.id.iv_avatar)
    public CircleImageView iv_avatar;
    @BindView(R.id.tv_name)
    public TextView tv_name;
    @BindView(R.id.tv_Void)
    public TextView tv_Void;
    public MyReceiver myReceiver;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_currentuser_info);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        //---------------------------
        myReceiver=new MyReceiver();
        IntentFilter filter=new IntentFilter("FixPersonInfoOK");
        registerReceiver(myReceiver,filter);

        //-------------------------
        mInit();
    }

    private void mInit() {

        LoginData.LoginInfo.LoginAccountInfo currentUser= myUtils.readUser(CurrentUserInfoActivityVo.this);

        if(currentUser!=null){

            if(!TextUtils.isEmpty(currentUser.getAvatar())){
                //默认头像
                Picasso.with(CurrentUserInfoActivityVo.this).load(myUtils.readUser(CurrentUserInfoActivityVo.this).getAvatar())
                        .placeholder(R.drawable.fx_default_useravatar).into(iv_avatar);
            }

            //昵称
            tv_name.setText(myUtils.readUser(CurrentUserInfoActivityVo.this).getNickname());
            //void
            tv_Void.setText("vo号："+myUtils.readUser(CurrentUserInfoActivityVo.this).getAccount());
        }


    }
    @OnClick({R.id.re_avatar,R.id.re_name,R.id.re_void,R.id.re_qrcode,R.id.re_more})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.re_avatar:
                //头像
                startActivity(new Intent(CurrentUserInfoActivityVo.this,FixAvatarActivityVo.class));
                break;
            case R.id.re_name:
                //昵称
                startActivity(new Intent(CurrentUserInfoActivityVo.this,FixNameActivityVo.class));
                break;
            case R.id.re_void:
               LoginData.LoginInfo.LoginAccountInfo user= myUtils.readUser(CurrentUserInfoActivityVo.this);
                if(user!=null){
                    if("1".equals(user.getSet_account())){

                    }else {
                        startActivity(new Intent(CurrentUserInfoActivityVo.this,FixVOIDActivityVo.class));

                    }
                }



                //void
                break;
            case R.id.re_qrcode:
                //二维码
                startActivity(new Intent(CurrentUserInfoActivityVo.this,FixQrcodeActivityVo.class));
                break;
            case R.id.re_more:
                //更多
                startActivity(new Intent(CurrentUserInfoActivityVo.this,FixMoreActivityVo.class));
                break;
            default:
                break;
        }
    }

    /*****
     * 广播接收者
     */
    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if("FixPersonInfoOK".equals(action)){
                LoginData.LoginInfo.LoginAccountInfo currentUser= myUtils.readUser(CurrentUserInfoActivityVo.this);

                if(currentUser!=null){
                    if(!TextUtils.isEmpty(currentUser.getAvatar())){
                        //默认头像
                        Picasso.with(CurrentUserInfoActivityVo.this).load(myUtils.readUser(CurrentUserInfoActivityVo.this).getAvatar())
                                .placeholder(R.drawable.fx_default_useravatar).into(iv_avatar);
                    }
                    //昵称
                    tv_name.setText(myUtils.readUser(CurrentUserInfoActivityVo.this).getNickname());
                    //void
                    tv_Void.setText("vo号："+myUtils.readUser(CurrentUserInfoActivityVo.this).getAccount());
                }



            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(myReceiver!=null){
            unregisterReceiver(myReceiver);
        }
    }
}
