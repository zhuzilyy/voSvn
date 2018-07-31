package com.zl.vo_.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.zl.vo_.DemoModel;
import com.zl.vo_.R;
import com.zl.vo_.db.DemoDBManager;
import com.zl.vo_.main.Entity.LoginData;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_fragments.MainActivity;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.main.views.MEditText;
import com.zl.vo_.util.CodeTimerTask;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/9.
 */

public class BindPhoneActivityVo extends VoBaseActivity implements View.OnClickListener {
    @BindView(R.id.ll_back)
    public LinearLayout back;
    @BindView(R.id.reg_phone)
    public MEditText phone;
    @BindView(R.id.account)
    public MEditText account;
    @BindView(R.id.reg_smsCode)
    public MEditText smsCode;
    @BindView(R.id.reg_tv_smsCode)
    public TextView tv_smsCode;
    @BindView(R.id.reg_pwd)
    public MEditText pwd;
    @BindView(R.id.reg_confirm_pwd)
    public MEditText conPwd;
    @BindView(R.id.reg_submit)
    public Button reg_sub;
    private String type = "1";
    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;
    @BindView(R.id.ll_bindPhone)
    public LinearLayout ll_bindPhone;
    private String typeCode;
    private Intent intent;
    private String openid, unionid, nickname, headimgurl, sex, confirmPwd;
    private DemoModel demoModel = new DemoModel(BindPhoneActivityVo.this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        VoBaseActivity.addActivity(this);
        ButterKnife.bind(this);
        mInit();
    }

    private void mInit() {
        // LoginActivity.instance.finish();
        intent = getIntent();
        if (intent != null) {
            openid = intent.getStringExtra("openid");
            unionid = intent.getStringExtra("unionid");
            nickname = intent.getStringExtra("nickname");
            headimgurl = intent.getStringExtra("headimgurl");
            sex = intent.getStringExtra("sex");
        }

    }

    @OnClick({R.id.ll_back, R.id.reg_tv_smsCode, R.id.reg_submit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.reg_tv_smsCode:
                //     String userName=account.getText().toString().trim();
                //    String phoneStr= phone.getText().toString().trim();
//                if(TextUtils.isEmpty(userName)){
//                    Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if(TextUtils.isEmpty(phoneStr)){
//                    Toast.makeText(this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
//                    return;
//                }


                getCode();
                break;
            case R.id.reg_submit:
                String phone_ = phone.getText().toString().trim();
                String smsCode_ = smsCode.getText().toString().trim();
                String pwd_ = pwd.getText().toString().trim();
                confirmPwd = conPwd.getText().toString().trim();

                if (TextUtils.isEmpty(phone_)) {
                    Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(smsCode_)) {
                    Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pwd_.equals(confirmPwd)) {
                    Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                wechatRegister(phone_, smsCode_);

                break;
            default:
                break;
        }
    }

    //微信注册
    private void wechatRegister(String phone, String confirmCode) {
        loading_view.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams(Url.WX_REGISTER);
        params.addParameter("openid", openid);
        params.addParameter("unionid", unionid);
        params.addParameter("mobile", phone);
        params.addParameter("smscode", confirmCode);
        params.addParameter("headimgurl", headimgurl);
        params.addParameter("nickname", nickname);
        params.addParameter("sex", sex);
        params.addParameter("password", confirmPwd);

        x.http().post(params, new MyCommonCallback<Result<LoginData>>() {
            @Override
            public void success(Result<LoginData> data) {
                loading_view.setVisibility(View.GONE);
                LoginData loginData = data.data;
                if (loginData != null) {
                    LoginData.LoginInfo loginInfo = loginData.getInfo();
                    if (loginInfo != null) {
                        LoginData.LoginInfo.LoginAccountInfo user = loginInfo.getAccount_info();
                        if (user != null) {
                            //登录环信
                            LoginHX(user, user.getAccount());
                        }


                    }
                }
            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                loading_view.setVisibility(View.GONE);
                Log.i("u", ex.getMessage());
            }
        });
    }


    /****
     * 登录环信
     */
    private void LoginHX(final LoginData.LoginInfo.LoginAccountInfo user, final String name) {
        // DemoDBManager.getInstance().closeDB();
        EMClient.getInstance().login(user.getHuanxin_account(), user.getHuanxin_account(), new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i("cspp", "onSuccess");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // ** manually load all local groups and conversation
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();

                        demoModel.setCurrentUserName(user.getHuanxin_account());
                        demoModel.setSettingVerification("1".equals(user.getFriend_apply()) ? true : false);
//                        boolean updatenick = EMClient.getInstance().updateCurrentUserNick(
//                                user.getNickname());
//                        if (!updatenick) {
//                            Log.e("LoginActivity", "update current user nick fail");
//                        }
                        //保存账号
                        String accont = DemoDBManager.getInstance().getSwitchAccount(name);
                        if (!name.equals(accont)) {
                            DemoDBManager.getInstance().saveAccountSwitch(name);
                        }
                        /****
                         * 保存当前登录用户的信息
                         */
                        myUtils.saveUser(user, BindPhoneActivityVo.this);
                        startActivity(new Intent(BindPhoneActivityVo.this, MainActivity.class));
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
                        Toast.makeText(BindPhoneActivityVo.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {
                Log.i("cspp", "onProgress" + s);
            }
        });


    }


    /***
     * 去注册
     *
     * @param numPhone
     * @param pwd
     * @param smsCode
     * @param uType
     */
    private void gotoReg(String numPhone, String pwd, String smsCode, String uType) {
        loading_view.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams(Url.RegisterURL);
        params.addParameter("mobile", numPhone);
        params.addParameter("password", pwd);
        params.addParameter("smscode", smsCode);
        x.http().post(params, new MyCommonCallback<Result>() {
            @Override
            public void success(Result data) {
                loading_view.setVisibility(View.GONE);
                String code = data.code;
                Toast.makeText(BindPhoneActivityVo.this, data.info, Toast.LENGTH_SHORT).show();
                if ("0".equals(code)) {
                    finish();
                }
            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                loading_view.setVisibility(View.GONE);
            }
        });
    }

    /***
     * 获取验证码
     */
    private void getCode() {
        final String register_phone_str2 = phone.getText().toString().trim();
        if (TextUtils.isEmpty(register_phone_str2)) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!myUtils.isMobileNO(register_phone_str2)) {
            Toast.makeText(this, "格式错误", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestParams params = new RequestParams(Url.getCodeUrl);
        params.addParameter("mobile", register_phone_str2);
        params.addParameter("type", "3");
        x.http().post(params, new MyCommonCallback<Result>() {
            @Override
            public void success(Result data) {

                Toast.makeText(BindPhoneActivityVo.this, data.info, Toast.LENGTH_SHORT).show();
                if ("22".equals(data.code)) {
                    //需要输入密码框
                    CodeTimerTask.getInstence().starrTimer(tv_smsCode);
                    ll_bindPhone.setVisibility(View.VISIBLE);
                }
                if ("11".equals(data.code)) {
                    CodeTimerTask.getInstence().starrTimer(tv_smsCode);
                }
            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {

            }
        });


    }


}
