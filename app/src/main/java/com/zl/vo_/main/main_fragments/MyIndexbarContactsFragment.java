package com.zl.vo_.main.main_fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;
import com.zl.vo_.DemoApplication;
import com.zl.vo_.DemoHelper;
import com.zl.vo_.R;
import com.zl.vo_.db.DemoDBManager;
import com.zl.vo_.db.InviteMessgeDao;
import com.zl.vo_.main.Entity.FriendListEntity;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.activities.ChatActivity;
import com.zl.vo_.main.activities.Help_Feedback;
import com.zl.vo_.main.activities.Help_Feedback_front;
import com.zl.vo_.main.activities.MyFrindEntivity;
import com.zl.vo_.main.activities.addFriendActivity_ContactsVo;
import com.zl.vo_.main.activities.addFriendActivity_SearchVo;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.main.views.DetailsTypePopupWindow;
import com.zl.vo_.myindexbar.CityAdapter;
import com.zl.vo_.myindexbar.DividerItemDecoration;
import com.zl.vo_.ui.GroupsActivity;
import com.zl.vo_.ui.ScanCaptureActivity;
import com.zl.vo_.utils.Url;
import com.zl.vo_.widget.FXPopWindow;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/24.
 */

public class MyIndexbarContactsFragment extends Fragment implements View.OnClickListener {

    private static final String INDEX_STRING_TOP = " ";
    private View view;
    public MyReceiver myReceiver;
    private DemoDBManager demoDBManager= DemoDBManager.getInstance();
    private InviteMessgeDao inviteMessgeDao=new InviteMessgeDao(getActivity());
    //-----------------------------
    @BindView(R.id.rv)
    public RecyclerView mRv;
    public CityAdapter mAdapter;
    public LinearLayoutManager mManager;
    public List<MyFrindEntivity> mDatas = new ArrayList<>();

    public SuspensionDecoration mDecoration;

    /**
     * 右侧边栏导航区域
     */
    @BindView(R.id.indexBar)
    public IndexBar mIndexBar;

    /**
     * 显示指示器DialogText
     */
    @BindView(R.id.tvSideBarHint)
    public TextView mTvSideBarHint;
    //------------------------------
    protected List<MyFrindEntivity> BigList=new ArrayList<>();
    @BindView(R.id.iv_add)
    public ImageView iv_add;
    @BindView(R.id.iv_search)
    public ImageView iv_search;
    public FXPopWindow fxPopWindow;
    public int hasNewMsg=0;

    String[] single_list = {"删除好友", "加入黑名单"};
    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=LayoutInflater.from(getActivity()).inflate(R.layout.lay_myindexcontacts,null);
        ButterKnife.bind(this,view);
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

        mInit();
        getData();
        return view;
    }
    private void mInit() {

        mRv.setLayoutManager(mManager = new LinearLayoutManager(getActivity()));
        mAdapter = new CityAdapter(getActivity(), mDatas,hasNewMsg);
        mRv.setAdapter(mAdapter);
        mRv.addItemDecoration(mDecoration = new SuspensionDecoration(getActivity(),mDatas));
        //如果add两个，那么按照先后顺序，依次渲染。

        //mRv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        //indexbar初始化
        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mManager);//设置RecyclerView的LayoutManager
    //******************************************
        mDatas.clear();
        mAdapter=new CityAdapter(getActivity(),mDatas,hasNewMsg);
        mRv.setAdapter(mAdapter);
        mIndexBar.setmSourceDatas(mDatas)//设置数据
                .invalidate();
        mDecoration.setmDatas(mDatas);

        mAdapter.setMyContactClickListener(new CityAdapter.myContactsClickListener() {
            @Override
            public void onMyContactItemClick(int positon) {
             //   Toast.makeText(getActivity(), "点击position"+positon, Toast.LENGTH_SHORT).show();
                  Intent intent=new Intent(getActivity(), ChatActivity.class);
                        intent.putExtra("userId",mDatas.get(positon).getHuanxinID());
                        intent.putExtra("account",mDatas.get(positon).getAccount());
                        intent.putExtra("avatar", myUtils.readUser(getActivity()).getAvatar());
                        intent.putExtra("nick", myUtils.readUser(getActivity()).getNickname());
                        intent.putExtra("remark",mDatas.get(positon).getRemark());
                        getActivity().startActivity(intent);
            }

            @Override
            public void onMyContactItemLongClick(int position) {
                Toast.makeText(getActivity(), "长按position"+position, Toast.LENGTH_SHORT).show();
                showSingleChoiceDialog(position);
            }
        });
    }
    /****
     * 加入黑名单
     */
    private void showSingleChoiceDialog(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("vo好友");
        builder.setIcon(R.mipmap.logo);
        builder.setSingleChoiceItems(single_list, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String str = single_list[which];
                switch(str){
                    case "删除好友":
                            deleteFriend(mDatas.get(position).getUserID());
                        break;
                    case "加入黑名单":
                        addBlackList(myUtils.readUser(getActivity()).getHuanxin_account(),mDatas.get(position).getHuanxinID());
                        break;
                    default:
                        break;
                }

                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
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

                        synsConversation();
                    }
                }
            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                Log.i("uu","error"+ex.getMessage());
                //从数据库中获取好友列表
                List<MyFrindEntivity> allFriends= demoDBManager.getAllFriends();
                Log.i("friend","====好友="+allFriends.size());
                mDatas.clear();
                //------------------
                mDatas.add((MyFrindEntivity) new MyFrindEntivity("新的朋友").setTop(true).setBaseIndexTag(INDEX_STRING_TOP));
                mDatas.add((MyFrindEntivity) new MyFrindEntivity("通讯录好友").setTop(true).setBaseIndexTag(INDEX_STRING_TOP));
                mDatas.add((MyFrindEntivity) new MyFrindEntivity("群聊").setTop(true).setBaseIndexTag(INDEX_STRING_TOP));
               // mDatas.add((MyFrindEntivity) new MyFrindEntivity("创建分组").setTop(true).setBaseIndexTag(INDEX_STRING_TOP));
                //------------------------
                mDatas.addAll(allFriends);

                mAdapter=new CityAdapter(getActivity(), mDatas,hasNewMsg);
                mIndexBar.setmSourceDatas(mDatas)//设置数据
                        .invalidate();
                mDecoration.setmDatas(mDatas);
                mRv.setAdapter(mAdapter);

                mAdapter.setMyContactClickListener(new CityAdapter.myContactsClickListener() {
                    @Override
                    public void onMyContactItemClick(int positon) {
                      //  Toast.makeText(getActivity(), "点击position"+positon, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getActivity(), ChatActivity.class);
                        intent.putExtra("userId",mDatas.get(positon).getHuanxinID());
                        intent.putExtra("account",mDatas.get(positon).getAccount());
                        intent.putExtra("avatar", myUtils.readUser(getActivity()).getAvatar());
                        intent.putExtra("nick", myUtils.readUser(getActivity()).getNickname());
                        intent.putExtra("remark",mDatas.get(positon).getRemark());
                        getActivity().startActivity(intent);
                    }
                    @Override
                    public void onMyContactItemLongClick(int position) {
                        showSingleChoiceDialog(position);
                    }
                });
            }
        });
    }
    private void synsConversation() {
            DemoDBManager demoDBManager=DemoDBManager.getInstance();
            List<MyFrindEntivity> entivities=demoDBManager.getAllFriends();
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
            friend.setSex(en.getSex());
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

       // mDatas=new ArrayList<>();
        mDatas.clear();

        //------------------
        mDatas.add((MyFrindEntivity) new MyFrindEntivity("新的朋友").setTop(true).setBaseIndexTag(INDEX_STRING_TOP));
        mDatas.add((MyFrindEntivity) new MyFrindEntivity("通讯录好友").setTop(true).setBaseIndexTag(INDEX_STRING_TOP));
        mDatas.add((MyFrindEntivity) new MyFrindEntivity("群聊").setTop(true).setBaseIndexTag(INDEX_STRING_TOP));
      //  mDatas.add((MyFrindEntivity) new MyFrindEntivity("创建分组").setTop(true).setBaseIndexTag(INDEX_STRING_TOP));
        //------------------------

        for (int i = 0; i <BigList.size() ; i++) {

            if(TextUtils.isEmpty(BigList.get(i).getPassid())){
                //passid是空的 就说明没有加密
                mDatas.add(BigList.get(i));
            }
        }


        //查询有临时状态的人

        List<MyFrindEntivity> hashideStateMyFriendList=demoDBManager.getHashideState();
        Log.i("null",hashideStateMyFriendList.size()+" 临时状态为1的");
        if(hashideStateMyFriendList.size()>0){
            mDatas.addAll(hashideStateMyFriendList);
        }


        //***********************************************
        //indexbar初始化
        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mManager);//设置RecyclerView的LayoutManager
        mIndexBar.setmSourceDatas(mDatas).invalidate();//设置数据
        mDecoration.setmDatas(mDatas);
        mAdapter=new CityAdapter(getActivity(),mDatas,hasNewMsg);
        mRv.setAdapter(mAdapter);
        //删除好友，发送广播到好友分组页面
        DemoApplication.nn=100;
        getActivity().sendBroadcast(new Intent("UpdateFriendOK"));


        mAdapter.setMyContactClickListener(new CityAdapter.myContactsClickListener() {
            @Override
            public void onMyContactItemClick(int positon) {
               // Toast.makeText(getActivity(), "点击position"+positon, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("userId",mDatas.get(positon).getHuanxinID());
                intent.putExtra("account",mDatas.get(positon).getAccount());
                intent.putExtra("avatar", myUtils.readUser(getActivity()).getAvatar());
                intent.putExtra("nick", myUtils.readUser(getActivity()).getNickname());
                intent.putExtra("remark",mDatas.get(positon).getRemark());
                getActivity().startActivity(intent);
            }

            @Override
            public void onMyContactItemLongClick(int position) {

                showSingleChoiceDialog(position);
            }
        });




    }

    @Override
    public void onResume() {
        super.onResume();
        if (inviteMessgeDao == null) {
            inviteMessgeDao = new InviteMessgeDao(getActivity());
        }
        if (inviteMessgeDao.getUnreadMessagesCount() > 0) {
            hasNewMsg=1;
           // mAdapter.notifyDataSetChanged();
            mIndexBar.setmSourceDatas(mDatas)//设置数据
                    .invalidate();
            mDecoration.setmDatas(mDatas);
            mAdapter=new CityAdapter(getActivity(),mDatas,hasNewMsg);
            mRv.setAdapter(mAdapter);

            mAdapter.setMyContactClickListener(new CityAdapter.myContactsClickListener() {
                @Override
                public void onMyContactItemClick(int positon) {
                   // Toast.makeText(getActivity(), "点击position"+positon, Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("userId",mDatas.get(positon).getHuanxinID());
                    intent.putExtra("account",mDatas.get(positon).getAccount());
                    intent.putExtra("avatar", myUtils.readUser(getActivity()).getAvatar());
                    intent.putExtra("nick", myUtils.readUser(getActivity()).getNickname());
                    intent.putExtra("remark",mDatas.get(positon).getRemark());
                    getActivity().startActivity(intent);
                }
                @Override
                public void onMyContactItemLongClick(int position) {
                    showSingleChoiceDialog(position);
                }
            });
        } else {
            //applicationItem.hideUnreadMsgView();
            hasNewMsg=0;
           // mAdapter.notifyDataSetChanged();
            mIndexBar.setmSourceDatas(mDatas)//设置数据
                    .invalidate();
            mDecoration.setmDatas(mDatas);
            mAdapter=new CityAdapter(getActivity(),mDatas,hasNewMsg);
            mRv.setAdapter(mAdapter);
            mAdapter.setMyContactClickListener(new CityAdapter.myContactsClickListener() {
                @Override
                public void onMyContactItemClick(int positon) {
                 //   Toast.makeText(getActivity(), "点击position"+positon, Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("userId",mDatas.get(positon).getHuanxinID());
                    intent.putExtra("account",mDatas.get(positon).getAccount());
                    intent.putExtra("avatar", myUtils.readUser(getActivity()).getAvatar());
                    intent.putExtra("nick", myUtils.readUser(getActivity()).getNickname());
                    intent.putExtra("remark",mDatas.get(positon).getRemark());
                    getActivity().startActivity(intent);
                }

                @Override
                public void onMyContactItemLongClick(int position) {

                    showSingleChoiceDialog(position);
                }
            });
        }
    }


    @OnClick({R.id.iv_add, R.id.iv_search})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_add:
                DetailsTypePopupWindow typePopupWindow = new DetailsTypePopupWindow(getActivity(),iv_add,"","");
                typePopupWindow.setmItemsOnClick(new DetailsTypePopupWindow.ItemsOnClick() {
                    @Override
                    public void itemsOnClick(int position) {
                        switch (position) {
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
                                Intent intent=new Intent(getActivity(),Help_Feedback.class);
                                intent.putExtra("url", Url.FunctionIntroduceUrl);
                                intent.putExtra("param","15");
                                intent.putExtra("title","功能介绍");
                                startActivity(intent);

                                break;
                        }
                    }
                });





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
        params.addParameter("userid", myUtils.readUser(getActivity()).getUserid());
        params.addParameter("friend_userid",friendUserId);
        x.http().post(params, new MyCommonCallback<Result>() {
            @Override
            public void success(Result data) {
                loading_view.setVisibility(View.GONE);
                Toast.makeText(getActivity(), data.info, Toast.LENGTH_SHORT).show();
                if("0".equals(data.code)){
                    DemoHelper.textnum=101;
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
    public class MyReceiver extends BroadcastReceiver {

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
                    //newmsg_icon.setVisibility(View.VISIBLE);
                    hasNewMsg=1;
                    mAdapter.notifyDataSetChanged();
                } else {
                    //applicationItem.hideUnreadMsgView();
                    //newmsg_icon.setVisibility(View.GONE);
                    hasNewMsg=0;
                    mAdapter.notifyDataSetChanged();
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


        Map<String,EMConversation> conversationMap=EMClient.getInstance().chatManager().getAllConversations();
//                    DemoDBManager demoDBManager=DemoDBManager.getInstance();
//                    List<MyFrindEntivity> entivities=demoDBManager.gethidefriends_pwdstate();
//                    for (int i = 0; i <entivities.size() ; i++) {
//                        String hxid=entivities.get(i).getHuanxinID();
//                        Map<String,EMConversation> conversationMap=EMClient.getInstance().chatManager().getAllConversations();
//                        EMConversation conversation= EMClient.getInstance().chatManager().getConversation(hxid);
//                        if(conversation!=null){
//                            conversation.clearAllMessages();
//                        }
//                    }
//


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
