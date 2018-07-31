package com.zl.vo_.main.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zl.vo_.R;
import com.zl.vo_.db.DemoDBManager;
import com.zl.vo_.main.Entity.MyPwdEntity;
import com.zl.vo_.main.Entity.Result;
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
 * Created by Administrator on 2017/11/25.
 */

public class PrivacyPwdSettingActivityVo extends VoBaseActivity implements View.OnClickListener{
    @BindView(R.id.iv_back)
    public ImageView iv_back;
    @BindView(R.id.addpwd)
    public TextView addpwd;
    @BindView(R.id.btn_addpwd)
    public Button btn_addpwd;
    @BindView(R.id.me_privacypwd)
    public MEditText me_privacypwd;
    @BindView(R.id.me_confirm_privacypwd)
    public MEditText me_confirm_privacypwd;
    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;
    public DemoDBManager dbManager=DemoDBManager.getInstance();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_privacy_pwd_setting);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        mInit();
    }
    private void mInit() {
    }
    @OnClick({R.id.addpwd,R.id.iv_back,R.id.btn_addpwd})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.addpwd:

                me_privacypwd.setText(null);
                me_confirm_privacypwd.setText(null);

                break;
            case R.id.btn_addpwd:

               String privacyPWD= me_privacypwd.getText().toString().trim();
                String comfrimPWD=me_confirm_privacypwd.getText().toString().trim();
                if(TextUtils.isEmpty(privacyPWD)){
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(comfrimPWD)){

                    Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!privacyPWD.equals(comfrimPWD)){
                    Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                loading_view.setVisibility(View.VISIBLE);
                RequestParams params=new RequestParams(Url.addPrivaacyPwdUrl);
                params.addParameter("userid", myUtils.readUser(PrivacyPwdSettingActivityVo.this).getUserid());
                params.addParameter("privacypass",privacyPWD);
                x.http().post(params, new MyCommonCallback<Result<MyPwdEntity>>() {
                    @Override
                    public void success(Result<MyPwdEntity> data) {
                        loading_view.setVisibility(View.GONE);
                        Toast.makeText(PrivacyPwdSettingActivityVo.this, data.info, Toast.LENGTH_SHORT).show();
                        if("0".equals(data.code)){
                           MyPwdEntity entity= data.data;
                            MyPwdEntity.MyPwdInfo pwdInfo=entity.getInfo();
                            MyPwdEntity.MyPwdInfo.MyPwdCell cell=pwdInfo.getPrivacypassinfo();
                            if(cell!=null){
                               boolean b= saveMyPrivacyPwd(cell);
                                if(b){
                                    me_privacypwd.setText(null);
                                    me_confirm_privacypwd.setText(null);
                                }else {
                                    Toast.makeText(PrivacyPwdSettingActivityVo.this, "设置失败", Toast.LENGTH_SHORT).show();
                                }
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
    /****
     * 将设置的隐私密码保存在数据库中
     * @param cell
     * @return
     */
    private boolean saveMyPrivacyPwd(MyPwdEntity.MyPwdInfo.MyPwdCell cell) {
        return dbManager.saveMyPwd(cell);
    }
}











