package com.zl.vo_.main.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;
import com.hyphenate.chat.EMClient;
import com.zl.vo_.DemoApplication;
import com.zl.vo_.R;
import com.zl.vo_.main.main_fragments.MainActivity;
import com.zl.vo_.main.main_utils.ImageLoaderUtils;
import com.zl.vo_.main.main_utils.RxCountDown;
import com.zl.vo_.main.main_utils.myUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/11/9.
 */

public class SplashActivityVo extends SplashFatherActivityVo implements View.OnClickListener{
    boolean isGranted;
    public String url;
    @BindView(R.id.splashImg)
    public ImageView splashImg;
    public Handler handler=new Handler();
    public int COUT_DOWN_TIME;
    @BindView(R.id.splash_view)
    ImageView mSplashView;
    @BindView(R.id.skip_real)
    TextView mSkipReal;
    @BindView(R.id.guide_fragment)
    FrameLayout mGuideFragment;
    @BindView(R.id.ad_click_small)
    ImageView mAdClickSmall;
    @BindView(R.id.ad_click)
    LinearLayout ad_click;
    @BindView(R.id.ad_skip_loading)
    ImageView mAdSkipLoading;
    @BindView(R.id.ad_ignore)
    FrameLayout mAdIgnore;
    @BindView(R.id.splash_video_frame)
    FrameLayout mSplashVideoFrame;
    @BindView(R.id.splash_video_layout)
    RelativeLayout mSplashVideoLayout;
    private Subscription mSubscription;
    private boolean isClick=false;
    private String[] perminssions={Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS};
   //游戏参数
    private String title;
    private String gameavatar;
    private String gameId;
    private String roomId;
    private String kindId;
    private String content;
    private String packagename;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lay_vo_splash);
        VoBaseActivity.addActivity(this);
        ButterKnife.bind(this);
        /****
         * 将动态权限的申请放在启动界面
         * 第一步：检查权限
         */
        isGranted=checkedAllPermission(perminssions);
        if(!isGranted){
            /***
             * 第二步：请求权限
             */
            ActivityCompat.requestPermissions(SplashActivityVo.this,perminssions,102);
          //  Toast.makeText(this, "弹框权限", Toast.LENGTH_SHORT).show();
        }else{
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goMain();
                }
            },500);
        }
        //****************获取游戏参数开始******************************
        Intent i_getvalue = getIntent();
        String action = i_getvalue.getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
            Uri uri = i_getvalue.getData();
            if (uri != null) {
                if (!TextUtils.isEmpty(uri.toString().trim())) {
                    title = uri.getQueryParameter("title");
                    gameavatar = uri.getQueryParameter("gameavatar");
                    content = uri.getQueryParameter("content");
                    gameId = uri.getQueryParameter("gameId");
                    roomId = uri.getQueryParameter("roomId");
                    kindId = uri.getQueryParameter("kindId");
                    packagename=uri.getQueryParameter("packagename");

                    if(!TextUtils.isEmpty(title)){
                        DemoApplication.GAME_NAME=title;
                    }
                    if(!TextUtils.isEmpty(gameavatar)){
                        DemoApplication.GAME_AVATAR=gameavatar;
                    }
                    if(!TextUtils.isEmpty(content)){
                        DemoApplication.GAME_CONTENT=content;
                    }
                    if(!TextUtils.isEmpty(gameId)){
                        DemoApplication.PARAM_GAMEID=gameId;
                    } if(!TextUtils.isEmpty(roomId)){
                        DemoApplication.PARAM_ROOMID=roomId;
                    }
                    if(!TextUtils.isEmpty(kindId)){
                        DemoApplication.PARAM_KINDID=kindId;
                    }
                    if(!TextUtils.isEmpty(packagename)){
                        DemoApplication.PACKAGE_NAME=packagename;
                    }
                }
            }
        }

        //****************获取游戏参数结束******************************

        //***********************************************
        /****
         * 在splashFather中注释掉请求广告的方法
         */
//      new Handler().postDelayed(new Runnable() {
//          @Override
//          public void run() {
//
//              if(!TextUtils.isEmpty(picUrl)&&!TextUtils.isEmpty(postTime)&&!TextUtils.isEmpty(linkUrl)){
//                  //获取成功
//                  splashImg.setVisibility(View.GONE);
//                  COUT_DOWN_TIME=Integer.parseInt(postTime);
//                  mSubscription = RxCountDown.countDown(COUT_DOWN_TIME)
//                          .doOnSubscribe(new Action0() {
//                              @Override
//                              public void call() {
//                                  ImageLoaderUtils.displayBigImage(picUrl, mSplashView,SplashActivityVo.this);
//                                  mAdClickSmall.setVisibility(View.VISIBLE);
//                                  mSplashView.setVisibility(View.VISIBLE);
//                                  mAdIgnore.setVisibility(View.VISIBLE);
//                              }
//                          })
//                          .subscribe(new Subscriber<Integer>() {
//                              @Override
//                              public void onCompleted() {
//                                  if(!isClick){
//                                      isClick=false;
//                                      goMain();
//                                  }
//                              }
//                              @Override
//                              public void onError(Throwable e) {
//                                  Log.i("ui",e.getMessage());
//                              }
//                              @Override
//                              public void onNext(Integer integer) {
//                                  mSkipReal.setText(TextUtils.concat(integer.intValue() + "s", getResources().getString(R.string.splash_ad_ignore)));
//                              }
//                          });
//
//              }else {
//                  goMain();
//              }
//          }
//      },2000);

    }
    private void goMain() {
        SharedPreferences adShare = getSharedPreferences("adfirst",Context.MODE_PRIVATE);
        int first =adShare.getInt("first",0);
        if(EMClient.getInstance().isLoggedInBefore()&& myUtils.readUser(SplashActivityVo.this)!=null){
            if(1==first){
                startActivity(new Intent(SplashActivityVo.this, MainActivity.class));
            }else {
                startActivity(new Intent(SplashActivityVo.this, AppInfoActivity.class));
            }
            finish();
        }else {
            if(1==first){
                startActivity(new Intent(SplashActivityVo.this, LoginActivity.class));
            }else {
                startActivity(new Intent(SplashActivityVo.this, AppInfoActivity.class));
            }
           finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //请求吗正确
        if(102==requestCode){
            boolean isGranded=true;

            for (int permissionCode:grantResults) {
                if(permissionCode!= PackageManager.PERMISSION_GRANTED){
                    //权限被拒绝
                    isGranded=false;
                    break;
                }
            }
            if(isGranded){
                Log.i("TTT","请求权限被赋予");
                //请求权限被赋予
                handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    }
                },500);
            }else {
                //权限被拒绝
                Log.i("TTT","权限被拒绝");
            }

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goMain();
                }
            },500);
        }
    }
    /****
     * 检查是否有数组中的权限
     * @param strings
     * @return
     */
    private boolean checkedAllPermission(String[] strings) {
        for (String p:strings) {

            if(ContextCompat.checkSelfPermission(SplashActivityVo.this,p)!= PackageManager.PERMISSION_GRANTED){
                //有一个没授予权限，就返回false
                return false;
            }
        }
        return true;
    }

    @OnClick({R.id.skip_real,R.id.ad_click_small})
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.skip_real:
                isClick=true;
                goMain();
            break;
            case R.id.ad_click_small:
                Log.i("linkurl",linkUrl+"  /");
                Intent intent=new Intent(SplashActivityVo.this,AdDetatilsActivity.class);
                intent.putExtra("url",linkUrl);
                intent.putExtra("title","详情");
                startActivity(intent);
                isClick=true;
                finish();
                break;
            default:
            break;


        }
    }
}
