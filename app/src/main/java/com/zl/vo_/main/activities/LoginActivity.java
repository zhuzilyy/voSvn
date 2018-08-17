package com.zl.vo_.main.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.zl.vo_.DemoApplication;
import com.zl.vo_.DemoModel;
import com.zl.vo_.R;
import com.zl.vo_.main.views.MEditText;
import com.zl.vo_.ui.BaseActivity;
import com.zl.vo_.util.CameraUtil;
import com.zl.vo_.util.MobileInfoUtils;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/14.
 */

public class LoginActivity extends VoBaseActivity implements View.OnClickListener, SurfaceHolder.Callback {
    private int screenWidth;
    private int screenheight;

    private Camera mCamera;
    private SurfaceView surfaceView;
    private SurfaceHolder mHolder;
    //默认前置或者后置相机 0:后置 1:前置
    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private int video_width;
    private int video_height;
    private int recorderRotation;
    @BindView(R.id.login_btn)
    public CircularProgressButton login_btn;
    public Handler handler = new Handler();
    @BindView(R.id.login_forgetpwd)
    public TextView login_forgetpwd;
    @BindView(R.id.login_reg)
    public TextView login_reg;
    @BindView(R.id.login_service_item)
    public TextView login_service_item;
    @BindView(R.id.login_name)
    public MEditText login_name;
    @BindView(R.id.login_pwd)
    public MEditText login_pwd;

    //--------------------------
    private DemoModel demoModel;
    @BindView(R.id.wx_login_tv)
    public TextView wx_login_tv;
    @BindView(R.id.switch_account)
    public TextView switch_account;
    private String ResultAccount;
    public static Activity instance;
    //---新ui---------------------------------
    @BindView(R.id.ll_phone_login)
    public LinearLayout ll_phone_login;
    @BindView(R.id.ll_phone_register)
    public LinearLayout ll_phone_register;
    @BindView(R.id.ll_wx_login)
    public LinearLayout ll_wx_login;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //记录广告页
        SharedPreferences adShare = getSharedPreferences("adfirst",Context.MODE_PRIVATE);
        adShare.edit().putInt("first",1).commit();

        setContentView(R.layout.lay_login);
        ButterKnife.bind(this);
        //VoBaseActivity.addActivity(this);
        screenWidth = this.getResources().getDisplayMetrics().widthPixels;
        screenheight = this.getResources().getDisplayMetrics().heightPixels;

        Log.i("wh", "width: " + screenWidth + "    heeight: " + screenheight);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mHolder = surfaceView.getHolder();
        mHolder.addCallback(this);
        minit();

    }

    private void minit() {
        instance=this;
        demoModel = new DemoModel(LoginActivity.this);
        login_btn.setIndeterminateProgressMode(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mCamera == null) {

            mCamera = getCamera(1);
            if (mHolder != null && mCamera != null) {
                //开启预览
                startPreview(mCamera, mHolder);
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }


    /**
     * 获取Camera实例
     *
     * @return
     */
    private Camera getCamera(int id) {
        Camera camera = null;
        try {
            camera = Camera.open(id);
        } catch (Exception e) {
            Log.i("ui",e.getMessage()+"///////3333333333333333////");

        }
        return camera;
    }

    /**
     * 预览相机
     */
    private void startPreview(Camera camera, SurfaceHolder holder) {
        if(camera==null){
            return;
        }
        try {
            setupCamera(camera);
            camera.setPreviewDisplay(holder);
            //获取相机预览角度， 后面录制视频需要用
            recorderRotation = CameraUtil.getInstance().getRecorderRotation(mCameraId);
            CameraUtil.getInstance().setCameraDisplayOrientation(this, mCameraId, camera);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置
     */
    private void setupCamera(Camera camera) {
        if (camera != null) {
            Camera.Parameters parameters = camera.getParameters();

            List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes != null && focusModes.size() > 0) {
                if (focusModes.contains(
                        Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                    //设置自动对焦
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                }
            }

            //这里第三个参数为最小尺寸 getPropPreviewSize方法会对从最小尺寸开始升序排列 取出所有支持尺寸的最小尺寸
            Camera.Size previewSize = CameraUtil.getInstance().getPropPreviewSize(parameters.getSupportedPreviewSizes(), video_width);
            parameters.setPreviewSize(previewSize.width, previewSize.height);

            Camera.Size pictrueSize = CameraUtil.getInstance().getPropPictureSize(parameters.getSupportedPictureSizes(), video_width);
            parameters.setPictureSize(pictrueSize.width, pictrueSize.height);

            camera.setParameters(parameters);

            /**
             * 设置surfaceView的尺寸 因为camera默认是横屏，所以取得支持尺寸也都是横屏的尺寸
             * 我们在startPreview方法里面把它矫正了过来，但是这里我们设置设置surfaceView的尺寸的时候要注意 previewSize.height<previewSize.width
             * previewSize.width才是surfaceView的高度
             * 一般相机都是屏幕的宽度 这里设置为屏幕宽度 高度自适应 你也可以设置自己想要的大小
             */
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(screenWidth, screenheight);//(screenWidth * video_width) / video_height
            //这里当然可以设置拍照位置 比如居中 我这里就置顶了
            surfaceView.setLayoutParams(params);

        }
    }


    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
       startPreview(mCamera, mHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if(mCamera==null){
            return;
        }
        mCamera.stopPreview();
        startPreview(mCamera, surfaceHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        releaseCamera();
    }

    @OnClick({R.id.login_btn, R.id.login_forgetpwd, R.id.login_reg,
            R.id.login_service_item, R.id.wx_login_tv,R.id.switch_account,
            R.id.ll_wx_login,R.id.ll_phone_register,R.id.ll_phone_login
   })

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_wx_login:
                //微信登录
                loginWX();
                break;
            case R.id.ll_phone_login:
                //手机号登录
                startActivity(new Intent(LoginActivity.this,LoginAfterActivityVo.class));
                //MobileInfoUtils.jumpStartInterface(LoginActivity.this);
                break;
            case R.id.ll_phone_register:
                //手机号注册
                startActivity(new Intent(LoginActivity.this,RegisterActivityVo.class));
                break;
            case R.id.login_btn:

             break;
            case R.id.login_forgetpwd:

               break;

           case R.id.switch_account:
//                //切换账号
              //  switchAccount();
                break;
            default:
                break;
        }
    }



    /****
     * 登录环信
     *
     */
//    private void LoginHX(final LoginData.LoginInfo.LoginAccountInfo user, final String name) {
//       // DemoDBManager.getInstance().closeDB();
//        EMClient.getInstance().login(user.getHuanxin_account(),user.getHuanxin_account(),new EMCallBack() {
//            @Override
//            public void onSuccess() {
//                Log.i("cspp", "onSuccess");
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // ** manually load all local groups and conversation
//                        EMClient.getInstance().groupManager().loadAllGroups();
//                        EMClient.getInstance().chatManager().loadAllConversations();
//                        login_btn.setProgress(100);
//                        demoModel.setCurrentUserName(user.getHuanxin_account());
////                        boolean updatenick = EMClient.getInstance().updateCurrentUserNick(
////                                user.getNickname());
////                        if (!updatenick) {
////                            Log.e("LoginActivity", "update current user nick fail");
////                        }
//                        //保存账号
//                       String accont= DemoDBManager.getInstance().getSwitchAccount(name);
//                        if(!name.equals(accont)){
//                            DemoDBManager.getInstance().saveAccountSwitch(name);
//                        }
//                        /****
//                         * 保存当前登录用户的信息
//                         */
//                        myUtils.saveUser(user,LoginActivity.this);
//                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                        finish();
//                    }
//                });
//
//            }
//
//            @Override
//            public void onError(int i, final String s) {
//                Log.i("cspp", "onError" + s);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        login_btn.setProgress(-1);
//                        Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onProgress(int i, String s) {
//                Log.i("cspp", "onProgress" + s);
//            }
//        });
//

   // }

//    /***
//     * 微信登录
//     */
    private void loginWX() {
        if (!DemoApplication.mWxApi.isWXAppInstalled()) {
              Toast.makeText(this, "您还没安装微信客户端", Toast.LENGTH_SHORT).show();
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login";
        DemoApplication.mWxApi.sendReq(req);
    }
//******************************************************************************
    /***
     * 切换账号
     */
//    private void switchAccount() {
//        Intent intent=new Intent(LoginActivity.this,AccountListActivityVo.class);
//        startActivityForResult(intent,102);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(200==resultCode){
//            ResultAccount=data.getStringExtra("account");
//            if(!TextUtils.isEmpty(ResultAccount)){
//                login_name.setText(ResultAccount);
//            }else {
//
//            }
//
//        }

  //  }
}
