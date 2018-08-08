package com.zl.vo_.main.main_fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.util.NetUtils;
import com.zl.vo_.Constant;
import com.zl.vo_.R;
import com.zl.vo_.db.DemoDBManager;
import com.zl.vo_.db.InviteMessgeDao;
import com.zl.vo_.main.activities.ChatActivity;
import com.zl.vo_.main.activities.MyFrindEntivity;
import com.zl.vo_.main.main_utils.myUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConversationListFragment extends EaseConversationListFragment {

    private TextView errorText;
    private MyReceiver myReceiver;
    public static DemoDBManager dbManager=DemoDBManager.getInstance();
    Map<String,String> Remarks=new HashMap<>();

    @Override
    protected void initView() {
        super.initView();

        View errorView =  View.inflate(getActivity(), R.layout.em_chat_neterror_item,null);
        //**************************************
        myReceiver=new MyReceiver();
        IntentFilter filter=new IntentFilter("UpdateFriendOK");
        IntentFilter filter1=new IntentFilter("needRefresh");
        IntentFilter filter2=new IntentFilter("hide");
        IntentFilter filter3=new IntentFilter("updateRemark");

        getActivity().registerReceiver(myReceiver,filter);
        getActivity().registerReceiver(myReceiver,filter1);
        getActivity().registerReceiver(myReceiver,filter2);
        getActivity().registerReceiver(myReceiver,filter3);
        //**************************************
        //查询隐藏好友的环信 id
        List<String> ids=getHIDEContactsIds();
        setHideContactsIds(ids);

        setContactsIds(getContactsIdsFromDB());
        refresh();
        //**************************************
        errorItemContainer.addView(errorView);
        errorText = errorView.findViewById(R.id.tv_connect_errormsg);
    }





    /***
     * 获取好友的环信id集合
     * @return
     */
    private List<String> getContactsIdsFromDB() {
        List<String> contactsIds=new ArrayList<>();
        DemoDBManager demoDBManager=DemoDBManager.getInstance();
        List<MyFrindEntivity> entivities=demoDBManager.getAllFriends();
        for (MyFrindEntivity f:entivities) {
            if(f!=null){
                contactsIds.add(f.getHuanxinID());
            }
        }
        return contactsIds;
    }


    @Override
    protected void setUpView() {

        conversationList.clear();
        conversationList.addAll(loadConversationList());
        if(conversationList.size()>0){
            //当不同设备登录，删除好友后，需要同步会话
           // conversationList=delConversation(conversationList);
            Remarks= getallConversationsRemarks(conversationList);
        }
        //设置会话数据
        conversationListView.init(conversationList,null,Remarks);

      /*  if(listItemClickListener != null){
            conversationListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    EMConversation conversation = conversationListView.getItem(position);
                    listItemClickListener.onListItemClicked(conversation);
                }
            });
        }*/

        EMClient.getInstance().addConnectionListener(connectionListener);

        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                conversationListView.filter(s);
                if (s.length() > 0) {
                    clearSearch.setVisibility(View.VISIBLE);
                } else {
                    clearSearch.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
                hideSoftKeyboard();
            }
        });

        conversationListView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return false;
            }
        });



        //super.setUpView();
        // register context menu
        registerForContextMenu(conversationListView);
        //会话的点击事件
        conversationListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = conversationListView.getItem(position);
                //环信id
                String username = conversation.conversationId();
                if (username.equals(EMClient.getInstance().getCurrentUser()))
                    Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                else {
                    // start chat acitivity

                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    if(conversation.isGroup()){
                        if(conversation.getType() == EMConversationType.ChatRoom){
                            // it's group chat
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
                        }else{
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                            intent.putExtra("avatar", myUtils.readUser(getActivity()).getAvatar());
                            intent.putExtra("nick", myUtils.readUser(getActivity()).getNickname());
                        }
                        intent.putExtra(Constant.EXTRA_USER_ID, username);
                        intent.putExtra("userId", username);
                        startActivity(intent);
                    }else {
                        // it's single chat
                        intent.putExtra(Constant.EXTRA_USER_ID, username);
                        intent.putExtra("userId", username);
                        //vo账号
                        MyFrindEntivity entivity=dbManager.getFriendByHxID(username);
                        if(entivity!=null) {
                            intent.putExtra("account", entivity.getAccount());
                            Log.i("newVo",entivity.getAccount()+"  88");
                            intent.putExtra("avatar", myUtils.readUser(getActivity()).getAvatar());
                            intent.putExtra("nick", myUtils.readUser(getActivity()).getNickname());
                            Log.i("newVo",entivity.getRemark()+"  88");
                            intent.putExtra("remark",entivity.getRemark());
                        }
                        //*****************************
                        if("000000".equals(conversation.conversationId())){
                            EMMessage msg=conversation.getLastMessage();
                            intent.putExtra("account","vo助手");
                            intent.putExtra("nick", "vo助手");
                            intent.putExtra("remark","vo助手");

                        }


                        startActivity(intent);

                    }

                }
            }
        });
        //red packet code : 红包回执消息在会话列表最后一条消息的展示
//        conversationListView.setConversationListHelper(new EaseConversationListHelper() {
//            @Override
//            public String onSetItemSecondaryText(EMMessage lastMessage) {
//                if (lastMessage.getBooleanAttribute(RPConstant.MESSAGE_ATTR_IS_RED_PACKET_ACK_MESSAGE, false)) {
//                    String sendNick = lastMessage.getStringAttribute(RPConstant.EXTRA_RED_PACKET_SENDER_NAME, "");
//                    String receiveNick = lastMessage.getStringAttribute(RPConstant.EXTRA_RED_PACKET_RECEIVER_NAME, "");
//                    String msg;
//                    if (lastMessage.direct() == EMMessage.Direct.RECEIVE) {
//                        msg = String.format(getResources().getString(R.string.msg_someone_take_red_packet), receiveNick);
//                    } else {
//                        if (sendNick.equals(receiveNick)) {
//                            msg = getResources().getString(R.string.msg_take_red_packet);
//                        } else {
//                            msg = String.format(getResources().getString(R.string.msg_take_someone_red_packet), sendNick);
//                        }
//                    }
//                    return msg;
//                }
//                return null;
//            }
//        });
        super.setUpView();
        //end of red packet code
    }



    /****
     * 获取联系人的备注
     * @param conversationList
     * @return
     */
    private Map<String,String> getallConversationsRemarks(List<EMConversation> conversationList) {
       Map<String,String> remarkMap=new HashMap<>();

        for (int i = 0; i <conversationList.size() ; i++) {
          MyFrindEntivity fri=  dbManager.getFriendByHxID(conversationList.get(i).conversationId());
            if(fri!=null){
                remarkMap.put(fri.getHuanxinID(),fri.getRemark());
            }

        }

        return remarkMap;
    }

    @Override
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
        if (NetUtils.hasNetwork(getActivity())){
         errorText.setText(R.string.can_not_connect_chat_server_connection);
        } else {
          errorText.setText(R.string.the_current_network);
        }
    }
    
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.em_delete_message, menu); 
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean deleteMessage = false;
        if (item.getItemId() == R.id.delete_message) {
            deleteMessage = true;
        } else if (item.getItemId() == R.id.delete_conversation) {
            deleteMessage = false;
        }
    	EMConversation tobeDeleteCons = conversationListView.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
    	if (tobeDeleteCons == null) {
    	    return true;
    	}
        if(tobeDeleteCons.getType() == EMConversationType.GroupChat){
            EaseAtMessageHelper.get().removeAtMeGroup(tobeDeleteCons.conversationId());
        }
        try {
            // delete conversation
            EMClient.getInstance().chatManager().deleteConversation(tobeDeleteCons.conversationId(), deleteMessage);
            InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
            inviteMessgeDao.deleteMessage(tobeDeleteCons.conversationId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        refresh();

        // update unread count
        ((MainActivity) getActivity()).updateUnreadLabel();
        return true;
    }

    /****
     *
     */
    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if("UpdateFriendOK".equals(action)){
                List<String> ids=getHIDEContactsIds();
                setHideContactsIds(ids);
              //  Toast.makeText(context, "我收到更新的广播了  隐藏人数是"+ids.size(), Toast.LENGTH_SHORT).show();
                refresh();
                setUpView();
            }
            if("needRefresh".equals(action)){
                String hxid=intent.getStringExtra("hxid");
                if(!TextUtils.isEmpty(hxid)){
                    //删除和某个user会话，如果需要保留聊天记录，传false
                    EMClient.getInstance().chatManager().deleteConversation(hxid,false);
                    refresh();
                }


            }

            if("hide".equals(action)){
                String passids=intent.getStringExtra("passid");
                if(passids!=null){
                    List<MyFrindEntivity> entivities=dbManager.getAllFriendsByPassid(passids);
                    if(entivities.size()>0){

                        for (int i = 0; i <entivities.size() ; i++) {
                            String hxid=entivities.get(i).getHuanxinID();
                            if(!TextUtils.isEmpty(hxid)){
                                //删除和某个user会话，如果需要保留聊天记录，传false
                                EMClient.getInstance().chatManager().deleteConversation(hxid,false);
                                refresh();
                            }

                        }

                    }


                }

            }

            if("updateRemark".equals(action)){
                refresh();
            }


        }
    }

    /****
     * 获取隐藏好友的环信id
     * @return
     */
    public static List<String> getHIDEContactsIds() {
        List<String> ids=new ArrayList<>();
        List<MyFrindEntivity> hideMyFriends=dbManager.gethidefriends();
        for (int i = 0; i < hideMyFriends.size(); i++) {
            ids.add(hideMyFriends.get(i).getHuanxinID());
        }

        return ids;
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        if(myReceiver!=null){
            getActivity().unregisterReceiver(myReceiver);
        }
    }



}
