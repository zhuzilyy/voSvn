package com.zl.vo_.main.main_fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zl.vo_.DemoApplication;
import com.zl.vo_.DemoHelper;
import com.zl.vo_.R;
import com.zl.vo_.adapter.MyContactsListAdapter;
import com.zl.vo_.db.DemoDBManager;
import com.zl.vo_.db.InviteMessgeDao;
import com.zl.vo_.db.UserDao;
import com.zl.vo_.main.Entity.FriendListEntity;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.activities.ChatActivity;
import com.zl.vo_.main.activities.GroupManagerActivityVo;
import com.zl.vo_.main.activities.Help_Feedback;
import com.zl.vo_.main.activities.MyFrindEntivity;
import com.zl.vo_.main.activities.NewFriendsMsgActivityVo;
import com.zl.vo_.main.activities.addFriendActivity_ContactsVo;
import com.zl.vo_.main.activities.addFriendActivity_SearchVo;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.ui.GroupsActivity;
import com.zl.vo_.ui.ScanCaptureActivity;
import com.zl.vo_.utils.Url;
import com.zl.vo_.widget.FXPopWindow;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/7.
 */

public class MyContactsListFragment extends Fragment implements View.OnClickListener{
    private View vv;
    @BindView(R.id.myContactLv)
    public ListView myContactLV;
    public MyContactsListAdapter adapter;
    //-----------------------
    public RelativeLayout ll_applyNotify;
    public LinearLayout ll_newFriend;
    public LinearLayout ll_chatGroup;
    public LinearLayout ll_createGroup;
    public LinearLayout ll_voHelper;
    public UserDao userDao;
    private InviteMessgeDao inviteMessgeDao;
    public MyReceiver myReceiver;

    protected List<MyFrindEntivity> BigList=new ArrayList<>();
    private DemoDBManager demoDBManager=DemoDBManager.getInstance();

    public RelativeLayout newmsg_icon;
    public List<FriendListEntity.FriendListInfo.friend_list> biglist=new ArrayList<>();

    @BindView(R.id.friend_function_re)
    public RelativeLayout friend_function_re;
    @BindView(R.id.friendDelete_tv)
    public TextView friendDelete_tv;
    @BindView(R.id.friendblackList_tv)
    public TextView friendblackList_tv;

    public String currentUserId="";
    public String currentUserHxId="";

    @BindView(R.id.iv_search)
    public ImageView iv_search;

    @BindView(R.id.iv_add)
    public ImageView iv_add;

    public FXPopWindow fxPopWindow;
    public List<MyFrindEntivity> bbList=new ArrayList<>();
    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        
        vv=LayoutInflater.from(getActivity()).inflate(R.layout.lay_mycontacts_,null);
        ButterKnife.bind(this,vv);
        //***************************

        myReceiver=new MyReceiver();
        IntentFilter filter=new IntentFilter("addFrindOK");
        IntentFilter filter1=new IntentFilter("onContactInvited");
        IntentFilter filter2=new IntentFilter("needRefresh");
        IntentFilter filter3=new IntentFilter("timerOver");
        IntentFilter filter4=new IntentFilter("createTimer");


        getActivity().registerReceiver(myReceiver,filter);
        getActivity().registerReceiver(myReceiver,filter1);
        getActivity().registerReceiver(myReceiver,filter2);
        getActivity().registerReceiver(myReceiver,filter3);
        getActivity().registerReceiver(myReceiver,filter4);


        //*****************************

        List<MyFrindEntivity> allFriends= demoDBManager.getAllFriends();

        mInit();

        getData();


        return vv;
    }

    /***
     * 获取好友列表
     */
    private void getData() {
        //刷新列表
        RequestParams params=new RequestParams(Url.FriendListUrl);
        params.addParameter("userid", myUtils.readUser(DemoApplication.applicationContext).getUserid());
        x.http().post(params, new MyCommonCallback<Result<FriendListEntity>>() {
            @Override
            public void success(Result<FriendListEntity> data) {
               Log.i("uu","success");

                if(!"-101".equals(data.code)){

                FriendListEntity listEntity=data.data;
                FriendListEntity.FriendListInfo listInfo=listEntity.getInfo();
                if(listInfo!=null){
                    List<FriendListEntity.FriendListInfo.friend_list> lists=listInfo.getFriend_list();
                   ///刷新ui保存到数据库中
                    refreshUIAndIntoDb(lists);
                }
                }
            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                Log.i("uu","error"+ex.getMessage());
                //从数据库中获取好友列表
                List<MyFrindEntivity> allFriends= demoDBManager.getAllFriends();
                Log.i("friend","====好友="+allFriends.size());
                BigList.clear();
                BigList.addAll(allFriends);
                adapter=new MyContactsListAdapter(getActivity(),BigList);
                if(myContactLV==null){
                    myContactLV=vv.findViewById(R.id.myContactLv);
                }
                myContactLV.setAdapter(adapter);

            }
        });

    }

    /***
     * 从网络上请求下来 刷新界面并保存到数据库
     * @param lists
     */
    private void refreshUIAndIntoDb(List<FriendListEntity.FriendListInfo.friend_list> lists) {
        List<MyFrindEntivity> entivities=new ArrayList<>();
        for (FriendListEntity.FriendListInfo.friend_list en:lists) {

            MyFrindEntivity friend=new MyFrindEntivity();
            friend.setAccount(en.getAccount());
            friend.setGrooupID(en.getGroupid());
            friend.setNike(en.getNickname());
            friend.setHuanxinID(en.getHuanxin_account());
            friend.setAvatar(en.getAvatar());
            friend.setUserID(en.getUserid());
            friend.setPhone(en.getMobile());
            //--------------------
            friend.setAddress(en.getAddress());
            friend.setRemark(en.getRemark());
            friend.setGroupname(en.getGroupname());
            friend.setPassid(en.getPassid());
            friend.setFriend_check(en.getFriend_check());
            entivities.add(friend);
        }

        BigList.clear();
        BigList.addAll(entivities);
        //保存到数据库中()
        demoDBManager.saveMyFriendList(BigList);

        //*************************************************

        bbList=new ArrayList<>();

        for (int i = 0; i <BigList.size() ; i++) {

            if(TextUtils.isEmpty(BigList.get(i).getPassid())){
                //passid是空的 就说明没有加密
                bbList.add(BigList.get(i));
            }
        }


        //查询有临时状态的人



        List<MyFrindEntivity> hashideStateMyFriendList=demoDBManager.getHashideState();
        Log.i("null",hashideStateMyFriendList.size()+" 临时状态为1的");
        if(hashideStateMyFriendList.size()>0){
            bbList.addAll(hashideStateMyFriendList);
        }


        //***********************************************
        adapter=new MyContactsListAdapter(getActivity(),bbList);

        if(myContactLV==null){
            myContactLV=vv.findViewById(R.id.myContactLv);
        }
        myContactLV.setAdapter(adapter);

        //删除好友，发送广播到好友分组页面

        getActivity().sendBroadcast(new Intent("UpdateFriendOK"));







    }

    @Override
    public void onResume() {
        super.onResume();
        if (inviteMessgeDao == null) {
            inviteMessgeDao = new InviteMessgeDao(getActivity());
        }
        if (inviteMessgeDao.getUnreadMessagesCount() > 0) {
            newmsg_icon.setVisibility(View.VISIBLE);
        } else {
            //applicationItem.hideUnreadMsgView();
            newmsg_icon.setVisibility(View.GONE);
        }
    }

    private void mInit() {


        fxPopWindow=new FXPopWindow(getActivity(),R.layout.fx_popupwindow_add,new FXPopWindow.OnItemClickListener(){
            @Override
            public void onClick(int position) {
                switch (position){
                    //发起群聊
                    case 0:
                        startActivity(new Intent(getActivity(),GroupsActivity.class));
                        break;
                    //添加新的好友
                    case 1:
                        startActivity(new Intent(getActivity(),addFriendActivity_ContactsVo.class));
                        break;
                    //扫一扫
                    case 2:
                        startActivity(new Intent(getActivity(),ScanCaptureActivity.class));
                        break;
                    //帮助及反馈
                    case 3:
                        startActivity(new Intent(getActivity(),Help_Feedback.class));
                        break;
                }
            }
        });


        View Headview=LayoutInflater.from(getActivity()).inflate(R.layout.lay_mycontact_headview,null);
        userDao=new UserDao(getActivity());
        newmsg_icon=Headview.findViewById(R.id. newmsg_icon);
        myContactLV.addHeaderView(Headview);


        ll_applyNotify= vv.findViewById(R.id.ll_applyNotify);
        ll_newFriend= vv.findViewById(R.id.ll_newFriend);
        ll_chatGroup= vv.findViewById(R.id.ll_chatGroup);
        ll_createGroup= vv.findViewById(R.id.ll_createGroup);
        ll_voHelper= vv.findViewById(R.id.ll_voHelper);

        ll_applyNotify.setOnClickListener(this);
        ll_newFriend.setOnClickListener(this);
        ll_chatGroup.setOnClickListener(this);
        ll_createGroup.setOnClickListener(this);
        ll_voHelper.setOnClickListener(this);


        //从数据库中获取好友列表

        bbList.clear();
        adapter=new MyContactsListAdapter(getActivity(),bbList);
        myContactLV.setAdapter(adapter);


        myContactLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("userId",bbList.get(i-1).getHuanxinID());
                intent.putExtra("account",bbList.get(i-1).getAccount());
                intent.putExtra("avatar",myUtils.readUser(getActivity()).getAvatar());
                intent.putExtra("nick",myUtils.readUser(getActivity()).getNickname());
                startActivity(intent);
               // startActivity(new Intent(getActivity(), ChatActivity.class).putExtra("userId", BigList.get(i-1).getHuanxinID()));
            }
        });

        myContactLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                friend_function_re.setVisibility(View.VISIBLE);
                currentUserId=bbList.get(i-1).getUserID();
                currentUserHxId=bbList.get(i-1).getHuanxinID();


                return true;
            }
        });






    }


    @OnClick({R.id.friendDelete_tv,R.id.friendblackList_tv,R.id.friend_function_re,R.id.iv_add,R.id.iv_search})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ll_applyNotify:
                // 申请与通知
                startActivity(new Intent(getActivity(), NewFriendsMsgActivityVo.class));
                break;
            case R.id.ll_newFriend:
                // 新朋友
                startActivity(new Intent(getActivity(), addFriendActivity_ContactsVo.class));
                break;
            case R.id.ll_chatGroup:
                // 群聊
                startActivity(new Intent(getActivity(), GroupsActivity.class));
                break;
            case R.id.ll_createGroup:
                //创建分组
                startActivity(new Intent(getActivity(), GroupManagerActivityVo.class));
                break;
            case R.id.ll_voHelper:
                break;
            case R.id.friendDelete_tv:
                //删除好友
                deleteFriend(currentUserId);
                friend_function_re.setVisibility(View.GONE);

                break;
            case R.id.friendblackList_tv:
                //添加进黑名单
               addBlackList(myUtils.readUser(getActivity()).getHuanxin_account(),currentUserHxId);



                friend_function_re.setVisibility(View.GONE);
                break;
            case R.id.friend_function_re:
                friend_function_re.setVisibility(View.GONE);
                break;
            case R.id.iv_add:
                fxPopWindow.showPopupWindow(iv_add);
                break;
            case R.id.iv_search:
                startActivity(new Intent(getActivity(), addFriendActivity_SearchVo.class));
                break;

            default:
                break;
        }
    }

    /****
     * 将好友加入黑名单
     * @param huanxin_account
     * @param currentUserHxId
     */
    private void addBlackList(String huanxin_account, final String currentUserHxId) {
        loading_view.setVisibility(View.VISIBLE);
        RequestParams params=new RequestParams(Url.addToBlackListUrl);
        params.addParameter("huanxin_account",huanxin_account);
        params.addParameter("friend_huanxin_account",currentUserHxId);
        x.http().post(params, new MyCommonCallback<Result>() {
            @Override
            public void success(Result data) {
                loading_view.setVisibility(View.GONE);
                if("0".equals(data.code)){
                    Intent intent=new Intent("needRefresh");
                    intent.putExtra("hxid",currentUserHxId);
                    getActivity().sendBroadcast(intent);
                }

            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                loading_view.setVisibility(View.GONE);
                Log.i("addBlackList",ex.getMessage());
            }
        });
    }

    /****
     * 点击删除按钮，删除好友
     */
    private void deleteFriend(String friendUserId) {
        loading_view.setVisibility(View.VISIBLE);
        RequestParams params=new RequestParams(Url.DeleteFriendURL);
        params.addParameter("userid",myUtils.readUser(getActivity()).getUserid());
        params.addParameter("friend_userid",friendUserId);
        x.http().post(params, new MyCommonCallback<Result>() {
            @Override
            public void success(Result data) {
                loading_view.setVisibility(View.GONE);
                Toast.makeText(getActivity(), data.info, Toast.LENGTH_SHORT).show();
                if("0".equals(data.code)){
                    DemoHelper.textnum=1;
                    getData();

                }

            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                loading_view.setVisibility(View.GONE);
                Log.i("uu",ex+"==删除好友失败");
            }
        });

    }


    /****
     * 广播接受者
     */
  public   class MyReceiver extends BroadcastReceiver {

        public MyReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if("addFrindOK".equals(action)){
                //点击同意后发的广播，从数据库中获取好友列表，刷新好友列表
                mineRefre();
            }
            if("onContactInvited".equals(action)){
                //只要好友添加成功，回调这里
                if (inviteMessgeDao == null) {
                    inviteMessgeDao = new InviteMessgeDao(getActivity());
                }
                if (inviteMessgeDao.getUnreadMessagesCount() > 0) {
                    newmsg_icon.setVisibility(View.VISIBLE);
                } else {
                    //applicationItem.hideUnreadMsgView();
                    newmsg_icon.setVisibility(View.GONE);
                }
                //刷新界面
                mineRefre();
            }
            if("needRefresh".equals(action)){
                //刷新界面
                mineRefre();
            }
            if("createTimer".equals(action)){

                //定时器触发
                String passid_= intent.getStringExtra("passid");
                boolean b=demoDBManager.updateMyPwd2("0",passid_);
                if(b){
                    mineRefre();
                }

            }
        }
    }

    /****
     * 刷新数据
     */
    public void mineRefre() {

        getData();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myReceiver!=null){
            getActivity().unregisterReceiver(myReceiver);
        }
    }
}
