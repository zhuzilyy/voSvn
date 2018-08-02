package com.zl.vo_.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.hyphenate.easeui.ui.EaseBaseActivity;

import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by Administrator on 2018/1/3.
 */

public class JcPlayerActivity2 extends EaseBaseActivity {
    public ProgressBar progressBar;
    private String TAG = "JcPlayerActivity";
    private JZVideoPlayerStandard jzVideoPlayerStandard;
    private String localFilePath;
    private ImageView back;
    private boolean isPreview = false;
    private Button video_send;
    private String time;
    private ImageGridFragment gridFragment = new ImageGridFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.hyphenate.easeui.R.layout.lay_jcplayer);
        jzVideoPlayerStandard = (JZVideoPlayerStandard) findViewById(com.hyphenate.easeui.R.id.videoplayer);
        back = (ImageView) findViewById(com.hyphenate.easeui.R.id.video_back);
        video_send = (Button) findViewById(com.hyphenate.easeui.R.id.video_send);
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
         //   video_send.setVisibility(View.VISIBLE);
            video_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //发送视频
                    Intent intent =new Intent("videoPreviewOK");
                    intent.putExtra("path",localFilePath);
                    intent.putExtra("time",time);
                    sendBroadcast(intent);
                    gridFragment.finshVideo();
                    finish();

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



