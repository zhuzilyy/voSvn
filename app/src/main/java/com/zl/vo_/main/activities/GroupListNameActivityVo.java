package com.zl.vo_.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.zl.vo_.R;
import com.zl.vo_.adapter.switchGroupAdapter;
import com.zl.vo_.db.DemoDBManager;
import com.zl.vo_.db.MyFriendGroupDao;
import com.zl.vo_.main.Entity.MyFriendGroupEntity;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/9.
 */

public class GroupListNameActivityVo extends VoBaseActivity {
    @BindView(R.id.group_list_name_lv)
    public ListView LVgroupName;
    public DemoDBManager dbManager=DemoDBManager.getInstance();
    public switchGroupAdapter adapter;
    public String accountStr;

    private MyFriendGroupDao groupDao;
    public MyFrindEntivity friend;

    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_grouplistnames);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        accountStr=getIntent().getStringExtra("account");
        List<MyFrindEntivity> entivities=dbManager.getAllFriends();
        if(!TextUtils.isEmpty(accountStr)){
            friend=dbManager.getFriend(accountStr);
            if(friend!=null){
                String ni=friend.getHuanxinID();
                Log.i("ui",ni+"");
            }
        }else {

        }



        minit();


    }

    private void minit() {
        groupDao=new MyFriendGroupDao();

        final List<MyFriendGroupEntity> entities=groupDao.selectGroupEntities();
        adapter=new switchGroupAdapter(GroupListNameActivityVo.this,entities);
        LVgroupName.setAdapter(adapter);
        LVgroupName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                switchGroup(entities.get(i).getGroupId());
            }
        });






    }

    /****
     * 切换分组
     */
    private void switchGroup(final int groupid) {
        loading_view.setVisibility(View.VISIBLE);
        RequestParams params=new RequestParams(Url.SwitchGroupUrl);
        params.addParameter("userid", myUtils.readUser(GroupListNameActivityVo.this).getUserid());
        params.addParameter("friend_userid",friend.getUserID());
        params.addParameter("groupid",groupid+"");

        x.http().post(params, new MyCommonCallback<Result>() {
            @Override
            public void success(Result data) {
                loading_view.setVisibility(View.GONE);
              //  Toast.makeText(GroupListNameActivityVo.this, data.info, Toast.LENGTH_SHORT).show();
                Log.i("uu","77777777");
                MyFriendGroupEntity groupEntity=groupDao.setlecMyFriendOneGroup(String.valueOf(groupid));
                Log.i("uu","8888888"+groupEntity.getGroupName());
                Intent intent=new Intent();
                if(groupEntity!=null){
                    intent.putExtra("groupName",groupEntity.getGroupName());
                }
                setResult(200,intent);
                sendBroadcast(new Intent("needRefresh"));
                finish();

            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                loading_view.setVisibility(View.GONE);
            }
        });


    }
}
