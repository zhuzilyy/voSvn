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
package com.zl.vo_;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.zl.vo_.alive_app.service.PlayerMusicService;
import com.zl.vo_.main.Entity.CheckPwdEntivity;
import com.zl.vo_.main.Entity.MyPhoneContactEntity;
import com.zl.vo_.main.Entity.SearchContactsEntivity;
import com.zl.vo_.main.Entity.adEntivity;
import com.zl.vo_.main.main_fragments.MainActivity;
import com.zl.vo_.main.services.ShakeService;
import com.zl.vo_.utils.AppConst;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
// ============== fabric start
//import com.crashlytics.android.Crashlytics;
//import io.fabric.sdk.android.Fabric;
// ============== fabric end

public class DemoApplication extends Application {
    public static String TAG = "DemoApplication";
    public static Context applicationContext;
    //*********游戏参数*********************
    public static String PARAM_GAMEID;
    public static String PARAM_ROOMID;
    public static String PARAM_KINDID;
    public static String GAME_CONTENT;
    public static String PACKAGE_NAME;
    public static String GAME_AVATAR;
    public static String GAME_NAME;

    private static DemoApplication instance;
    // login user name
    public final String PREF_USERNAME = "username";
    public static CheckPwdEntivity.CheckPwdInfo pwdInfo;
    public static List<MyPhoneContactEntity> phoneContactEntities = new ArrayList<>();
    public static List<String> Phones = new ArrayList<>();
    public static int nn = 0;

    /**
     * nickname for current user, the nickname instead of ID be shown when user receive notification from APNs
     */
    public static String currentUserNick = "";
    private static final int sleepTime = 4000;

    public static List<adEntivity.adInfo.adImage> adImages = new ArrayList<>();
    public static Bitmap bitmap = null;
    public static IWXAPI mWxApi;
    public static String phoneStrRes = "";
    public static SearchContactsEntivity.SearchContactsInfo.SearchContactsAccountInfo accountInfo;
    ///=====是否之前登录过===================
    public static boolean isLoginBefore;
    /***
     * 选择是修改加密密码还是设置加密密码
     */
    public static int whichActivity;

    public static int hasNerVersion = 0;

    //小米消息推送 APP_ID ，APP_KEY
    private static final String APP_ID = "2882303761517839521";
    private static final String APP_KEY = "5871783979521";
    private static DemoHandler sHandler = null;
    private static MainActivity sMainActivity = null;
    private boolean isRunInBackground = false;
    private int appCount = 0;


    @Override
    public void onCreate() {
        x.Ext.init(this);
        Log.i("lanuge", "DemoApplication===onCreate");
        //方法数量大于65535
        MultiDex.install(this);
        super.onCreate();
        applicationContext = this;
        instance = this;
        registToWX();
        //初始化demoHelper
        DemoHelper.getInstance().init(applicationContext);
        //检查是否登录过
        //checkLoginBefore();
        //初始化小米推送
        initXiaomiPush();
        initBackgroundCallBack();
    }

    /***
     * 监听应用前后台切换
     */
    private void initBackgroundCallBack() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                appCount++;
                if (isRunInBackground) {
                    //应用从后台回到前台 需要做的操作
                    back2App(activity);
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                appCount--;
                if (appCount == 0) {
                    //应用进入后台 需要做的操作
                    leaveApp(activity);
                }

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });

    }


    /**
     * 从后台回到前台需要执行的逻辑
     *
     * @param activity
     */
    private void back2App(Activity activity) {
        isRunInBackground = false;
        // 暂停播放无声音乐Service
        stopPlayMusicService();
        Toast.makeText(getInstance(), "暂停播放无声音乐Service", Toast.LENGTH_SHORT).show();
    }

    /**
     * 离开应用 压入后台或者退出应用
     *
     * @param activity
     */
    private void leaveApp(Activity activity) {
        isRunInBackground = true;
        // 启动播放无声音乐Service
        startPlayMusicService();
        Toast.makeText(getInstance(), "启动播放无声音乐Service", Toast.LENGTH_SHORT).show();

    }

    private void stopPlayMusicService() {
        Intent intent = new Intent(getInstance(), PlayerMusicService.class);
        getInstance().stopService(intent);
    }

    private void startPlayMusicService() {
        Intent intent = new Intent(getInstance(), PlayerMusicService.class);
        startService(intent);
    }


    /** 初始化小米消息推送 **/
    private void initXiaomiPush() {
        // 注册 push 服务，注册成功后会向 DemoMessageReceiver 发送广播
        // 可以从 DemoMessageReceiver 的 onCommandResult 方法中 MiPushCommandMessage 对象参数中获取注册信息
        if (shouldInit()) {
            MiPushClient.registerPush(this, APP_ID, APP_KEY);
        }

        LoggerInterface newLogger = new LoggerInterface() {
            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                Log.d("mi", content, t);
            }

            @Override
            public void log(String content) {
                Log.d("mi", content);
            }
        };

        Logger.setLogger(this, newLogger);
        if (sHandler == null) {
            sHandler = new DemoHandler(getApplicationContext());
        }
    }

    public static void setMainActivity(MainActivity activity) {
        sMainActivity = activity;
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        //int myPid = Process.myPid();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }


    public static DemoHandler getHandler() {
        return sHandler;
    }


    public static class DemoHandler extends Handler {
        private Context context;

        public DemoHandler(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            String s = (String) msg.obj;
            if (sMainActivity != null) {
                sMainActivity.refreshLogInfo();
            }
            if (!TextUtils.isEmpty(s)) {
                Toast.makeText(context, s, Toast.LENGTH_LONG).show();
            }
        }
    }


    /***
     * 注册微信
     */
    private void registToWX() {
        //AppConst.WEIXIN.APP_ID是指你应用在微信开放平台上的AppID，记得替换。
        mWxApi = WXAPIFactory.createWXAPI(this, AppConst.APP_ID, false);
        // 将该app注册到微信
        mWxApi.registerApp(AppConst.APP_ID);
    }

    public static DemoApplication getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    /**
     * 检查之前是否登录过
     */
    private void checkLoginBefore() {
        new Thread(new Runnable() {
            public void run() {
                if (EMClient.getInstance().isLoggedInBefore()) {
                    // auto login mode, make sure all group and conversation is loaed before enter the main screen
                    long start = System.currentTimeMillis();
                    EMClient.getInstance().chatManager().loadAllConversations();
                    EMClient.getInstance().groupManager().loadAllGroups();
                    long costTime = System.currentTimeMillis() - start;
                    //wait
                    if (sleepTime - costTime > 0) {
                        try {
                            Thread.sleep(sleepTime - costTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    isLoginBefore = true;
                } else {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                    }
                    isLoginBefore = false;

                }
            }
        }).start();
    }
}
