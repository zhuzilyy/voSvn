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
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.zl.vo_.R;
import com.zl.vo_.adapter.ContactsWithoutMineGoupAdapter;
import com.zl.vo_.db.DemoDBManager;
import com.zl.vo_.db.MyFriendGroupDao;
import com.zl.vo_.db.UserDao;
import com.zl.vo_.main.Entity.BigMineGroupList;
import com.zl.vo_.main.Entity.GroupData;
import com.zl.vo_.main.Entity.MyFriendGroupEntity;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.Entity.pwdCell;
import com.zl.vo_.main.activities.GroupManagerActivityVo;
import com.zl.vo_.main.activities.MyFrindEntivity;
import com.zl.vo_.main.activities.UserDetailsActivityVo;
import com.zl.vo_.main.adapter.MyExpandableListViewAdapter;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.main.views.NewListview;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/16.
 */

public class ContactsGroupFragment extends Fragment implements View.OnClickListener{
    private View vv;
    @BindView(R.id.goodFriend_addgroup_re)
    public RelativeLayout goodFriend_addgroup_re;
    ///-----------------------
    @BindView(R.id.exList)
    public ExpandableListView exList;
    private Map<String,List<MyFrindEntivity>> dataset = new HashMap<>();
    private List<String> parentNames = new ArrayList<>();
    private MyExpandableListViewAdapter exListAdapter;
    private MyFriendGroupDao groupDao;
    private UserDao userDao;
    private MyReceiver myReceiver;
    public DemoDBManager dbManager=DemoDBManager.getInstance();
    //有组的组的集合
    public List<BigMineGroupList> BigGroupLists=new ArrayList<>();
    //好友中此人没有分组信息的集合
    public List<MyFrindEntivity> withoutGruopList=new ArrayList<>();
    //没有组的适配器
    //public  ContactsWithoutMineGoupAdapter adapter_withoutGroup;

    public NewListview lv;
    //从数据库中查询分组信息
    public  List<MyFriendGroupEntity> groupEntities;

    List<MyFrindEntivity> entivities=new ArrayList<>();
    public View currentfooter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        vv=LayoutInflater.from(getActivity()).inflate(R.layout.lay_contact_group_fragment,null);
        ButterKnife.bind(this.vv);
        //-----------------------
        myReceiver=new MyReceiver();

        IntentFilter filter=new IntentFilter("creatGroupOk");
        IntentFilter filter1=new IntentFilter("onContactInvited");
        IntentFilter filter2=new IntentFilter("UpdateFriendOK");
        IntentFilter filter3=new IntentFilter("addFrindOK");


        getActivity().registerReceiver(myReceiver,filter);
        getActivity().registerReceiver(myReceiver,filter1);
        getActivity().registerReceiver(myReceiver,filter2);
        getActivity().registerReceiver(myReceiver,filter3);
        //----------------------------

        getActivity().sendBroadcast(new Intent("netGroupOK"));
        Log.i("88)","==1111111===netGroupOK====");
        groupDao=new MyFriendGroupDao();
        userDao=new UserDao(getActivity());

        exList=vv.findViewById(R.id.exList);
        //发送广播 到通讯录界面，请求数据，更新界面
        //网络同步分组
        getMyGroupFromNet();
        mInit();
        return vv;
    }

    /**
     * /网络同步分组
     */
    private void getMyGroupFromNet() {

        RequestParams parasm=new RequestParams(Url.GetGroupInfoUrl);
        parasm.addParameter("userid", myUtils.readUser(getActivity()).getUserid());
        x.http().post(parasm, new MyCommonCallback<Result<GroupData>>() {
            @Override
            public void success(Result<GroupData> data) {
                if("0".equals(data.code)||"-120".equals(data.code)){
                    GroupData groupData= data.data;
                    GroupData.GroupInfo groupInfo=groupData.getInfo();
                    List<GroupData.GroupInfo.GrouopList> grouopLists=groupInfo.getGrouplist();

                    refrshUIAndIntoDB(grouopLists);

                    Log.i("88)","==3333333===needRefresh====");
                    getActivity().sendBroadcast(new Intent("needRefresh"));
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

        //保存到数据库
        saveGroups(groupEntities);
    }

    /****
     * 将从网络上请求下来的数据保存到数据库，并发送广播更新好友分组
     * @param bigList
     */
    private void saveGroups(List<MyFriendGroupEntity> bigList) {
        groupDao.saveGroups(bigList);
        getActivity().sendBroadcast(new Intent("needRefresh"));
    }



    private void mInit() {

        dataset.clear();
        parentNames.clear();
        withoutGruopList.clear();
        parentNames.clear();
        BigGroupLists.clear();




        View footer=LayoutInflater.from(getActivity()).inflate(R.layout.lay_contact_without_minegroup,null);
        currentfooter=footer;
        //---------------------------------------
        //从数据库中查询分组信息
        groupEntities= groupDao.selectGroupEntities();
        for (int i = 0; i <groupEntities.size() ; i++) {
            parentNames.add(groupEntities.get(i).getGroupName());
            BigMineGroupList groupList=new BigMineGroupList();
            groupList.setGroupId(groupEntities.get(i).getGroupId()+"");
            groupList.setGroupName(groupEntities.get(i).getGroupName());
            BigGroupLists.add(groupList);
        }
        //从数据库中查询好友的分组情况
        List<MyFrindEntivity> allFriends=dbManager.getAllFriends();
        //遍历查询出来的好友，添加进各自的分组
        if(allFriends.size()>0){
            for (int i = 0; i <allFriends.size() ; i++) {

                String passid=allFriends.get(i).getPassid();
                pwdCell cell=dbManager.selectMyPwdState(passid);
                String state=cell.getHidestate();
                if((!TextUtils.isEmpty(allFriends.get(i).getPassid()))&&(("0".equals(state))||TextUtils.isEmpty(state))){
                    //有passid的是隐藏的
                    continue;
                }

                //如果此人没有组的信息就添加进 withoutGruopList
                if(TextUtils.isEmpty(allFriends.get(i).getGrooupID())){
                    withoutGruopList.add(allFriends.get(i));

                }else {
                    //有组的信息
                    for (int j = 0; j <groupEntities.size() ; j++) {
                        if((String.valueOf(groupEntities.get(j).getGroupId()).equals(allFriends.get(i).getGrooupID()))){
                            for (int k = 0; k <BigGroupLists.size() ; k++) {
                                if(BigGroupLists.get(k).getGroupId().equals(allFriends.get(i).getGrooupID())){
                                    BigGroupLists.get(k).getFriendList().add(allFriends.get(i));
                                }

                            }
                        }
                    }
                }

            }
        }

       for (int i = 0; i <parentNames.size() ; i++) {
            dataset.put(parentNames.get(i),BigGroupLists.get(i).getFriendList());
        }

        exListAdapter=new MyExpandableListViewAdapter(dataset,getActivity(),parentNames,BigGroupLists);
        exList.setAdapter(exListAdapter);
        exList.addFooterView(currentfooter);
        ListView lv=footer.findViewById(R.id.lv);
        //****添加footer**********************************************************
        //没有组的人的布局
        ContactsWithoutMineGoupAdapter adapter_withoutGroup=new ContactsWithoutMineGoupAdapter(getActivity(),withoutGruopList);
        lv.setAdapter(adapter_withoutGroup);

        //**************************************************************
        //点击分组管理
        vv.findViewById(R.id.goodFriend_addgroup_re).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), GroupManagerActivityVo.class));
            }
        });

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 //   Toast.makeText(getActivity(), "点击事件**----", Toast.LENGTH_SHORT).show();
                    //没有组的人的点击事件
                    Intent intent=new Intent(getActivity(), UserDetailsActivityVo.class);
                    intent.putExtra("HXid",withoutGruopList.get(i).getHuanxinID());
                    //账号
                    intent.putExtra("account",withoutGruopList.get(i).getAccount());
                    //userID
                    intent.putExtra("userid",withoutGruopList.get(i).getUserID());
                    intent.putExtra("remark",withoutGruopList.get(i).getRemark());

                    startActivity(intent);

                }
            });

        }

    @OnClick({R.id.goodFriend_addgroup_re})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.goodFriend_addgroup_re:
                break;
            default:
                break;
        }
    }

    /*****
     * 广播接受者
     */
    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if("creatGroupOk".equals(action)){
                //分组创建成功，刷新界面
                exList.removeFooterView(currentfooter);

                mInit();
            }
            if("UpdateFriendOK".equals(action)){
                //删除好友成功，刷新界面
                exList.removeFooterView(currentfooter);

                mInit();
            }

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(myReceiver!=null){
            getActivity().unregisterReceiver(myReceiver);
        }

    }




}
