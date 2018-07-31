package com.zl.vo_.main.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hyphenate.easeui.widget.EaseSwitchButton;
import com.zl.vo_.R;
import com.zl.vo_.main.Entity.LoginData;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/23.
 */

public class AddMeAsFriendWayActivityVo extends VoBaseActivity implements View.OnClickListener{
    @BindView(R.id.iv_back)
    public ImageView back;
    @BindView(R.id.switch_VO_Verification)
    public EaseSwitchButton switch_VO_Verification;
    @BindView(R.id.switch_phoneNumber)
    public EaseSwitchButton switch_phoneNumber;
    @BindView(R.id.switch_groupchat)
    public EaseSwitchButton switch_groupchat;
    @BindView(R.id.switch_qrcode)
    public EaseSwitchButton switch_qrcode;
    @BindView(R.id.switch_card)
    public EaseSwitchButton switch_card;
    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_addme_asfriend);
        VoBaseActivity.addActivity(this);
        ButterKnife.bind(this);
        mInit();
    }

    private void mInit() {
       LoginData.LoginInfo.LoginAccountInfo user= myUtils.readUser(AddMeAsFriendWayActivityVo.this);
        if(user!=null){

            //vo账号搜索到我
            if("1".equals(user.getApply_account())){
                switch_VO_Verification.openSwitch();
            }else {
                switch_VO_Verification.closeSwitch();
            }

            //手机号搜索到我
            if("1".equals(user.getApply_mobile())){
                switch_phoneNumber.openSwitch();
            }else {
                switch_phoneNumber.closeSwitch();
            }

            //群聊可以添加我
            if("1".equals(user.getApply_group())){
                switch_groupchat.openSwitch();
            }else {
                switch_groupchat.closeSwitch();
            }

            //二维码可以添加我
            if("1".equals(user.getApply_erweima())){
                switch_qrcode.openSwitch();
            }else {
                switch_qrcode.closeSwitch();
            }

            //名片可以添加我
            if("1".equals(user.getApply_card())){
                switch_card.openSwitch();
            }else {
                switch_card.closeSwitch();
            }



        }


    }
    @OnClick({R.id.iv_back,R.id.switch_VO_Verification,R.id.switch_phoneNumber,
            R.id.switch_groupchat,R.id.switch_qrcode,R.id.switch_card})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.switch_VO_Verification:
                //vo号
                if(switch_VO_Verification.isSwitchOpen()){
                    switch_VO_Verification.closeSwitch();

                    setWay("apply_account");

                }else {
                    switch_VO_Verification.openSwitch();
                    setWay("apply_account");
                }
                break;
            case R.id.switch_phoneNumber:
                //手机号
                if(switch_phoneNumber.isSwitchOpen()){
                    switch_phoneNumber.closeSwitch();
                    setWay("apply_mobile");

                }else {
                    switch_phoneNumber.openSwitch();
                    setWay("apply_mobile");
                }
                break;
            case R.id.switch_groupchat:
                //群组
                if(switch_groupchat.isSwitchOpen()){
                    switch_groupchat.closeSwitch();
                    setWay("apply_group");

                }else {
                    switch_groupchat.openSwitch();
                    setWay("apply_group");
                }
                break;
            case R.id.switch_qrcode:
                //二维码
                if(switch_qrcode.isSwitchOpen()){
                    switch_qrcode.closeSwitch();
                    setWay("apply_erweima");
                }else {
                    switch_qrcode.openSwitch();
                    setWay("apply_erweima");
                }
                break;
            case R.id.switch_card:
                //名片
                if(switch_card.isSwitchOpen()){
                    switch_card.closeSwitch();
                    setWay("apply_card");

                }else {
                    switch_card.openSwitch();
                    setWay("apply_card");
                }
                break;
            default:
                break;
        }
    }


    public void setWay(String way){
        loading_view.setVisibility(View.VISIBLE);
        RequestParams params=new RequestParams(Url.UpdateInfoUrl);
        params.addParameter("userid",myUtils.readUser(AddMeAsFriendWayActivityVo.this).getUserid());
        params.addParameter("param",way);
        x.http().post(params, new MyCommonCallback<Result<LoginData>>() {
            @Override
            public void success(Result<LoginData> data) {
                loading_view.setVisibility(View.GONE);
                LoginData loginData=data.data;
                LoginData.LoginInfo loginInfo=loginData.getInfo();
                if(loginInfo!=null){
                    LoginData.LoginInfo.LoginAccountInfo user=loginInfo.getAccount_info();
                    if(user!=null){
                        Log.i("uu","pp=="+user.getAvatar());
                        //清除本地保存的用户信息
                        myUtils.clearSharedUser(AddMeAsFriendWayActivityVo.this);
                        myUtils.saveUser(user,AddMeAsFriendWayActivityVo.this);

                    }
                }
            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                loading_view.setVisibility(View.GONE);
            }
        });
    }


}
