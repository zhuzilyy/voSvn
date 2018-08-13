package com.zl.vo_.main.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.zl.vo_.DemoHelper;
import com.zl.vo_.DemoModel;

import com.zl.vo_.R;
import com.zl.vo_.db.DemoDBManager;
import com.zl.vo_.main.Entity.LoginData;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_fragments.MainActivity;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.main.views.ClearEditText;
import com.zl.vo_.main.views.MEditText;
import com.zl.vo_.util.Utils;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/26.
 */

public class LoginAfterActivityVo extends VoBaseActivity implements View.OnClickListener {
    @BindView(R.id.login2_back)
    public LinearLayout login2_back;
    @BindView(R.id.login_name)
    public ClearEditText login_name;
    @BindView(R.id.login_pwd)
    public ClearEditText login_pwd;
    @BindView(R.id.login_submit)
    public CircularProgressButton login_submit;
    public DemoModel demoModel;
    @BindView(R.id.loading_view)
    public RelativeLayout loadingview;
    @BindView(R.id.login_forgetpwd)
    public TextView login_forgetpwd;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.lay_login2);
        ButterKnife.bind(this);
        //===========更改状态栏的颜色开始============================
//        Window window = this.getWindow();
//        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        //设置状态栏颜色
//        window.setStatusBarColor(getResources().getColor(R.color.blue02version));

        //**********************更改状态栏的颜色结束**************************

        VoBaseActivity.addActivity(this);
        mInit();
    }
    private void mInit() {
        demoModel = new DemoModel(LoginAfterActivityVo.this);
        login_submit.setIndeterminateProgressMode(true);
    }
    @OnClick({R.id.login2_back,R.id.login_submit,R.id.login_forgetpwd})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login2_back:
                finish();
                break;
            case R.id.login_submit:
                loginMethod();
                Utils.downInput(LoginAfterActivityVo.this,login_submit);
                break;
            case R.id.login_forgetpwd:
                //重置密码
                startActivity(new Intent(LoginAfterActivityVo.this,ForGetPWDActivityVo.class));
                break;
            default:
                break;

        }
    }
    private void loginMethod() {
        final String loginName = login_name.getText().toString().trim();
                final String loginPwd = login_pwd.getText().toString().trim();
                if (TextUtils.isEmpty(loginName)) {
                    Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(loginPwd)) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                login_submit.setProgress(0);
                login_submit.setProgress(99);

                RequestParams params=new RequestParams(Url.LoginURL);
                params.addParameter("account_mobile",loginName);
                params.addParameter("password",loginPwd);
                x.http().post(params, new MyCommonCallback<Result<LoginData>>() {
                    @Override
                    public void success(Result<LoginData> data) {

                       // loadingview.setVisibility(View.GONE);
                        LoginData loginData=data.data;
                        LoginData.LoginInfo loginInfo=loginData.getInfo();
                        if("0".equals(data.code)){
                          //  Toast.makeText(LoginAfterActivityVo.this, ""+data.info, Toast.LENGTH_SHORT).show();
                            if(loginInfo!=null){
                                LoginData.LoginInfo.LoginAccountInfo user=loginInfo.getAccount_info();
                                if(user!=null){
                                    //登录环信
                                    LoginHX(user,loginName);
                                }
                            }
                        }else {
                            login_submit.setProgress(0);
                            Toast.makeText(LoginAfterActivityVo.this, data.info, Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void error(Throwable ex, boolean isOnCallback) {
                       // loadingview.setVisibility(View.GONE);
                         login_submit.setProgress(-1);
                      //  Log.i("io","log");
                       // Toast.makeText(LoginAfterActivityVo.this, ""+ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /****
     * 登录环信
     *
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
                     //loadingview.setVisibility(View.GONE);
                        // ** manually load all local groups and conversation
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        login_submit.setProgress(100);
                        demoModel.setCurrentUserName(user.getHuanxin_account());
                        demoModel.setSettingVerification("1".equals(user.getFriend_apply())?true:false);

                        //保存账号
                        String accont = DemoDBManager.getInstance().getSwitchAccount(name);
                        if (!name.equals(accont)) {
                            DemoDBManager.getInstance().saveAccountSwitch(name);
                        }
                        /****
                         * 保存当前登录用户的信息
                         */
                        myUtils.saveUser(user, LoginAfterActivityVo.this);
                        startActivity(new Intent(LoginAfterActivityVo.this, MainActivity.class));
                      // LoginActivity.instance.finish();
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
                      //  loadingview.setVisibility(View.GONE);
                        login_submit.setProgress(-1);
                      //  Toast.makeText(LoginAfterActivityVo.this, s, Toast.LENGTH_SHORT).show();
                        if("User is already login".equals(s)){
                            logout(true);
                        }


                    }
                });
            }
            @Override
            public void onProgress(int i, String s) {
                Log.i("cspp", "onProgress" + s);
            }
        });


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
                            myUtils.clearSharedUser(LoginAfterActivityVo.this);
                            //杀死进程

                        }else {
                            //Toast.makeText(SettingsActivityVo.this, "退出登录", Toast.LENGTH_SHORT).show();
                            myUtils.clearSharedUser(LoginAfterActivityVo.this);

                        }

                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(LoginAfterActivityVo.this, "退出登录移除", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onProgress(int i, String s) {
            }
        });
    }
}
