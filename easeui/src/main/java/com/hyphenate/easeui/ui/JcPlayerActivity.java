package com.hyphenate.easeui.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
    private String TAG="JcPlayerActivity";
    private JZVideoPlayerStandard jzVideoPlayerStandard;
    private String localFilePath;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_jcplayer);
        jzVideoPlayerStandard= (JZVideoPlayerStandard) findViewById(R.id.videoplayer);
        back= (ImageView) findViewById(R.id.video_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mInit();
    }

    private void mInit() {
        localFilePath=getIntent().getStringExtra("url");

        if (localFilePath != null ) {
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


