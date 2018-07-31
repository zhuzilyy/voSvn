/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zl.vo_.main.main_fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;

import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;
import com.zl.vo_.DemoHelper;
import com.zl.vo_.R;
import com.zl.vo_.db.InviteMessgeDao;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.activities.ChatActivity;
import com.zl.vo_.main.activities.GroupManagerActivityVo;
import com.zl.vo_.main.activities.Help_Feedback;
import com.zl.vo_.main.activities.NewFriendsMsgActivityVo;
import com.zl.vo_.main.activities.addFriendActivity_ContactsVo;
import com.zl.vo_.main.activities.addFriendActivity_SearchVo;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.ui.GroupsActivity;
import com.zl.vo_.ui.ScanCaptureActivity;
import com.zl.vo_.utils.Url;
import com.zl.vo_.widget.ContactItemView;
import com.zl.vo_.widget.FXPopWindow;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * contact list
 *
 */
public class Main_ContactListFragment extends EaseContactListFragment implements OnClickListener {

    private static final String TAG = Main_ContactListFragment.class.getSimpleName();
    private ContactSyncListener contactSyncListener;
    private BlackListSyncListener blackListSyncListener;
    private ContactInfoSyncListener contactInfoSyncListener;
    private View loadingView;
    private ContactItemView applicationItem;
    private InviteMessgeDao inviteMessgeDao;
    public FXPopWindow fxPopWindow;
    public MyReceiver myReceiver;
    private Map<String, EaseUser> contactsMap;


    @SuppressLint("InflateParams")
    @Override
    protected void initView() {
        super.initView();
        @SuppressLint("InflateParams")
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.em_contacts_header, null);
        HeaderItemClickListener clickListener = new HeaderItemClickListener();
        applicationItem = (ContactItemView) headerView.findViewById(R.id.application_item);
        applicationItem.setOnClickListener(clickListener);

        ///----------------------

        myReceiver=new MyReceiver();
        IntentFilter filter=new IntentFilter("addFrindOK");
        IntentFilter filter1=new IntentFilter("onContactInvited");

        getActivity().registerReceiver(myReceiver,filter);
        getActivity().registerReceiver(myReceiver,filter1);

        ///------------------------
        headerView.findViewById(R.id.group_item).setOnClickListener(clickListener);
        headerView.findViewById(R.id.new_friend).setOnClickListener(clickListener);
        headerView.findViewById(R.id.chat_room_item).setOnClickListener(clickListener);
        headerView.findViewById(R.id.robot_item).setOnClickListener(clickListener);
        listView.addHeaderView(headerView);
        //add loading view
        loadingView = LayoutInflater.from(getActivity()).inflate(R.layout.em_layout_loading_data, null);
        contentContainer.addView(loadingView);
        //iv_search=iv_search.findViewById(R.id.iv_search);
        registerForContextMenu(listView);
    }

    /****
     * 在监听到好友发生变化后，刷新好友列表
     */
    @Override
    public void refresh() {
        Log.i("oo","=refresh====");
        //从数据库中获取好友
        Map<String, EaseUser> ContactMaps = DemoHelper.getInstance().getContactList();
        if (ContactMaps instanceof Hashtable<?, ?>) {
            //noinspection unchecked
            ContactMaps = (Map<String, EaseUser>) ((Hashtable<String, EaseUser>) ContactMaps).clone();
        }
        //给好友页面添加数据

     /*   DemoDBManager dbManager=DemoDBManager.getInstance();
       List<EaseUser> easeUsers= dbManager.getContactsFromDb();
        Log.i("ui","");*/


        setContactsMap(ContactMaps);
        super.refresh();

        //显示未读标记
        if (inviteMessgeDao == null) {
            inviteMessgeDao = new InviteMessgeDao(getActivity());
        }
        if (inviteMessgeDao.getUnreadMessagesCount() > 0) {
            applicationItem.showUnreadMsgView();
        } else {
            applicationItem.hideUnreadMsgView();
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    protected void setUpView() {
//        titleBar.setRightImageResource(R.drawable.em_add);
//        titleBar.setRightLayoutClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
////                startActivity(new Intent(getActivity(), AddContactActivity.class));
//                NetUtils.hasDataConnection(getActivity());
//            }
//        });


        fxPopWindow = new FXPopWindow(getActivity(), R.layout.fx_popupwindow_add, new FXPopWindow.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                switch (position) {
                    //发起群聊
                    case 0:
                        startActivity(new Intent(getActivity(),GroupsActivity.class));
                        // Toast.makeText(getActivity(), "发起群聊", Toast.LENGTH_SHORT).show();
                        break;
                    //添加新的好友
                    case 1:
                      startActivity(new Intent(getActivity(),addFriendActivity_ContactsVo.class));
                        //Toast.makeText(getActivity(), "添加新朋友", Toast.LENGTH_SHORT).show();
                        break;
                    //扫一扫
                    case 2:
                            startActivity(new Intent(getActivity(),ScanCaptureActivity.class));
                       // Toast.makeText(getActivity(), "扫一扫", Toast.LENGTH_SHORT).show();
                        break;
                    //帮助及反馈
                    case 3:
                        Intent intent=new Intent(getActivity(),Help_Feedback.class);
                        intent.putExtra("url","http://47.95.115.55:8080/voadmin/home/api/page_list");
                        intent.putExtra("param","16");
                        intent.putExtra("title","帮助与反馈");
                        getActivity().startActivity(intent);
                        break;
                }
            }
        });


        iv_add.setOnClickListener(this);
        iv_search.setOnClickListener(this);

        //设置联系人数据
        Map<String, EaseUser> m = DemoHelper.getInstance().getContactList();
        if (m instanceof Hashtable<?, ?>) {
            m = (Map<String, EaseUser>) ((Hashtable<String, EaseUser>) m).clone();
        }

        //----------------
        Log.i("yui","7777777777777777777");
       // setContactsMap(m);
        Log.i("yui","888888888888888888888");
        super.setUpView();

        //------------------

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EaseUser user = (EaseUser) listView.getItemAtPosition(position);
                if (user != null) {
                    String username = user.getUsername();
                    // demo中直接进入聊天页面，实际一般是进入用户详情页
                    startActivity(new Intent(getActivity(), ChatActivity.class).putExtra("userId", username));
                }
            }
        });


//        // 进入添加好友页
//        titleBar.getRightLayout().setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), AddContactActivity.class));
//            }
//        });
//

        contactSyncListener = new ContactSyncListener();
        DemoHelper.getInstance().addSyncContactListener(contactSyncListener);

        blackListSyncListener = new BlackListSyncListener();
        DemoHelper.getInstance().addSyncBlackListListener(blackListSyncListener);

        contactInfoSyncListener = new ContactInfoSyncListener();
        DemoHelper.getInstance().getUserProfileManager().addSyncContactInfoListener(contactInfoSyncListener);

        if (DemoHelper.getInstance().isContactsSyncedWithServer()) {
            loadingView.setVisibility(View.GONE);
        } else if (DemoHelper.getInstance().isSyncingContactsWithServer()) {
            loadingView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (contactSyncListener != null) {
            DemoHelper.getInstance().removeSyncContactListener(contactSyncListener);
            contactSyncListener = null;
        }

        if (blackListSyncListener != null) {
            DemoHelper.getInstance().removeSyncBlackListListener(blackListSyncListener);
        }

        if (contactInfoSyncListener != null) {
            DemoHelper.getInstance().getUserProfileManager().removeSyncContactInfoListener(contactInfoSyncListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add:
                fxPopWindow.showPopupWindow(iv_add);
                break;
            case R.id.iv_search:
                startActivity(new Intent(getActivity(),addFriendActivity_SearchVo.class));
                break;
            default:
                break;
        }
    }


    protected class HeaderItemClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.application_item:
                    // 申请与通知
                    startActivity(new Intent(getActivity(), NewFriendsMsgActivityVo.class));
                    break;
                case R.id.new_friend:
                    // 新朋友
                    startActivity(new Intent(getActivity(), addFriendActivity_ContactsVo.class));
                    break;
                case R.id.group_item:
                    // 群聊
                    startActivity(new Intent(getActivity(), GroupsActivity.class));
                    break;
                case R.id.chat_room_item:
                    //创建封装
                    startActivity(new Intent(getActivity(), GroupManagerActivityVo.class));
                    break;
                case R.id.robot_item:
                    //进入Robot列表页面
                    // startActivity(new Intent(getActivity(), RobotsActivity.class));
                    break;

                default:
                    break;
            }
        }

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        toBeProcessUser = (EaseUser) listView.getItemAtPosition(((AdapterContextMenuInfo) menuInfo).position);
        toBeProcessUsername = toBeProcessUser.getUsername();
        getActivity().getMenuInflater().inflate(R.menu.em_context_contact_list, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_contact) {
            try {
                // 删除联系人（自己接口）
                deleteContact(toBeProcessUser);
                // remove invitation message
                InviteMessgeDao dao = new InviteMessgeDao(getActivity());
                dao.deleteMessage(toBeProcessUser.getUsername());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        } else if (item.getItemId() == R.id.add_to_blacklist) {
            moveToBlacklist(toBeProcessUsername);
            return true;
        }
        return super.onContextItemSelected(item);
    }


    /**
     * 删除联系人
     *
     * @param tobeDeleteUser
     */
    public void deleteContact(final EaseUser tobeDeleteUser) {
        //TODO 删除好友未完



        String st1 = getResources().getString(R.string.deleting);
        final String st2 = getResources().getString(R.string.Delete_failed);
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage(st1);
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        RequestParams params=new RequestParams(Url.DeleteContactUrl);
        params.addParameter("userid", myUtils.readUser(getActivity()).getUserid());
        params.addParameter("friend_userid",tobeDeleteUser.getUsername());
        x.http().post(params, new MyCommonCallback<Result>() {
            @Override
            public void success(Result data) {

            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {

            }
        });



    }

    class ContactSyncListener implements DemoHelper.DataSyncListener {
        @Override
        public void onSyncComplete(final boolean success) {
            EMLog.d(TAG, "on contact list sync success:" + success);
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (success) {
                                loadingView.setVisibility(View.GONE);
                                refresh();
                            } else {
                                String s1 = getResources().getString(R.string.get_failed_please_check);
                                Toast.makeText(getActivity(), s1, Toast.LENGTH_LONG).show();
                                loadingView.setVisibility(View.GONE);
                            }
                        }

                    });
                }
            });
        }
    }

    class BlackListSyncListener implements DemoHelper.DataSyncListener {

        @Override
        public void onSyncComplete(boolean success) {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    refresh();
                }
            });
        }

    }

    class ContactInfoSyncListener implements DemoHelper.DataSyncListener {

        @Override
        public void onSyncComplete(final boolean success) {
            EMLog.d(TAG, "on contactinfo list sync success:" + success);
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    loadingView.setVisibility(View.GONE);
                    if (success) {
                        refresh();
                    }
                }
            });
        }

    }

    /****
     * 广播接受者
     */
    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if("addFrindOK".equals(action)){
                //从数据库中获取好友列表，刷新好友列表
               refresh();
            }
            if("onContactInvited".equals(action)){
               // Toast.makeText(context, "有人加你了", Toast.LENGTH_SHORT).show();
                //显示未读标记
                if (inviteMessgeDao == null) {
                    inviteMessgeDao = new InviteMessgeDao(getActivity());
                }
                if (inviteMessgeDao.getUnreadMessagesCount() > 0) {
                    applicationItem.showUnreadMsgView();
                } else {
                    applicationItem.hideUnreadMsgView();
                }
            }
        }
    }


    /**
     * get contact list and sort, will filter out users in blacklist
     * 得到好友并排序，并从黑名单中过滤
     */
    protected void getContactList() {
        contactList.clear();
        DemoHelper demoHelper=DemoHelper.getInstance();
        contactsMap= demoHelper.getContactList();
        synchronized (this.contactsMap) {
            Iterator<Map.Entry<String, EaseUser>> iterator = contactsMap.entrySet().iterator();
            List<String> blackList = EMClient.getInstance().contactManager().getBlackListUsernames();
            while (iterator.hasNext()) {
                Map.Entry<String, EaseUser> entry = iterator.next();
                // to make it compatible with data in previous version, you can remove this check if this is new app
                if (!entry.getKey().equals("item_new_friends")
                        && !entry.getKey().equals("item_groups")
                        && !entry.getKey().equals("item_chatroom")
                        && !entry.getKey().equals("item_robots")){
                    if(!blackList.contains(entry.getKey())){
                        //filter out users in blacklist
                        EaseUser user = entry.getValue();
                        EaseCommonUtils.setUserInitialLetter(user);
                        contactList.add(user);
                    }
                }
            }
        }

      /*  // sorting
        Collections.sort(contactList, new Comparator<EaseUser>() {

            @Override
            public int compare(EaseUser lhs, EaseUser rhs) {
                if(lhs.getInitialLetter().equals(rhs.getInitialLetter())){
                    return lhs.getNick().compareTo(rhs.getNick());
                }else{
                    if("#".equals(lhs.getInitialLetter())){
                        return 1;
                    }else if("#".equals(rhs.getInitialLetter())){
                        return -1;
                    }
                    return lhs.getInitialLetter().compareTo(rhs.getInitialLetter());
                }

            }
        });
*/
    }







}
