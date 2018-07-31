package com.zl.vo_.main.activities;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.zl.vo_.R;
import com.zl.vo_.db.MyFriendGroupDao;
import com.zl.vo_.main.Entity.FixGroupData;
import com.zl.vo_.main.Entity.GroupData;
import com.zl.vo_.main.Entity.MineGroupEntityNet;
import com.zl.vo_.main.Entity.MyFriendGroupEntity;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.adapter.GroupManagerAdapter;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/17.
 */

public class GroupManagerActivityVo extends VoBaseActivity implements View.OnClickListener {
    @BindView(R.id.iv_back)
    public ImageView back;
    private MyFriendGroupDao groupDao;
    @BindView(R.id.lv)
    public ListView lv;
    private GroupManagerAdapter adapter;

    private RelativeLayout addgroup_re;
    private List<MyFriendGroupEntity> BigList=new ArrayList<>();
    private  View header;
    private int uu=0;
    private MyReceiver myReceiver;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_group_manager);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        //--------------------
        myReceiver=new MyReceiver();
        IntentFilter filter=new IntentFilter("netGroupOK");
        registerReceiver(myReceiver,filter);


        //--------------------
        groupDao=new MyFriendGroupDao();
        VoBaseActivity.addActivity(this);
        header = LayoutInflater.from(GroupManagerActivityVo.this).inflate(R.layout.lay_add_group_head,null);
        lv.addHeaderView(header);
        mInit();
    }

    private void mInit() {


        //需要网络同步
        //从数据库中查组信息
        BigList.clear();
        List<MyFriendGroupEntity> entities=groupDao.selectGroupEntities();
        BigList.addAll(entities);
        adapter=new GroupManagerAdapter(GroupManagerActivityVo.this,BigList);
        lv.setAdapter(adapter);

        /***
         * 从网络上获取好友分组
         */
        getGroupData();





        /***
         * 修改名称
         */
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                DialogUIUtils.showAlertInput(GroupManagerActivityVo.this, "修改组名", "请输入新组名", null, "确认", "取消", new DialogUIListener() {
                    @Override
                    public void onPositive() {
                        DialogUIUtils.dismiss();
                    }
                    @Override
                    public void onNegative() {
                    }
                    @Override
                    public void onGetInput(CharSequence input1, CharSequence input2) {
                        super.onGetInput(input1, input2);
                        if(TextUtils.isEmpty(input1)){
                            return;
                        }
                        //修改组名称
                        fixGroupName(input1,BigList.get(i-1).getGroupId());




                    }
                }).show();
            }
        });
        /****
         * 删除组
         */
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                DialogUIUtils.showMdAlert(GroupManagerActivityVo.this, "删除分组", "确认要删除该组吗", new DialogUIListener() {
                    @Override
                    public void onPositive() {
                        deleteGroup(BigList.get(i-1).getGroupId());

                    }

                    @Override
                    public void onNegative() {

                    }
                }).show();


                return true;
            }
        });
        /***
         * 添加组
         */
        header.findViewById(R.id.addgroup_re).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUIUtils.showAlertInput(GroupManagerActivityVo.this, "创建分组", "请输入分组名称", null, "创建", "取消",false,false, new DialogUIListener() {
                    @Override
                    public void onPositive() {
                        DialogUIUtils.dismiss();
                    }
                    @Override
                    public void onNegative() {
                    }
                    @Override
                    public void onGetInput(CharSequence input1, CharSequence input2) {
                        super.onGetInput(input1, input2);
                        if(TextUtils.isEmpty(input1)){
                            return;
                        }
                        //创建一个组同步到服务器
                        //Toast.makeText(GroupManagerActivityVo.this, "11"+input1+"   2="+input2, Toast.LENGTH_SHORT).show();
                        createMyGroup(String.valueOf(input1));


                    }
                }).show();
            }
        });

    }

    /****
     * 删除一个分组
     * @param groupId
     */
    private void deleteGroup(int groupId) {
        RequestParams params=new RequestParams(Url.DeleteGroupUrl);
        params.addParameter("groupid",groupId);
        x.http().post(params, new MyCommonCallback<Result>() {
            @Override
            public void success(Result data) {
                if("0".equals(data.code)){

                    /***
                     * 从网络上获取好友分组
                     */
                uu=1;
                    getGroupData();

                }

            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {

            }
        });


    }

    /****
     * 修改组名
     * @param input1
     * @param groupId
     */
    private void fixGroupName(CharSequence input1, int groupId) {

        RequestParams params=new RequestParams(Url.UpdataGroupNameUrl);
        params.addParameter("groupid",groupId);
        params.addParameter("groupname",input1);
        x.http().post(params, new MyCommonCallback<Result<FixGroupData>>() {
            @Override
            public void success(Result<FixGroupData> data) {
                if("0".equals(data.code)){

                    mInit();



                }

            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                Log.i("uu","ex"+"");
            }
        });

    }

    /****
     * 创建一个分组
     * @param groupName
     */
    private void createMyGroup(String groupName) {

        RequestParams params=new RequestParams(Url.CreateGroupURL);
        params.addParameter("userid", myUtils.readUser(GroupManagerActivityVo.this).getUserid());
        params.addParameter("groupname",groupName);
        x.http().post(params, new MyCommonCallback<Result<MineGroupEntityNet>>() {
            @Override
            public void success(Result<MineGroupEntityNet> data) {
                if("0".equals(data.code)){
                    MineGroupEntityNet Data=data.data;
                    MineGroupEntityNet.MineGroupInfo bigInfo=Data.getInfo();
                    MineGroupEntityNet.MineGroupInfo.MineGroupInfoArr infoArr= bigInfo.getGroupinfo();
                    if(infoArr!=null){
                        //创建成功，
                        try {
                            groupDao.saveMyGroup(infoArr);
                            BigList.clear();
                            List<MyFriendGroupEntity> entities=groupDao.selectGroupEntities();
                            BigList.addAll(entities);
                            adapter.notifyDataSetChanged();
                            //发送广播
                            sendBroadcast(new Intent("creatGroupOk"));
                        }catch (Exception ex){
                            ex.printStackTrace();
                            Log.i("ex",ex+"==错");
                        }
                    }
                }


            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {

            }
        });






    }


    /**
     * 从网络上获取组的信息，刷新列表更新到数据库
     */
    private void getGroupData() {
        RequestParams parasm=new RequestParams(Url.GetGroupInfoUrl);
        parasm.addParameter("userid",myUtils.readUser(GroupManagerActivityVo.this).getUserid());
        x.http().post(parasm, new MyCommonCallback<Result<GroupData>>() {
            @Override
            public void success(Result<GroupData> data) {
               if("0".equals(data.code)||"-120".equals(data.code)){
                   GroupData groupData= data.data;
                   GroupData.GroupInfo groupInfo=groupData.getInfo();
                   List<GroupData.GroupInfo.GrouopList> grouopLists=groupInfo.getGrouplist();

                       refrshUIAndIntoDB(grouopLists);
                       Log.i("uu","**success[][]***");
                   Log.i("88)","==3333333===needRefresh====");
                       sendBroadcast(new Intent("needRefresh"));
                       Log.i("uu","**success{}{}***");


               }


            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                Log.i("ui","=="+ex);
            }
        });
    }

    /****
     * 从网络获取分组信息更新界面ui并保存数据库
     * @param grouopLists
     */
    private void refrshUIAndIntoDB(List<GroupData.GroupInfo.GrouopList> grouopLists) {
        List<MyFriendGroupEntity> groupEntities=new ArrayList<>();

        for (int i = 0; i <grouopLists.size() ; i++) {
            MyFriendGroupEntity entity=new MyFriendGroupEntity();
            entity.setGroupId(Integer.parseInt(grouopLists.get(i).getGroupid()));
            entity.setGroupName(grouopLists.get(i).getGroupname());
            groupEntities.add(entity);

        }

        BigList.clear();
        BigList.addAll(groupEntities);
        adapter.notifyDataSetChanged();
        //保存到数据库
        saveGroups(BigList);
    }

    /****
     * 将从网络上请求下来的数据保存到数据库，并发送广播更新好友分组
     * @param bigList
     */
    private void saveGroups(List<MyFriendGroupEntity> bigList) {
        groupDao.saveGroups(bigList);
    }


    @OnClick({R.id.iv_back})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }


    private class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
           String action= intent.getAction();
            if("netGroupOK".equals(action)){
                Log.i("88)","==2222222===收到netGroupOK====");
                getGroupData();

            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(myReceiver!=null){
            unregisterReceiver(myReceiver);
        }
    }
}
