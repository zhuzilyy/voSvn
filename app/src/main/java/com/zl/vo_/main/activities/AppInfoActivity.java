package com.zl.vo_.main.activities;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.hyphenate.chat.EMClient;
import com.zl.vo_.R;
import com.zl.vo_.main.Fragmengs.appinfo01;
import com.zl.vo_.main.Fragmengs.appinfo02;
import com.zl.vo_.main.Fragmengs.appinfo03;
import com.zl.vo_.main.main_fragments.MainActivity;
import com.zl.vo_.main.main_utils.myUtils;

/**
 * Created by Administrator on 2018/3/27.
 */

public class AppInfoActivity extends AppIntro2 {
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        addSlide(new appinfo01());
        addSlide(new appinfo02());
        addSlide(new appinfo03());
        //设置跳过按钮的图片
        setImageSkipButton(new Drawable() {
            @Override
            public void draw(Canvas canvas) {

            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter colorFilter) {

            }

            @Override
            public int getOpacity() {
                return 100;
            }
        });

        setDepthAnimation();
    }
    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        //Toast.makeText(this, "结束引导页", Toast.LENGTH_SHORT).show();
      //  startActivity(new Intent(AppInfoActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        if(EMClient.getInstance().isLoggedInBefore()&& myUtils.readUser(AppInfoActivity.this)!=null){
            startActivity(new Intent(AppInfoActivity.this, MainActivity.class));
            finish();
        }else {
            startActivity(new Intent(AppInfoActivity.this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
      //  Toast.makeText(this, "onSlideChanged", Toast.LENGTH_SHORT).show();
    }
}
