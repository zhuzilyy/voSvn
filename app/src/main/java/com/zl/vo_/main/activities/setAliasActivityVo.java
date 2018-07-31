package com.zl.vo_.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zl.vo_.R;
import com.zl.vo_.db.DemoDBManager;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.Entity.UserInfoEntity;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/12.
 */

public class setAliasActivityVo extends VoBaseActivity {
    @BindView(R.id.et_info)
    public EditText et_info;
    @BindView(R.id.tv_save)
    public TextView tv_save;
    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;
    private String remark_;
    private String friendUserId_;
    private DemoDBManager dbManager=DemoDBManager.getInstance();
    public    UserInfoEntity.UserInfo.UserFriendInfo friendInfo;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.lay_setalias);

        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        remark_=getIntent().getStringExtra("remark");
        friendUserId_=getIntent().getStringExtra("friendUserId");
        mInit();
    }
    private void mInit() {

        et_info.setText(remark_);


        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading_view.setVisibility(View.VISIBLE);
                String remrak=et_info.getText().toString().trim();

                RequestParams params=new RequestParams(Url.UpdateRemarkUrl);
                params.addParameter("userid",myUtils.readUser(setAliasActivityVo.this).getUserid());
                params.addParameter("friend_userid",friendUserId_);
                params.addParameter("remark",remrak);
                x.http().post(params, new MyCommonCallback<Result<UserInfoEntity>>() {
                    @Override
                    public void success(Result<UserInfoEntity> data) {

                        if("0".equals(data.code)){

                            loading_view.setVisibility(View.GONE);
                            UserInfoEntity userInfoEntity=  data.data;
                            UserInfoEntity.UserInfo userInfo=userInfoEntity.getInfo();
                            if(userInfo!=null){
                                friendInfo=  userInfo.getFriend_info();
                                if(friendInfo!=null){
                                    UpdataFriend(friendInfo);
                                }
                            }
                        }else {
                            Log.i("code",data.code+"");
                        }
                    }
                    @Override
                    public void error(Throwable ex, boolean isOnCallback) {
                        loading_view.setVisibility(View.GONE);
                        Log.i("ui",ex.getMessage());
                    }
                });
            }
        });
    }
    /****
     * 网络请求成功，更新数据库中该对象的信息
     * @param friendInfo
     */
    private void UpdataFriend(UserInfoEntity.UserInfo.UserFriendInfo friendInfo) {
        MyFrindEntivity entivity=new MyFrindEntivity();
        entivity.setAccount(friendInfo.getAccount());
        entivity.setAvatar(friendInfo.getAvatar());
        entivity.setNike(friendInfo.getNickname());
        entivity.setPhone(friendInfo.getMobile());
        entivity.setPassid(friendInfo.getPassid());
        entivity.setRemark(friendInfo.getRemark());
        entivity.setAddress(friendInfo.getAddress());
        entivity.setSex(friendInfo.getSex());
        //更新数据
        boolean b=dbManager.updateMyFriendInfo(entivity);
        Log.i("jj","更新备注=="+b+entivity.getRemark());
        Intent intent=new Intent();
        intent.putExtra("allas",friendInfo.getRemark());
        setResult(201,intent);
        sendBroadcast(new Intent("needRefresh"));

        finish();








    }

}
