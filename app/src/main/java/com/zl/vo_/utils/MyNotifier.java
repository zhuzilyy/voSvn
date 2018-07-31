package com.zl.vo_.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.NotificationCompat;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.model.EaseNotifier;

/**
 * Created by Administrator on 2017/12/1.
 */

public class MyNotifier extends EaseNotifier {





    @Override
    protected void sendNotification(EMMessage message, boolean isForeground, boolean numIncrease) {
        //这条信息来自哪里
        String username = message.getFrom();
        try {
            String notifyText = username + " ";
            switch (message.getType()) {
                case TXT:
                    notifyText += msgs[0];   //003 发来一条消息

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
            // String contentTitle = "hello_hyp";
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
            mBuilder.setTicker(notifyText);
            mBuilder.setContentText(summaryBody);
            mBuilder.setContentIntent(pendingIntent);
            // mBuilder.setNumber(notificationNum);
            Notification notification = mBuilder.build();

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
}
