package com.zl.vo_;

import android.app.Activity;
import android.app.NotificationChannel;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMMultiDeviceListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.chat.EMMessage.Status;
import com.hyphenate.chat.EMMessage.Type;
import com.hyphenate.chat.EMMucSharedFile;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;

import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.EaseUI.EaseSettingsProvider;
import com.hyphenate.easeui.EaseUI.EaseUserProfileProvider;
import com.hyphenate.easeui.domain.EaseAvatarOptions;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.easeui.model.EaseNotifier.EaseNotificationInfoProvider;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;
import com.zl.vo_.db.DemoDBManager;
import com.zl.vo_.db.InviteMessgeDao;
import com.zl.vo_.db.UserDao;
import com.zl.vo_.domain.InviteMessage;
import com.zl.vo_.domain.RobotUser;
import com.zl.vo_.httpUtils.UserProfileManager;
import com.zl.vo_.main.Entity.GruoupContactData;
import com.zl.vo_.main.Entity.LoginData;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.Entity.UserInfoEntity;
import com.zl.vo_.main.activities.ChatActivity;
import com.zl.vo_.main.activities.MyFrindEntivity;
import com.zl.vo_.main.activities.UserDetailsActivityVo;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.MinPianConstant;
import com.zl.vo_.main.main_utils.ShareConstant;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.receiver.CallReceiver;
import com.zl.vo_.ui.VideoCallActivity;
import com.zl.vo_.ui.VoiceCallActivity;
import com.zl.vo_.utils.PreferenceManager;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DemoHelper {
    //数据同步监听接口
    public interface DataSyncListener {
        //同步完成的方法
        void onSyncComplete(boolean success);
    }
    protected static final String TAG = "DemoHelper";
    private EaseUI easeUI;
    //消息监听器
    protected EMMessageListener messageListener = null;
    //联系人集合-map
    private Map<String, EaseUser> contactList;
    //机器人集合-map
    private Map<String, RobotUser> robotList;
    //当前用户的配置管理者
    private UserProfileManager userProManager;
    //DemoHelper的实例
    private static DemoHelper instance = null;
    //DemeMode实例
    private DemoModel demoModel = null;
    public EaseUser userFromMine;
    public static int textnum = 0;
    //同步群组的监听数据
    private List<DataSyncListener> syncGroupsListeners;
    //同步联系人的监听集合
    private List<DataSyncListener> syncContactsListeners;
    //同步黑名单监听集合
    private List<DataSyncListener> syncBlackListListeners;
    //是否从服务器上同步群组
    private boolean isSyncingGroupsWithServer = false;
    //是否从服务器上同步联系人
    private boolean isSyncingContactsWithServer = false;
    //是否从服务器上同步黑名单
    private boolean isSyncingBlackListWithServer = false;
    //是否完成从服务器上同步群组
    private boolean isGroupsSyncedWithServer = false;
    //是否完成从服务器上同步联系人
    private boolean isContactsSyncedWithServer = false;
    //是否完成从服务器上同步黑名单
    private boolean isBlackListSyncedWithServer = false;
    //音频聊天
    public boolean isVoiceCalling;
    //视频聊天
    public boolean isVideoCalling;
    //当前用户的名称
    private String username;
    //当前上下文
    private Context appContext;
    //音视频广播接受者
    private CallReceiver callReceiver;
    //处理消息的对象
    private InviteMessgeDao inviteMessgeDao;
    //处理用户的对象
    private UserDao userDao;
    private LocalBroadcastManager broadcastManager;
    //是否群组和黑名单的监听器已经注册
    private boolean isGroupAndContactListenerRegisted;
    private ExecutorService executor;
    protected android.os.Handler handler;
    Queue<String> msgQueue = new ConcurrentLinkedQueue<>();
    private DemoHelper() {
        executor = Executors.newCachedThreadPool();
    }
    public synchronized static DemoHelper getInstance() {
        if (instance == null) {
            instance = new DemoHelper();
        }
        return instance;
    }
    public void execute(Runnable runnable) {
        executor.execute(runnable);
    }
    public void init(Context context) {
        //创建DemoHelper的对象
        demoModel = new DemoModel(context);
        //设置群加我时需要验证
        demoModel.setAutoAcceptGroupInvitation(false);
        //实例化聊天的选项
        EMOptions options = initChatOptions();
//        options.setRestServer("118.193.28.212:31080");
//        options.setIMServer("118.193.28.212");
//        options.setImPort(31097);
        //use default options if options is null
        //初始化EaseUI
        if (EaseUI.getInstance().init(context, options)) {
            appContext = context;
            //debug mode, you'd better set it to false, if you want release your App officially.
            EMClient.getInstance().setDebugMode(true);
            //get easeui instance
            easeUI = EaseUI.getInstance();
            //to set user's profile and avatar
            setEaseUIProviders();
            //initialize preference manager
            PreferenceManager.init(context);
            //initialize profile manager
            getUserProfileManager().init(context);
            //set Call options
            setCallOptions();

            // TODO: set Call options
            // min video kbps
            int minBitRate = PreferenceManager.getInstance().getCallMinVideoKbps();
            if (minBitRate != -1) {
                EMClient.getInstance().callManager().getCallOptions().setMinVideoKbps(minBitRate);
            }
            // max video kbps
            int maxBitRate = PreferenceManager.getInstance().getCallMaxVideoKbps();
            if (maxBitRate != -1) {
                EMClient.getInstance().callManager().getCallOptions().setMaxVideoKbps(maxBitRate);
            }
            // max frame rate
            int maxFrameRate = PreferenceManager.getInstance().getCallMaxFrameRate();
            if (maxFrameRate != -1) {
                EMClient.getInstance().callManager().getCallOptions().setMaxVideoFrameRate(maxFrameRate);
            }

            // audio sample rate
            int audioSampleRate = PreferenceManager.getInstance().getCallAudioSampleRate();
            if (audioSampleRate != -1) {
                EMClient.getInstance().callManager().getCallOptions().setAudioSampleRate(audioSampleRate);
            }

            /**
             * This function is only meaningful when your app need recording
             * If not, remove it.
             * This function need be called before the video stream started, so we set it in onCreate function.
             * This method will set the preferred video record encoding codec.
             * Using default encoding format, recorded file may not be played by mobile player.
             */
            //EMClient.getInstance().callManager().getVideoCallHelper().setPreferMovFormatEnable(true);

            // resolution
            String resolution = PreferenceManager.getInstance().getCallBackCameraResolution();
            if (resolution.equals("")) {
                resolution = PreferenceManager.getInstance().getCallFrontCameraResolution();
            }
            String[] wh = resolution.split("x");
            if (wh.length == 2) {
                try {
                    EMClient.getInstance().callManager().getCallOptions().setVideoResolution(new Integer(wh[0]).intValue(), new Integer(wh[1]).intValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // enabled fixed sample rate
            boolean enableFixSampleRate = PreferenceManager.getInstance().isCallFixedVideoResolution();
            EMClient.getInstance().callManager().getCallOptions().enableFixedVideoResolution(enableFixSampleRate);

            // Offline call push
            EMClient.getInstance().callManager().getCallOptions().setIsSendPushIfOffline(getModel().isPushCall());

            setGlobalListeners();
            broadcastManager = LocalBroadcastManager.getInstance(appContext);
            initDbDao();

        }
    }
    private EMOptions initChatOptions() {

        EMOptions options = new EMOptions();
        // set if accept the invitation automatically
        options.setAcceptInvitationAlways(false);
        // set if you need read ack
        options.setRequireAck(true);
        // set if you need delivery ack
        options.setRequireDeliveryAck(false);
        //设置自动接收群组邀请
        options.setAutoAcceptGroupInvitation(true);
        //you need apply & set your own id if you want to use google cloud messaging.
        //options.setGCMNumber("769764571397");
        options.setFCMNumber("769764571397");
        //设置小米推送配置
        options.setMipushConfig("2882303761517839521","5871783979521");

        //set custom servers, commonly used in private deployment
        if (demoModel.isCustomServerEnable() && demoModel.getRestServer() != null && demoModel.getIMServer() != null) {
            options.setRestServer(demoModel.getRestServer());
            options.setIMServer(demoModel.getIMServer());
            if (demoModel.getIMServer().contains(":")) {
                options.setIMServer(demoModel.getIMServer().split(":")[0]);
                options.setImPort(Integer.valueOf(demoModel.getIMServer().split(":")[1]));
            }
        }

        if (demoModel.isCustomAppkeyEnabled() && demoModel.getCutomAppkey() != null && !demoModel.getCutomAppkey().isEmpty()) {
            options.setAppKey(demoModel.getCutomAppkey());
        }

        options.allowChatroomOwnerLeave(getModel().isChatroomOwnerLeaveAllowed());
        options.setDeleteMessagesAsExitGroup(getModel().isDeleteMessagesAsExitGroup());
        options.setAutoAcceptGroupInvitation(getModel().isAutoAcceptGroupInvitation());

        return options;
    }

    /****
     * 设置音视频的回调
     */
    private void setCallOptions() {
        // min video kbps
        int minBitRate = PreferenceManager.getInstance().getCallMinVideoKbps();
        if (minBitRate != -1) {
            EMClient.getInstance().callManager().getCallOptions().setMinVideoKbps(minBitRate);
        }

        // max video kbps
        int maxBitRate = PreferenceManager.getInstance().getCallMaxVideoKbps();
        if (maxBitRate != -1) {
            EMClient.getInstance().callManager().getCallOptions().setMaxVideoKbps(maxBitRate);
        }

        // max frame rate
        int maxFrameRate = PreferenceManager.getInstance().getCallMaxFrameRate();
        if (maxFrameRate != -1) {
            EMClient.getInstance().callManager().getCallOptions().setMaxVideoFrameRate(maxFrameRate);
        }

        // audio sample rate
        int audioSampleRate = PreferenceManager.getInstance().getCallAudioSampleRate();
        if (audioSampleRate != -1) {
            EMClient.getInstance().callManager().getCallOptions().setAudioSampleRate(audioSampleRate);
        }

        /**
         * This function is only meaningful when your app need recording
         * If not, remove it.
         * This function need be called before the video stream started, so we set it in onCreate function.
         * This method will set the preferred video record encoding codec.
         * Using default encoding format, recorded file may not be played by mobile player.
         */
        //EMClient.getInstance().callManager().getVideoCallHelper().setPreferMovFormatEnable(true);
        // resolution
        String resolution = PreferenceManager.getInstance().getCallBackCameraResolution();
        if (resolution.equals("")) {
            resolution = PreferenceManager.getInstance().getCallFrontCameraResolution();
        }
        String[] wh = resolution.split("x");
        if (wh.length == 2) {
            try {
                EMClient.getInstance().callManager().getCallOptions().setVideoResolution(new Integer(wh[0]).intValue(), new Integer(wh[1]).intValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // enabled fixed sample rate
        boolean enableFixSampleRate = PreferenceManager.getInstance().isCallFixedVideoResolution();
        EMClient.getInstance().callManager().getCallOptions().enableFixedVideoResolution(enableFixSampleRate);

        // Offline call push
        EMClient.getInstance().callManager().getCallOptions().setIsSendPushIfOffline(getModel().isPushCall());
    }

    /*****
     * 设置EaseUI
     */
    protected void setEaseUIProviders() {
        //set user avatar to circle shape
        EaseAvatarOptions avatarOptions = new EaseAvatarOptions();
        avatarOptions.setAvatarShape(1);
        easeUI.setAvatarOptions(avatarOptions);

        // set profile provider if you want easeUI to handle avatar and nickname
        easeUI.setUserProfileProvider(new EaseUserProfileProvider() {

            @Override
            public EaseUser getUser(String username) {
                return getUserInfo(username);
            }
        });

        //set options 
        easeUI.setSettingsProvider(new EaseSettingsProvider() {

            @Override
            public boolean isSpeakerOpened() {
                //听筒
                // Toast.makeText(appContext, "demohelper+==听筒初始化： "+demoModel.getSettingMsgSpeaker(), Toast.LENGTH_SHORT).show();
                Log.i("ysq==", "demohelp的  " + demoModel.getSettingMsgSpeaker());
                return demoModel.getSettingMsgSpeaker();
            }

            @Override
            public boolean isMsgVibrateAllowed(EMMessage message) {
                return demoModel.getSettingMsgVibrate();
            }

            @Override
            public boolean isMsgSoundAllowed(EMMessage message) {
                return demoModel.getSettingMsgSound();
            }

            @Override
            public boolean isMsgNotifyAllowed(EMMessage message) {
                if (message == null) {
                    return demoModel.getSettingMsgNotification();
                }
                if (!demoModel.getSettingMsgNotification()) {
                    return false;
                } else {
                    String chatUsename = null;
                    List<String> notNotifyIds = null;
                    // get user or group id which was blocked to show message notifications
                    if (message.getChatType() == ChatType.Chat) {
                        chatUsename = message.getFrom();
                        notNotifyIds = demoModel.getDisabledIds();
                    } else {
                        chatUsename = message.getTo();
                        notNotifyIds = demoModel.getDisabledGroups();
                    }

                    if (notNotifyIds == null || !notNotifyIds.contains(chatUsename)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }

        });
        /*****
         * 设置表情
         */
        //set emoji icon provider
//        easeUI.setEmojiconInfoProvider(new EaseEmojiconInfoProvider() {
//
//            @Override
//            public EaseEmojicon getEmojiconInfo(String emojiconIdentityCode) {
//                EaseEmojiconGroupEntity data = EmojiconExampleGroupData.getData();
//                for(EaseEmojicon emojicon : data.getEmojiconList()){


//                    if(emojicon.getIdentityCode().equals(emojiconIdentityCode)){
//                        return emojicon;
//                    }
//                }
//                return null;
//            }
//
//            @Override
//            public Map<String, Object> getTextEmojiconMapping() {
//                return null;
//            }
//        });

        //set notification options, will use default if you don't set it
        easeUI.getNotifier().setNotificationInfoProvider(new EaseNotificationInfoProvider() {

            @Override
            public String getTitle(EMMessage message) {
                //you can update title here
                return null;
            }

            @Override
            public int getSmallIcon(EMMessage message) {
                //you can update icon here
                return 0;
            }

            @Override
            public String getDisplayedText(EMMessage message) {
                // be used on notification bar, different text according the message type.
                String ticker = EaseCommonUtils.getMessageDigest(message, appContext);
                if (message.getType() == Type.TXT) {
                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                }
                EaseUser user = getUserInfo(message.getFrom());
                if (user != null) {
                    if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
                        return String.format(appContext.getString(R.string.at_your_in_group), user.getNick());
                    }
                    return user.getNick() + ": " + ticker;
                } else {
                    if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
                        return String.format(appContext.getString(R.string.at_your_in_group), message.getFrom());
                    }
                    return message.getFrom() + ": " + ticker;
                }
            }

            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                // here you can customize the text.
                // return fromUsersNum + "contacts send " + messageNum + "messages to you";
                return null;
            }

            @Override
            public Intent getLaunchIntent(EMMessage message) {
                // you can set what activity you want display when user click the notification
                Intent intent = new Intent(appContext, ChatActivity.class);
                // open calling activity if there is call
                if (isVideoCalling) {
                    intent = new Intent(appContext, VideoCallActivity.class);
                } else if (isVoiceCalling) {
                    intent = new Intent(appContext, VoiceCallActivity.class);
                } else {
                    ChatType chatType = message.getChatType();
                    if (chatType == ChatType.Chat) { // single chat message
                        intent.putExtra("userId", message.getFrom());
                        intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);
                    } else { // group chat message
                        // message.getTo() is the group id
                        intent.putExtra("userId", message.getTo());
                        if (chatType == ChatType.GroupChat) {
                            intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
                        } else {
                            intent.putExtra("chatType", Constant.CHATTYPE_CHATROOM);
                        }

                    }
                }
                return intent;
            }
        });
    }

    EMConnectionListener connectionListener;

    /**
     * set global listener
     */
    protected void setGlobalListeners() {
        syncGroupsListeners = new ArrayList<>();
        syncContactsListeners = new ArrayList<>();
        syncBlackListListeners = new ArrayList<>();

        isGroupsSyncedWithServer = demoModel.isGroupsSynced();
        isContactsSyncedWithServer = demoModel.isContactSynced();
        isBlackListSyncedWithServer = demoModel.isBacklistSynced();

        // create the global connection listener
        //全局连接监听器
        connectionListener = new EMConnectionListener() {
            @Override
            public void onDisconnected(int error) {
                EMLog.d("global listener", "onDisconnect" + error);
                if (error == EMError.USER_REMOVED) {
                    //用户被移除
                    onUserException(Constant.ACCOUNT_REMOVED);
                } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    //用户登录其他设备
                    onUserException(Constant.ACCOUNT_CONFLICT);
                } else if (error == EMError.SERVER_SERVICE_RESTRICTED) {
                    //账号被禁用
                    onUserException(Constant.ACCOUNT_FORBIDDEN);
                } else if (error == EMError.USER_KICKED_BY_CHANGE_PASSWORD) {
                    //账号被踢应为密码改变
                    onUserException(Constant.ACCOUNT_KICKED_BY_CHANGE_PASSWORD);
                } else if (error == EMError.USER_KICKED_BY_OTHER_DEVICE) {
                    //用户被踢应为其他设备登录
                    onUserException(Constant.ACCOUNT_KICKED_BY_OTHER_DEVICE);
                }
            }

            @Override
            public void onConnected() {
                // in case group and contact were already synced,
                // we supposed to notify sdk we are ready to receive the events
                if (isGroupsSyncedWithServer && isContactsSyncedWithServer) {
                    EMLog.d(TAG, "group and contact already synced with servre");
                } else {
                    if (!isGroupsSyncedWithServer) {
                        //异步从服务器上获取群组
                        asyncFetchGroupsFromServer(null);
                    }

                    if (!isContactsSyncedWithServer) {
                        //异步从服务器上火联系人
                        asyncFetchContactsFromServer(null);
                    }

                    if (!isBlackListSyncedWithServer) {
                        //异步从服务器上获取黑名单
                        asyncFetchBlackListFromServer(null);
                    }
                }
            }
        };

        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        if (callReceiver == null) {
            callReceiver = new CallReceiver();
        }

        //register incoming call receiver
        appContext.registerReceiver(callReceiver, callFilter);
        //register connection listener
        //添加链接监听设备
        EMClient.getInstance().addConnectionListener(connectionListener);
        //register group and contact event listener
        //注册群组和联系人监听
        registerGroupAndContactListener();
        //register message event listener
        //注册消息监听
        registerMessageListener();

    }

    private void initDbDao() {
        inviteMessgeDao = new InviteMessgeDao(appContext);
        userDao = new UserDao(appContext);
    }

    /**
     * register group and contact listener, you need register when login
     * 当登录后注册群组和联系人监听
     */
    public void registerGroupAndContactListener() {
        if (!isGroupAndContactListenerRegisted) {
            EMClient.getInstance().groupManager().addGroupChangeListener(new MyGroupChangeListener());
            EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
            EMClient.getInstance().addMultiDeviceListener(new MyMultiDeviceListener());
            isGroupAndContactListenerRegisted = true;
        }

    }

    /**
     * group change listener
     * 群组改变监听
     */
    class MyGroupChangeListener implements EMGroupChangeListener {

        @Override
        public void onInvitationReceived(final String groupId, final String groupName, final String inviter, final String reason) {

            //根据groupId,从自己的服务器上查询这个组的信息赋值给message

            RequestParams params = new RequestParams(Url.GroupInvitationUrl);
            params.addParameter("group_id", groupId);
            params.addParameter("huanxin_account", inviter);
            x.http().post(params, new MyCommonCallback<Result<GruoupContactData>>() {
                @Override
                public void success(Result<GruoupContactData> data) {
                    if ("0".equals(data.code)) {
                        GruoupContactData data_ = data.data;
                        GruoupContactData.GruoupContactInfo info = data_.getInfo();
                        GruoupContactData.GruoupContactInfo.ContactInfo contactInfo = info.getAccount_info();
                        GruoupContactData.GruoupContactInfo.GroupInfo groupInfo = info.getGroup_info();
                        new InviteMessgeDao(appContext).deleteMessage(groupId);
                        // user invite you to join group
                        InviteMessage msg = new InviteMessage();
                        msg.setFrom(groupId);
                        msg.setGroup_desc(groupInfo.getDescription());
                        msg.setTime(System.currentTimeMillis());
                        msg.setGroupId(groupId);
                        msg.setGroupName(groupInfo.getName());
                        msg.setReason(reason);
                        msg.setGroupInviter(contactInfo.getNickname());
                        showToast("receive invitation to join the group：" + groupName);
                        msg.setStatus(InviteMessage.InviteMessageStatus.GROUPINVITATION);
                        notifyNewInviteMessage(msg);
                        //发送广播
                        broadcastManager.sendBroadcast(new Intent(Constant.ACTION_GROUP_CHANAGED));
                        appContext.sendBroadcast(new Intent("onContactInvited"));
                    }
                }

                @Override
                public void error(Throwable ex, boolean isOnCallback) {
                    Log.i("onInvitationReceived", ex.getMessage());
                }
            });


        }

        @Override
        public void onInvitationAccepted(final String groupId, final String invitee, final String reason) {
            //invitee 环信账号 也就是谁同意了你
            RequestParams params = new RequestParams(Url.FriendInfoURL);
            params.addParameter("huanxin_account", myUtils.readUser(appContext).getHuanxin_account());
            params.addParameter("friend_huanxin_account", invitee);
            x.http().post(params, new MyCommonCallback<Result<UserInfoEntity>>() {
                @Override
                public void success(Result<UserInfoEntity> data) {
                    UserInfoEntity userInfoEntity = data.data;
                    UserInfoEntity.UserInfo userInfo = userInfoEntity.getInfo();
                    if (userInfo != null) {
                        new InviteMessgeDao(appContext).deleteMessage(groupId);

                        //user accept your invitation
                        //用户接受了你的邀请
                        boolean hasGroup = false;
                        EMGroup _group = null;
                        for (EMGroup group : EMClient.getInstance().groupManager().getAllGroups()) {
                            if (group.getGroupId().equals(groupId)) {
                                hasGroup = true;
                                _group = group;
                                break;
                            }
                        }
                        if (!hasGroup)
                            return;

                        InviteMessage msg = new InviteMessage();
                        msg.setFrom(groupId);
                        msg.setTime(System.currentTimeMillis());
                        msg.setGroupId(groupId);
                        msg.setGroupName(_group == null ? groupId : _group.getGroupName());
                        msg.setReason(reason);
                        msg.setGroup_desc(_group.getDescription());
                        msg.setGroupInviter(userInfo.getFriend_info().getNickname());
                        showToast(invitee + "Accept to join the group：" + _group == null ? groupId : _group.getGroupName());
                        msg.setStatus(InviteMessage.InviteMessageStatus.GROUPINVITATION_ACCEPTED);
                        //将邀请信息保存在数据库中
                        notifyNewInviteMessage(msg);
                        //发送广播
                        broadcastManager.sendBroadcast(new Intent(Constant.ACTION_GROUP_CHANAGED));
                        appContext.sendBroadcast(new Intent("onContactInvited"));

                    }

                }

                @Override
                public void error(Throwable ex, boolean isOnCallback) {
                    Log.i("ui", ex.getMessage());
                }
            });

        }

        @Override
        public void onInvitationDeclined(final String groupId, String invitee, final String reason) {

            //根据groupId,从自己的服务器上查询这个组的信息赋值给message

            RequestParams params = new RequestParams(Url.GroupInvitationUrl);
            params.addParameter("group_id", groupId);
            params.addParameter("huanxin_account", invitee);
            x.http().post(params, new MyCommonCallback<Result<GruoupContactData>>() {
                @Override
                public void success(Result<GruoupContactData> data) {
                    if ("0".equals(data.code)) {
                        GruoupContactData data_ = data.data;
                        GruoupContactData.GruoupContactInfo info = data_.getInfo();
                        GruoupContactData.GruoupContactInfo.ContactInfo contactInfo = info.getAccount_info();
                        GruoupContactData.GruoupContactInfo.GroupInfo groupInfo = info.getGroup_info();
                        new InviteMessgeDao(appContext).deleteMessage(groupId);
                        // user invite you to join group
                        InviteMessage msg = new InviteMessage();
                        msg.setFrom(groupId);
                        msg.setTime(System.currentTimeMillis());
                        msg.setGroupId(groupId);
                        msg.setGroup_desc(groupInfo.getDescription());
                        msg.setGroupName(groupInfo.getName());
                        msg.setReason(reason);
                        msg.setGroupInviter(contactInfo.getNickname());
                        showToast("receive invitation to join the group：" + groupInfo.getName());
                        msg.setStatus(InviteMessage.InviteMessageStatus.GROUPINVITATION);
                        notifyNewInviteMessage(msg);
                        //发送广播
                        broadcastManager.sendBroadcast(new Intent(Constant.ACTION_GROUP_CHANAGED));
                    }
                }

                @Override
                public void error(Throwable ex, boolean isOnCallback) {
                    Log.i("onInvitationReceived", ex.getMessage());
                }
            });


//
//            new InviteMessgeDao(appContext).deleteMessage(groupId);
//
//            //user declined your invitation
//            //用户拒绝了你的邀请
//            EMGroup group = null;
//            for (EMGroup _group : EMClient.getInstance().groupManager().getAllGroups()) {
//                if (_group.getGroupId().equals(groupId)) {
//                    group = _group;
//                    break;
//                }
//            }
//            if (group == null)
//                return;
//
//            InviteMessage msg = new InviteMessage();
//            msg.setFrom(groupId);
//            msg.setTime(System.currentTimeMillis());
//            msg.setGroupId(groupId);
//            msg.setGroupName(group.getGroupName());
//            msg.setReason(reason);
//            msg.setGroupInviter(invitee);
//            showToast(invitee + "Declined to join the group：" + group.getGroupName());
//            msg.setStatus(InviteMessage.InviteMessageStatus.GROUPINVITATION_DECLINED);
//            //将信息保存进数据库
//            notifyNewInviteMessage(msg);
//            //发送广播
//            broadcastManager.sendBroadcast(new Intent(Constant.ACTION_GROUP_CHANAGED));
        }

        @Override
        public void onUserRemoved(String groupId, String groupName) {
            //user is removed from group
            //用户从群组里移除
            //发送广播
            broadcastManager.sendBroadcast(new Intent(Constant.ACTION_GROUP_CHANAGED));
            showToast("current user removed, groupId:" + groupId);
        }

        @Override
        public void onGroupDestroyed(String groupId, String groupName) {
            // group is dismissed,
            //群组别销毁
            broadcastManager.sendBroadcast(new Intent(Constant.ACTION_GROUP_CHANAGED));
            showToast("group destroyed, groupId:" + groupId);
        }

        @Override
        public void onRequestToJoinReceived(final String groupId, String groupName, String applyer, final String reason) {

            RequestParams params = new RequestParams(Url.GroupInvitationUrl);
            params.addParameter("group_id", groupId);
            params.addParameter("huanxin_account", applyer);
            x.http().post(params, new MyCommonCallback<Result<GruoupContactData>>() {
                @Override
                public void success(Result<GruoupContactData> data) {
                    if ("0".equals(data.code)) {
                        GruoupContactData data_ = data.data;
                        GruoupContactData.GruoupContactInfo info = data_.getInfo();
                        GruoupContactData.GruoupContactInfo.ContactInfo contactInfo = info.getAccount_info();
                        GruoupContactData.GruoupContactInfo.GroupInfo groupInfo = info.getGroup_info();
                        //*****************************

                        // user apply to join group
                        //用户申请加入群组
                        InviteMessage msg = new InviteMessage();
                        msg.setFrom(contactInfo.getNickname());
                        msg.setTime(System.currentTimeMillis());
                        msg.setGroupId(groupId);
                        msg.setGroupName(groupInfo.getName());
                        msg.setReason(reason);
                        msg.setGroup_desc(groupInfo.getDescription());
                        showToast(contactInfo.getNickname() + " Apply to join group：" + groupInfo.getName());
                        msg.setStatus(InviteMessage.InviteMessageStatus.BEAPPLYED);
                        //保存进数据库
                        notifyNewInviteMessage(msg);
                        //发送广播
                        broadcastManager.sendBroadcast(new Intent(Constant.ACTION_GROUP_CHANAGED));
                    }
                }

                @Override
                public void error(Throwable ex, boolean isOnCallback) {
                    Log.i("onInvitationReceived", ex.getMessage());
                }
            });


        }

        @Override
        public void onRequestToJoinAccepted(String groupId, String groupName, String accepter) {

            RequestParams params = new RequestParams(Url.GroupInvitationUrl);
            params.addParameter("group_id", groupId);
            params.addParameter("huanxin_account", accepter);
            x.http().post(params, new MyCommonCallback<Result<GruoupContactData>>() {
                @Override
                public void success(Result<GruoupContactData> data) {
                    if ("0".equals(data.code)) {
                        GruoupContactData data_ = data.data;
                        GruoupContactData.GruoupContactInfo info = data_.getInfo();
                        GruoupContactData.GruoupContactInfo.ContactInfo contactInfo = info.getAccount_info();
                        GruoupContactData.GruoupContactInfo.GroupInfo groupInfo = info.getGroup_info();
                        //*****************************


                        //你的申请被接收  同意了你的群聊申请
                        String st4 = appContext.getString(R.string.Agreed_to_your_group_chat_application);
                        // your application was accepted
                        EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
                        msg.setChatType(ChatType.GroupChat);
                        msg.setFrom(contactInfo.getNickname());
                        msg.setTo(groupInfo.getName());

                        msg.setMsgId(UUID.randomUUID().toString());
                        msg.addBody(new EMTextMessageBody(contactInfo.getNickname() + " " + st4));
                        msg.setStatus(Status.SUCCESS);
                        // save accept message
                        EMClient.getInstance().chatManager().saveMessage(msg);
                        // notify the accept message
                        getNotifier().vibrateAndPlayTone(msg);

                        showToast("request to join accepted, groupId:" + groupInfo.getName());
                        //发送广播
                        broadcastManager.sendBroadcast(new Intent(Constant.ACTION_GROUP_CHANAGED));
                    }
                }

                @Override
                public void error(Throwable ex, boolean isOnCallback) {
                    Log.i("onInvitationReceived", ex.getMessage());
                }
            });


        }

        @Override
        public void onRequestToJoinDeclined(String groupId, String groupName, String decliner, String reason) {
            // your application was declined, we do nothing here in demo
            //你的申请被拒绝 demo中什么也不做
            showToast("request to join declined, groupId:" + groupId);
        }

        @Override
        public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {
            // got an invitation
            //自动接收从群里的邀请
            String st3 = appContext.getString(R.string.Invite_you_to_join_a_group_chat);
            EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
            msg.setChatType(ChatType.GroupChat);
            msg.setFrom(inviter);
            msg.setTo(groupId);
            msg.setMsgId(UUID.randomUUID().toString());
            msg.addBody(new EMTextMessageBody(inviter + " " + st3));
            msg.setStatus(EMMessage.Status.SUCCESS);
            // save invitation as messages
            EMClient.getInstance().chatManager().saveMessage(msg);
            // notify invitation message
            getNotifier().vibrateAndPlayTone(msg);
            showToast("auto accept invitation from groupId:" + groupId);
            broadcastManager.sendBroadcast(new Intent(Constant.ACTION_GROUP_CHANAGED));
        }

        // ============================= group_reform new add api begin
        @Override
        public void onMuteListAdded(String groupId, final List<String> mutes, final long muteExpire) {
            StringBuilder sb = new StringBuilder();
            for (String member : mutes) {
                sb.append(member).append(",");
            }
            showToast("onMuterListAdded: " + sb.toString());
        }


        @Override
        public void onMuteListRemoved(String groupId, final List<String> mutes) {
            StringBuilder sb = new StringBuilder();
            for (String member : mutes) {
                sb.append(member).append(",");
            }
            showToast("onMuterListRemoved: " + sb.toString());
        }


        @Override
        public void onAdminAdded(String groupId, String administrator) {
            showToast("onAdminAdded: " + administrator);
        }

        @Override
        public void onAdminRemoved(String groupId, String administrator) {
            showToast("onAdminRemoved: " + administrator);
        }

        @Override
        public void onOwnerChanged(String groupId, String newOwner, String oldOwner) {
            showToast("onOwnerChanged new:" + newOwner + " old:" + oldOwner);
        }

        @Override
        public void onMemberJoined(String groupId, String member) {
            showToast("onMemberJoined: " + member);
        }

        @Override
        public void onMemberExited(String groupId, String member) {
            showToast("onMemberExited: " + member);
        }

        @Override
        public void onAnnouncementChanged(String groupId, String announcement) {
            showToast("onAnnouncementChanged, groupId" + groupId);
        }

        @Override
        public void onSharedFileAdded(String groupId, EMMucSharedFile sharedFile) {
            showToast("onSharedFileAdded, groupId" + groupId);
        }

        @Override
        public void onSharedFileDeleted(String groupId, String fileId) {
            showToast("onSharedFileDeleted, groupId" + groupId);
        }
        // ============================= group_reform new add api end
    }

    void showToast(final String message) {
        Log.d(TAG, "receive invitation to join the group：" + message);
        if (handler != null) {
            Message msg = Message.obtain(handler, 0, message);
            handler.sendMessage(msg);
        } else {
            msgQueue.add(message);
        }
    }

    public void initHandler(Looper looper) {
        handler = new android.os.Handler(looper) {
            @Override
            public void handleMessage(Message msg) {
                String str = (String) msg.obj;
                Toast.makeText(appContext, str, Toast.LENGTH_LONG).show();
            }
        };
        while (!msgQueue.isEmpty()) {
            showToast(msgQueue.remove());
        }
    }

    /***
     * 好友变化listener
     */
    public class MyContactListener implements EMContactListener {

        @Override
        public void onContactAdded(String username) {

            Log.i("abcd", "朋友添加");

            // save contact                    从数据库中获取联系人
            //当点击同意，从自己服务器上获取好友信息，添加到数据库并刷新列表
            // getFriendInfo(username);

           /* Map<String, EaseUser> localUsers = getContactList();
            Map<String, EaseUser> toAddUsers = new HashMap<String, EaseUser>();


            Log.i("oo","556");

            EaseUser user = new EaseUser(username);
            //如果本地没有这个用户，就添加
            if (!localUsers.containsKey(username)) {

                userDao.saveContact(user);
            }
            toAddUsers.put(username, user);
            localUsers.putAll(toAddUsers);
            //发送广播
            broadcastManager.sendBroadcast(new Intent(Constant.ACTION_CONTACT_CHANAGED));
            showToast("onContactAdded:" + username);*/
        }

        @Override
        public void onContactDeleted(String username) {
            DemoDBManager demoDBManager = DemoDBManager.getInstance();
            MyFrindEntivity frindEntivity = demoDBManager.selectMyFrind(username);
            //联系人被删除
            Map<String, EaseUser> localUsers = DemoHelper.getInstance().getContactList();
            localUsers.remove(username);
            userDao.deleteContact(username);
            //同时删除来自这个人的信息
            inviteMessgeDao.deleteMessage(username);
            //同时删除这个人的会话记录
            EMClient.getInstance().chatManager().deleteConversation(username, true);
            //发送广播
            broadcastManager.sendBroadcast(new Intent(Constant.ACTION_CONTACT_CHANAGED));
            if (frindEntivity != null) {
                showToast("onContactDeleted:" + frindEntivity.getRemark());
            }
            //showToast("onContactDeleted:" + username);
            //当朋友删除你的时候，发送广播，刷新数据
            appContext.sendBroadcast(new Intent("onContactInvited"));


        }

        @Override
        public void onContactInvited(final String username, final String reason) {
            final String[] ressonArr = reason.split("：");
            Log.i("abcd", "收到朋友邀请");
            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
            //联系人被邀请 遍历邀请信息  如果有这个人的邀请 就删除这个人的申请
            for (InviteMessage inviteMessage : msgs) {
                if (inviteMessage.getGroupId() == null && username.equals(inviteMessage.getFrom())) {
                    inviteMessgeDao.deleteMessage(username);
                }
            }
            RequestParams params = new RequestParams(Url.FriendInfoURL);
            params.addParameter("huanxin_account", myUtils.readUser(appContext).getHuanxin_account());
            params.addParameter("friend_huanxin_account", username);
            x.http().post(params, new MyCommonCallback<Result<UserInfoEntity>>() {
                @Override
                public void success(Result<UserInfoEntity> data) {
                    UserInfoEntity userInfoEntity = data.data;
                    UserInfoEntity.UserInfo userInfo = userInfoEntity.getInfo();
                    if (userInfo != null) {
                        UserInfoEntity.UserInfo.UserFriendInfo friend_info = userInfo.getFriend_info();

                        // save invitation as message
                        //保存邀请信息
                        InviteMessage msg = new InviteMessage();
                        msg.setFrom(username);
                        msg.setTime(System.currentTimeMillis());
                        msg.setAvatar(friend_info.getAvatar());
                        msg.setNick(friend_info.getNickname());
                        msg.setReason(reason);
                        // showToast(friend_info.getNickname() + "apply to be your friend,reason: " + reason);
                        // set invitation status
                        msg.setStatus(InviteMessage.InviteMessageStatus.BEINVITEED);
                        //收到邀请 震动和播放声音
                        notifyNewInviteMessage(msg);
                        //发送广播，底部小红点
                        broadcastManager.sendBroadcast(new Intent(Constant.ACTION_CONTACT_CHANAGED));
                        appContext.sendBroadcast(new Intent("onContactInvited"));
                    }
                }

                @Override
                public void error(Throwable ex, boolean isOnCallback) {
                    Log.i("err", ex.getMessage());
                }
            });

        }

        @Override
        synchronized public void onFriendRequestAccepted(final String username) {

            //  getFriendInfo(username);
            //你的邀请被接收
            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
            for (InviteMessage inviteMessage : msgs) {
                if (inviteMessage.getFrom().equals(username)) {
                    return;
                }
            }

            RequestParams params = new RequestParams(Url.FriendInfoURL);
            params.addParameter("huanxin_account", myUtils.readUser(appContext).getHuanxin_account());
            params.addParameter("friend_huanxin_account", username);
            x.http().post(params, new MyCommonCallback<Result<UserInfoEntity>>() {
                @Override
                public void success(Result<UserInfoEntity> data) {
                    UserInfoEntity userInfoEntity = data.data;
                    UserInfoEntity.UserInfo userInfo = userInfoEntity.getInfo();
                    if (userInfo != null) {
                        UserInfoEntity.UserInfo.UserFriendInfo friend_info = userInfo.getFriend_info();

                        // save invitation as message
                        InviteMessage msg = new InviteMessage();
                        msg.setFrom(username);
                        msg.setTime(System.currentTimeMillis());
                        msg.setNick(friend_info.getNickname());
                        msg.setAvatar(friend_info.getAvatar());
                        showToast(friend_info.getNickname() + " accept your to be friend");
                        msg.setStatus(InviteMessage.InviteMessageStatus.BEAGREED);
                        //震动提示音
                        notifyNewInviteMessage(msg);
                        //发送广播，小红点
                        broadcastManager.sendBroadcast(new Intent(Constant.ACTION_CONTACT_CHANAGED));
                        appContext.sendBroadcast(new Intent("onContactInvited"));
                    }
                }

                @Override
                public void error(Throwable ex, boolean isOnCallback) {

                }
            });


        }

        @Override
        public void onFriendRequestDeclined(String username) {
            Log.i("abcd", "朋友拒绝");
            // your request was refused
            //你的邀请被拒绝
            showToast(username + " refused to be your friend");
        }
    }

    /*****
     * 从自己的服务器上火好友信息
     *
     * @param username
     * @return
     */
    private void getFriendInfo(String username) {
        RequestParams params = new RequestParams(Url.NativeAddFriendUrl);
        params.addParameter("huanxin_account", myUtils.readUser(appContext).getHuanxin_account());
        params.addParameter("friend_huanxin_account", username);
        x.http().post(params, new MyCommonCallback<Result<UserInfoEntity>>() {
            @Override
            public void success(Result<UserInfoEntity> data) {
                UserInfoEntity userInfoEntity = data.data;
                UserInfoEntity.UserInfo userInfo = userInfoEntity.getInfo();
                if (userInfo != null) {
                    UserInfoEntity.UserInfo.UserFriendInfo friend_info = userInfo.getFriend_info();

                }
            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {

            }
        });


    }

    /****
     * 保存根据环信id从自己服务上获取的好友
     *
     * @param friend_info
     */
    private void saveMyContact(UserInfoEntity.UserInfo.UserFriendInfo friend_info) {


        try {

            MyFrindEntivity friend = new MyFrindEntivity();
            friend.setAccount(friend_info.getAccount());
            friend.setAvatar(friend_info.getAvatar());
            friend.setHuanxinID(friend_info.getHuanxin_account());
            friend.setNike(friend_info.getNickname());
            DemoDBManager dbManager = DemoDBManager.getInstance();
            dbManager.saveMyFriend(friend);
            Log.i("friend", "demolhelp  添加数据库成功");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("friend", "demolhelp  添加数据库失败");
        }


    }

    /****
     * 多设备监听
     */
    public class MyMultiDeviceListener implements EMMultiDeviceListener {

        @Override
        public void onContactEvent(int event, String target, String ext) {
            switch (event) {
                case EMMultiDeviceListener.CONTACT_REMOVE: {
                    Map<String, EaseUser> localUsers = getContactList();
                    localUsers.remove(target);
                    userDao.deleteContact(target);
                    inviteMessgeDao.deleteMessage(target);

                    EMClient.getInstance().chatManager().deleteConversation(username, true);
                    broadcastManager.sendBroadcast(new Intent(Constant.ACTION_CONTACT_CHANAGED));
                    showToast("CONTACT_REMOVE");
                }
                break;
                case EMMultiDeviceListener.CONTACT_ACCEPT: {
                    Map<String, EaseUser> localUsers = getContactList();
                    EaseUser user = new EaseUser(target);
                    if (!localUsers.containsKey(target)) {
                        userDao.saveContact(user);
                    }
                    localUsers.put(target, user);
                    updateContactNotificationStatus(target, "", InviteMessage.InviteMessageStatus.MULTI_DEVICE_CONTACT_ACCEPT);
                    broadcastManager.sendBroadcast(new Intent(Constant.ACTION_CONTACT_CHANAGED));
                    showToast("CONTACT_ACCEPT");
                }
                break;
                case EMMultiDeviceListener.CONTACT_DECLINE:
                    updateContactNotificationStatus(target, "", InviteMessage.InviteMessageStatus.MULTI_DEVICE_CONTACT_DECLINE);
                    showToast("CONTACT_DECLINE");
                    break;
//                case CONTACT_ADD:
//                    updateContactNotificationStatus(target, "", InviteMessageStatus.MULTI_DEVICE_CONTACT_ADD);
//                    showToast("CONTACT_ADD");
//                break;
                case CONTACT_BAN:
                    updateContactNotificationStatus(target, "", InviteMessage.InviteMessageStatus.MULTI_DEVICE_CONTACT_BAN);
                    showToast("CONTACT_BAN");

                    Map<String, EaseUser> localUsers = DemoHelper.getInstance().getContactList();
                    localUsers.remove(username);
                    userDao.deleteContact(username);
                    inviteMessgeDao.deleteMessage(username);
                    EMClient.getInstance().chatManager().deleteConversation(username, false);
                    broadcastManager.sendBroadcast(new Intent(Constant.ACTION_CONTACT_CHANAGED));
                    break;
                case CONTACT_ALLOW:
                    updateContactNotificationStatus(target, "", InviteMessage.InviteMessageStatus.MULTI_DEVICE_CONTACT_ALLOW);
                    showToast("CONTACT_ALLOW");
                    break;
                default:
                    break;
            }
        }

        private void updateContactNotificationStatus(String from, String reason, InviteMessage.InviteMessageStatus status) {
            InviteMessage msg = null;
            for (InviteMessage _msg : inviteMessgeDao.getMessagesList()) {
                if (_msg.getFrom().equals(from)) {
                    msg = _msg;
                    break;
                }
            }
            if (msg != null) {
                ContentValues values = new ContentValues();
                msg.setStatus(status);
                values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
                inviteMessgeDao.updateMessage(msg.getId(), values);
            } else {
                // save invitation as message
                msg = new InviteMessage();
                msg.setFrom(username);
                msg.setTime(System.currentTimeMillis());
                msg.setReason(reason);
                msg.setStatus(status);
                notifyNewInviteMessage(msg);
            }
        }

        @Override
        public void onGroupEvent(final int event, final String target, final List<String> usernames) {
            execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        String groupId = target;
                        switch (event) {
                            case GROUP_CREATE:
                                showToast("GROUP_CREATE");
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/"", /*reason*/"", InviteMessage.InviteMessageStatus.MULTI_DEVICE_GROUP_CREATE);
                                break;
                            case GROUP_DESTROY:
                                showToast("GROUP_DESTROY");
                                inviteMessgeDao.deleteGroupMessage(groupId);
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/"", /*reason*/"", InviteMessage.InviteMessageStatus.MULTI_DEVICE_GROUP_DESTROY);
                                broadcastManager.sendBroadcast(new Intent(Constant.ACTION_GROUP_CHANAGED));
                                break;
                            case GROUP_JOIN:
                                showToast("GROUP_JOIN");
                                broadcastManager.sendBroadcast(new Intent(Constant.ACTION_GROUP_CHANAGED));
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/"", /*reason*/"", InviteMessage.InviteMessageStatus.MULTI_DEVICE_GROUP_JOIN);
                                break;
                            case GROUP_LEAVE:
                                showToast("GROUP_LEAVE");
                                inviteMessgeDao.deleteGroupMessage(groupId);
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/"", /*reason*/"", InviteMessage.InviteMessageStatus.MULTI_DEVICE_GROUP_LEAVE);
                                broadcastManager.sendBroadcast(new Intent(Constant.ACTION_GROUP_CHANAGED));
                                break;
                            case GROUP_APPLY:
                                showToast("GROUP_APPLY");
                                inviteMessgeDao.deleteGroupMessage(groupId);
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/"", /*reason*/"", InviteMessage.InviteMessageStatus.MULTI_DEVICE_GROUP_APPLY);
                                break;
                            case GROUP_APPLY_ACCEPT:
                                showToast("GROUP_ACCEPT");
                                inviteMessgeDao.deleteGroupMessage(groupId, usernames.get(0));
                                // TODO: person, reason from ext
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/usernames.get(0), /*reason*/"", InviteMessage.InviteMessageStatus.MULTI_DEVICE_GROUP_APPLY_ACCEPT);
                                break;
                            case GROUP_APPLY_DECLINE:
                                showToast("GROUP_APPLY_DECLINE");
                                inviteMessgeDao.deleteGroupMessage(groupId, usernames.get(0));
                                // TODO: person, reason from ext
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/usernames.get(0), /*reason*/"", InviteMessage.InviteMessageStatus.MULTI_DEVICE_GROUP_APPLY_DECLINE);
                                break;
                            case GROUP_INVITE:
                                showToast("GROUP_INVITE");
                                // TODO: person, reason from ext
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/usernames.get(0), /*reason*/"", InviteMessage.InviteMessageStatus.MULTI_DEVICE_GROUP_INVITE);
                                break;
                            case GROUP_INVITE_ACCEPT:
                                showToast("GROUP_INVITE_ACCEPT");
                                String st3 = appContext.getString(R.string.Invite_you_to_join_a_group_chat);
                                EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
                                msg.setChatType(ChatType.GroupChat);
                                // TODO: person, reason from ext
                                String from = "";
                                if (usernames != null && usernames.size() > 0) {
                                    msg.setFrom(usernames.get(0));
                                }
                                msg.setTo(groupId);
                                msg.setMsgId(UUID.randomUUID().toString());
                                msg.addBody(new EMTextMessageBody(msg.getFrom() + " " + st3));
                                msg.setStatus(EMMessage.Status.SUCCESS);
                                // save invitation as messages
                                EMClient.getInstance().chatManager().saveMessage(msg);

                                inviteMessgeDao.deleteMessage(groupId);
                                // TODO: person, reason from ext
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/"", /*reason*/"", InviteMessage.InviteMessageStatus.MULTI_DEVICE_GROUP_INVITE_ACCEPT);
                                broadcastManager.sendBroadcast(new Intent(Constant.ACTION_GROUP_CHANAGED));
                                break;
                            case GROUP_INVITE_DECLINE:
                                showToast("GROUP_INVITE_DECLINE");
                                inviteMessgeDao.deleteMessage(groupId);
                                // TODO: person, reason from ext
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/usernames.get(0), /*reason*/"", InviteMessage.InviteMessageStatus.MULTI_DEVICE_GROUP_INVITE_DECLINE);
                                break;
                            case GROUP_KICK:
                                showToast("GROUP_KICK");
                                // TODO: person, reason from ext
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/usernames.get(0), /*reason*/"", InviteMessage.InviteMessageStatus.MULTI_DEVICE_GROUP_INVITE_DECLINE);
                                break;
                            case GROUP_BAN:
                                showToast("GROUP_BAN");
                                // TODO: person from ext
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/usernames.get(0), /*reason*/"", InviteMessage.InviteMessageStatus.MULTI_DEVICE_GROUP_BAN);
                                break;
                            case GROUP_ALLOW:
                                showToast("GROUP_ALLOW");
                                // TODO: person from ext
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/usernames.get(0), /*reason*/"", InviteMessage.InviteMessageStatus.MULTI_DEVICE_GROUP_ALLOW);
                                break;
                            case GROUP_BLOCK:
                                showToast("GROUP_BLOCK");
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/"", /*reason*/"", InviteMessage.InviteMessageStatus.MULTI_DEVICE_GROUP_BLOCK);
                                break;
                            case GROUP_UNBLOCK:
                                showToast("GROUP_UNBLOCK");
                                // TODO: person from ext
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/"", /*reason*/"", InviteMessage.InviteMessageStatus.MULTI_DEVICE_GROUP_UNBLOCK);
                                break;
                            case GROUP_ASSIGN_OWNER:
                                showToast("GROUP_ASSIGN_OWNER");
                                // TODO: person from ext
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/usernames.get(0), /*reason*/"", InviteMessage.InviteMessageStatus.MULTI_DEVICE_GROUP_ASSIGN_OWNER);
                                break;
                            case GROUP_ADD_ADMIN:
                                showToast("GROUP_ADD_ADMIN");
                                // TODO: person from ext
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/usernames.get(0), /*reason*/"", InviteMessage.InviteMessageStatus.MULTI_DEVICE_GROUP_ADD_ADMIN);
                                break;
                            case GROUP_REMOVE_ADMIN:
                                showToast("GROUP_REMOVE_ADMIN");
                                // TODO: person from ext
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/usernames.get(0), /*reason*/"", InviteMessage.InviteMessageStatus.MULTI_DEVICE_GROUP_REMOVE_ADMIN);
                                break;
                            case GROUP_ADD_MUTE:
                                showToast("GROUP_ADD_MUTE");
                                // TODO: person from ext
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/usernames.get(0), /*reason*/"", InviteMessage.InviteMessageStatus.MULTI_DEVICE_GROUP_ADD_MUTE);
                                break;
                            case GROUP_REMOVE_MUTE:
                                showToast("GROUP_REMOVE_MUTE");
                                // TODO: person from ext
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/usernames.get(0), /*reason*/"", InviteMessage.InviteMessageStatus.MULTI_DEVICE_GROUP_REMOVE_MUTE);
                                break;
                            default:
                                break;
                        }

                        if (false) { // keep the try catch structure
                            throw new HyphenateException("");
                        }
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }

                }
            });
        }

        private void saveGroupNotification(String groupId, String groupName, String inviter, String reason, InviteMessage.InviteMessageStatus status) {
            InviteMessage msg = new InviteMessage();
            msg.setFrom(groupId);
            msg.setTime(System.currentTimeMillis());
            msg.setGroupId(groupId);
            msg.setGroupName(groupName);
            msg.setReason(reason);
            msg.setGroupInviter(inviter);
            Log.d(TAG, "receive invitation to join the group：" + groupName);
            msg.setStatus(status);
            notifyNewInviteMessage(msg);
        }

        private void updateGroupNotificationStatus(String groupId, String groupName, String inviter, String reason, InviteMessage.InviteMessageStatus status) {
            InviteMessage msg = null;
            for (InviteMessage _msg : inviteMessgeDao.getMessagesList()) {
                if (_msg.getGroupId().equals(groupId)) {
                    msg = _msg;
                    break;
                }
            }
            if (msg != null) {
                ContentValues values = new ContentValues();
                msg.setStatus(status);
                values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
                inviteMessgeDao.updateMessage(msg.getId(), values);
            }
        }
    }

    /**
     * save and notify invitation message
     *
     * @param msg 每次调用这个方法  未读消息加一
     */
    private void notifyNewInviteMessage(InviteMessage msg) {
        if (inviteMessgeDao == null) {
            inviteMessgeDao = new InviteMessgeDao(appContext);
        }
        inviteMessgeDao.saveMessage(msg);
        //increase the unread message count
        inviteMessgeDao.saveUnreadMessageCount(1);
        // notify there is new message
        getNotifier().vibrateAndPlayTone(null);
    }

    /**
     * user met some exception: conflict, removed or forbidden
     */
    protected void onUserException(String exception) {
        EMLog.e(TAG, "onUserException: " + exception);
        //Intent intent = new Intent(appContext,com.zl.vo_.main.main_fragments.MainActivity.class);
        Intent intent = new Intent("conflict");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intent.putExtra(exception, true);
        // appContext.startActivity(intent);
        appContext.sendBroadcast(intent);
        showToast(exception);
    }

    private EaseUser getUserInfo(String username) {
        // To get instance of EaseUser, here we get it from the user list in memory
        // You'd better cache it if you get it from your server
        /****
         * 根据用户名获取EaseUser对象，如果是登录的用户就从自己的服务器中获取，
         * 如果不是从本地数据库中查，如果还没有就从机器人表中查
         * 如果这个人不是你的联系人，就new一个EaseUser对象，并给他设置一个索引字母
         */
        EaseUser user = null;

       /* if(username.equals(EMClient.getInstance().getCurrentUser()))
            //从pase服务器中获取当前用户的信息，我们可以从自己的服务器中获取  返回的是个EaseUser对象
            return getUserProfileManager().getCurrentUserInfo();*/

        if (myUtils.readUser(appContext).getHuanxin_account().equals(username)) {
            LoginData.LoginInfo.LoginAccountInfo info = myUtils.readUser(appContext);
            user = new EaseUser();
            user.setAvatar(info.getAvatar());
            user.setNick(info.getNickname());
            return user;
        }


        //====获取用户信息开始===================================================
        MyFrindEntivity frindEntivity = DemoDBManager.getInstance().selectMyFrind(username);
        Log.i("聊天界面", "frindEntivity=" + frindEntivity);
        if (frindEntivity != null) {
            user = new EaseUser();
            user.setAvatar(frindEntivity.getAvatar());
            user.setNick(frindEntivity.getNike());
        }


        //user = getContactList().get(username);
        if (user == null && getRobotList() != null) {
            user = getRobotList().get(username);
        }
        //====获取用户信息结束===================================================
        // if user is not in your contacts, set inital letter for him/her
        if (user == null) {
            user = new EaseUser(username);
            EaseCommonUtils.setUserInitialLetter(user);
        }
        return user;
    }

    /**
     * Global listener
     * If this event already handled by an activity, you don't need handle it again
     * activityList.size() <= 0 means all activities already in background or not in Activity Stack
     */
    protected void registerMessageListener() {
        messageListener = new EMMessageListener() {
            private BroadcastReceiver broadCastReceiver = null;
            private boolean isMsgBelongToEncryption = false;

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    EMLog.d(TAG, "onMessageReceived id : " + message.getMsgId());
                    //（单聊）检测发送该消息的人是否在我的好友列表中，如果不在，获取信息添加进数据库中
                    testIsMyFriend(message);
                    //检测发送消息的人是否在我的加密列表中
                    isMsgBelongToEncryption = testFriendWithinMyEncryptionTable(message);
                    //检查是不是名片类型
                    isMingPianTexteMessage(message);
                    //检查是不是分享类型
                    //  isShareTexteMessage(message);[保留]
                    //************接收并处理扩展消息***********************
                    String userAvatar = message.getStringAttribute("avatar", "");
                    String userNick = message.getStringAttribute("nick", "");
                    String hxIdFrom = message.getFrom();
                    MyFrindEntivity frindEntivity = DemoDBManager.getInstance().getFriendByHxID(hxIdFrom);
                    //群聊时查找此人无
                    if (frindEntivity != null) {
                        //比对头像昵称
                        if (!userAvatar.equals(frindEntivity.getAvatar()) || !userNick.equals(frindEntivity.getNike())) {
                            DemoDBManager.getInstance().updateFriendAvatar(hxIdFrom, userAvatar, userNick);
                            appContext.sendBroadcast(new Intent("needRefresh"));
                        }
                    }
                    // ******************扩展信息处理完成**********************
                    //在后台运行
                    if (!easeUI.hasForegroundActivies()) {
                        if (demoModel.getIsDisturb()) {
                            //设置了消息免打扰
                            boolean isdisturbWithIn = myUtils.WithInDisturbTime(demoModel);
                            if (!isdisturbWithIn) {
                                /**
                                 * 不在设置的时间段内，判断是否设置了声音和震动，是否显示消息详情
                                 */
                                //如果这个好友是我加密的，就不响铃，不震动，不显示消息详情
                                if (!isMsgBelongToEncryption) {
                                    getNotifier().onNewMsg(message, demoModel.getSettingMsgDetails(), appContext);
                                }

                            } else {
                                /***
                                 * 在设置的消息免打扰时间段内，所有不响铃
                                 */
                            }
                        } else {
                            //没有设置消息免打扰
                            /**
                             * 判断是否设置了声音和震动，是否显示消息详情
                             */
                            //如果这个好友是我加密的，就不响铃，不震动，不显示消息详情
                            if (!isMsgBelongToEncryption) {
                                getNotifier().onNewMsg(message, demoModel.getSettingMsgDetails(), appContext);
                            }

                        }
                    } else {
                        //在程序内部
                        /***
                         * 在程序内部，在MAinActivity中接收消息刷新会话的未读消息，在EaseChatFragment中，监听
                         * 实现在聊天页面中处理声音和震动
                         */
                    }
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    EMLog.d(TAG, "receive command message");
                    Log.i("miandarao", "helper中的消息监听器");
                    //get message body
                    EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                    final String action = cmdMsgBody.action();//获取自定义action
                    //red packet code : 处理红包回执透传消息
//                    if(!easeUI.hasForegroundActivies()){
//                        if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION)){
//                            RedPacketUtil.receiveRedPacketAckMessage(message);
//                            broadcastManager.sendBroadcast(new Intent(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION));
//                        }
//                    }

                    if (action.equals("__Call_ReqP2P_ConferencePattern")) {
                        String title = message.getStringAttribute("em_apns_ext", "conference call");
                        Toast.makeText(appContext, title, Toast.LENGTH_LONG).show();
                    }
                    //end of red packet code
                    //获取扩展属性 此处省略
                    //maybe you need get extension of your message
                    //message.getStringAttribute("");
                    EMLog.d(TAG, String.format("Command：action:%s,message:%s", action, message.toString()));
                }
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
            }

            @Override
            public void onMessageRecalled(List<EMMessage> messages) {
                for (EMMessage msg : messages) {
                    if (msg.getChatType() == ChatType.GroupChat && EaseAtMessageHelper.get().isAtMeMsg(msg)) {
                        EaseAtMessageHelper.get().removeAtMeGroup(msg.getTo());
                    }
                    EMMessage msgNotification = EMMessage.createReceiveMessage(Type.TXT);
                    EMTextMessageBody txtBody = new EMTextMessageBody(String.format(appContext.getString(R.string.msg_recall_by_user), msg.getFrom()));
                    msgNotification.addBody(txtBody);
                    msgNotification.setFrom(msg.getFrom());
                    msgNotification.setTo(msg.getTo());
                    msgNotification.setUnread(false);
                    msgNotification.setMsgTime(msg.getMsgTime());
                    msgNotification.setLocalTime(msg.getMsgTime());
                    msgNotification.setChatType(msg.getChatType());
                    msgNotification.setAttribute(Constant.MESSAGE_TYPE_RECALL, true);
                    EMClient.getInstance().chatManager().saveMessage(msgNotification);
                }
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                EMLog.d(TAG, "change:");
                EMLog.d(TAG, "change:" + change);
            }
        };

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    /****
     * 检测发送消息的人是否在我的加密列表中
     *
     * @param messages
     * @return
     */
    private boolean testFriendWithinMyEncryptionTable(EMMessage messages) {
        //from 是环信id
        String from = messages.getFrom();
        DemoDBManager dbManager = DemoDBManager.getInstance();
        List<MyFrindEntivity> frindEntivities = dbManager.gethidefriends();
        for (MyFrindEntivity f : frindEntivities) {
            if (f.getHuanxinID().equals(from)) {
                return true;
            }
        }
        return false;
    }
//*****添加新朋友==开始*********************************************************

    /***
     * 检测
     *
     * @param message
     */
    private void testIsMyFriend(EMMessage message) {
        boolean isMyFriend = TestDbForIsMyFriend(message);
        DemoDBManager demoDBManager = DemoDBManager.getInstance();
        if (isMyFriend) {
            String huxi_account = message.getFrom();
            MyFrindEntivity myFrindEntivity = demoDBManager.getFriendByHxID(huxi_account);
            message.setAttribute("remark",myFrindEntivity.getRemark());

            String ss = message.getStringAttribute("remark","");
            Log.i("ss",message.getStringAttribute("remark",""));
            return;
        } else {
            MyFrindEntivity frindEntivity = getNewFriendData(message);


            try {
                demoDBManager.saveMyFriend(frindEntivity);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    /***
     * 是朋友，但不在我的数据库中
     *
     * @param message
     * @return
     */
    private MyFrindEntivity getNewFriendData(EMMessage message) {

        RequestParams params = new RequestParams(Url.FriendInfoURL);
        params.addParameter("huanxin_account", myUtils.readUser(appContext).getHuanxin_account());
        params.addParameter("friend_huanxin_account", message.getFrom());
        try {
            Log.i("OP", "123456");
            MyFrindEntivity myFrindEntivity = x.http().postSync(params, MyFrindEntivity.class);
            Log.i("OP", "456789");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return null;
    }

    /**
     * @param message
     * @return
     */
    private boolean TestDbForIsMyFriend(EMMessage message) {
        DemoDBManager demoDBManager = DemoDBManager.getInstance();
        MyFrindEntivity friend = demoDBManager.selectMyFrind(message.getFrom());
        if (friend == null) {
            return false;
        } else {
            return true;
        }

    }
//********添加新朋友==结束******************************************************

    /**
     * if ever logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        return EMClient.getInstance().isLoggedInBefore();
    }

    /**
     * logout
     *
     * @param unbindDeviceToken whether you need unbind your device token
     * @param callback          callback
     */
    public void logout(boolean unbindDeviceToken, final EMCallBack callback) {
        endCall();
        Log.d(TAG, "logout: " + unbindDeviceToken);
        EMClient.getInstance().logout(unbindDeviceToken, new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.d(TAG, "logout: onSuccess");
                reset();
                if (callback != null) {
                    callback.onSuccess();
                }

            }

            @Override
            public void onProgress(int progress, String status) {
                if (callback != null) {
                    callback.onProgress(progress, status);
                }
            }

            @Override
            public void onError(int code, String error) {
                Log.d(TAG, "logout: onSuccess");
                reset();
                if (callback != null) {
                    callback.onError(code, error);
                }
            }
        });
    }

    /**
     * get instance of EaseNotifier
     *
     * @return
     */
    public EaseNotifier getNotifier() {
        return easeUI.getNotifier();
    }

    public DemoModel getModel() {
        return (DemoModel) demoModel;
    }

    /**
     * update contact list
     *
     * @param aContactList
     */
    public void setContactList(Map<String, EaseUser> aContactList) {
        if (aContactList == null) {
            if (contactList != null) {
                contactList.clear();
            }
            return;
        }

        contactList = aContactList;
    }

    /**
     * save single contact
     */
    public void saveContact(EaseUser user) {
        contactList.put(user.getUsername(), user);
        demoModel.saveContact(user);
    }

    /**
     * get contact list
     *
     * @return
     */
    public Map<String, EaseUser> getContactList() {
        if (isLoggedIn() && contactList == null) {
            contactList = demoModel.getContactList();
        }

        // return a empty non-null object to avoid app crash
        if (contactList == null) {
            return new Hashtable<String, EaseUser>();
        }

        return demoModel.getContactList();
    }

    /**
     * set current username
     *
     * @param username
     */
    public void setCurrentUserName(String username) {
        this.username = username;
        demoModel.setCurrentUserName(username);
    }

    /**
     * get current user's id
     */
    public String getCurrentUsernName() {
        if (username == null) {
            username = demoModel.getCurrentUsernName();
        }
        return username;
    }

    public void setRobotList(Map<String, RobotUser> robotList) {
        this.robotList = robotList;
    }

    public Map<String, RobotUser> getRobotList() {
        if (isLoggedIn() && robotList == null) {
            robotList = demoModel.getRobotList();
        }
        return robotList;
    }

    /**
     * update user list to cache and database
     *
     * @param contactInfoList
     */
    public void updateContactList(List<EaseUser> contactInfoList) {
        for (EaseUser u : contactInfoList) {
            contactList.put(u.getUsername(), u);
        }
        ArrayList<EaseUser> mList = new ArrayList<EaseUser>();
        mList.addAll(contactList.values());
        demoModel.saveContactList(mList);
    }

    public UserProfileManager getUserProfileManager() {
        if (userProManager == null) {
            userProManager = new UserProfileManager();
        }
        return userProManager;
    }

    void endCall() {
        try {
            EMClient.getInstance().callManager().endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSyncGroupListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (!syncGroupsListeners.contains(listener)) {
            syncGroupsListeners.add(listener);
        }
    }

    public void removeSyncGroupListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (syncGroupsListeners.contains(listener)) {
            syncGroupsListeners.remove(listener);
        }
    }

    public void addSyncContactListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (!syncContactsListeners.contains(listener)) {
            syncContactsListeners.add(listener);
        }
    }

    public void removeSyncContactListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (syncContactsListeners.contains(listener)) {
            syncContactsListeners.remove(listener);
        }
    }

    public void addSyncBlackListListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (!syncBlackListListeners.contains(listener)) {
            syncBlackListListeners.add(listener);
        }
    }

    public void removeSyncBlackListListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (syncBlackListListeners.contains(listener)) {
            syncBlackListListeners.remove(listener);
        }
    }

    /**
     * Get group list from server
     * This method will save the sync state
     *
     * @throws HyphenateException
     */
    public synchronized void asyncFetchGroupsFromServer(final EMCallBack callback) {
        if (isSyncingGroupsWithServer) {
            return;
        }

        isSyncingGroupsWithServer = true;

        new Thread() {
            @Override
            public void run() {
                try {
                    List<EMGroup> groups = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();

                    // in case that logout already before server returns, we should return immediately
                    if (!isLoggedIn()) {
                        isGroupsSyncedWithServer = false;
                        isSyncingGroupsWithServer = false;
                        noitifyGroupSyncListeners(false);
                        return;
                    }

                    demoModel.setGroupsSynced(true);

                    isGroupsSyncedWithServer = true;
                    isSyncingGroupsWithServer = false;

                    //notify sync group list success
                    noitifyGroupSyncListeners(true);

                    if (callback != null) {
                        callback.onSuccess();
                    }
                } catch (HyphenateException e) {
                    demoModel.setGroupsSynced(false);
                    isGroupsSyncedWithServer = false;
                    isSyncingGroupsWithServer = false;
                    noitifyGroupSyncListeners(false);
                    if (callback != null) {
                        callback.onError(e.getErrorCode(), e.toString());
                    }
                }

            }
        }.start();
    }

    public void noitifyGroupSyncListeners(boolean success) {
        for (DataSyncListener listener : syncGroupsListeners) {
            listener.onSyncComplete(success);
        }
    }

    public void asyncFetchContactsFromServer(final EMValueCallBack<List<String>> callback) {
        if (isSyncingContactsWithServer) {
            return;
        }

        isSyncingContactsWithServer = true;

        new Thread() {
            @Override
            public void run() {
                List<String> usernames = null;
                List<String> selfIds = null;
                try {
                    usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    selfIds = EMClient.getInstance().contactManager().getSelfIdsOnOtherPlatform();
                    // in case that logout already before server returns, we should return immediately
                    if (!isLoggedIn()) {
                        isContactsSyncedWithServer = false;
                        isSyncingContactsWithServer = false;
                        notifyContactsSyncListener(false);
                        return;
                    }
                    if (selfIds.size() > 0) {
                        usernames.addAll(selfIds);
                    }
                    Map<String, EaseUser> userlist = new HashMap<String, EaseUser>();
                    for (String username : usernames) {
                        EaseUser user = new EaseUser(username);
                        EaseCommonUtils.setUserInitialLetter(user);
                        userlist.put(username, user);
                    }
                    // save the contact list to cache
                    getContactList().clear();
                    getContactList().putAll(userlist);
                    // save the contact list to database
                    UserDao dao = new UserDao(appContext);
                    List<EaseUser> users = new ArrayList<EaseUser>(userlist.values());
                    dao.saveContactList(users);

                    demoModel.setContactSynced(true);
                    EMLog.d(TAG, "set contact syn status to true");

                    isContactsSyncedWithServer = true;
                    isSyncingContactsWithServer = false;

                    //notify sync success
                    notifyContactsSyncListener(true);

                    getUserProfileManager().asyncFetchContactInfosFromServer(usernames, new EMValueCallBack<List<EaseUser>>() {

                        @Override
                        public void onSuccess(List<EaseUser> uList) {
                            updateContactList(uList);
                            getUserProfileManager().notifyContactInfosSyncListener(true);
                        }

                        @Override
                        public void onError(int error, String errorMsg) {
                        }
                    });
                    if (callback != null) {
                        callback.onSuccess(usernames);
                    }
                } catch (HyphenateException e) {
                    demoModel.setContactSynced(false);
                    isContactsSyncedWithServer = false;
                    isSyncingContactsWithServer = false;
                    notifyContactsSyncListener(false);
                    e.printStackTrace();
                    if (callback != null) {
                        callback.onError(e.getErrorCode(), e.toString());
                    }
                }

            }
        }.start();
    }

    public void notifyContactsSyncListener(boolean success) {
        for (DataSyncListener listener : syncContactsListeners) {
            listener.onSyncComplete(success);
        }
    }

    public void asyncFetchBlackListFromServer(final EMValueCallBack<List<String>> callback) {

        if (isSyncingBlackListWithServer) {
            return;
        }

        isSyncingBlackListWithServer = true;

        new Thread() {
            @Override
            public void run() {
                try {
                    List<String> usernames = EMClient.getInstance().contactManager().getBlackListFromServer();

                    // in case that logout already before server returns, we should return immediately
                    if (!isLoggedIn()) {
                        isBlackListSyncedWithServer = false;
                        isSyncingBlackListWithServer = false;
                        notifyBlackListSyncListener(false);
                        return;
                    }

                    demoModel.setBlacklistSynced(true);

                    isBlackListSyncedWithServer = true;
                    isSyncingBlackListWithServer = false;

                    notifyBlackListSyncListener(true);
                    if (callback != null) {
                        callback.onSuccess(usernames);
                    }
                } catch (HyphenateException e) {
                    demoModel.setBlacklistSynced(false);

                    isBlackListSyncedWithServer = false;
                    isSyncingBlackListWithServer = true;
                    e.printStackTrace();

                    if (callback != null) {
                        callback.onError(e.getErrorCode(), e.toString());
                    }
                }

            }
        }.start();
    }

    public void notifyBlackListSyncListener(boolean success) {
        for (DataSyncListener listener : syncBlackListListeners) {
            listener.onSyncComplete(success);
        }
    }

    public boolean isSyncingGroupsWithServer() {
        return isSyncingGroupsWithServer;
    }

    public boolean isSyncingContactsWithServer() {
        return isSyncingContactsWithServer;
    }

    public boolean isSyncingBlackListWithServer() {
        return isSyncingBlackListWithServer;
    }

    public boolean isGroupsSyncedWithServer() {
        return isGroupsSyncedWithServer;
    }

    public boolean isContactsSyncedWithServer() {
        return isContactsSyncedWithServer;
    }

    public boolean isBlackListSyncedWithServer() {
        return isBlackListSyncedWithServer;
    }

    synchronized void reset() {
        isSyncingGroupsWithServer = false;
        isSyncingContactsWithServer = false;
        isSyncingBlackListWithServer = false;

        demoModel.setGroupsSynced(false);
        demoModel.setContactSynced(false);
        demoModel.setBlacklistSynced(false);

        isGroupsSyncedWithServer = false;
        isContactsSyncedWithServer = false;
        isBlackListSyncedWithServer = false;

        isGroupAndContactListenerRegisted = false;

        setContactList(null);
        setRobotList(null);
        getUserProfileManager().reset();
        DemoDBManager.getInstance().closeDB();
    }

    public void pushActivity(Activity activity) {
        easeUI.pushActivity(activity);
    }

    public void popActivity(Activity activity) {
        easeUI.popActivity(activity);
    }

    //*****************************

    /***
     * 检测是不是名片类型
     *
     * @param message
     * @return
     */
    public boolean isMingPianTexteMessage(EMMessage message) {
        String TYPE = message.getStringAttribute(MinPianConstant.myExtType, null);
        if (TYPE == null) {
            return false;
        }
        if (TYPE.equals("mingpianType")) {
            message.getStringAttribute(MinPianConstant.cardUserPhone, null);
            message.getStringAttribute(MinPianConstant.huanxinID, null);
            message.getStringAttribute(MinPianConstant.name, null);
            message.getStringAttribute(MinPianConstant.userAvater, null);

            return true;

        }
        return false;
    }


    /***
     * 检测是不是分享类型
     *
     * @param message
     * @return
     */
    public boolean isShareTexteMessage(EMMessage message) {
        String TYPE = message.getStringAttribute(ShareConstant.myExtType, null);
        if (TYPE == null) {

            return false;
        }
        if (TYPE.equals("ShareType")) {
            message.getStringAttribute(ShareConstant.gameName, null);
            message.getStringAttribute(ShareConstant.huanxinID, null);
            message.getStringAttribute(ShareConstant.gameContent, null);
            message.getStringAttribute(ShareConstant.gameAvatar, null);
            message.getStringAttribute(ShareConstant.gameAvatar, null);

            return true;
        }
        return false;
    }


}
