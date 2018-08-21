/************************************************************
 *  * Hyphenate CONFIDENTIAL 
 * __________________ 
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved. 
 *  
 * NOTICE: All information contained herein is, and remains 
 * the property of Hyphenate Inc.
 * Dissemination of this information or reproduction of this material 
 * is strictly forbidden unless prior written permission is obtained
 * from Hyphenate Inc.
 */
package com.hyphenate.easeui.model;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.EaseUI.EaseSettingsProvider;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.EasyUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * new message notifier class
 * 
 * this class is subject to be inherited and implement the relative APIs
 */
public class EaseNotifier {
    private final static String TAG = "notify";
    Ringtone ringtone = null;

    protected final static String[] msg_eng = { "sent a message", "sent a picture", "sent a voice",
                                                "sent location message", "sent a video", "sent a file", "%1 contacts sent %2 messages"
                                              };
    protected final static String[] msg_ch = { "发来一条消息", "发来一张图片", "发来一段语音", "发来位置信息", "发来一个视频", "发来一个文件",
                                               "%1个联系人发来%2条消息"
                                             };

    protected static int notifyID = 0525; // start notification id
    protected static int foregroundNotifyID = 0555;

    protected NotificationManager notificationManager = null;

    protected HashSet<String> fromUsers = new HashSet<String>();
    protected int notificationNum = 0;

    protected Context appContext;
    protected String packageName;
    protected String[] msgs;
    protected long lastNotifiyTime;
    protected AudioManager audioManager;
    protected Vibrator vibrator;
    protected EaseNotificationInfoProvider notificationInfoProvider;
    //是否显示消息详情
    private boolean isShowDetails;

    public EaseNotifier() {
    }

    /**
     * this function can be override
     * @param context
     * @return
     */
    public EaseNotifier init(Context context){
        appContext = context;
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        packageName = appContext.getApplicationInfo().packageName;
        if (Locale.getDefault().getLanguage().equals("zh")) {
            msgs = msg_ch;
        } else {
            msgs = msg_eng;
        }

        audioManager = (AudioManager) appContext.getSystemService(Context.AUDIO_SERVICE);
        vibrator = (Vibrator) appContext.getSystemService(Context.VIBRATOR_SERVICE);

        return this;
    }

    /**
     * this function can be override
     */
    public void reset(){
        resetNotificationCount();
        cancelNotificaton();
    }

    void resetNotificationCount() {
        notificationNum = 0;
        fromUsers.clear();
    }

    void cancelNotificaton() {
        if (notificationManager != null)
            notificationManager.cancel(notifyID);
    }

    /***
     * 一个参数的
     * @param message
     * @param
     */
    public synchronized void onNewMsg(EMMessage message) {
        //静默消息
        if(EaseCommonUtils.isSilentMessage(message)){
            return;
        }
        EaseSettingsProvider settingsProvider = EaseUI.getInstance().getSettingsProvider();
        //此消息被设置成免打扰
        if(!settingsProvider.isMsgNotifyAllowed(message)){
            return;
        }
        // 检查app是否运行在后台
        if (!EasyUtils.isAppRunningForeground(appContext)) {
            EMLog.d(TAG, "app is running in backgroud");
            //后台
            sendNotification(message, false);
        } else {
            //前台
            sendNotification(message, true);

        }

        vibrateAndPlayTone(message);
    }
    private void showCustomerNotification(EMMessage message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "chat";
            String channelName = "聊天消息";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance,message);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance,EMMessage message) {
        String content=message.getBody().toString();
        String username = message.getStringAttribute("nick",null);
        //显示消息详情
        if (isShowDetails){
            switch (message.getType()){
                case TXT:
                    content =message.getBody().toString();
                    content=content.substring(5,content.length()-1);
                    break;
                case IMAGE:
                    content="图片消息";
                    break;
                case VOICE:
                    content="语音消息";
                    break;
                case LOCATION:
                    content="位置消息";
                    break;
                case VIDEO:
                    content="视频消息";
                    break;
                case FILE:
                    content="文件消息";
                    break;
            }
        }else{
            content=username+"发来一条消息";
        }
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        channel.enableLights(true); //是否在桌面icon右上角展示小红点
        channel.setLightColor(Color.GREEN); //小红点颜色
        channel.setShowBadge(true);
        NotificationManager manager = (NotificationManager) appContext.getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
        long id = System.currentTimeMillis();

       /* Intent msgIntent = appContext.getPackageManager().getLaunchIntentForPackage(packageName);
        if (notificationInfoProvider != null) {
            msgIntent = notificationInfoProvider.getLaunchIntent(message);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(appContext, notifyID, msgIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        Intent msgIntent = appContext.getPackageManager().getLaunchIntentForPackage(packageName);*/
        //PendingIntent pendingIntent = PendingIntent.getActivity(appContext, (int)id, msgIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent msgIntent = appContext.getPackageManager().getLaunchIntentForPackage(packageName);
        if (notificationInfoProvider != null) {
            msgIntent = notificationInfoProvider.getLaunchIntent(message);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(appContext,(int)id, msgIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager manager1 = (NotificationManager) appContext.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = manager.getNotificationChannel("chat");
            if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, appContext.getPackageName());
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel1.getId());
                appContext.startActivity(intent);
                Toast.makeText(appContext, "请手动将通知打开", Toast.LENGTH_SHORT).show();
            }
        }
        Notification notification = new NotificationCompat.Builder(appContext,"chat")
                .setContentTitle(username+"发来一条消息")
                .setContentText(content)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.logo)
                .setLargeIcon(BitmapFactory.decodeResource(appContext.getResources(), R.mipmap.logo))
                .setAutoCancel(true).setContentIntent(pendingIntent)
                .build();
        //notification.flags = Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONGOING_EVENT;
        manager1.notify((int)id, notification);
    }
    /**
     * handle the new message
     * this function can be override
     *
     * @param message
     */
    public synchronized void onNewMsg(EMMessage message,boolean isMesDetails,Context appContext) {
        isShowDetails=isMesDetails;
        //静默消息
        if(EaseCommonUtils.isSilentMessage(message)){
            return;
        }
        EaseSettingsProvider settingsProvider = EaseUI.getInstance().getSettingsProvider();
        //此消息被设置成免打扰
        if(!settingsProvider.isMsgNotifyAllowed(message)){
            return;
        }
        // 检查app是否运行在后台
        if (!EasyUtils.isAppRunningForeground(appContext)) {
            EMLog.d(TAG, "app is running in backgroud");
            Log.i("xx","=="+isShowDetails);
            //后台
            //sendNotification(message, false);
            Log.i("xx","");
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                showCustomerNotification(message);
            }else{
                sendNotification(message, false);
            }
        } else {
            //前台
            sendNotification(message, true);

        }
        vibrateAndPlayTone(message);

    }

    public synchronized void onNewMesg(List<EMMessage> messages) {
        if(EaseCommonUtils.isSilentMessage(messages.get(messages.size()-1))){
            return;
        }
        EaseSettingsProvider settingsProvider = EaseUI.getInstance().getSettingsProvider();
        if(!settingsProvider.isMsgNotifyAllowed(null)){
            return;
        }
        // check if app running background
        if (!EasyUtils.isAppRunningForeground(appContext)) {
            EMLog.d(TAG, "app is running in backgroud");
            sendNotification(messages, false);
        } else {
            sendNotification(messages, true);
        }
        vibrateAndPlayTone(messages.get(messages.size()-1));
    }

    /**
     * send it to notification bar
     * This can be override by subclass to provide customer implementation
     * @param messages
     * @param isForeground
     */
    protected void sendNotification (List<EMMessage> messages, boolean isForeground){
        for(EMMessage message : messages){
            if(!isForeground){
               // notificationNum++;
                fromUsers.add(message.getFrom());
            }
        }
        sendNotification(messages.get(messages.size()-1), isForeground, false);
    }

    /****
     * 发送通知
     * @param message
     * @param isForeground   是否在后台
     */
    protected void sendNotification (EMMessage message, boolean isForeground){
        sendNotification(message, isForeground, true);
    }

    /**
     * 发送通知到通知栏
     * send it to notification bar
     * This can be override by subclass to provide customer implementation
     * @param message
     */
    protected void sendNotification(EMMessage message, boolean isForeground, boolean numIncrease) {
        //这条信息来自哪里
        String username = message.getStringAttribute("nick",null);
        try {
            String notifyText = username + " ";
            switch (message.getType()) {
            case TXT:
                if(isShowDetails){
                    //显示消息详情
                     //003 发来一条消息
                    String content=message.getBody().toString();
                    sendMyNotification(message,isForeground,username+"发来消息："+content.substring(5,content.length()));
                    return;
                }else {
                    //不显示消息详情
                    notifyText += msgs[0];
                }

                break;
            case IMAGE:
                notifyText += msgs[1];
                break;
            case VOICE:

                notifyText += msgs[2];
                break;
            case LOCATION:
                notifyText += msgs[3];
                break;
            case VIDEO:
                notifyText += msgs[4];
                break;
            case FILE:
                notifyText += msgs[5];
                break;
            }
            //包管理器
            PackageManager packageManager = appContext.getPackageManager();
            //app名字
            String appname = (String) packageManager.getApplicationLabel(appContext.getApplicationInfo());

            //notification title
            String contentTitle = appname;

            if (notificationInfoProvider != null) {
                String customNotifyText = notificationInfoProvider.getDisplayedText(message);
                String customCotentTitle = notificationInfoProvider.getTitle(message);
                if (customNotifyText != null){
                    notifyText = customNotifyText;
                }
                if (customCotentTitle != null){
                    contentTitle = customCotentTitle;
                }
            }

            // create and send notificaiton
            //chuang
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(appContext)
                                                                        .setSmallIcon(appContext.getApplicationInfo().icon)
                                                                        .setWhen(System.currentTimeMillis())
                                                                        .setAutoCancel(true);

            Intent msgIntent = appContext.getPackageManager().getLaunchIntentForPackage(packageName);
            if (notificationInfoProvider != null) {
                msgIntent = notificationInfoProvider.getLaunchIntent(message);
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(appContext, notifyID, msgIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            if(numIncrease){
                // prepare latest event info section
                if(!isForeground){
                    notificationNum++;
                    fromUsers.add(message.getFrom());
                }
            }

            int fromUsersNum = fromUsers.size();
            String summaryBody = msgs[6].replaceFirst("%1", Integer.toString(fromUsersNum)).replaceFirst("%2",Integer.toString(notificationNum));

            if (notificationInfoProvider != null) {
                // lastest text
                String customSummaryBody = notificationInfoProvider.getLatestText(message, fromUsersNum,notificationNum);
                if (customSummaryBody != null){
                    summaryBody = customSummaryBody;
                }

                // small icon
                int smallIcon = notificationInfoProvider.getSmallIcon(message);
                if (smallIcon != 0){
                    mBuilder.setSmallIcon(smallIcon);
                }
            }

            mBuilder.setContentTitle(contentTitle);
           // mBuilder.setTicker(notifyText);
            mBuilder.setContentText(summaryBody);
            mBuilder.setContentIntent(pendingIntent);
            mBuilder.setNumber(notificationNum);
            //Toast.makeText(appContext, "notificationNum"+notificationNum, Toast.LENGTH_SHORT).show();
            Notification notification = mBuilder.build();
            Log.i("notificationNum",notificationNum+"   (*)");
            //桌面显示
            //sendToXiaoMi2(appContext,notificationNum);

            if (isForeground) {
                notificationManager.notify(foregroundNotifyID, notification);
                //如果在前台，取消通知
                notificationManager.cancel(foregroundNotifyID);
            } else {
                notificationManager.notify(notifyID, notification);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 显示消息详情
     * @param message
     * @param isForeground
     * @param s
     */
    private void sendMyNotification(EMMessage message, boolean isForeground, String s) {
        //包管理器
        PackageManager packageManager = appContext.getPackageManager();
        //app名字
        String appname = (String) packageManager.getApplicationLabel(appContext.getApplicationInfo());

        //notification title
        String contentTitle = appname;
        Notification.Builder builder = new Notification.Builder(appContext);
        builder.setSmallIcon(appContext.getApplicationInfo().icon);
        builder.setTicker(appname);
        builder.setContentTitle(appname);
        builder.setContentText(s);
        builder.setWhen(System.currentTimeMillis()); //发送时间

        Intent msgIntent = appContext.getPackageManager().getLaunchIntentForPackage(packageName);
        if (notificationInfoProvider != null) {
            msgIntent = notificationInfoProvider.getLaunchIntent(message);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(appContext, notifyID, msgIntent,PendingIntent.FLAG_UPDATE_CURRENT);


         builder.setContentIntent(pendingIntent);
        //builder.setDefaults(Notification.DEFAULT_ALL);

        Notification notification = builder.build();

        if (isForeground) {
            notificationManager.notify(foregroundNotifyID, notification);
            //如果在前台，取消通知
            notificationManager.cancel(foregroundNotifyID);
        } else {
            notificationManager.notify(notifyID, notification);

        }



    }

    /**
     * 向小米手机发送未读消息数广播miui6以后
     *
     * @param count
     */
    private static void sendToXiaoMi2(Context context, int count) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context).setContentTitle("title").setContentText("text").setSmallIcon(context.getApplicationInfo().icon);
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
        }
        try {
            Field field = notification.getClass().getDeclaredField("extraNotification");
            Object extraNotification = field.get(notification);
            Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);
            method.invoke(extraNotification, count);
            mNotificationManager.notify(10, notification);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("sendToXiaoMi2",e.toString());
            // miui 6之前的版本
            Intent localIntent = new Intent(
                    "android.intent.action.APPLICATION_MESSAGE_UPDATE");
            localIntent.putExtra(
                    "android.intent.extra.update_application_component_name",
                    context.getPackageName() + "/" );
            localIntent.putExtra(
                    "android.intent.extra.update_application_message_text", String.valueOf(count == 0 ? "" : count));
            context.sendBroadcast(localIntent);
        }
    }




    /**
     * 设置震动和声音
     * vibrate and  play tone
     */
    public void vibrateAndPlayTone(EMMessage message) {
        if(message != null){
            if(EaseCommonUtils.isSilentMessage(message)){
                return;
            }
        }

        if (System.currentTimeMillis() - lastNotifiyTime < 1000) {
            // received new messages within 2 seconds, skip play ringtone
            return;
        }

        try {
            lastNotifiyTime = System.currentTimeMillis();

            // check if in silent mode
            if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
                EMLog.e(TAG, "in slient mode now");
                return;
            }
            EaseSettingsProvider settingsProvider = EaseUI.getInstance().getSettingsProvider();
            //检测是否设置震动
            if(settingsProvider.isMsgVibrateAllowed(message)){
                long[] pattern = new long[] { 0, 180, 80, 120 };
                vibrator.vibrate(pattern, -1);
            }
            //检测是否设置声音
            boolean tt=settingsProvider.isMsgSoundAllowed(message);
            if(settingsProvider.isMsgSoundAllowed(message)){
                if (ringtone == null) {
                    Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                    ringtone = RingtoneManager.getRingtone(appContext, notificationUri);
                    if (ringtone == null) {
                        EMLog.d(TAG, "cant find ringtone at:" + notificationUri.getPath());
                        return;
                    }
                }
                if (!ringtone.isPlaying()) {
                    String vendor = Build.MANUFACTURER;

                    ringtone.play();
                    // for samsung S3, we meet a bug that the phone will
                    // continue ringtone without stop
                    // so add below special handler to stop it after 3s if
                    // needed
                    if (vendor != null && vendor.toLowerCase().contains("samsung")) {
                        Thread ctlThread = new Thread() {
                            public void run() {
                                try {
                                    Thread.sleep(3000);
                                    if (ringtone.isPlaying()) {
                                        ringtone.stop();
                                    }
                                } catch (Exception e) {
                                }
                            }
                        };
                        ctlThread.run();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * set notification info Provider
     *
     * @param provider
     */
    public void setNotificationInfoProvider(EaseNotificationInfoProvider provider) {
        notificationInfoProvider = provider;
    }

    public interface EaseNotificationInfoProvider {
        /**
         * set the notification content, such as "you received a new image from xxx"
         * 
         * @param message
         * @return null-will use the default text
         */
        String getDisplayedText(EMMessage message);

        /**
         * set the notification content: such as "you received 5 message from 2 contacts"
         * 
         * @param message
         * @param fromUsersNum- number of message sender
         * @param messageNum -number of messages
         * @return null-will use the default text
         */
        String getLatestText(EMMessage message, int fromUsersNum, int messageNum);

        /**
         * 设置notification标题
         * 
         * @param message
         * @return null- will use the default text
         */
        String getTitle(EMMessage message);

        /**
         * set the small icon
         * 
         * @param message
         * @return 0- will use the default icon
         */
        int getSmallIcon(EMMessage message);

        /**
         * set the intent when notification is pressed
         * 
         * @param message
         * @return null- will use the default icon
         */
        Intent getLaunchIntent(EMMessage message);
    }
}
