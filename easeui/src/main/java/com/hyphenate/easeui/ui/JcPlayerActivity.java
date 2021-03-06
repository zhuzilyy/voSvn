package com.hyphenate.easeui.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.easeui.R;
import com.hyphenate.util.EMLog;

import java.io.File;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;


/**
 * Created by Administrator on 2018/1/3.
 */

public class JcPlayerActivity extends EaseBaseActivity {
    public ProgressBar progressBar;
    private String TAG = "JcPlayerActivity";
    private JZVideoPlayerStandard jzVideoPlayerStandard;
    private String localFilePath;
    private ImageView back;
    private boolean isPreview = false;
    private Button video_send;
    private String time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_jcplayer);
        jzVideoPlayerStandard = (JZVideoPlayerStandard) findViewById(R.id.videoplayer);
        back = (ImageView) findViewById(R.id.video_back);
        video_send = (Button) findViewById(R.id.video_send);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mInit();
    }

    private void mInit() {
        localFilePath = getIntent().getStringExtra("url");
        String preview = getIntent().getStringExtra("isPreview");
        time = getIntent().getStringExtra("time");
        if ("preview".equals(preview)) {
            //是预览界面，显示直接发送视频
            video_send.setVisibility(View.VISIBLE);
            video_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //发送视频
                    Intent intent =new Intent("videoPreviewOK");
                    intent.putExtra("path",localFilePath);
                    intent.putExtra("time",time);
                    sendBroadcast(intent);



                }
            });
        } else {
            video_send.setVisibility(View.GONE);
        }
        if (localFilePath != null) {
            jzVideoPlayerStandard.setUp(localFilePath, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");
            jzVideoPlayerStandard.startVideo();
        }


    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        jzVideoPlayerStandard.release();
    }
}


