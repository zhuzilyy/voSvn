package com.zl.vo_.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hyphenate.easeui.widget.EaseSwitchButton;
import com.zl.vo_.DemoModel;
import com.zl.vo_.R;
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
 * Created by Administrator on 2017/11/22.
 */

public class Setting_CurrencyActivityVo extends VoBaseActivity implements View.OnClickListener{
    @BindView(R.id.iv_back)
    public ImageView iv_back;
    @BindView(R.id.privacy_addmeway_re)
    public RelativeLayout privacy_addmeway_re;@BindView(R.id.privacy_blackList_re)
    public RelativeLayout privacy_blackList_re;
    @BindView(R.id.switch_Verification)
    public EaseSwitchButton switch_Verification;
    @BindView(R.id.switch_Recommended_address_friends)
    public EaseSwitchButton switch_Recommended_address_friends;
    public DemoModel demoModel;
    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_currency);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        mInit();
    }

    private void mInit() {
        demoModel=new DemoModel(Setting_CurrencyActivityVo.this);
        //是否加我为好友需要验证
        if (demoModel.getSettingVerification()) {
            switch_Verification.openSwitch();
        } else {
            switch_Verification.closeSwitch();


        }
        //像我推荐通讯录好友
        if(demoModel.getSettingRecommedFriend()){
            switch_Recommended_address_friends.openSwitch();
        }else {
            switch_Recommended_address_friends.closeSwitch();
        }

    }
    @OnClick({R.id.iv_back,R.id.switch_Verification,R.id.switch_Recommended_address_friends,
            R.id.privacy_blackList_re,R.id.privacy_addmeway_re,})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.switch_Verification:
                //加我为好友时需要验证
                //是否显示消息详情
                if (switch_Verification.isSwitchOpen()) {
                    switch_Verification.closeSwitch();
                    demoModel.setSettingVerification(false);
                    //设置不需要验证
                    setAddMeAsFriend("friend_apply");



                } else {
                    switch_Verification.openSwitch();
                    demoModel.setSettingVerification(true);
                    //设置需要验证
                    setAddMeAsFriend("friend_apply");
                }

                break;
            case R.id.switch_Recommended_address_friends:
                //设置是否像我推荐通讯录好友
                if(switch_Recommended_address_friends.isSwitchOpen()){
                    switch_Recommended_address_friends.closeSwitch();
                    demoModel.setSettingRecommedFriend(false);
                    boolean b= demoModel.getSettingRecommedFriend();
                    Log.i("","");

                }else {
                    switch_Recommended_address_friends.openSwitch();
                    demoModel.setSettingRecommedFriend(true);

                }
                break;
            case R.id.privacy_addmeway_re:
                //添加我的方式
                startActivity(new Intent(Setting_CurrencyActivityVo.this,AddMeAsFriendWayActivityVo.class));
                break;
            case R.id.privacy_blackList_re:
                //通讯录黑名单
                startActivity(new Intent(Setting_CurrencyActivityVo.this, MyBlackListActivityVo.class));
                break;

            default:
                break;
        }
    }

    /****
     * 添加朋友时需不需要验证
     * @param friend_apply
     */
    private void setAddMeAsFriend(String friend_apply) {
        loading_view.setVisibility(View.VISIBLE);

        RequestParams params=new RequestParams(Url.UpdateInfoUrl);
        params.addParameter("userid", myUtils.readUser(Setting_CurrencyActivityVo.this).getUserid());
        params.addParameter("param",friend_apply);
        x.http().post(params, new MyCommonCallback<Result>() {
            @Override
            public void success(Result data) {
                loading_view.setVisibility(View.GONE);
                if("0".equals(data.code)){
                   // Toast.makeText(Setting_CurrencyActivityVo.this, "设置成功", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Setting_CurrencyActivityVo.this, "设置失败", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                loading_view.setVisibility(View.GONE);
                Toast.makeText(Setting_CurrencyActivityVo.this, "设置失败"+ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
