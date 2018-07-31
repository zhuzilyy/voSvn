package com.zl.vo_.main.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zl.vo_.R;
import com.zl.vo_.main.Entity.LoginData;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.main.views.MEditText;
import com.zl.vo_.utils.Url;

import org.xutils.common.util.MD5;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/23.
 */

public class LifeNotePasswordActivityVo extends VoBaseActivity implements View.OnClickListener{
    @BindView(R.id.lifeNote_pwd)
    public MEditText lifeNote_pwd;
    @BindView(R.id.lifeNote_pwd_comfirm)
    public MEditText lifeNote_pwd_comfirm;
    @BindView(R.id.lifeNote_submit)
    public Button lifeNote_submit;
    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_setlifenotepwd);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        mInit();
    }

    private void mInit() {
    }
    @OnClick({R.id.iv_back,R.id.lifeNote_submit})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.lifeNote_submit:
                final String pwdstr=lifeNote_pwd.getText().toString().trim();
                String pwdStrCom=lifeNote_pwd_comfirm.getText().toString().trim();
                if(TextUtils.isEmpty(pwdstr)){
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pwdStrCom)){
                    Toast.makeText(this, "确认密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!pwdstr.equals(pwdStrCom)){
                    Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
               loading_view.setVisibility(View.VISIBLE);
                RequestParams params=new RequestParams(Url.SetLifeNotePwdUrl);
                params.addParameter("userid", myUtils.readUser(LifeNotePasswordActivityVo.this).getUserid());
                params.addParameter("personpass",pwdstr);
                x.http().post(params, new MyCommonCallback<Result>() {
                    @Override
                    public void success(Result data) {
                        loading_view.setVisibility(View.GONE);
                        if("0".equals(data.code)){
                           LoginData.LoginInfo.LoginAccountInfo user= myUtils.readUser(LifeNotePasswordActivityVo.this);
                            if(user!=null){
                                String pwdStr=MD5.md5(pwdstr);

                                user.setPersonpass(pwdStr.toUpperCase());
                                myUtils.clearSharedUser(LifeNotePasswordActivityVo.this);
                                myUtils.saveUser(user,LifeNotePasswordActivityVo.this);
                                LoginData.LoginInfo.LoginAccountInfo user2= myUtils.readUser(LifeNotePasswordActivityVo.this);
                                Toast.makeText(LifeNotePasswordActivityVo.this, "设置成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }
                    @Override
                    public void error(Throwable ex, boolean isOnCallback) {
                        loading_view.setVisibility(View.GONE);
                    }
                });

                break;
            default:
                break;
        }
    }
}
