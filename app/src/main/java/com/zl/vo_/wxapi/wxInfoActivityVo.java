package com.zl.vo_.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.zl.vo_.DemoModel;
import com.zl.vo_.R;
import com.zl.vo_.main.Entity.LoginData;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.activities.VoBaseActivity;
import com.zl.vo_.main.activities.BindPhoneActivityVo;
import com.zl.vo_.main.activities.Help_Feedback;
import com.zl.vo_.main.activities.LoginActivity;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_fragments.MainActivity;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/12/26.
 */

public class wxInfoActivityVo extends VoBaseActivity implements View.OnClickListener {
    private String openidStr;
    private String unionidStr;
    private String nicknameStr;
    private String sexStr;
    private String headimgurlStr;
    public DemoModel demoModel=new DemoModel(wxInfoActivityVo.this);
    //----------------
    @BindView(R.id.wx_head)
    public CircleImageView wx_head;
    @BindView(R.id.wx_name)
    public TextView wx_name;
    @BindView(R.id.ll_Protocol)
    public LinearLayout ll_Protocol;
    @BindView(R.id.wx_next)
    public Button wx_next;
    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.lay_wxinfo);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        mInit();

    }

    private void mInit() {
        Intent intent= getIntent();
        openidStr=intent.getStringExtra("openid");
        unionidStr=intent.getStringExtra("unionid");
        nicknameStr=intent.getStringExtra("nickname");
        sexStr=intent.getStringExtra("sex");
        headimgurlStr=intent.getStringExtra("headimgurl");

        if(!TextUtils.isEmpty(headimgurlStr)){
            Glide.with(wxInfoActivityVo.this).load(headimgurlStr).into(wx_head);
        }
        if(!TextUtils.isEmpty(nicknameStr)){
            wx_name.setText(nicknameStr+",欢迎来到这里!");
        }


    }
    @OnClick({R.id.ll_Protocol,R.id.wx_next})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ll_Protocol:
                //用户协议
                Intent intent=new Intent(wxInfoActivityVo.this, Help_Feedback.class);
                intent.putExtra("title","用户协议");
                intent.putExtra("url","http://47.95.115.55:8080/voadmin/home/api/webview?id=3");
                startActivity(intent);
                break;
            case R.id.wx_next:
                WechatLogin(openidStr,unionidStr,nicknameStr,Integer.parseInt(sexStr),headimgurlStr);
                break;
            default:
                break;
        }
    }

    //微信登录
    private void WechatLogin(final String openId,final String unionId,final String nickname,final int sex,final String headimgurl){
        loading_view.setVisibility(View.VISIBLE);
        RequestParams params=new RequestParams(Url.WX_LOGIN);
        params.addParameter("openid",openId);
        params.addParameter("unionid",unionId);
        x.http().post(params, new MyCommonCallback<Result<LoginData>>() {
            @Override
            public void success(Result<LoginData> data) {
                Log.i("123",data.info);
                Intent intentSuccess=new Intent();
                intentSuccess.setAction("com.action.successLogin");
                sendBroadcast(intentSuccess);
                String code = data.code;
                if (code.equals("-1111")) {
                    Intent intent = new Intent(wxInfoActivityVo.this, BindPhoneActivityVo.class);
                    intent.putExtra("openid", openId);
                    intent.putExtra("unionid", unionId);
                    intent.putExtra("nickname", nickname);
                    intent.putExtra("headimgurl", headimgurl);
                    if (sex == 1) {
                        intent.putExtra("sex", "0");
                    } else {
                        intent.putExtra("sex", "1");
                    }
                    startActivity(intent);
                    LoginActivity.instance.finish();
                    finish();

                }else if ("0".equals(code)) {
                    LoginData loginData = data.data;
                    if (loginData != null) {
                        LoginData.LoginInfo loginInfo = loginData.getInfo();
                        if (loginInfo != null) {
                            LoginData.LoginInfo.LoginAccountInfo user=loginInfo.getAccount_info();
                            if(user!=null){
                                //登录环信
                                LoginHX(user,user.getAccount());
                            }
                        }
                    }
                }
            }
            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                Log.i("123",ex.getMessage());
                loading_view.setVisibility(View.GONE);
                Intent intentFail=new Intent();
                intentFail.setAction("com.action.failLogin");
                sendBroadcast(intentFail);
            }
        });
    }
    /****
     * 登录环信
     *
     */
    private void LoginHX(final LoginData.LoginInfo.LoginAccountInfo user, final String name) {
        // DemoDBManager.getInstance().closeDB();
        EMClient.getInstance().login(user.getHuanxin_account(),user.getHuanxin_account(),new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i("cspp", "onSuccess");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading_view.setVisibility(View.GONE);
                        // ** manually load all local groups and conversation
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();

                        demoModel.setCurrentUserName(user.getHuanxin_account());
                        demoModel.setSettingVerification("1".equals(user.getFriend_apply())?true:false);
//                        boolean updatenick = EMClient.getInstance().updateCurrentUserNick(
//                                user.getNickname());
//                        if (!updatenick) {
//                            Log.e("LoginActivity", "update current user nick fail");
//                        }
                        //保存账号
//						String accont= DemoDBManager.getInstance().getSwitchAccount(name);
//						if(!name.equals(accont)){
//							DemoDBManager.getInstance().saveAccountSwitch(name);
//						}
                        /****
                         * 保存当前登录用户的信息
                         */
                        myUtils.saveUser(user,wxInfoActivityVo.this);
                        startActivity(new Intent(wxInfoActivityVo.this, MainActivity.class));
                        LoginActivity.instance.finish();
                        finish();
                    }
                });
            }
            @Override
            public void onError(int i, final String s) {
                Log.i("cspp", "onError" + s);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading_view.setVisibility(View.GONE);
                        Toast.makeText(wxInfoActivityVo.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onProgress(int i, String s) {
                Log.i("cspp", "onProgress" + s);
            }
        });
    }
}
