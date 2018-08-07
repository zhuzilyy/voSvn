package com.zl.vo_.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zl.vo_.DemoApplication;
import com.zl.vo_.R;
import com.zl.vo_.main.Entity.CheckPwdEntivity;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.main.views.ClearEditText;
import com.zl.vo_.main.views.MEditText;
import com.zl.vo_.main.views.XTitleView;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2018/1/24.
 */

public class SettingsPrivacyActivity4 extends VoBaseActivity implements View.OnClickListener {
    @BindView(R.id.title)
    public RelativeLayout title;
    @BindView(R.id.privacy4_pwd)
    public ClearEditText privacy4_pwd;
    @BindView(R.id.privacy4_submit)
    public TextView privacy4_submit;
    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.lay_settings_pravacy4);
        ButterKnife.bind(this);
        mInit();
    }

    private void mInit() {
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @OnClick(R.id.privacy4_submit)
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.privacy4_submit:
                String pwdStr=privacy4_pwd.getText().toString().trim();
                if(TextUtils.isEmpty(pwdStr)){
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                loading_view.setVisibility(View.VISIBLE);
                RequestParams params=new RequestParams(Url.CheckPwdUrl);
                params.addParameter("userid", myUtils.readUser(SettingsPrivacyActivity4.this).getUserid());
                params.addParameter("privacypass",pwdStr);
                x.http().post(params, new MyCommonCallback<Result<CheckPwdEntivity>>() {
                    @Override
                    public void success(Result<CheckPwdEntivity> data) {
                        loading_view.setVisibility(View.GONE);

                        if("0".equals(data.code)){
                            CheckPwdEntivity pwdEntivity= data.data;
                            CheckPwdEntivity.CheckPwdInfo pwdInfo=pwdEntivity.getInfo();
                            //DemoApplication.pwdInfo=pwdInfo;
                            //进入加密页面
                            Intent intent=new Intent(SettingsPrivacyActivity4.this, PrivacyPasswordActivityVo.class);
                            //隐藏密码
                            intent.putExtra("passId",pwdInfo.getPrivacypassinfo().getPassid());
                            //是否设置时间
                            intent.putExtra("isSetTime",pwdInfo.getPrivacypassinfo().getIsset());
                            //是否设置摇一摇
                            intent.putExtra("isSetShake",pwdInfo.getPrivacypassinfo().getIsmove());
                            //是否设置隐藏的开始时间
                            intent.putExtra("startTime",pwdInfo.getPrivacypassinfo().getStarttime());

                            startActivity(intent);
                            finish();

                        }else {
                            Toast.makeText(SettingsPrivacyActivity4.this, data.info, Toast.LENGTH_SHORT).show();
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
