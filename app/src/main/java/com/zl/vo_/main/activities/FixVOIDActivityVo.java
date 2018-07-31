package com.zl.vo_.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zl.vo_.R;
import com.zl.vo_.main.Entity.LoginData;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/11/22.
 */

public class FixVOIDActivityVo extends VoBaseActivity {
    @BindView(R.id.et_info)
    public EditText et_info;
    @BindView(R.id.tv_save)
    public TextView tv_save;
    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_fixvoid);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        mInit();
    }

    private void mInit() {

        LoginData.LoginInfo.LoginAccountInfo currentUser= myUtils.readUser(FixVOIDActivityVo.this);

        if(currentUser!=null){

            et_info.setText(currentUser.getAccount());


        }
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading_view.setVisibility(View.VISIBLE);
                String account=et_info.getText().toString().trim();

                RequestParams params=new RequestParams(Url.SetVOAccountUrl);
                params.addParameter("userid",myUtils.readUser(FixVOIDActivityVo.this).getUserid());
                params.addParameter("account",account);

                x.http().post(params, new MyCommonCallback<Result<LoginData>>() {
                    @Override
                    public void success(Result<LoginData> data) {
                        loading_view.setVisibility(View.GONE);
                        LoginData loginData=data.data;
                        LoginData.LoginInfo loginInfo=loginData.getInfo();
                        if(loginInfo!=null){
                            LoginData.LoginInfo.LoginAccountInfo user=loginInfo.getAccount_info();
                            if(user!=null){
                                Log.i("uu","pp=="+user.getNickname());


                                //清除本地保存的用户信息
                                myUtils.clearSharedUser(FixVOIDActivityVo.this);
                                myUtils.saveUser(user,FixVOIDActivityVo.this);
                                sendBroadcast(new Intent("FixPersonInfoOK"));
                                finish();






                            }
                        }
                    }

                    @Override
                    public void error(Throwable ex, boolean isOnCallback) {
                        loading_view.setVisibility(View.GONE);
                    }
                });






            }
        });
    }


}
