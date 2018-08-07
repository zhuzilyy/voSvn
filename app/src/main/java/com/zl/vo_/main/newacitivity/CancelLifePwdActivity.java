package com.zl.vo_.main.newacitivity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
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

import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/25.
 */

public class CancelLifePwdActivity extends VoBaseActivity implements View.OnClickListener{
    @BindView(R.id.iv_back) public ImageView back;
    @BindView(R.id.tv_title) public TextView title;
    @BindView(R.id.loading_view) public RelativeLayout loading_view;
    @BindView(R.id.button2) public Button btn2;
    @BindView(R.id.et_pwd_y) public ClearEditText et_pwd_y;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.lay_cancel_life_pwd);
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

        title.setText("取消密码");

    }
    @OnClick({R.id.button2})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button2:
              String pwdStr=  et_pwd_y.getText().toString().trim();
                if(TextUtils.isEmpty(pwdStr)){
                    Toast.makeText(this, "原密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                ShowWarningDiaLog(pwdStr);


            break;

            default:
            break;


        }
    }

    private void ShowWarningDiaLog(final String pwdStr) {
        final Dialog dialog =new Dialog(CancelLifePwdActivity.this);
        View vv= LayoutInflater.from(CancelLifePwdActivity.this).inflate(R.layout.lay_diag_cancelpwd,null);
        dialog.setContentView(vv);
        vv.findViewById(R.id.imageView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        vv.findViewById(R.id.tv_confrim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelPwd(pwdStr);
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    private void CancelPwd(String pwdStr) {
        loading_view.setVisibility(View.VISIBLE);
        LoginData.LoginInfo.LoginAccountInfo user =myUtils.readUser(CancelLifePwdActivity.this);
        if(user==null){
            return;
        }
        RequestParams params=new RequestParams(Url.CancelLifeNotePwdUrl2);
        params.addParameter("userid",user.getUserid());
        params.addParameter("personpass",pwdStr);
        x.http().post(params, new MyCommonCallback<CommentBean>() {
                @Override
                public void success(CommentBean data) {
                loading_view.setVisibility(View.GONE);
                String code =data.getCode();
                if("0".equals(code)){
                    Toast.makeText(CancelLifePwdActivity.this, data.getInfo(), Toast.LENGTH_SHORT).show();
                    try {
                        LoginData.LoginInfo.LoginAccountInfo user =myUtils.readUser(CancelLifePwdActivity.this);
                        if(user!=null){
                            user.setPersonpass("");
                        }
                        myUtils.saveUser(user,CancelLifePwdActivity.this);
                    }catch (Exception e){
                        Log.i("dd",e.getMessage());
                    }
                    finish();
                }else {
                    Toast.makeText(CancelLifePwdActivity.this, data.getInfo(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                loading_view.setVisibility(View.GONE);
            }
        });
    }
}
