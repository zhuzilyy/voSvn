package com.zl.vo_.main.newacitivity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zl.vo_.R;
import com.zl.vo_.main.Entity.CommentBean;
import com.zl.vo_.main.Entity.LoginData;
import com.zl.vo_.main.activities.VoBaseActivity;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.main.views.ClearEditText;
import com.zl.vo_.main.views.MEditText;
import com.zl.vo_.utils.Url;

import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/25.
 */

public class SettingLifePwdActivity extends VoBaseActivity implements View.OnClickListener {
    @BindView(R.id.iv_back)
    public ImageView back;
    @BindView(R.id.tv_title)
    public TextView title;
    @BindView(R.id.btn_confirm)
    public Button btn_confirm;
    @BindView(R.id.et_pwd)
    public ClearEditText et_pwd;
    @BindView(R.id.et_pwd_confirm)
    public ClearEditText et_pwd_confirm;
    @BindView(R.id.et_mibao_question_answer)
    public ClearEditText et_mibao_question_answer;
    @BindView(R.id.et_mibao_question_answer_again)
    public ClearEditText et_mibao_question_answer_again;
    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.lay_setting_life_pwd);
        ButterKnife.bind(this);
        mInit();
    }
    private void mInit() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title.setText("设置密码");

    }

    @OnClick({R.id.btn_confirm})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                LoginData.LoginInfo.LoginAccountInfo user = myUtils.readUser(SettingLifePwdActivity.this);
                if(user==null){
                    return;
                }
                if(!TextUtils.isEmpty(user.getPersonpass())){
                    Toast.makeText(this, "您已经设置过人生笔记密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                String pwdStr = et_pwd.getText().toString().trim();
                if (TextUtils.isEmpty(pwdStr)) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                String pwdConfrimStr = et_pwd_confirm.getText().toString().trim();
                if (TextUtils.isEmpty(pwdConfrimStr)) {
                    Toast.makeText(this, "请确认密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pwdStr.equals(pwdConfrimStr)) {
                    Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                String answerStr = et_mibao_question_answer.getText().toString().trim();
                if (TextUtils.isEmpty(answerStr)) {
                    Toast.makeText(this, "请输入密保答案", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (answerStr.length()!=4) {
                    Toast.makeText(this, "密保答案长度不符合规范", Toast.LENGTH_SHORT).show();
                    return;
                }

                String answer_ConfirmStr = et_mibao_question_answer_again.getText().toString().trim();
                if (TextUtils.isEmpty(answer_ConfirmStr)) {
                    Toast.makeText(this, "请确认密保答案", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!answerStr.equals(answer_ConfirmStr)) {
                    Toast.makeText(this, "密保答案两次输入不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                setPwd(pwdStr, pwdConfrimStr, answerStr, answer_ConfirmStr);
                break;
            default:
                break;


        }
    }

    /***
     * 设置人生笔记密码
     *
     * @param pwdStr
     * @param pwdConfrimStr
     * @param answerStr
     * @param answer_confirmStr
     */
    private void setPwd(final String pwdStr, String pwdConfrimStr, String answerStr, String answer_confirmStr) {
        loading_view.setVisibility(View.VISIBLE);
        LoginData.LoginInfo.LoginAccountInfo user = myUtils.readUser(SettingLifePwdActivity.this);
        if (user == null) {
            return;
        }

        RequestParams params = new RequestParams(Url.SetLifeNotePwdUrl2);
        params.addParameter("userid", user.getUserid());
        params.addParameter("personpass", pwdStr);
        params.addParameter("repersonpass", pwdConfrimStr);
        params.addParameter("personkey", answerStr);
        params.addParameter("repersonkey", answer_confirmStr);
        x.http().post(params, new MyCommonCallback<CommentBean>() {
            @Override
            public void success(CommentBean data) {
                loading_view.setVisibility(View.GONE);
                String code = data.getCode();
                if ("0".equals(code)) {
                    //弹出对话框
//                    Dialog dialog =new Dialog(SettingLifePwdActivity.this);
//                    dialog.setContentView(R.layout.lay_success_setlifenote_pwd);
//                    dialog.show();
                    try {
                        LoginData.LoginInfo.LoginAccountInfo user =myUtils.readUser(SettingLifePwdActivity.this);
                        if(user!=null){
                            String pp = org.xutils.common.util.MD5.md5(pwdStr);
                            Log.i("mmd5", "==9999:" + pp);
                            user.setPersonpass(pp.toUpperCase());
                        }
                        myUtils.saveUser(user,SettingLifePwdActivity.this);
                    }catch (Exception e){
                        Log.i("dd",e.getMessage());
                    }
                    Toast.makeText(SettingLifePwdActivity.this,data.getInfo(), Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    Toast.makeText(SettingLifePwdActivity.this, data.getInfo(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                loading_view.setVisibility(View.GONE);
            }
        });


    }
}
