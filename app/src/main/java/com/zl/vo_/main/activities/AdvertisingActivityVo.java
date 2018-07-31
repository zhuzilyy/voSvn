package com.zl.vo_.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.hyphenate.chat.EMClient;
import com.zl.vo_.DemoApplication;
import com.zl.vo_.R;
import com.zl.vo_.main.Entity.LoginData;
import com.zl.vo_.main.Entity.adEntivity;
import com.zl.vo_.main.main_fragments.MainActivity;
import com.zl.vo_.main.main_utils.myUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/11/29.
 */

public class AdvertisingActivityVo extends VoBaseActivity {
    @BindView(R.id.add_iv)
    public ImageView add;
    private Handler handler=new Handler();
    private static final int sleepTime = 2000;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_ad_page);
        VoBaseActivity.addActivity(this);
        ButterKnife.bind(this);
        mInit();
    }


    private void mInit() {



        if(DemoApplication.adImages.size()>0){
            adEntivity.adInfo.adImage adImage= DemoApplication.adImages.get(0);

           // Picasso.with(AdvertisingActivityVo.this).load(adImage.getPicurl()).placeholder(R.drawable.vo_bg_welcome).into(add);
            if (DemoApplication.bitmap!=null){
                add.setImageBitmap(DemoApplication.bitmap);
            }

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkLoginBefore();

                }
            },Integer.parseInt(adImage.getTime()));
        }else {
            add.setImageResource(R.mipmap.add);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkLoginBefore();

                }
            },2000);
        }



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

                    LoginData.LoginInfo.LoginAccountInfo user=myUtils.readUser(AdvertisingActivityVo.this);
                    if(user==null){
                        startActivity(new Intent(AdvertisingActivityVo.this, LoginActivity.class));
                    }else {
                        startActivity(new Intent(AdvertisingActivityVo.this, MainActivity.class));
                    }


                    finish();
                }else {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                    }
                      startActivity(new Intent(AdvertisingActivityVo.this, LoginActivity.class));
                   // startActivity(new Intent(SplashActivityVo.this, AdvertisingActivityVo.class));
                    finish();
                }
            }
        }).start();
    }


}
