package com.zl.vo_.main.activities;

import android.app.Activity;
import android.os.Bundle;

import com.zl.vo_.R;

import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by Administrator on 2018/1/3.
 */

public class JcPlayerActivity extends VoBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_jcplayer);
        VoBaseActivity.addActivity(this);
        mInit();
    }

    private void mInit() {
        JZVideoPlayerStandard jzVideoPlayerStandard = (JZVideoPlayerStandard) findViewById(R.id.videoplayer);
        jzVideoPlayerStandard.setUp("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4"
                , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "饺子闭眼睛");
        jzVideoPlayerStandard.startVideo();
    }
}
