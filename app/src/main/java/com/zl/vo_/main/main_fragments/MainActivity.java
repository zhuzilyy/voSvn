package com.zl.vo_.main.main_fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cpiz.android.bubbleview.Utils;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMClientListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMMultiDeviceListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.zl.vo_.Constant;
import com.zl.vo_.DemoApplication;
import com.zl.vo_.DemoHelper;
import com.zl.vo_.DemoModel;
import com.zl.vo_.HMSPushHelper;
import com.zl.vo_.R;
import com.zl.vo_.alive_app.receiver.ScreenReceiverUtil;
import com.zl.vo_.alive_app.service.DaemonService;
import com.zl.vo_.alive_app.service.PlayerMusicService;
import com.zl.vo_.alive_app.utils.JobSchedulerManager;
import com.zl.vo_.alive_app.utils.ScreenManager;
import com.zl.vo_.db.DemoDBManager;
import com.zl.vo_.db.InviteMessgeDao;
import com.zl.vo_.db.UserDao;
import com.zl.vo_.main.Entity.ApkBean;
import com.zl.vo_.main.Entity.LoginData;
import com.zl.vo_.main.Entity.MyPhoneContactEntity;
import com.zl.vo_.main.Entity.MyPwdList;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.Entity.pwdCell;
import com.zl.vo_.main.activities.AllFriendsForShareActivityVo;
import com.zl.vo_.main.activities.ChatActivity;
import com.zl.vo_.main.activities.LifeNote;
import com.zl.vo_.main.activities.LoginActivity;
import com.zl.vo_.main.activities.MyFrindEntivity;
import com.zl.vo_.main.activities.SplashActivityVo;
import com.zl.vo_.main.activities.VoBaseActivity;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.DensityUtils;
import com.zl.vo_.main.main_utils.TimeUtil;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.runtimepermissions.PermissionsManager;
import com.zl.vo_.runtimepermissions.PermissionsResultAction;
import com.zl.vo_.ui.BaseActivity;
import com.zl.vo_.ui.GroupsActivity;
import com.zl.vo_.utils.Url;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends VoBaseActivity implements View.OnClickListener{
    protected static final String TAG = "MainActivity";
    private int indexq=0;
    //小米推送loglist
    public static List<String> logList = new CopyOnWriteArrayList<String>();
    private TextView unreadLabel;
    //未读地址的数量
    private TextView unreadAddressLable;
    private Button[] mTabs;
    private Fragment[] fragments;
    private int index;
    private int currentTabIndex;
    // user logged into another deviceon
    public boolean isConflict = false;
    // user account was removed
    private boolean isCurrentAccountRemoved = false;
    //**************************************
    private boolean isExceptionDialogShow = false;
    private android.app.AlertDialog.Builder exceptionBuilder;

    //今天将双击back键的代码移植到mainActivity2018.2.9
    private InviteMessgeDao inviteMessgeDao;
    //-------
    private Main_MessageFragment main_messageFragment;
    // private Main_ContactListFragment main_contactListFragment;
    //*****************
    private MyIndexbarContactsFragment contactsListFragment;
    //*****************
    private Main_FragmentFind main_fragmentFind;
    private Main_FragmentProfile main_fragmentProfile;
    //--------
    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver broadcastReceiver;
    private BroadcastReceiver internalDebugReceiver;


    //*******************************************
    private FragmentManager fragmentManager;

    private DemoModel demoModel;
    private DemoDBManager dbManager = DemoDBManager.getInstance();
    private int num = 0;
    private Timer timer;
    private boolean isExit = false;
    //****************************
    @BindView(R.id.close_menu)
    public RelativeLayout close_menu;
    @BindView(R.id.menu_bg_re)
    public RelativeLayout menu_bg;
    @BindView(R.id.lifeNote_img_menu)
    public ImageView lifeNote_img_menu;
    @BindView(R.id.hideFriend_img_menu)
    public ImageView hideFriend_img_menu;
    @BindView(R.id.showFriend_img_menu)
    public ImageView showFriend_img_menu;
    @BindView(R.id.noclick)
    public RelativeLayout noclick;
    @BindView(R.id.show_incircle)
    public RelativeLayout show_incircle;
    @BindView(R.id.hideMean_re)
    public RelativeLayout hideMean_re;
    @BindView(R.id.tv_newVersion) public TextView tv_newVersion;



    /**
     * 库 phone表字段
     **/
    private static final String[] PHONES_PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.Contacts.Photo.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID};
    /**
     * 电话号码
     **/
    private static final int PHONES_NUMBER_INDEX = 1;

    /**
     * 联系人显示名称
     **/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;

    /**
     * check if current user account was remove
     */
    public boolean getCurrentAccountRemoved() {
        return isCurrentAccountRemoved;
    }

    public MyReceiver myReceiver;
    public static MainActivity mainInstance;
    public RelativeLayout re_plus;
   //------------------保活开始-----------------------------》》

    // 动态注册锁屏等广播
    private ScreenReceiverUtil mScreenListener;
    // 1像素Activity管理类
    private ScreenManager mScreenManager;
    // JobService，执行系统任务
    private JobSchedulerManager mJobManager;

    private ScreenReceiverUtil.SreenStateListener mScreenListenerer = new ScreenReceiverUtil.SreenStateListener() {
        @Override
        public void onSreenOn() {
            // 亮屏，移除"1像素"
            mScreenManager.finishActivity();
            Log.i("ss","ff");
        }

        @Override
        public void onSreenOff() {
            // 接到锁屏广播，将SportsActivity切换到可见模式
            // "咕咚"、"乐动力"、"悦动圈"就是这么做滴
//            Intent intent = new Intent(SportsActivity.this,SportsActivity.class);
//            startActivity(intent);
            // 如果你觉得，直接跳出SportActivity很不爽
            // 那么，我们就制造个"1像素"惨案
            mScreenManager.startActivity();
        }

        @Override
        public void onUserPresent() {
            // 解锁，暂不用，保留
        }
    };
    //------------------保活结束-----------------------------》》
    @Override
    protected void onStart() {
        super.onStart();
        //检查更新
        checkPackageVersion();

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //------alive-start-------------------------------
        //  （保活）启动系统任务
        // 1. 注册锁屏广播监听器
        mScreenListener = new ScreenReceiverUtil(this);
        mScreenManager = ScreenManager.getScreenManagerInstance(this);
        mScreenListener.setScreenReceiverListener(mScreenListenerer);
        mJobManager = JobSchedulerManager.getJobSchedulerInstance(this);
        mJobManager.startJobScheduler();
        startDaemonService();
        // 启动播放无声音乐Service
        startPlayMusicService();
        //-------alive-end-----------------------------
        DemoApplication.setMainActivity(this);
        //设置别名，撤销别名（alias）
        MiPushClient.setAlias(MainActivity.this, "vo", null);
        //MiPushClient.unsetAlias(MainActivity.this, "demo1", null);
        //设置账号，撤销账号（account）
        MiPushClient.setUserAccount(MainActivity.this, "user1", null);
        //MiPushClient.unsetUserAccount(MainActivity.this, "user1", null);
        //设置标签，撤销标签（topic：话题、主题）
        MiPushClient.subscribe(MainActivity.this, "IT", null);
        //MiPushClient.unsubscribe(MainActivity.this, "IT", null);
        //设置接收时间（startHour, startMin, endHour, endMin）
        MiPushClient.setAcceptTime(MainActivity.this, 7, 0, 23, 0, null);
        //暂停和恢复推送 //MiPushClient.pausePush(MainActivity.this, null);
        //MiPushClient.resumePush(MainActivity.this, null);

      // 获取华为 HMS 推送 token
        HMSPushHelper.getInstance().getHMSToken(this);
        ButterKnife.bind(this);
        mainInstance = this;
        VoBaseActivity.addActivity(this);
        //隐藏显示弹出页
        re_plus=findViewById(R.id.re_plus);
        re_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(close_menu!=null&&close_menu.isShown()){
                    //隐藏中间的功能圈
                    hideFunctionCircle();

                }else if(close_menu!=null){
                    //显示中间的功能圈
                    showFunctionCircle();
                }
            }
        });

        //-----------------------------

        lifeNote_img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入人生笔记
                LoginData.LoginInfo.LoginAccountInfo user=myUtils.readUser(MainActivity.this);
                if(!TextUtils.isEmpty(myUtils.readUser(MainActivity.this).getPersonpass())){
                    DialogUIUtils.showAlertInput(MainActivity.this, null, "请输入密码", null, "确认", "取消", new DialogUIListener() {
                        @Override
                        public void onPositive() {
                            DialogUIUtils.dismiss();
                        }
                        @Override
                        public void onNegative() {
                            // Toast.makeText(getActivity(), "onNegative", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onGetInput(CharSequence input1, CharSequence input2) {
                            super.onGetInput(input1, input2);
                            if(TextUtils.isEmpty(input1)){
                                return;
                            }
                            String pp=  org.xutils.common.util.MD5.md5(input1.toString());
                            pp=pp.toUpperCase();
                            Log.i("mmd5","==:"+pp);
                            Log.i("mmd5","==:"+myUtils.readUser(MainActivity.this).getPersonpass());
                            if(pp.equals(myUtils.readUser(MainActivity.this).getPersonpass())){
                                startActivity(new Intent(MainActivity.this, LifeNote.class));
                            }else {
                                Toast.makeText(MainActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).show();
                }else {
                    startActivity(new Intent(MainActivity.this, LifeNote.class));
                }
                hideFunctionCircle();

            }
        });
        showFriend_img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 显示
                if(main_messageFragment!= null){
                    main_messageFragment.unlockMain();
                }
                hideFunctionCircle();
            }
        });
        hideFriend_img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 隐藏
                if(main_messageFragment!=null){
                    main_messageFragment.lockMain();
                }
                hideFunctionCircle();
            }
        });

        //runtime permission for android 6.0, just require all permissions here for simple
        requestPermissions();
        DemoApplication.phoneStrRes = getPhoneRes();
        //******游戏传递参数**************************
        if (!TextUtils.isEmpty(DemoApplication.PACKAGE_NAME)
                && !TextUtils.isEmpty(DemoApplication.GAME_AVATAR)
                && !TextUtils.isEmpty(DemoApplication.GAME_CONTENT)
                && !TextUtils.isEmpty(DemoApplication.GAME_NAME)) {
            Intent intent = new Intent(MainActivity.this, AllFriendsForShareActivityVo.class);
            if (!TextUtils.isEmpty(DemoApplication.PARAM_GAMEID)) {
                intent.putExtra("param_gameid", DemoApplication.PARAM_GAMEID);
            }
            if (!TextUtils.isEmpty(DemoApplication.PARAM_ROOMID)) {
                intent.putExtra("param_roomid", DemoApplication.PARAM_ROOMID);
            }
            if (!TextUtils.isEmpty(DemoApplication.PARAM_KINDID)) {
                intent.putExtra("param_kindid", DemoApplication.PARAM_KINDID);
            }
            intent.putExtra("package_name", DemoApplication.PACKAGE_NAME);
            intent.putExtra("game_avatar", DemoApplication.GAME_AVATAR);
            intent.putExtra("game_content", DemoApplication.GAME_CONTENT);
            intent.putExtra("game_name", DemoApplication.GAME_NAME);
            startActivity(intent);

        }


        //*******其他设备登录************
        showExceptionDialogFromIntent(getIntent());
        //********************
        //更新密码表
        updatePwdTable();
        //创建定时器，隐藏好友
        createTimerHideFriend();
        //**************
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("needRefresh");
        //账号在其他设备上登录
        IntentFilter filter1 = new IntentFilter("conflict");

        registerReceiver(myReceiver, filter);
        registerReceiver(myReceiver, filter1);
        //**************
        initView();
        showExceptionDialogFromIntent(getIntent());
        inviteMessgeDao = new InviteMessgeDao(this);
        UserDao userDao = new UserDao(this);
        demoModel = new DemoModel(this);
        main_messageFragment = new Main_MessageFragment();
        //******************
        // main_contactListFragment=new Main_ContactListFragment();
        contactsListFragment = new MyIndexbarContactsFragment();
        //*****************
        main_fragmentFind = new Main_FragmentFind();
        main_fragmentProfile = new Main_FragmentProfile();

        fragments = new Fragment[]{main_messageFragment, contactsListFragment, main_fragmentFind,main_fragmentProfile};

        fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            fragmentManager.beginTransaction().add(R.id.fragment_container, main_messageFragment)
                    .add(R.id.fragment_container, contactsListFragment).hide(contactsListFragment).show(main_messageFragment)
                    .commit();
        } else {
            Log.i("tt", "xiangzhengyu");
        }
        //register broadcast receiver to receive the change of group from DemoHelper
        registerBroadcastReceiver();
        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
        EMClient.getInstance().addClientListener(clientListener);
        EMClient.getInstance().addMultiDeviceListener(new MyMultiDeviceListener());
        //debug purpose only
        registerInternalDebugReceiver();
    }
//----------------保活服务--------------------------------------------------
    private void stopPlayMusicService() {
        Intent intent = new Intent(MainActivity.this, PlayerMusicService.class);
        stopService(intent);
    }

    private void startPlayMusicService() {
        Intent intent = new Intent(MainActivity.this,PlayerMusicService.class);
        startService(intent);
    }

    private void startDaemonService() {
        Intent intent = new Intent(MainActivity.this, DaemonService.class);
        startService(intent);
    }

    private void stopDaemonService() {
        Intent intent = new Intent(MainActivity.this, DaemonService.class);
        stopService(intent);
    }

    //----------------保活服务--------------------------------------------------

    /***
     * 检查版本
     */
    private void checkPackageVersion() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                RequestParams params=new RequestParams(Url.GetNewVOVewsionUrl);
                x.http().post(params, new Callback.CacheCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                            Log.i("ss",result);
                        try {
                            JSONObject jsonObject =  new JSONObject(result);
                           String version =  jsonObject.getString("version");
                           String downloadPath =  jsonObject.getString("url");
                            if(!TextUtils.isEmpty(version)){
                                CompareVersion(version);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.i("ss","onCancelled");
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        Log.i("ss","onCancelled");
                    }

                    @Override
                    public void onFinished() {
                        Log.i("ss","onFinished");
                    }

                    @Override
                    public boolean onCache(String result) {
                        return false;
                    }
                });



              /*  RequestParams params=new RequestParams(Url.GetNewVOVewsionUrl);
                x.http().post(params, new MyCommonCallback<ApkBean>() {
                    @Override
                    public void success(ApkBean data) {
                        Log.i("ss",data+"");
                        String code=data.getCode();
                        if("0".equals(code)){
                            ApkBean.ApkData apkData=data.getData();
                            if(apkData!=null){
                                ApkBean.ApkData.ApkInfo apkInfo=apkData.getInfo();
                                if(apkInfo!=null){
                                    String mVersion=apkInfo.getVersion();
                                    if(!TextUtils.isEmpty(mVersion)){
                                        CompareVersion(mVersion);
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void error(Throwable ex, boolean isOnCallback) {
                        Log.i("ss",ex.getMessage());
                    }
                });
           */


            }
        }).start();
    }

    /***
     * 比对版本号
     * @param mVersion
     */
    private void CompareVersion(String mVersion) {
        float currentVersion= getCurrentVersion();
        Float newVerSion=Float.parseFloat(mVersion);
        if(newVerSion>currentVersion){
            tv_newVersion.setVisibility(View.VISIBLE);
            DemoApplication.hasNerVersion=1;
        }else {
            tv_newVersion.setVisibility(View.GONE);
            DemoApplication.hasNerVersion=0;
        }
    }
    /**
     * 获取当前版本号
     */
    private float getCurrentVersion() {
        try {

            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);

            Log.e(TAG, "当前版本名和版本号" + info.versionName + "--" + info.versionCode);

            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();

            Log.e(TAG, "获取当前版本号出错");
            return 0;
        }
    }

    /**
     * 显示中间的功能圈
     */
    private void showFunctionCircle() {
        hideMean_re.setVisibility(View.VISIBLE);
        noclick.setVisibility(View.VISIBLE);
        noclick.setClickable(true);
        close_menu.setVisibility(View.VISIBLE);

        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1,10f,1,10f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(100);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setFillAfter(true);
        menu_bg.startAnimation(animationSet);
        //人生笔记动画
        AnimatorSet set_lifenote = new AnimatorSet();//组合动画
        //缩放
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(lifeNote_img_menu, "scaleX", 0, 4.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(lifeNote_img_menu, "scaleY", 0, 4.0f);
        //位移
        ObjectAnimator translationX = new ObjectAnimator().ofFloat(lifeNote_img_menu,"translationX",0,0f);
        ObjectAnimator translationY = new ObjectAnimator().ofFloat(lifeNote_img_menu,"translationY",0,DensityUtils.dp2px(MainActivity.this,-75));
        //透明度
        ObjectAnimator anim = ObjectAnimator.ofFloat(lifeNote_img_menu, "alpha", 1f, 0.1f, 1f, 0.5f, 1f);
        set_lifenote.setDuration(200);
        set_lifenote.setInterpolator(new LinearInterpolator());

        set_lifenote.playTogether(scaleX,scaleY,translationX,translationY,anim);
        set_lifenote.start();

        //隐藏好友动画
        AnimatorSet set_hidefriend = new AnimatorSet();//组合动画
        //缩放
        ObjectAnimator scaleX_hide = ObjectAnimator.ofFloat(hideFriend_img_menu, "scaleX", 0, 4.0f);
        ObjectAnimator scaleY_hide = ObjectAnimator.ofFloat(hideFriend_img_menu, "scaleY", 0, 4.0f);
        //位移
        ObjectAnimator translationX_hide = new ObjectAnimator().ofFloat(hideFriend_img_menu,"translationX",0,DensityUtils.dp2px(MainActivity.this,-73));
        ObjectAnimator translationY_hide = new ObjectAnimator().ofFloat(hideFriend_img_menu,"translationY",0,DensityUtils.dp2px(MainActivity.this,-12));
        //透明度
        ObjectAnimator anim_hide = ObjectAnimator.ofFloat(hideFriend_img_menu, "alpha", 1f, 0.1f, 1f, 0.5f, 1f);
        set_hidefriend.setDuration(200);
        set_hidefriend.setInterpolator(new DecelerateInterpolator());
        set_hidefriend.playTogether(scaleX_hide,scaleY_hide,translationX_hide,translationY_hide,anim_hide);
        set_hidefriend.start();

        //显示好友动画
        AnimatorSet set_showfriend = new AnimatorSet();//组合动画
        //缩放
        ObjectAnimator scaleX_show = ObjectAnimator.ofFloat(showFriend_img_menu, "scaleX", 0, 4.0f);
        ObjectAnimator scaleY_show = ObjectAnimator.ofFloat(showFriend_img_menu, "scaleY", 0, 4.0f);
        //位移
        ObjectAnimator translationX_show = new ObjectAnimator().ofFloat(showFriend_img_menu,"translationX",0,DensityUtils.dp2px(MainActivity.this,73));
        ObjectAnimator translationY_show = new ObjectAnimator().ofFloat(showFriend_img_menu,"translationY",0,DensityUtils.dp2px(MainActivity.this,-12));
        //透明度
        ObjectAnimator anim_show = ObjectAnimator.ofFloat(showFriend_img_menu, "alpha", 1f, 0.1f, 1f, 0.5f, 1f);
        set_showfriend.setDuration(200);
        set_showfriend.setInterpolator(new DecelerateInterpolator());
        set_showfriend.playTogether(scaleX_show,scaleY_show,translationX_show,translationY_show,anim_show);
        set_showfriend.start();

        //内环变大
        AnimationSet animationSet_withinCircle = new AnimationSet(true);
        ScaleAnimation scaleAnimation_withinCircle = new ScaleAnimation(1,1.2f,1,1.2f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animationSet_withinCircle.addAnimation(scaleAnimation_withinCircle);
        animationSet_withinCircle.setFillAfter(true);
        show_incircle.startAnimation(animationSet_withinCircle);

    }

    /**
     * 根据手机的分辨率PX(像素)转成DP
     * @param context
     * @param pxValue
     * @return
     */
    public static float px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (pxValue / scale + 0.5f);
    }


    /***
     * 隐藏中间的功能圈
     */
    private void hideFunctionCircle() {
        hideMean_re.setVisibility(View.GONE);
        noclick.setVisibility(View.GONE);
        noclick.setClickable(false);

        close_menu.setVisibility(View.INVISIBLE);
        //背景隐藏
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1,0.3f,1,0.3f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(100);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setFillAfter(true);
        menu_bg.startAnimation(animationSet);

        //人生笔记动画
        AnimatorSet set_lifenote = new AnimatorSet();//组合动画
        //缩放
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(lifeNote_img_menu, "scaleX", 4.0f, 0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(lifeNote_img_menu, "scaleY", 4.0f, 0f);
        //位移
        ObjectAnimator translationX = new ObjectAnimator().ofFloat(lifeNote_img_menu,"translationX",0,0f);
        ObjectAnimator translationY = new ObjectAnimator().ofFloat(lifeNote_img_menu,"translationY",DensityUtils.dp2px(MainActivity.this,-75),0f);
        //透明度
        ObjectAnimator anim = ObjectAnimator.ofFloat(lifeNote_img_menu, "alpha", 1f, 0.1f, 1f, 0.5f, 1f);
        set_lifenote.setDuration(200);
        set_lifenote.setInterpolator(new DecelerateInterpolator());
        set_lifenote.playTogether(scaleX,scaleY,translationX,translationY,anim);
        set_lifenote.start();

        //隐藏好友动画
        AnimatorSet set_hidefriend = new AnimatorSet();//组合动画
        //缩放
        ObjectAnimator scaleX_hide = ObjectAnimator.ofFloat(hideFriend_img_menu, "scaleX", 4.0f, 0f);
        ObjectAnimator scaleY_hide = ObjectAnimator.ofFloat(hideFriend_img_menu, "scaleY", 4.0f, 0f);
        //位移
        ObjectAnimator translationX_hide = new ObjectAnimator().ofFloat(hideFriend_img_menu,"translationX",DensityUtils.dp2px(MainActivity.this,-73),0f);
        ObjectAnimator translationY_hide = new ObjectAnimator().ofFloat(hideFriend_img_menu,"translationY",DensityUtils.dp2px(MainActivity.this,-12),40f);
        //透明度
        ObjectAnimator anim_hide = ObjectAnimator.ofFloat(hideFriend_img_menu, "alpha", 1f, 0.1f, 1f, 0.5f, 1f);
        set_hidefriend.setDuration(200);
        set_hidefriend.setInterpolator(new DecelerateInterpolator());
        set_hidefriend.playTogether(scaleX_hide,scaleY_hide,translationX_hide,translationY_hide,anim_hide);
        set_hidefriend.start();


        //显示好友动画
        AnimatorSet set_showfriend = new AnimatorSet();//组合动画
        //缩放
        ObjectAnimator scaleX_show = ObjectAnimator.ofFloat(showFriend_img_menu, "scaleX", 4.0f, 0f);
        ObjectAnimator scaleY_show = ObjectAnimator.ofFloat(showFriend_img_menu, "scaleY", 4.0f, 0f);
        //位移
        ObjectAnimator translationX_show = new ObjectAnimator().ofFloat(showFriend_img_menu,"translationX",DensityUtils.dp2px(MainActivity.this,73),0f);
        ObjectAnimator translationY_show = new ObjectAnimator().ofFloat(showFriend_img_menu,"translationY",DensityUtils.dp2px(MainActivity.this,12),DensityUtils.dp2px(MainActivity.this,0));
        //透明度
        ObjectAnimator anim_show = ObjectAnimator.ofFloat(showFriend_img_menu, "alpha", 1f, 0.1f, 1f, 0.5f, 1f);
        set_showfriend.setDuration(200);
        set_showfriend.setInterpolator(new DecelerateInterpolator());
        set_showfriend.playTogether(scaleX_show,scaleY_show,translationX_show,translationY_show,anim_show);
        set_showfriend.start();

        //内环变小
        AnimationSet animationSet_withinCircle = new AnimationSet(true);
        ScaleAnimation scaleAnimation_withinCircle = new ScaleAnimation(1,0.8f,1,0.8f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);

        animationSet_withinCircle.addAnimation(scaleAnimation_withinCircle);
        animationSet_withinCircle.setFillAfter(true);
        show_incircle.startAnimation(animationSet_withinCircle);



    }



    /****
     * 创建定时器
     */
    private void createTimerHideFriend() {
        timer = new Timer();
        //查询有“下次隐藏时间的”
        List<pwdCell> pwdCells = dbManager.gethasNextHideTime();
        for (int i = 0; i < pwdCells.size(); i++) {
            String nextTime = pwdCells.get(i).getNexttime();
            String passid = pwdCells.get(i).getPassid();
            createTimerTask(passid, nextTime);
        }
    }

    /***
     * 创建定时器
     *
     * @param passid
     * @param nextTime
     */
    private void createTimerTask(final String passid, String nextTime) {
        long nowTime = System.currentTimeMillis();
        Log.i("nowTime", nowTime + "");
        if (nowTime > switchTime(nextTime)) {
            //如果当前时间大于设定的隐藏时间，直接发送广播隐藏
            //定时器触发
            boolean b = dbManager.updateMyPwd2("0", passid);
            if (b) {
                Intent intent = new Intent("hide");
                intent.putExtra("passid", passid);
                sendBroadcast(intent);
            }
            Log.i("nowTime", nowTime + "  大于设定的时间 隐藏" + passid);
            return;
        }
        long nexttime = switchTime(nextTime);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent("createTimer");
                intent.putExtra("passid", passid);
                sendBroadcast(intent);
            }
        }, nexttime);
    }

    /***
     * 时间转化  String转化为long
     *
     * @param nextTime
     * @return
     */
    private long switchTime(String nextTime) {
        long l = TimeUtil.dataOne(nextTime);
        return l;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("tag", "onNewIntent");
        showExceptionDialogFromIntent(intent);
    }

    /**
     * debug purpose only, you can ignore this
     */
    private void registerInternalDebugReceiver() {
        Log.i("lanuge", "registerInternalDebugReceiver");
        internalDebugReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                DemoHelper.getInstance().logout(false, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                finish();
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            }
                        });
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                    }

                    @Override
                    public void onError(int code, String message) {
                    }
                });
            }
        };
        IntentFilter filter = new IntentFilter(getPackageName() + ".em_internal_debug");
        registerReceiver(internalDebugReceiver, filter);
    }

    /***
     * 001版
     */
    private void registerBroadcastReceiver() {

        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);

        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnreadLabel();
                updateUnreadAddressLable();
                if (currentTabIndex == 0) {
                    // refresh conversation list
//                    if (conversationListFragment != null) {
//                        conversationListFragment.refresh();
//                    }
                } else if (currentTabIndex == 1) {
                    if (contactsListFragment != null) {
                        // contactsListFragment.refresh();
                    }
                }
                String action = intent.getAction();
                if (action.equals(Constant.ACTION_GROUP_CHANAGED)) {
                    if (EaseCommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
                        GroupsActivity.instance.onResume();
                    }
                }
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("lanuge", "onResume");
        if (!isConflict && !isCurrentAccountRemoved) {
            updateUnreadLabel();
            updateUnreadAddressLable();
        }
        // unregister this event listener when this activity enters the
        // background
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.pushActivity(this);
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    protected void onStop() {
        Log.i("lanuge", "onStop");
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        EMClient.getInstance().removeClientListener(clientListener);
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.popActivity(this);
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i("lanuge", "onSaveInstanceState");
        outState.putBoolean("isConflict", isConflict);
        outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
        super.onSaveInstanceState(outState);
    }

    /****
     * 消息监听
     */
    EMMessageListener messageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            //(更新'vo'tab上的未读标记)
            boolean isMsgBelongToEncryption = false;

            refreshUIWithMessage();
            //fa送广播，更新UI（更惨会话列表上的未读标记）===》Main_MessageFragment
            sendBroadcast(new Intent("newMsg"));

            //***new 2018.1.25
            for (EMMessage message : messages) {
                //检测发送消息的人是否在我的加密列表中
                isMsgBelongToEncryption = testFriendWithinMyEncryptionTable(message);


                if (demoModel.getIsDisturb()) {
                    boolean isdisturbWithIn = myUtils.WithInDisturbTime(demoModel);
                    if (!isdisturbWithIn) {
                        /**
                         * 不在设置的时间段内，判断是否设置了声音和震动，是否显示消息详情
                         */
                        if (!isMsgBelongToEncryption) {
                            DemoHelper.getInstance().getNotifier().onNewMsg(message, demoModel.getSettingMsgDetails(), MainActivity.this);
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
                    if (!isMsgBelongToEncryption) {
                        DemoHelper.getInstance().getNotifier().onNewMsg(message, demoModel.getSettingMsgDetails(), MainActivity.this);
                    }
                }
            }
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //red packet code : 处理红包回执透传消息
//            for (EMMessage message : messages) {
//                EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
//                final String action = cmdMsgBody.action();//获取自定义action
//                if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION)) {
//                    RedPacketUtil.receiveRedPacketAckMessage(message);
//                }
//            }
            //end of red packet code
            refreshUIWithMessage();
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };

    /**
     * get unread message count
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadCount = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        Log.i("aabbcc", "789getUnreadMsgCountTotal////" + unreadCount);
        List<EMConversation> emConversations = loadConversationList();

        int hideUnreadCount = getHideContactsUnreadMessageCount(emConversations);
        int unreadCount_ = unreadCount - hideUnreadCount;
        return unreadCount_;
    }

    /****
     * 检测发送消息的人此刻是否处在隐藏状态
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

    /****
     * 获取隐藏好友的未读消息数
     *
     * @param emConversations
     * @return
     */
    private int getHideContactsUnreadMessageCount(List<EMConversation> emConversations) {
        int hideContactUnreadCount = 0;
        //获取所有隐藏的的联系人
        List<MyFrindEntivity> entivities = dbManager.gethidefriends();

        if (emConversations.size() > 0) {
            for (int i = 0; i < emConversations.size(); i++) {
                if (entivities.size() > 0) {
                    for (int j = 0; j < entivities.size(); j++) {
                        if (emConversations.get(i).conversationId().equals(entivities.get(j).getHuanxinID())) {
                            hideContactUnreadCount += emConversations.get(i).getUnreadMsgCount();
                        }

                    }
                } else {
                    hideContactUnreadCount = 0;
                }

            }


        }
        return hideContactUnreadCount;
    }
//*************获取所有的会话开始******************************************************

    /***
     * 获取所有的会话
     *
     * @return
     */
    protected List<EMConversation> loadConversationList() {
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();

        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * sort conversations according time stamp of last message
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }
//*************获取所有的会话结束******************************************************

    /**
     * update unread message count
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        if (count > 0) {
            unreadLabel.setText(String.valueOf(count));
            unreadLabel.setVisibility(View.VISIBLE);
        } else {
            unreadLabel.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * update the total unread count
     */
    public void updateUnreadAddressLable() {
        Log.i("lanuge", "updateUnreadAddressLable");
        runOnUiThread(new Runnable() {
            public void run() {
                int count = getUnreadAddressCountTotal();
                if (count > 0) {
                    unreadAddressLable.setVisibility(View.VISIBLE);
                } else {
                    unreadAddressLable.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    /**
     * get unread event notification count, including application, accepted, etc
     *
     * @return
     */
    public int getUnreadAddressCountTotal() {
        Log.i("lanuge", "getUnreadAddressCountTotal");
        int unreadAddressCountTotal = 0;
        unreadAddressCountTotal = inviteMessgeDao.getUnreadMessagesCount();
        return unreadAddressCountTotal;
    }

    @TargetApi(23)
    private void requestPermissions() {
        Log.i("lanuge", "requestPermissions");
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
             //   Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
              //  Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * init views
     */
    private void initView() {
        Log.i("lanuge", "initView");
        unreadLabel = findViewById(R.id.unread_msg_number);
        unreadAddressLable = findViewById(R.id.unread_address_number);
        mTabs = new Button[4];
        mTabs[0] = findViewById(R.id.btn_conversation);
        mTabs[1] = findViewById(R.id.btn_address_list);
        mTabs[2] = findViewById(R.id.btn_find);
        mTabs[3] = findViewById(R.id.btn_profile);
        // select first tab
        mTabs[0].setSelected(true);
    }

    /**
     * on tab clicked
     *
     * @param view
     */
    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_conversation:
                index = 0;
                break;
            case R.id.btn_address_list:
                index = 1;
                break;
            case R.id.btn_find:
                index = 2;
//                if(tv_newVersion.isShown()){
//                    tv_newVersion.setVisibility(View.INVISIBLE);
//                }
                break;
            case R.id.btn_profile:
                index = 3;
                break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        // set current tab selected
        mTabs[index].setSelected(true);
        currentTabIndex = index;
    }

    private void showExceptionDialogFromIntent(Intent intent) {
        Log.i("lanuge", "showExceptionDialogFromIntent");
        EMLog.e(TAG, "showExceptionDialogFromIntent");
        if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false)) {
            showExceptionDialog(Constant.ACCOUNT_CONFLICT);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false)) {
            showExceptionDialog(Constant.ACCOUNT_REMOVED);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_FORBIDDEN, false)) {
            showExceptionDialog(Constant.ACCOUNT_FORBIDDEN);
        } else if (intent.getBooleanExtra(Constant.ACCOUNT_KICKED_BY_CHANGE_PASSWORD, false) ||
                intent.getBooleanExtra(Constant.ACCOUNT_KICKED_BY_OTHER_DEVICE, false)) {
            this.finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private int getExceptionMessageId(String exceptionType) {
        Log.i("lanuge", "getExceptionMessageId");
        if (exceptionType.equals(Constant.ACCOUNT_CONFLICT)) {
            return R.string.connect_conflict;
        } else if (exceptionType.equals(Constant.ACCOUNT_REMOVED)) {
            return R.string.em_user_remove;
        } else if (exceptionType.equals(Constant.ACCOUNT_FORBIDDEN)) {
            return R.string.user_forbidden;
        }
        return R.string.Network_error;
    }

    /**
     * show the dialog when user met some exception: such as login on another device, user removed or user forbidden
     */
    private void showExceptionDialog(String exceptionType) {
        isExceptionDialogShow = true;
        DemoHelper.getInstance().logout(false, null);
        String st = getResources().getString(R.string.Logoff_notification);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (exceptionBuilder == null)
                    exceptionBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                exceptionBuilder.setTitle(st);
                exceptionBuilder.setMessage(getExceptionMessageId(exceptionType));
                exceptionBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        exceptionBuilder = null;
                        isExceptionDialogShow = false;
                        finish();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                exceptionBuilder.setCancelable(false);
                exceptionBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
            }
        }
    }
    @OnClick({R.id.hideMean_re})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.hideMean_re:
                hideFunctionCircle();
            break;

            default:
            break;


        }
    }

    //*****监听器******************************
    public class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {
        }

        @Override
        public void onContactDeleted(final String username) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (ChatActivity.activityInstance != null && ChatActivity.activityInstance.toChatUsername != null &&
                            username.equals(ChatActivity.activityInstance.toChatUsername)) {
                        String st10 = getResources().getString(R.string.have_you_removed);
                        Toast.makeText(MainActivity.this, ChatActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_LONG)
                                .show();
                        ChatActivity.activityInstance.finish();
                    }
                }
            });
            updateUnreadAddressLable();
        }

        @Override
        public void onContactInvited(String username, String reason) {
        }

        @Override
        public void onFriendRequestAccepted(String username) {
        }

        @Override
        public void onFriendRequestDeclined(String username) {
        }
    }

    public class MyMultiDeviceListener implements EMMultiDeviceListener {
        @Override
        public void onContactEvent(int event, String target, String ext) {
        }

        @Override
        public void onGroupEvent(int event, String target, final List<String> username) {
            switch (event) {
                case EMMultiDeviceListener.GROUP_LEAVE:
                    ChatActivity.activityInstance.finish();
                    break;
                default:
                    break;
            }
        }
    }

    EMClientListener clientListener = new EMClientListener() {
        @Override
        public void onMigrate2x(boolean success) {
          //  Toast.makeText(MainActivity.this, "onUpgradeFrom 2.x to 3.x " + (success ? "success" : "fail"), Toast.LENGTH_LONG).show();
            if (success) {
                refreshUIWithMessage();
            }
        }
    };

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
        }

        VoBaseActivity.removeActivity();
        if (exceptionBuilder != null) {
            exceptionBuilder.create().dismiss();
            exceptionBuilder = null;
            isExceptionDialogShow = false;
        }
        unregisterBroadcastReceiver();

        try {
            unregisterReceiver(internalDebugReceiver);
        } catch (Exception e) {

        }

        //startActivity(new Intent(MainActivity.this, SplashActivityVo.class));
    }

    private void unregisterBroadcastReceiver() {
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
                updateUnreadLabel();
                if (currentTabIndex == 0) {
                    // refresh conversation list
//                    if (conversationListFragment != null) {
//                        conversationListFragment.refresh();
//                    }
                }
            }
        });
    }
    /**
     * 获取手机联系人
     */
    private String getPhoneRes() {
        StringBuilder stringBuilder = new StringBuilder();
        ContentResolver resolver = this.getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String phoneNum = cursor.getString(PHONES_NUMBER_INDEX).replace("", "");
                if (phoneNum == null || phoneNum == "")
                    continue;
                String phoneName = cursor.getString(PHONES_DISPLAY_NAME_INDEX);
                phoneNum = myUtils.replaceBlank(phoneNum);
                stringBuilder.append(phoneName + "-" + phoneNum + ",");
                MyPhoneContactEntity entity = new MyPhoneContactEntity(phoneName, phoneNum);
                DemoApplication.phoneContactEntities.add(entity);
                DemoApplication.Phones.add(phoneNum);
            }
        }
        return stringBuilder.toString().trim();
    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("needRefresh".equals(action)) {
                updateUnreadLabel();
            }
            if ("conflict".equals(action)) {
                showExceptionDialogFromIntent(intent);
            }
        }
    }

    //--------------------------------------------------------
    public void updatePwdTable() {
        //更新密码表
        RequestParams params = new RequestParams(Url.updateMyPWDUrl);
        params.addParameter("userid", myUtils.readUser(getApplication()).getUserid());
        x.http().post(params, new MyCommonCallback<Result<MyPwdList>>() {
            @Override
            public void success(Result<MyPwdList> data) {
                if ("0".equals(data.code)) {
                    MyPwdList data1 = data.data;
                    MyPwdList.MyPwdListInfo info = data1.getInfo();
                    List<MyPwdList.MyPwdListInfo.pwdList> pwdLists = info.getPrivacypass_list();
                    if (pwdLists.size() > 0) {
                        boolean b = updateMyPwdTable(pwdLists);
                        if (b) {
                            Log.i("**", "更新密码界面成功");
                        } else {
                            Log.i("**", "更新密码界面失败");
                        }
                    }
                }
            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {
            }
        });
    }

    /****
     * 更新密码表
     *
     * @param pwdLists
     */
    private boolean updateMyPwdTable(List<MyPwdList.MyPwdListInfo.pwdList> pwdLists) {
        return dbManager.updateOrInsertPwdTable(pwdLists);
    }

    //--------------------------------------------------------
    public void refreshLogInfo() {
        String AllLog = "";
        for (String log : logList) {
            AllLog = AllLog + log + "\n\n";
        }
       // mLogView.setText(AllLog);
    }

}
