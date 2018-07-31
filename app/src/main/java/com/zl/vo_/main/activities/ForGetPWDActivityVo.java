package com.zl.vo_.main.activities;

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
import com.zl.vo_.main.main_utils.myUtils;
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

public class ForGetPWDActivityVo extends VoBaseActivity implements View.OnClickListener{
    @BindView(R.id.forgetpwd_phone)
    public MEditText forgetpwd_phone;
    @BindView(R.id.forgetpwd_code)
    public MEditText forgetpwd_code;
    @BindView(R.id.forgetpwd_pwd)
    public MEditText forgetpwd_pwd;
    @BindView(R.id.forgetpwd_getcode)
    public TextView forgetpwd_getcode;
    @BindView(R.id.forgetpwd_submit)
    public Button forgetpwd_submit;

    @BindView(R.id.ll_back)
    public LinearLayout ll_back;




    //--------------------
    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_forgetpwd);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        mInit();
    }

    private void mInit() {
    }
    @OnClick({R.id.forgetpwd_submit,R.id.ll_back,R.id.forgetpwd_getcode})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.forgetpwd_submit:
                fixPWD();
                Utils.downInput(ForGetPWDActivityVo.this,forgetpwd_submit);
                break;
            case R.id.ll_back:
                finish();
                break;
            case R.id.forgetpwd_getcode:
                getCode();
                Utils.downInput(ForGetPWDActivityVo.this,forgetpwd_getcode);
                break;
            default:
                break;
        }

    }


    /***
     * 获取验证码
     */
    private void getCode() {
        final String register_phone_str2 = forgetpwd_phone.getText().toString().trim();
        if(TextUtils.isEmpty(register_phone_str2)){
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!myUtils.isMobileNO(register_phone_str2)){
            Toast.makeText(this, "格式错误", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestParams params=new RequestParams(Url.getCodeUrl);
        params.addParameter("mobile",register_phone_str2);
        params.addParameter("type","2");
        x.http().post(params, new MyCommonCallback<Result>() {
            @Override
            public void success(Result data) {

                Toast.makeText(ForGetPWDActivityVo.this, data.info, Toast.LENGTH_SHORT).show();
                if("0".equals(data.code)){
                    CodeTimerTask.getInstence().starrTimer(forgetpwd_getcode);
                }

            }

    @Override
    public void error(Throwable ex, boolean isOnCallback) {

    }
});



    }



    /***
     * 重置密码
     */
    private void fixPWD() {

        final String forgetpwd_phone_str = forgetpwd_phone.getText().toString().trim();
        final String forgetpwd_code_str = forgetpwd_code.getText().toString().trim();
        final String forgetpwd_pwd_str = forgetpwd_pwd.getText().toString().trim();

        if (TextUtils.isEmpty(forgetpwd_phone_str)) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            forgetpwd_phone.requestFocus();
            return;
        }else if(TextUtils.isEmpty(forgetpwd_code_str)){
            Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            forgetpwd_code.requestFocus();
        }else if (TextUtils.isEmpty(forgetpwd_pwd_str)) {
            Toast.makeText(this, getResources().getString(R.string.Password_cannot_be_empty), Toast.LENGTH_SHORT).show();
            forgetpwd_pwd.requestFocus();
            return;
        }
        if (!TextUtils.isEmpty(forgetpwd_phone_str) && !TextUtils.isEmpty(forgetpwd_pwd_str)) {
            loading_view.setVisibility(View.VISIBLE);
            RequestParams params=new RequestParams(Url.ForgetPwd);
            params.addParameter("account",forgetpwd_phone_str);
            params.addParameter("smscode",forgetpwd_code_str);
            params.addParameter("mobile",forgetpwd_phone_str);
            params.addParameter("password",forgetpwd_pwd_str);
            x.http().post(params, new MyCommonCallback<Result>() {
                @Override
                public void success(Result data) {
                    loading_view.setVisibility(View.GONE);
                    Toast.makeText(ForGetPWDActivityVo.this, data.info, Toast.LENGTH_SHORT).show();
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
