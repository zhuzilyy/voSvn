package com.zl.vo_.main.newacitivity;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.zl.vo_.main.views.MEditText;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/25.
 */

public class FixLifePwdActivity extends VoBaseActivity implements View.OnClickListener{
    @BindView(R.id.iv_back) public ImageView back;
    @BindView(R.id.tv_title) public TextView title;
    @BindView(R.id.et_pwd) public MEditText et_pwd;
    @BindView(R.id.et_pwdnew) public MEditText et_pwdnew;
    @BindView(R.id.et_pwd_confirm) public MEditText et_pwd_confirm;
    @BindView(R.id.button2) public Button btn_confirm;
    @BindView(R.id.loading_view) public RelativeLayout loading_view;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.lay_fix_life_pwd);
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
        title.setText("修改密码");


    }
    @OnClick({R.id.button2})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button2:
                String pwdStr_y=et_pwd.getText().toString().trim();
                if(TextUtils.isEmpty(pwdStr_y)){
                    Toast.makeText(this, "原密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String pwdStr_new=et_pwdnew.getText().toString().trim();
                if(TextUtils.isEmpty(pwdStr_new)){
                    Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                String pwdStr_confirm=et_pwd_confirm.getText().toString().trim();
                if(TextUtils.isEmpty(pwdStr_confirm)){
                    Toast.makeText(this, "请确认新密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!pwdStr_new.equals(pwdStr_confirm)){
                    Toast.makeText(this, "新密码两次输入不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                fixPwd(pwdStr_y,pwdStr_new,pwdStr_confirm);


            break;

            default:
            break;


        }

    }

    private void fixPwd(String pwdStr_y, String pwdStr_new, String pwdStr_confirm) {
        loading_view.setVisibility(View.VISIBLE);
        LoginData.LoginInfo.LoginAccountInfo user =myUtils.readUser(FixLifePwdActivity.this);
        if(user==null){
            return;
        }

        RequestParams params=new RequestParams(Url.FixLifeNotePwdUrl2);
        params.addParameter("userid",user.getUserid());
        params.addParameter("origin_personpass",pwdStr_y);
        params.addParameter("personpass",pwdStr_new);
        params.addParameter("repersonpass",pwdStr_confirm);
        x.http().post(params, new MyCommonCallback<CommentBean>() {
            @Override
            public void success(CommentBean data) {
                loading_view.setVisibility(View.GONE);
                String code = data.getCode();
                if("0".equals(code)){
                    Toast.makeText(FixLifePwdActivity.this, data.getInfo(), Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(FixLifePwdActivity.this, data.getInfo(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                loading_view.setVisibility(View.GONE);
            }
        });

    }
}
