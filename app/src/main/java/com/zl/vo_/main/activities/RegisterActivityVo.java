package com.zl.vo_.main.activities;

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

import com.zl.vo_.R;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.views.ClearEditText;
import com.zl.vo_.main.views.MEditText;
import com.zl.vo_.util.CodeTimerTask;
import com.zl.vo_.util.Utils;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/27.
 */

public class RegisterActivityVo extends VoBaseActivity implements View.OnClickListener{
    @BindView(R.id.register_phone)
    public ClearEditText register_phone;
    @BindView(R.id.register_code)
    public ClearEditText register_code;
    @BindView(R.id.register_pwd)
    public ClearEditText register_pwd;
    @BindView(R.id.register_getcode)
    public TextView register_getcode;
    @BindView(R.id.register_nick)
    public ClearEditText register_nick;
    @BindView(R.id.register_submit)
    public Button register_submit;

    @BindView(R.id.ll_back)
    public LinearLayout ll_back;
    @BindView(R.id.ll_Protocol)
    public LinearLayout ll_Protocol;

    //--------------------
    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_register);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        mInit();
    }

    private void mInit() {

    }
    @OnClick({R.id.register_submit,R.id.ll_back,R.id.register_getcode,R.id.ll_Protocol})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.register_submit:
                register();
                Utils.downInput(RegisterActivityVo.this,register_submit);
                break;
            case R.id.ll_back:
                finish();
                break;
            case R.id.register_getcode:
                getCode();
                Utils.downInput(RegisterActivityVo.this,register_getcode);
                break;
            case R.id.ll_Protocol:
                Intent intent=new Intent(RegisterActivityVo.this,Help_Feedback.class);
                intent.putExtra("title","用户协议");
                intent.putExtra("url",Url.NEW_UserAgreementUrl);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    /***
     * 获取验证码
     */
    private void getCode() {
        final String register_phone_str2 = register_phone.getText().toString().trim();
        if(TextUtils.isEmpty(register_phone_str2)){
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
//        if(!myUtils.isMobileNO(register_phone_str2)){
//            Toast.makeText(this, "格式错误", Toast.LENGTH_SHORT).show();
//            return;
//        }

        if(register_phone_str2.length()!=11){
            Toast.makeText(this, "格式错误", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestParams params=new RequestParams(Url.getCodeUrl);
        params.addParameter("mobile",register_phone_str2);
        params.addParameter("type","1");
        x.http().post(params, new MyCommonCallback<Result>() {
            @Override
            public void success(Result data) {
                    Log.i("sss",data.info);
                    Toast.makeText(RegisterActivityVo.this, data.info, Toast.LENGTH_SHORT).show();
                    if("0".equals(data.code)){
                        CodeTimerTask.getInstence().starrTimer(register_getcode);
                    }else {
                        Log.i("errr",data.info);
                    }

            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                Log.i("errr",ex.getMessage());
            }
        });



    }

    /***
     * 注册
     */
    private void register() {



        final String register_phone_str = register_phone.getText().toString().trim();
        final String register_code_str = register_code.getText().toString().trim();
        final String register_pwd_str = register_pwd.getText().toString().trim();
        final String register_nick_str = register_nick.getText().toString().trim();


        if (TextUtils.isEmpty(register_phone_str)) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            register_phone.requestFocus();
            return;
        }else if(TextUtils.isEmpty(register_code_str)){
            Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            register_code.requestFocus();
        }else if (TextUtils.isEmpty(register_pwd_str)) {
            Toast.makeText(this, getResources().getString(R.string.Password_cannot_be_empty), Toast.LENGTH_SHORT).show();
            register_pwd.requestFocus();
            return;
        }
        if (!TextUtils.isEmpty(register_phone_str) && !TextUtils.isEmpty(register_pwd_str)) {

            loading_view.setVisibility(View.VISIBLE);
            RequestParams params=new RequestParams(Url.RegisterURL);
            params.addParameter("account",register_phone_str);
            params.addParameter("smscode",register_code_str);
            params.addParameter("mobile",register_phone_str);
            params.addParameter("password",register_pwd_str);
            params.addParameter("nickname",register_nick_str);
            x.http().post(params, new MyCommonCallback<Result>() {
                @Override
                public void success(Result data) {
                    loading_view.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivityVo.this, data.info, Toast.LENGTH_SHORT).show();
                    if("0".equals(data.code)){
                        finish();
                    }
                }

                @Override
                public void error(Throwable ex, boolean isOnCallback) {
                    loading_view.setVisibility(View.GONE);
                    Log.i("yy",ex+"======//-------error------*");
                }
            });


        }


    }
}
