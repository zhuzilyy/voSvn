package com.zl.vo_.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.zl.vo_.DemoHelper;
import com.zl.vo_.DemoModel;
import com.zl.vo_.R;
import com.zl.vo_.main.main_utils.myUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/22.
 */

public class SettingsActivityVo extends VoBaseActivity implements View.OnClickListener {
    @BindView(R.id.iv_back)
    public ImageView iv_back;
    @BindView(R.id.re_newmsgNotify)
    public RelativeLayout re_newmsgNotify;
    @BindView(R.id.re_do_not_disturb_mode)
    public RelativeLayout re_do_not_disturb_mode;
    @BindView(R.id.re_chat)
    public RelativeLayout re_chat;
    @BindView(R.id.re_Privacy)
    public RelativeLayout re_Privacy;
    @BindView(R.id.re_currency)
    public RelativeLayout re_currency;
    @BindView(R.id.quite)
    public RelativeLayout quite;
    @BindView(R.id.tv_quite)
    public TextView tv_quite;
    @BindView(R.id.re_quite)
    public RelativeLayout re_quite;
    @BindView(R.id.quite_vo)
    public LinearLayout quite_vo;
    @BindView(R.id.quite_login)
    public LinearLayout quite_login;
    //-------------------------
    private  DemoModel demoModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_settings);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        mInit();
    }

    private void mInit() {
        demoModel=new DemoModel(SettingsActivityVo.this);
        //tv_quite.setText("退出（"+ myUtils.readUser(SettingsActivityVo.this).getNickname()+")");


    }
    @OnClick({R.id.iv_back,R.id.re_newmsgNotify,R.id.re_do_not_disturb_mode,
            R.id.re_chat,R.id.re_Privacy, R.id.re_currency,R.id.quite,R.id.quite_vo,R.id.quite_login,R.id.re_quite})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.re_newmsgNotify:
                startActivity(new Intent(SettingsActivityVo.this,Setting_NewMsgSettingsActivityVo.class));
                break;
            case R.id.re_do_not_disturb_mode:
                startActivity(new Intent(SettingsActivityVo.this,Setting_DonotDisturbModeActivityVo.class));
                break;
            case R.id.re_chat:
                startActivity(new Intent(SettingsActivityVo.this,Setting_ChatActivityVo.class));
                break;
            case R.id.re_Privacy:
                //隐私
                startActivity(new Intent(SettingsActivityVo.this,Setting_PrivacyActivityVo.class));
                break;
            case R.id.re_currency:
                startActivity(new Intent(SettingsActivityVo.this,Setting_CurrencyActivityVo.class));
                break;
            case R.id.quite:

                //退出登录
               re_quite.setVisibility(View.VISIBLE);
            break;
            case R.id.re_quite:
                re_quite.setVisibility(View.GONE);
                break;
            case R.id.quite_vo:
                //退出程序
                re_quite.setVisibility(View.GONE);
                logout(true);
                break;
            case R.id.quite_login:
                //退出登录
                re_quite.setVisibility(View.GONE);
                logout(false);
                break;
            default:
                break;
        }
    }
    /****
     * 退出登录
     */
    private void logout(final Boolean b) {
        DemoHelper.getInstance().logout(false, new EMCallBack() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        // show login scree
                        if(b){
                            //Toast.makeText(SettingsActivityVo.this, "退出程序", Toast.LENGTH_SHORT).show();
                            myUtils.clearSharedUser(SettingsActivityVo.this);
                            //杀死进程
                            VoBaseActivity.removeActivity();
                            finish();
                        }else {
                            //Toast.makeText(SettingsActivityVo.this, "退出登录", Toast.LENGTH_SHORT).show();
                            myUtils.clearSharedUser(SettingsActivityVo.this);
                            startActivity(new Intent(SettingsActivityVo.this, LoginActivity.class));
                            VoBaseActivity.removeActivity();
                            finish();
                        }

                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(SettingsActivityVo.this, "退出登录移除", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onProgress(int i, String s) {
            }
        });
    }
}
