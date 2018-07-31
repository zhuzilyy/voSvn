package com.zl.vo_.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;
import com.zl.vo_.R;
import com.zl.vo_.adapter.AllContactsAdapter;
import com.zl.vo_.db.DemoDBManager;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.myindexbar.CityAdapter;
import com.zl.vo_.myindexbar.DividerItemDecoration;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/11.
 */

public class allContactsListActivityVo extends VoBaseActivity implements View.OnClickListener{
    @BindView(R.id.tv_save)
    public TextView tv_save;
//    @BindView(R.id.allcontactsLV)
//    public ListView allcontactsLV;

    public AllContactsAdapter adapter;
    public DemoDBManager dbManager=DemoDBManager.getInstance();
    private String groupId;
    private  List<MyFrindEntivity> entivities;
    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;
    public List<MyFrindEntivity> bigList=new ArrayList<>();

    ////==============有索引的listview===========================
    private static final String INDEX_STRING_TOP = " ";
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




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_allcontacts);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        groupId=getIntent().getStringExtra("groupId");
        mInit();
    }

    private void mInit() {
        bigList.clear();
        entivities=dbManager.getAllFriends();

        for (int i = 0; i <entivities.size() ; i++) {
            if(TextUtils.isEmpty(entivities.get(i).getPassid())){
                bigList.add(entivities.get(i));
            }
        }

        /***
         * 查询出有passid,但是是临时显示的
         */
        List<MyFrindEntivity> hashideStateMyFriendList=dbManager.getHashideState();
        Log.i("null",hashideStateMyFriendList.size()+" 临时状态为1的");
        if(hashideStateMyFriendList.size()>0){
            bigList.addAll(hashideStateMyFriendList);
        }
        for (int i = 0; i <bigList.size() ; i++) {
            if(bigList.get(i).getGrooupID().equals(groupId)){
                bigList.get(i).setMychecked(true);
            }else {
                bigList.get(i).setMychecked(false);
            }
        }

        if(bigList.size()>0){
//            adapter=new AllContactsAdapter(allContactsListActivityVo.this,bigList,groupId);
//            allcontactsLV.setAdapter(adapter);


            mRv.setLayoutManager(mManager = new LinearLayoutManager(allContactsListActivityVo.this));
            mAdapter = new CityAdapter(allContactsListActivityVo.this, mDatas,0);
            mRv.setAdapter(mAdapter);
            mRv.addItemDecoration(mDecoration = new SuspensionDecoration(allContactsListActivityVo.this,mDatas));
            //如果add两个，那么按照先后顺序，依次渲染。

            mRv.addItemDecoration(new DividerItemDecoration(allContactsListActivityVo.this, DividerItemDecoration.VERTICAL_LIST));

            //indexbar初始化
            mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                    .setNeedRealIndex(true)//设置需要真实的索引
                    .setmLayoutManager(mManager);//设置RecyclerView的LayoutManager
            //******************************************
            mDatas.clear();
            mAdapter=new CityAdapter(allContactsListActivityVo.this,mDatas,0);
            mRv.setAdapter(mAdapter);
            mIndexBar.setmSourceDatas(mDatas)//设置数据
                    .invalidate();
            mDecoration.setmDatas(mDatas);


        }

//
//        allcontactsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if(bigList.get(i).getMychecked()){
//                    bigList.get(i).setMychecked(false);
//                }else {
//                    bigList.get(i).setMychecked(true);
//                }
//                adapter.notifyDataSetChanged();
//            }
//        });






    }
    @OnClick({R.id.tv_save})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tv_save:
                //选择切换好友

                String ids=getfriendsIds();
                switchFriendToGroup(ids);


                break;
            default:

                break;
        }
    }

    /***
     * 获取好友的分组id
     * @return
     */
    private String getfriendsIds() {
        String ids="";

        for (int i = 0; i <bigList.size(); i++) {
            if(bigList.get(i).getMychecked()){
                ids+=bigList.get(i).getUserID()+",";
            }
        }






        return ids;
    }

    /****
     * 向好友分组群添加群删减
     */
    private void switchFriendToGroup(String friend_groupIds) {
        loading_view.setVisibility(View.VISIBLE);
        RequestParams params=new RequestParams(Url.AppendToGroupUrl);
        params.addParameter("userid", myUtils.readUser(allContactsListActivityVo.this).getUserid());
        params.addParameter("groupid",groupId);
        params.addParameter("friend_userid_str",friend_groupIds);

        x.http().post(params, new MyCommonCallback<Result>() {
            @Override
            public void success(Result data) {
                loading_view.setVisibility(View.GONE);
                if("0".equals(data.code)){
                    sendBroadcast(new Intent("needRefresh"));
                    Log.i("yu","选择成功");
                    finish();
                }
            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                loading_view.setVisibility(View.GONE);
            }
        });
    }
}
