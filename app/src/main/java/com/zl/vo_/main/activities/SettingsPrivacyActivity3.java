package com.zl.vo_.main.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zl.vo_.DemoApplication;
import com.zl.vo_.R;
import com.zl.vo_.db.DemoDBManager;
import com.zl.vo_.main.Entity.CheckPwdEntivity;
import com.zl.vo_.main.Entity.MyPwdEntity;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.main.views.MEditText;
import com.zl.vo_.main.views.XTitleView;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2018/1/24.
 */

public class SettingsPrivacyActivity3 extends VoBaseActivity {
    @BindView(R.id.iv_back)
    public ImageView title;
    @BindView(R.id.Met_privacy3_pwd)
    public MEditText Met_privacy3_pwd;
    @BindView(R.id.Met_privacy3_againmpwd)
    public MEditText Met_privacy3_againmpwd;
    @BindView(R.id.tv_privacy3_submit)
    public TextView tv_privacy3_submit;
    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;
    public DemoDBManager dbManager=DemoDBManager.getInstance();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.lay_settings_pravacy3);
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

        tv_privacy3_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final String privacyPwd= Met_privacy3_pwd.getText().toString().trim();
                String privacyPwdAgain=Met_privacy3_againmpwd.getText().toString().trim();
                if(TextUtils.isEmpty(privacyPwd)){
                    Toast.makeText(SettingsPrivacyActivity3.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(privacyPwdAgain)){
                    Toast.makeText(SettingsPrivacyActivity3.this, "请确认密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!privacyPwd.equals(privacyPwdAgain)){
                    Toast.makeText(SettingsPrivacyActivity3.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                loading_view.setVisibility(View.VISIBLE);
                RequestParams params=new RequestParams(Url.addPrivaacyPwdUrl);
                params.addParameter("userid", myUtils.readUser(SettingsPrivacyActivity3.this).getUserid());
                params.addParameter("privacypass",privacyPwd);
                x.http().post(params, new MyCommonCallback<Result<MyPwdEntity>>() {
                    @Override
                    public void success(Result<MyPwdEntity> data) {
                        loading_view.setVisibility(View.GONE);
                        Toast.makeText(SettingsPrivacyActivity3.this, data.info, Toast.LENGTH_SHORT).show();
                        if("0".equals(data.code)){
                            MyPwdEntity entity= data.data;
                            MyPwdEntity.MyPwdInfo pwdInfo=entity.getInfo();
                            MyPwdEntity.MyPwdInfo.MyPwdCell cell=pwdInfo.getPrivacypassinfo();
                            if(cell!=null){
                                boolean b= saveMyPrivacyPwd(cell);
                                if(b){
                                    //进入加密页面
                                    Intent intent=new Intent(SettingsPrivacyActivity3.this, PrivacyPasswordActivityVo.class);
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
                                    Toast.makeText(SettingsPrivacyActivity3.this, "设置失败", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    }

                    @Override
                    public void error(Throwable ex, boolean isOnCallback) {
                        Log.i("ui",ex.getMessage());
                        loading_view.setVisibility(View.GONE);
                    }
                });

            }
        });
    }
    /****
     * 将设置的隐私密码保存在数据库中
     * @param cell
     * @return
     */
    private boolean saveMyPrivacyPwd(MyPwdEntity.MyPwdInfo.MyPwdCell cell) {
        return dbManager.saveMyPwd(cell);
    }
}
