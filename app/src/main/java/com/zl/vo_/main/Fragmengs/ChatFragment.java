package com.zl.vo_.main.Fragmengs;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.ui.EaseChatFragment.EaseChatFragmentHelper;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EasyUtils;
import com.hyphenate.util.PathUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.zl.vo_.Constant;
import com.zl.vo_.DemoHelper;
import com.zl.vo_.DemoModel;
import com.zl.vo_.R;
import com.zl.vo_.db.DemoDBManager;
import com.zl.vo_.domain.RobotUser;
import com.zl.vo_.main.Entity.LoginData;
import com.zl.vo_.main.activities.MyFrindEntivity;
import com.zl.vo_.main.activities.UserDetailsActivityVo;
import com.zl.vo_.main.activities.selectContactsActivityVo;
import com.zl.vo_.main.main_fragments.MainActivity;
import com.zl.vo_.main.main_utils.MinPianConstant;
import com.zl.vo_.main.main_utils.ShareConstant;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.main.views.Hyp_dialog;
import com.zl.vo_.ui.ChatRoomDetailsActivity;
import com.zl.vo_.ui.ContextMenuActivity;
import com.zl.vo_.ui.ForwardMessageActivity;
import com.zl.vo_.ui.GroupDetailsActivity;
import com.zl.vo_.ui.ImageGridActivity;
import com.zl.vo_.ui.VideoCallActivity;
import com.zl.vo_.ui.VoiceCallActivity;
import com.zl.vo_.util.BitmapAndBase64Transform;
import com.zl.vo_.utils.Url;
import com.zl.vo_.widget.ChatRowVoiceCall;
import com.zl.vo_.widget.EaseChatRowRecall;
import com.zl.vo_.widget.scan.GameShareCharRow;
import com.zl.vo_.widget.scan.MingPianCharRow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChatFragment extends EaseChatFragment implements EaseChatFragmentHelper {

    // constant start from 11 to avoid conflict with constant in base class
    private static final int ITEM_VIDEO = 11;
    private static final int ITEM_FILE = 12;
    private static final int ITEM_VOICE_CALL = 13;
    private static final int ITEM_VIDEO_CALL = 14;

    private static final int REQUEST_CODE_SELECT_VIDEO = 11;
    private static final int REQUEST_CODE_SELECT_FILE = 12;
    private static final int REQUEST_CODE_GROUP_DETAIL = 13;
    private static final int REQUEST_CODE_CONTEXT_MENU = 14;
    private static final int REQUEST_CODE_SELECT_AT_USER = 15;


    private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 1;
    private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 2;
    private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 3;
    private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 4;
    private static final int MESSAGE_TYPE_RECALL = 9;
    //red packet code : 红包功能使用的常量
    private static final int MESSAGE_TYPE_RECV_RED_PACKET = 5;
    private static final int MESSAGE_TYPE_SEND_RED_PACKET = 6;
    private static final int MESSAGE_TYPE_SEND_RED_PACKET_ACK = 7;
    private static final int MESSAGE_TYPE_RECV_RED_PACKET_ACK = 8;
    private static final int MESSAGE_TYPE_RECV_RANDOM = 11;
    private static final int MESSAGE_TYPE_SEND_RANDOM = 12;
    private static final int ITEM_RED_PACKET = 16;
    //end of red packet code


    /****
     * 名片
     */
    private static final int MINGPIAN = 92;
    private static final int REQUEST_CODE_SELEST_MINGPIAN = 99;
    private static final int MESSAGE_TYPE_RECV_MINGPIAN_CALL = 5;
    private static final int MESSAGE_TYPE_SENT_MINGPIAN_CALL = 6;


    /****
     * 分享
     */
    private static final int GAMESHARE = 93;
    private static final int MESSAGE_TYPE_RECV_SHARE_CALL = 7;
    private static final int MESSAGE_TYPE_SENT_SHARE_CALL = 8;


    /**
     * if it is chatBot
     */
    private boolean isRobot;

    private DemoModel demoModel;
    private DemoDBManager dbManager = DemoDBManager.getInstance();
    private Hyp_dialog hyp_dialog;


    private List<LocalMedia> selectList = new ArrayList<>();

    private MyReceiver myReceiver;

    private String passid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState,
                DemoHelper.getInstance().getModel().isMsgRoaming() && (chatType != EaseConstant.CHATTYPE_CHATROOM));
    }

    @Override
    protected void setUpView() {
        //注册广播，用于预览后直接发送视频
        myReceiver =  new MyReceiver();
        IntentFilter filter =  new IntentFilter("videoPreviewOK");
        getActivity().registerReceiver(myReceiver,filter);


        demoModel = new DemoModel(getActivity());
        //设置聊天背景（在子类中设置）
        String bgUrl = demoModel.getChatBackGroundPicUrl();
        Log.i("bgUrl", bgUrl + "聊天背景图片");
        Bitmap bitmap = BitmapAndBase64Transform.base64ToBitmap(bgUrl);
        if(bitmap!=null){
            chatBackground_iv.setImageBitmap(bitmap);
        }



        setChatFragmentHelper(this);
        if (chatType == Constant.CHATTYPE_SINGLE) {
            Map<String, RobotUser> robotMap = DemoHelper.getInstance().getRobotList();
            if (robotMap != null && robotMap.containsKey(toChatUsername)) {
                isRobot = true;
            }
        }
        super.setUpView();
        // set click listener
        titleBar.setLeftLayoutClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EasyUtils.isSingleActivity(getActivity())) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
                onBackPressed();
            }
        });

        //注册自定义表情
        // ((EaseEmojiconMenu)inputMenu.getEmojiconMenu()).addEmojiconGroup(EmojiconExampleGroupData.getData());
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            inputMenu.getPrimaryMenu().getEditText().addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (count == 1 && "@".equals(String.valueOf(s.charAt(start)))) {
//                        startActivityForResult(new Intent(getActivity(), PickAtUserActivity.class).
//                                putExtra("groupId",toChatUsername),REQUEST_CODE_SELECT_AT_USER);
                    }
                }
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        //消息转发
        if (forward_msg_id != null) {
            forwardMessage(forward_msg_id);
        }
    }
    /***
     * 转发消息（复写父类）
     *
     * @param forward_msg_id
     */
    @Override
    protected void forwardMessage(String forward_msg_id) {
        final EMMessage forward_msg = EMClient.getInstance().chatManager().getMessage(forward_msg_id);
        EMMessage.Type type = forward_msg.getType();
        switch (type) {
            case TXT:
                if (forward_msg.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)) {
                    sendBigExpressionMessage(((EMTextMessageBody) forward_msg.getBody()).getMessage(),
                            forward_msg.getStringAttribute(EaseConstant.MESSAGE_ATTR_EXPRESSION_ID, null));
                } else {
                    String TYPE=forward_msg.getStringAttribute(MinPianConstant.myExtType,"");

                    if("mingpianType".equals(TYPE)){
                        String hxid=forward_msg.getStringAttribute(MinPianConstant.huanxinID,"");
                        String userimg=forward_msg.getStringAttribute(MinPianConstant.userAvater,"");
                        String nick=forward_msg.getStringAttribute(MinPianConstant.name,"");
                        String accont=forward_msg.getStringAttribute(MinPianConstant.cardUserPhone,"");
                        String toUsername_=forward_msg.getTo();

                        EMMessage message = EMMessage.createTxtSendMessage("[名片]",toChatUsername);
                        message.setChatType(chatType == 1 ? EMMessage.ChatType.Chat : EMMessage.ChatType.GroupChat);
                        message.setAttribute(MinPianConstant.myExtType, "mingpianType");//设置拓展字段
                        message.setAttribute(MinPianConstant.huanxinID, hxid);  //环信id
                        message.setAttribute(MinPianConstant.userAvater, userimg);//头像
                        message.setAttribute(MinPianConstant.name, nick);//昵称
                        message.setAttribute(MinPianConstant.cardUserPhone, accont);//账号
                        message.setAttribute("account",accont);
                        message.setAttribute("avatar", myUtils.readUser(getActivity()).getAvatar());//我的头像
                        message.setAttribute("nick", myUtils.readUser(getActivity()).getNickname());//我的昵称
                        try {
                            EMClient.getInstance().chatManager().sendMessage(message);
                        } catch (Exception e) {
                            Log.i("ui", e.getMessage());
                        }

                    }else if("ShareType".equals(TYPE)){
                        String gameAvatar=forward_msg.getStringAttribute(ShareConstant.gameAvatar,"");
                        String package_name=forward_msg.getStringAttribute(ShareConstant.packageName,"");
                        String game_content=forward_msg.getStringAttribute(ShareConstant.gameContent,"");
                        String game_name=forward_msg.getStringAttribute(ShareConstant.gameName,"");
                        String param_gameid=forward_msg.getStringAttribute(ShareConstant.params_gameid,"");
                        String param_roomid=forward_msg.getStringAttribute(ShareConstant.params_roomid,"");
                        String param_kindid=forward_msg.getStringAttribute(ShareConstant.params_kindid,"");
//                        String param_d=forward_msg.getStringAttribute(ShareConstant.params_d,"");
//                        String param_e=forward_msg.getStringAttribute(ShareConstant.params_e,"");
                        EMMessage message=EMMessage.createTxtSendMessage("[分享]",toChatUsername);

                        message.setAttribute(ShareConstant.myExtType,"ShareType");//设置拓展字段
                        message.setAttribute(ShareConstant.packageName,package_name);  //包名
                        message.setAttribute(ShareConstant.gameAvatar,gameAvatar);//头像
                        message.setAttribute(ShareConstant.huanxinID,toChatUsername);
                        message.setAttribute(ShareConstant.gameContent,game_content);//邀请信息
                        message.setAttribute(ShareConstant.gameName,game_name);  //游戏名称
                        if(!TextUtils.isEmpty(param_gameid)){
                            message.setAttribute(ShareConstant.params_gameid,param_gameid);//参数01
                        }
                        if(!TextUtils.isEmpty(param_roomid)){
                            message.setAttribute(ShareConstant.params_roomid,param_roomid);//参数02
                        }

                        if(!TextUtils.isEmpty(param_kindid)){
                            message.setAttribute(ShareConstant.params_kindid,param_kindid);//参数03
                        }

//                        if(!TextUtils.isEmpty(param_d)){
//                            message.setAttribute(ShareConstant.params_d,param_d);//参数04
//                        }
//
//                        if(!TextUtils.isEmpty(param_e)){
//                            message.setAttribute(ShareConstant.params_e,param_e);//参数05
//                        }
                        message.setAttribute("avatar", myUtils.readUser(getActivity()).getAvatar());//我的头像
                        message.setAttribute("nick",myUtils.readUser(getActivity()).getNickname());//我的昵称

                        try{
                            EMClient.getInstance().chatManager().sendMessage(message);
                        }catch (Exception e){
                            Log.i("ui",e.getMessage());
                        }


                    }
                    else {

                        String content = ((EMTextMessageBody) forward_msg.getBody()).getMessage();
                        sendTextMessage(content);
                    }



                }
                break;
            case IMAGE:
                // send image
                String filePath = ((EMImageMessageBody) forward_msg.getBody()).getLocalUrl();
                if (filePath != null) {
                    File file = new File(filePath);
                    if (!file.exists()) {
                        // send thumb nail if original image does not exist
                        filePath = ((EMImageMessageBody) forward_msg.getBody()).thumbnailLocalPath();
                    }
                    sendImageMessage(filePath);
                }
                break;
            default:
                break;
        }

        if (forward_msg.getChatType() == EMMessage.ChatType.ChatRoom) {
            EMClient.getInstance().chatroomManager().leaveChatRoom(forward_msg.getTo());
        }
    }


    /****
     * 进入用户的详情页面
     */
    @Override
    protected void emptyHistory() {
        super.emptyHistory();
        Intent intent = new Intent(getActivity(), UserDetailsActivityVo.class);
        intent.putExtra("HXid", toChatUsername);
        intent.putExtra("account", accountName);
        intent.putExtra("way", "apply_card");
        startActivity(intent);
    }

    @Override
    protected void registerExtendMenuItem() {
        //use the menu in base class
        super.registerExtendMenuItem();
        //extend menu items
        inputMenu.registerExtendMenuItem(R.string.attach_video, R.mipmap.send_video, ITEM_VIDEO, extendMenuItemClickListener);
        //**********************************************
        //增加一个名片发送
        inputMenu.registerExtendMenuItem("名片", R.mipmap.send_idcard, MINGPIAN, extendMenuItemClickListener);
        //************************************************
        //聊天室暂时不支持红包功能
        //red packet code : 注册红包菜单选项
        if (chatType != Constant.CHATTYPE_CHATROOM) {
            //   inputMenu.registerExtendMenuItem(R.string.attach_red_packet, R.drawable.em_chat_red_packet_selector, ITEM_RED_PACKET, extendMenuItemClickListener);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(getActivity(), "requestCode="+requestCode+"===resultCode "+resultCode, Toast.LENGTH_SHORT).show();
        if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
            switch (resultCode) {
                case ContextMenuActivity.RESULT_CODE_COPY: // copy
                    clipboard.setPrimaryClip(ClipData.newPlainText(null,
                            ((EMTextMessageBody) contextMenuMessage.getBody()).getMessage()));
                    break;
                case ContextMenuActivity.RESULT_CODE_DELETE: // delete
                    String msgId = contextMenuMessage.getMsgId();
                    conversation.removeMessage(contextMenuMessage.getMsgId());
                    messageList.refresh();
                    break;

                case ContextMenuActivity.RESULT_CODE_FORWARD: // forward
                    Intent intent = new Intent(getActivity(), ForwardMessageActivity.class);
                    intent.putExtra("forward_msg_id", contextMenuMessage.getMsgId());
                    startActivity(intent);
                    break;
                case ContextMenuActivity.RESULT_CODE_RECALL://recall
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMMessage msgNotification = EMMessage.createTxtSendMessage(" ", contextMenuMessage.getTo());
                                EMTextMessageBody txtBody = new EMTextMessageBody(getResources().getString(R.string.msg_recall_by_self));
                                msgNotification.addBody(txtBody);
                                msgNotification.setMsgTime(contextMenuMessage.getMsgTime());
                                msgNotification.setLocalTime(contextMenuMessage.getMsgTime());
                                msgNotification.setAttribute(Constant.MESSAGE_TYPE_RECALL, true);
                                EMClient.getInstance().chatManager().recallMessage(contextMenuMessage);
                                EMClient.getInstance().chatManager().saveMessage(msgNotification);
                                messageList.refresh();
                            } catch (final HyphenateException e) {
                                e.printStackTrace();
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() { Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                    break;

                default:

                    break;
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            Log.i("ii","request"+requestCode);
            switch (requestCode) {
                case REQUEST_CODE_SELECT_VIDEO: //send the video
                    if (data != null) {
                        int duration = data.getIntExtra("dur", 0);
                        String videoPath = data.getStringExtra("path");
                        //修改发送视频是
                        File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
                       //File file =createFile();

                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            Bitmap ThumbBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
                            ThumbBitmap.compress(CompressFormat.JPEG, 100, fos);
                           // Log.i("bitmaps",ThumbBitmap+"    ?");
                            fos.close();
                            sendVideoMessage(videoPath, file.getAbsolutePath(), duration);
                            Log.i("ui","123465");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case REQUEST_CODE_SELECT_FILE: //send the file
                    if (data != null) {
                        Uri uri = data.getData();
                        if (uri != null) {
                            sendFileByUri(uri);
                        }
                    }
                    break;
                case REQUEST_CODE_SELECT_AT_USER:
                    if (data != null) {
                        String username = data.getStringExtra("username");
                        inputAtUsername(username, false);
                    }
                    break;

                //---准备发送名片----------------------------
                case REQUEST_CODE_SELEST_MINGPIAN:
                    if (data != null) {
                        Log.i("xxx", "点击拓展菜单中的按钮发送名片");
                        Bundle bundle = data.getExtras(); //获取从selectContactActivity界面传回的数据
                        MyFrindEntivity friend = (MyFrindEntivity) bundle.getSerializable("ssss");
                        String account = friend.getAccount();
                        String userimg = friend.getAvatar();
                        String userhxid = friend.getHuanxinID();
                        String usernick = friend.getNike();

                        EMMessage message = EMMessage.createTxtSendMessage("[名片]",toChatUsername);
                        message.setChatType(chatType == 1 ? EMMessage.ChatType.Chat : EMMessage.ChatType.GroupChat);
                        message.setAttribute(MinPianConstant.myExtType, "mingpianType");//设置拓展字段
                        message.setAttribute(MinPianConstant.huanxinID, userhxid);  //环信id
                        message.setAttribute(MinPianConstant.userAvater, userimg);//头像
                        message.setAttribute(MinPianConstant.name, usernick);//昵称
                        message.setAttribute(MinPianConstant.cardUserPhone, account);//账号
                        message.setAttribute("account",account);
                        message.setAttribute("avatar", myUtils.readUser(getActivity()).getAvatar());//我的头像
                        message.setAttribute("nick", myUtils.readUser(getActivity()).getNickname());//我的昵称
                        try {
                            EMClient.getInstance().chatManager().sendMessage(message);
                        } catch (Exception e) {
                            Log.i("ui", e.getMessage());
                        }
                    }


                    break;
                case PictureConfig.REQUEST_CAMERA:
                    //第三方发送视频
                    selectList=PictureSelector.obtainMultipleResult(data);
                    if(selectList.size()>0){
                      String videoPath=  selectList.get(0).getPath();
                        File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            Bitmap ThumbBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
                            ThumbBitmap.compress(CompressFormat.JPEG, 100, fos);
                            // Log.i("bitmaps",ThumbBitmap+"    ?");
                            fos.close();
                            sendVideoMessage(videoPath, file.getAbsolutePath(), 0);
                            Log.i("ui","123465");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    break;

                default:
                    break;
            }
        }

    }

    /**
     * 创建一个文件
     * @return
     */
    private File createFile() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //如果为挂载状态，那么就通过Environment的getExternalStorageDirectory()方法来获取
           // 外置存储卡的目录，然后加上我们自己要创建的文件名（记住文件名前要加一个"/"），这样
           // 就生成了我们要创建的文件路径。
            String path = Environment.getExternalStorageDirectory() + "/vovideo";
            //新建一个File对象，把我们要建的文件路径传进去。
            File file = new File(path);
            //方便查看，在控制台打印一下我们的存储卡目录。
            Log.d("=====TAG=====", "onClick: "+Environment.getExternalStorageDirectory());
            //判断文件是否存在，如果存在就删除。
            if (file.exists()) {
                file.delete();
            }
            try {
                //通过文件的对象file的createNewFile()方法来创建文件
                file.createNewFile();
                return file;

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    /****
     * 处理消息的拓展熟悉[发送头像和昵称]
     *
     * @param message
     */
    @Override
    public void onSetMessageAttributes(EMMessage message) {
        if (isRobot) {
            //set message extension
            message.setAttribute("em_robot_message", isRobot);
        }
        //在这里将头像和昵称发送出去
        LoginData.LoginInfo.LoginAccountInfo user = myUtils.readUser(getActivity());
        if (user != null) {
            if (!TextUtils.isEmpty(user.getAvatar())) {
                message.setAttribute("avatar", user.getAvatar());
            }
            if (!TextUtils.isEmpty(user.getNickname())) {
                message.setAttribute("nick", user.getNickname());
            }
        }
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return new CustomChatRowProvider();
    }

    /****
     * 打开群组聊天详情
     */
    @Override
    public void onEnterToChatDetails() {
        if (chatType == Constant.CHATTYPE_GROUP) {
            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
            if (group == null) {
                Toast.makeText(getActivity(), R.string.gorup_not_found, Toast.LENGTH_SHORT).show();
                return;
            }
            startActivityForResult(
                    (new Intent(getActivity(), GroupDetailsActivity.class).putExtra("groupId", toChatUsername)),
                    REQUEST_CODE_GROUP_DETAIL);
        }else if (chatType == Constant.CHATTYPE_CHATROOM) {
            startActivityForResult(new Intent(getActivity(), ChatRoomDetailsActivity.class).putExtra("roomId", toChatUsername), REQUEST_CODE_GROUP_DETAIL);
        }
    }

    @Override
    public void onAvatarClick(String username) {
        //handling when user click avatar
        //点击头像进入个人详情界面
        if (EaseConstant.CHATTYPE_GROUP == chatType) {
            Intent intent = new Intent(getActivity(), UserDetailsActivityVo.class);
            intent.putExtra("HXid",username);
            intent.putExtra("way","apply_group");
            List<MyFrindEntivity> entivities = dbManager.gethidefriends_pwdstate();
            //passid是空的 就说明没有加密
            for (MyFrindEntivity u: entivities) {
                if(username.equals(u.getHuanxinID())){
                 if(!TextUtils.isEmpty(u.getPassid())){
                     intent.putExtra("passId",u.getPassid());
                 }
                }
            }
            startActivity(intent);

        }
    }

    @Override
    public void onAvatarLongClick(String username) {
        inputAtUsername(username);
    }


    @Override
    public boolean onMessageBubbleClick(EMMessage message) {

        String myMessageType = message.getStringAttribute("myExtType", null);
        if ("mingpianType".equals(myMessageType)) {
            String hxid = message.getStringAttribute(MinPianConstant.huanxinID, null);
            if (!TextUtils.isEmpty(hxid)) {
                Intent intent = new Intent(getActivity(), UserDetailsActivityVo.class);
                intent.putExtra("HXid", hxid);
                intent.putExtra("way", "apply_card");
                intent.putExtra("account", message.getStringAttribute(MinPianConstant.cardUserPhone, null));
                startActivity(intent);
            }
        }

        if ("ShareType".equals(myMessageType)) {
            String packageName = message.getStringAttribute(ShareConstant.packageName, null);
            String params_gameid = message.getStringAttribute(ShareConstant.params_gameid, null);
            String params_roomid = message.getStringAttribute(ShareConstant.params_roomid, null);
            String params_kindid = message.getStringAttribute(ShareConstant.params_kindid, null);
//            String paramD = message.getStringAttribute(ShareConstant.params_d, null);
//            String paramE = message.getStringAttribute(ShareConstant.params_e, null);
            Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage(packageName);
            if (intent != null) {
                intent.setAction("vo");
                if (!TextUtils.isEmpty(params_gameid)) {
                    intent.putExtra("gameId", params_gameid);
                }
                if (!TextUtils.isEmpty(params_roomid)) {
                    intent.putExtra("roomId", params_roomid);
                }
                if (!TextUtils.isEmpty(params_kindid)) {
                    intent.putExtra("kindId", params_kindid);
                }
              /*if(!TextUtils.isEmpty(paramD)) {
                    intent.putExtra("paramD", paramD);
                }
                if (!TextUtil s.isEmpty(paramE)) {
                    intent.putExtra("paramE", paramE);
                }*/
                getActivity().startActivity(intent);
            } else {
                //更新版本
                Toast.makeText(getActivity(), "您还没有下载" + packageName + ",快去下载吧", Toast.LENGTH_SHORT).show();
            }
        }

        return false;
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {
        super.onCmdMessageReceived(messages);
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {


        //长按气泡转发，删除，复制
        startActivityForResult((new Intent(getActivity(), ContextMenuActivity.class)).putExtra("message", message)
                        .putExtra("ischatroom", chatType == EaseConstant.CHATTYPE_CHATROOM),
                REQUEST_CODE_CONTEXT_MENU);
    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        switch (itemId) {
            case ITEM_VIDEO:
                Intent intent = new Intent(getActivity(), ImageGridActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
             // selectVideo_Vo();
                break;
            case MINGPIAN:
                //发送名片
                startActivityForResult(new Intent(getActivity(), selectContactsActivityVo.class), REQUEST_CODE_SELEST_MINGPIAN);
                break;
            case ITEM_VOICE_CALL:
                startVoiceCall();
                break;
            case ITEM_VIDEO_CALL:
                startVideoCall();
                break;
            default:
                break;
        }
        //keep exist extend menu
        return false;
    }

    /***
     * 根据客户要求，可选择视频
     */
    private void selectVideo_Vo() {
        if (true) {
            // 进入相册 以下是例子：不需要的api可以不写
            PictureSelector.create(this)
                    .openGallery(PictureMimeType.ofVideo())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .theme(com.hyphenate.easeui.R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                    .maxSelectNum(1)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .imageSpanCount(4)// 每行显示个数
                    .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                    .previewImage(true)// 是否可预览图片
                    .previewVideo(true)// 是否可预览视频
                     .enablePreviewAudio(true) // 是否可播放音频
                    // .isCamera(cb_isCamera.isChecked())// 是否显示拍照按钮
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                    //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                    //.enableCrop(cb_crop.isChecked())// 是否裁剪
                    .compress(true)// 是否压缩
                    .synOrAsy(true)//同步true或异步false 压缩 默认同步
                    //.compressSavePath(getPath())//压缩图片保存地址
                    //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    //.withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    // .hideBottomControls(cb_hide.isChecked() ? false : true)// 是否显示uCrop工具栏，默认不显示
                    // .isGif(cb_isGif.isChecked())// 是否显示gif图片
                    // .freeStyleCropEnabled(cb_styleCrop.isChecked())// 裁剪框是否可拖拽
                    // .circleDimmedLayer(cb_crop_circular.isChecked())// 是否圆形裁剪
                    // .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                    //.showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    //  .openClickSound(cb_voice.isChecked())// 是否开启点击声音
                    .selectionMedia(selectList)// 是否传入已选图片
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                    //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    .cropCompressQuality(90)// 裁剪压缩质量 默认100
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    //.rotateEnabled() // 裁剪是否可旋转图片
                    //.scaleEnabled()// 裁剪是否可放大缩小图片
                    .videoQuality(1)// 视频录制质量 0 or 1
                    //.videoSecond()//显示多少秒以内的视频or音频也可适用
                    .recordVideoSecond(30)//录制视频秒数 默认60s

                    .forResult(PictureConfig.REQUEST_CAMERA);//结果回调onActivityResult code

        }

    }

    /**
     * select file
     */
    protected void selectFileFromLocal() {
        Intent intent  = null;
        if (Build.VERSION.SDK_INT < 19) { //api 19 and later, we can't use this way, demo just select from images
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }

    /**
     * make a voice call
     */
    protected void startVoiceCall() {
        if (!EMClient.getInstance().isConnected()) {
            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(getActivity(), VoiceCallActivity.class).putExtra("username", toChatUsername)
                    .putExtra("isComingCall", false));
            // voiceCallBtn.setEnabled(false);
            inputMenu.hideExtendMenuContainer();
        }
    }

    /**
     * make a video call
     */
    protected void startVideoCall() {
        if (!EMClient.getInstance().isConnected())
            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
        else {
            startActivity(new Intent(getActivity(), VideoCallActivity.class).putExtra("username", toChatUsername)
                    .putExtra("isComingCall", false));
            // videoCallBtn.setEnabled(false);
            inputMenu.hideExtendMenuContainer();
        }
    }

    /**
     * chat row provider
     */
    private final class CustomChatRowProvider implements EaseCustomChatRowProvider {
        @Override
        public int getCustomChatRowTypeCount() {
            //here the number is the message type in EMMessage::Type
            //which is used to count the number of different chat row
            return 13;
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {
            if (message.getType() == EMMessage.Type.TXT) {
                //语言通话类型
                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL : MESSAGE_TYPE_SENT_VOICE_CALL;
                } else if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                    //视频类型
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO_CALL : MESSAGE_TYPE_SENT_VIDEO_CALL;
                }
                //messagee recall
                else if (message.getBooleanAttribute(Constant.MESSAGE_TYPE_RECALL, false)) {
                    return MESSAGE_TYPE_RECALL;

                    //发送名片
                } else if (DemoHelper.getInstance().isMingPianTexteMessage(message)) {
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_MINGPIAN_CALL : MESSAGE_TYPE_SENT_MINGPIAN_CALL;
                } else if (DemoHelper.getInstance().isShareTexteMessage(message)) {
                    //发送分享
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_SHARE_CALL : MESSAGE_TYPE_SENT_SHARE_CALL;
                }
            }
            return 0;
        }

        @Override
        public EaseChatRow getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
            if (message.getType() == EMMessage.Type.TXT) {
                // 音频视频的
                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false) ||
                        message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                    return new ChatRowVoiceCall(getActivity(), message, position, adapter);
                }
                //recall message
                else if (message.getBooleanAttribute(Constant.MESSAGE_TYPE_RECALL, false)) {
                    return new EaseChatRowRecall(getActivity(), message, position, adapter);
                }
                //发送名片
                if (DemoHelper.getInstance().isMingPianTexteMessage(message)) {
                    return new MingPianCharRow(getActivity(), message, position, adapter);
                }
                //发送分享
                if (DemoHelper.getInstance().isShareTexteMessage(message)) {
                    return new GameShareCharRow(getActivity(), message, position, adapter);
                }

            }
            return null;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(myReceiver!=null){
            getActivity().unregisterReceiver(myReceiver);
        }

    }

    /****
     * 接受预览后直接发送视频
     */
    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
           String action =  intent.getAction();
           int duration = 0;
           if("videoPreviewOK".equals(action)){

              String videoPath = intent.getStringExtra("path");
              String durationStr = intent.getStringExtra("time");
              duration = Integer.parseInt(durationStr);

               //修改发送视频是
               File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
               //File file =createFile();

               try {
                   FileOutputStream fos = new FileOutputStream(file);
                   Bitmap ThumbBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
                   ThumbBitmap.compress(CompressFormat.JPEG, 100, fos);
                   // Log.i("bitmaps",ThumbBitmap+"    ?");
                   fos.close();
                   sendVideoMessage(videoPath, file.getAbsolutePath(), duration);
                   Log.i("ui","123465");
               } catch (Exception e) {
                   e.printStackTrace();
               }

           }
        }
    }




}
