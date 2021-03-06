package com.zl.vo_.main.main_fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.SettingService;
import com.zl.vo_.DemoApplication;
import com.zl.vo_.R;
import com.zl.vo_.db.DemoDBManager;
import com.zl.vo_.main.Entity.CheckPwdEntivity;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.activities.Help_Feedback;
import com.zl.vo_.main.activities.LifeNote;
import com.zl.vo_.main.activities.addFriendActivity_ContactsVo;
import com.zl.vo_.main.activities.addFriendActivity_SearchVo;
import com.zl.vo_.main.adapter.TabsAdapter;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.TimeUtil;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.main.newacitivity.SettingLifePwdActivity;
import com.zl.vo_.main.views.DetailsTypePopupWindow;
import com.zl.vo_.ui.GroupsActivity;
import com.zl.vo_.ui.ScanCaptureActivity;
import com.zl.vo_.update.CheckUpdateManager;
import com.zl.vo_.update.DownloadService;
import com.zl.vo_.update.bean.Version;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Administrator on 2017/11/15.
 */

public class Main_MessageFragment extends Fragment implements View.OnClickListener,EasyPermissions.PermissionCallbacks, CheckUpdateManager.RequestPermissions {

    private View vv;
    @BindView(R.id.tab)
    public TabLayout tab;
    @BindView(R.id.pager)
    public ViewPager mViewPager;
    private List<String> tabTitle;
    private List<Fragment> fragments;
    private ConversationListFragment conversationListFragment;
    private ContactsGroupFragment contactsGroupFragment;
    private Handler handler=new Handler();
    private MyReceiver myReceiver;
    //-------------------------------
    @BindView(R.id.lock)
    public ImageView lock;
    @BindView(R.id.unlock)
    public ImageView unlock;

    public DemoDBManager dbManager=DemoDBManager.getInstance();
    public boolean locking=false;
    @BindView(R.id.vo_Entertainment)
    public TextView vo_Entertainment;
    private Version mVersion;
    private static final int RC_EXTERNAL_STORAGE = 0x04;//存储权限
    @BindView(R.id.iv_add)
    public ImageView iv_add;
    @BindView(R.id.iv_search)
    public ImageView iv_search;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vv=LayoutInflater.from(getActivity()).inflate(R.layout.lay_main_message_fragment,null);
        ButterKnife.bind(this,vv);
        //--------------------------
        myReceiver=new MyReceiver();
        IntentFilter filter=new IntentFilter("newMsg");
        getActivity().registerReceiver(myReceiver,filter);
        //--------------------------
        minit();
        return vv;
    }

    private void minit() {
        tab.setTabMode(TabLayout.MODE_FIXED);

        tabTitle=new ArrayList<>();
        tabTitle.add(getActivity().getResources().getString(R.string.message));
//        tabTitle.add(getActivity().getResources().getString(R.string.friends));


        fragments=new ArrayList<>();
        contactsGroupFragment=new ContactsGroupFragment();
        conversationListFragment =new ConversationListFragment();
        fragments.add(conversationListFragment);
       // fragments.add(contactsGroupFragment);

        TabsAdapter adapter=new TabsAdapter(getChildFragmentManager(),fragments,tabTitle);
        mViewPager.setAdapter(adapter);
        //一次缓存3个碎片
        //mViewPager.setOffscreenPageLimit(2);
        tab.setupWithViewPager(mViewPager);


    }
    @OnClick({R.id.lock,R.id.unlock,R.id.vo_Entertainment,R.id.iv_add, R.id.iv_search})
    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.iv_add:
                DetailsTypePopupWindow typePopupWindow = new DetailsTypePopupWindow(getActivity(),iv_add,"","");
                typePopupWindow.setmItemsOnClick(new DetailsTypePopupWindow.ItemsOnClick() {
                    @Override
                    public void itemsOnClick(int position) {
                        switch (position) {
                            case 0:
                                startActivity(new Intent(getActivity(),GroupsActivity.class));
                                break;
                            //添加新的好友
                            case 1:
                                startActivity(new Intent(getActivity(),addFriendActivity_ContactsVo.class));
                                break;
                            //扫一扫
                            case 2:
                                startActivity(new Intent(getActivity(),ScanCaptureActivity.class));
                                break;
                            //帮助及反馈
                            case 3:
                                Intent intent=new Intent(getActivity(),Help_Feedback.class);
                                intent.putExtra("url", Url.NEW_HELPANDFEEDBACK);
                                intent.putExtra("param","15");
                                intent.putExtra("title","功能介绍");
                                startActivity(intent);

                                break;
                        }
                    }
                });





                break;
            case R.id.iv_search:
                startActivity(new Intent(getActivity(), addFriendActivity_SearchVo.class));
                break;

            //下载V娱乐
            case R.id.vo_Entertainment:
                // 通过包名获取要跳转的app，创建intent对象
                Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage("com.zl.vomoni");
                // 这里如果intent为空，就说名没有安装要跳转的应用嘛
                if (intent != null) {
                    // 这里跟Activity传递参数一样的嘛，不要担心怎么传递参数，还有接收参数也是跟Activity和Activity传参数一样
                    intent.putExtra("name", "辛振宇");
                    intent.putExtra("birthday", "1993-12-28");
                    startActivity(intent);
                } else {
                    //更新版本
                    checkUpdate();
                }
                break;
            case R.id.lock:
                lockMain();
                break;
            case R.id.unlock:
                unlockMain();
                break;
            default:
                break;
        }
    }

    /**
     * 主界面调解锁
     */
    public void unlockMain() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(true);
        View vv = LayoutInflater.from(getActivity()).inflate(R.layout.lay_unlock_lifecode, null);
        dialog.setContentView(vv);
        final EditText ed_pwd = vv.findViewById(R.id.ed_pwd);
        TextView tv_confirm = vv.findViewById(R.id.tv_confirm);
        TextView tv_setpwd = vv.findViewById(R.id.tv_setpwd);
        ImageView cancel_iv = vv.findViewById(R.id.cancel_iv);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwdStr = ed_pwd.getText().toString().trim();
                if (TextUtils.isEmpty(pwdStr)) {
                    return;
                }
                try{
                    dowithTempHide(pwdStr);
                    dialog.dismiss();
                }catch (Exception e){
                }
            }
        });
        cancel_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
      /*  DialogUIUtils.showAlertInput(getActivity(), null, "请输入密码", null, "确认22", "取消", new DialogUIListener() {
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
                *//***
                 * 处理临时隐藏
                 *//*
                dowithTempHide(input1);
            }
        }).show();*/
    }

    /**
     * 主界面调加锁
     */
    public void lockMain() {
        locking=true;
        lockAll();
    }

    //********** 下载vo娱乐开始 ****************************
    private void checkUpdate() {
        CheckUpdateManager manager = new CheckUpdateManager(getActivity(), false);
        manager.setCaller(this);
        manager.checkUpdate();
    }
    @Override
    public void call(Version version) {
        this.mVersion = version;
        requestExternalStorage();
    }
    @AfterPermissionGranted(RC_EXTERNAL_STORAGE)
    public void requestExternalStorage() {
        if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(getActivity(), mVersion.getUrl(), Toast.LENGTH_SHORT).show();
            DownloadService.startService(getActivity(), mVersion.getUrl());
        } else {
            EasyPermissions.requestPermissions(this, "", RC_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    private static final int REQUEST_CODE_PERMISSION = 100;

    private static final int REQUEST_CODE_SETTING = 300;

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }
    //回调的监听
    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode) {
                case REQUEST_CODE_PERMISSION: {
                    //                    Toast.makeText(MainActivity.this, R.string.successfully, Toast.LENGTH_SHORT).show();
                    if(AndPermission.hasPermission(getActivity(), grantPermissions)) {
                        // TODO 执行拥有权限时的下一步。
                        //                        showPop(mView);
                    } else {
                        // 第三种：自定义dialog样式。
                        final SettingService settingService = AndPermission.defineSettingDialog(getActivity(), REQUEST_CODE_SETTING);
                        // 使用AndPermission提供的默认设置dialog，用户点击确定后会打开App的设置页面让用户授权。
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                        builder.setTitle("权限申请失败").
                                setMessage("我们需要的相机及存储权限被您拒绝或者系统发生错误" +
                                        "申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                                .setCancelable(false)
                                .setPositiveButton("好，去设置",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // 你的dialog点击了确定调用：
                                                settingService.execute();
                                            }
                                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 你的dialog点击了取消调用：
                                settingService.cancel();
                            }
                        }).show();

                    }
                    break;
                }
            }
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            switch (requestCode) {
                case REQUEST_CODE_PERMISSION: {
                    //Toast.makeText(MainActivity.this, R.string.old_password, Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission(getActivity(), deniedPermissions)) {
                // 第三种：自定义dialog样式。
                final SettingService settingService = AndPermission.defineSettingDialog(getActivity(), REQUEST_CODE_SETTING);
                // 使用AndPermission提供的默认设置dialog，用户点击确定后会打开App的设置页面让用户授权。
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                builder.setTitle("权限申请失败").
                        setMessage("我们需要的相机及存储权限被您拒绝或者系统发生错误" +
                                "申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                        .setCancelable(false)
                        .setPositiveButton("好，去设置",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 你的dialog点击了确定调用：
                                        settingService.execute();
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 你的dialog点击了取消调用：
                        settingService.cancel();
                    }
                }).show();
            }
        }
    };
    //**********下载vo娱乐结束****************************
    /****
     * 紧急加密所有人
     */
    private void lockAll() {
        /***
         * 把临时状态变为0
         */

        boolean b=dbManager.updateMyPwd2("0","");
        if(b){
            getActivity().sendBroadcast(new Intent("needRefresh"));
        }
    }

    /*****
     * 处理临时隐藏
     * @param input1
     */
    private void dowithTempHide(CharSequence input1) {
        RequestParams params=new RequestParams(Url.CheckPwdUrl);
        params.addParameter("userid", myUtils.readUser(getActivity()).getUserid());
        params.addParameter("privacypass",input1);
        x.http().post(params, new MyCommonCallback<Result<CheckPwdEntivity>>() {
            @Override
            public void success(Result<CheckPwdEntivity> data) {
                //获取该密码的passid
                if("0".equals(data.code)){
                    CheckPwdEntivity pwdEntivity= data.data;
                    CheckPwdEntivity.CheckPwdInfo pwdInfo=pwdEntivity.getInfo();
                    final String passid= pwdInfo.getPrivacypassinfo().getPassid();
                    String nexttime=pwdInfo.getPrivacypassinfo().getNexttime();
                    if(!TextUtils.isEmpty(passid)){
                        //打上临时显示的标记
                        boolean b=dbManager.updateMyPwd2("1",passid);
                        if(b){
                            DemoApplication.nn=78;
                            getActivity().sendBroadcast(new Intent("needRefresh"));
                        }
                    }
                    if(!TextUtils.isEmpty(nexttime)){
                        boolean b=dbManager.updateMyPwdnexttime(nexttime,passid);
                        if(b){
                            Log.i("hide","下次隐藏时间更新成功");
                            //添加计时器
                           long nexttimel= TimeUtil.dataOne(nexttime);
                            Log.i("hide",nexttimel+"long=="   +nexttime);
                            Timer timer=new Timer(true);
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    Intent intent=new Intent("createTimer");
                                    intent.putExtra("passid",passid);
                                    getActivity().sendBroadcast(intent);
                                }
                            },nexttimel-System.currentTimeMillis());
                        }
                    }
                }
            }
            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                Log.i("null","ex="+ex.getMessage());
            }
        });
    }



    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action= intent.getAction();
            if("newMsg".equals(action)){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // if (currentTabIndex == 0) {
                        // refresh conversation list
                        if (conversationListFragment != null) {
                            conversationListFragment.refresh();
                        }
                        // }
                    }
                });
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(myReceiver!=null){
            getActivity().unregisterReceiver(myReceiver);
        }
        Log.i("lanuge","onDestroy============Main_MessageFragment===============");
    }
}
