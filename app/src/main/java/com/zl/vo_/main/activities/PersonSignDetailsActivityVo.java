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
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/5.
 */

public class PersonSignDetailsActivityVo extends VoBaseActivity implements View.OnClickListener{
    @BindView(R.id.sign_ed)
    public EditText sign_ed;
    @BindView(R.id.tv_submitsign)
    public TextView tv_submitsign;
    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_person_sign_details);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        mInit();
    }

    private void mInit() {
        LoginData.LoginInfo.LoginAccountInfo currentUser= myUtils.readUser(PersonSignDetailsActivityVo.this);

        if(currentUser!=null){

            sign_ed.setText(currentUser.getSignature());


        }

    }


    @OnClick({R.id.tv_submitsign})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tv_submitsign:
                final String signStr=sign_ed.getText().toString().trim();

                loading_view.setVisibility(View.VISIBLE);
                RequestParams params=new RequestParams(Url.UpdateBaseInfoUrl);
                params.addParameter("userid",myUtils.readUser(PersonSignDetailsActivityVo.this).getUserid());
                params.addParameter("signature",signStr);


                x.http().post(params, new MyCommonCallback<Result<LoginData>>() {
                    @Override
                    public void success(Result<LoginData> data) {
                        loading_view.setVisibility(View.GONE);
                        LoginData loginData=data.data;
                        LoginData.LoginInfo loginInfo=loginData.getInfo();
                        if(loginInfo!=null){
                            LoginData.LoginInfo.LoginAccountInfo user=loginInfo.getAccount_info();
                            if(user!=null){
                                Log.i("uu","pp=="+user.getAvatar());


                                //清除本地保存的用户信息
                                myUtils.clearSharedUser(PersonSignDetailsActivityVo.this);
                                myUtils.saveUser(user,PersonSignDetailsActivityVo.this);
                                String hh= myUtils.readUser(PersonSignDetailsActivityVo.this).getAvatar();
                                Log.i("pp","更多"+hh);
                                sendBroadcast(new Intent("FixPersonInfoOK"));
                                Intent intent=new Intent();
                                intent.putExtra("sign",signStr);
                                setResult(200,intent);
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
