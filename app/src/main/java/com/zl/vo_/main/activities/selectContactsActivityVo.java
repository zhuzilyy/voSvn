package com.zl.vo_.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;
import com.zl.vo_.R;
import com.zl.vo_.adapter.SelectContactListAdapter;
import com.zl.vo_.db.DemoDBManager;
import com.zl.vo_.myindexbar.CityAdapter;
import com.zl.vo_.myindexbar.CityAdapter2;
import com.zl.vo_.myindexbar.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/19.
 */

public class selectContactsActivityVo extends VoBaseActivity {
//    @BindView(R.id.selectContactLV)
//    public ListView selectContactLV;
    public SelectContactListAdapter adapter;
    public List<MyFrindEntivity> biglist=new ArrayList<>();
    private static  final  int REQUEST_CODE_SELEST_MINGPIAN=99;
    public DemoDBManager dbManager=DemoDBManager.getInstance();

    ////==============有索引的listview===========================
    private static final String INDEX_STRING_TOP = " ";
    @BindView(R.id.rv)
    public RecyclerView mRv;
    public CityAdapter2 mAdapter;
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
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.lay_select_contacts);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        minit();
    }
    private void minit() {
        List<MyFrindEntivity> myFrindEntivityList=dbManager.getAllFriends();
        mDatas.addAll(myFrindEntivityList);

        mRv.setLayoutManager(mManager = new LinearLayoutManager(selectContactsActivityVo.this));
        mAdapter = new CityAdapter2(selectContactsActivityVo.this, mDatas,0);
        mRv.setAdapter(mAdapter);
        mRv.addItemDecoration(mDecoration = new SuspensionDecoration(selectContactsActivityVo.this,mDatas));
        //如果add两个，那么按照先后顺序，依次渲染。

        mRv.addItemDecoration(new DividerItemDecoration(selectContactsActivityVo.this, DividerItemDecoration.VERTICAL_LIST));

        //indexbar初始化
        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mManager);//设置RecyclerView的LayoutManager
        mIndexBar.setmSourceDatas(mDatas)//设置数据
                .invalidate();
        mDecoration.setmDatas(mDatas);
        mAdapter.setMyContactClickListener(new CityAdapter2.myContactsClickListener() {
            @Override
            public void onMyContactItemClick(int positon) {

                MyFrindEntivity friend=mDatas.get(positon);
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putSerializable("ssss",friend);
                intent.putExtras(bundle);
                setResult(-1,intent);
                finish();
            }

            @Override
            public void onMyContactItemLongClick(int position) {
                Toast.makeText(selectContactsActivityVo.this, "长按position"+position, Toast.LENGTH_SHORT).show();

            }
        });



    }


}
