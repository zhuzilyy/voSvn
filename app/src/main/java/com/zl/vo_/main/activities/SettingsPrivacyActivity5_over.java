package com.zl.vo_.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.vo_.DemoApplication;
import com.zl.vo_.R;
import com.zl.vo_.main.main_fragments.MainActivity;
import com.zl.vo_.main.views.XTitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2018/1/24.
 */

public class SettingsPrivacyActivity5_over extends VoBaseActivity implements View.OnClickListener {
    @BindView(R.id.iv_back)
    public ImageView iv_back;
    @BindView(R.id.goback_setting_btn)
    public Button goback_setting_btn;
    @BindView(R.id.setAgain)
    public TextView setAgain;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.lay_settings_pravacy5);
        ButterKnife.bind(this);
        mInit();
    }

    private void mInit() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SpannableString span3 = new SpannableString("手机摇一摇即可隐藏加密好友，(点击图标x，进入后点击图标x隐藏加密好友)");
        ImageSpan image = new ImageSpan(this,R.mipmap.over_add, DynamicDrawableSpan.ALIGN_BOTTOM);
        ImageSpan image2 = new ImageSpan(this,R.mipmap.over_privacy, DynamicDrawableSpan.ALIGN_BOTTOM);
        span3.setSpan(image,19,20, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span3.setSpan(image2,28,29, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
   @OnClick({R.id.goback_setting_btn,R.id.setAgain})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.goback_setting_btn:
                startActivity(new Intent(SettingsPrivacyActivity5_over.this,MainActivity.class));
                finish();
                break;
            case R.id.setAgain:
                if("0".equals(DemoApplication.whichActivity)){
                    startActivity(new Intent(SettingsPrivacyActivity5_over.this,SettingsPrivacyActivity3.class));
                }else {
                    startActivity(new Intent(SettingsPrivacyActivity5_over.this,SettingsPrivacyActivity4.class));
                }
                finish();
                break;
            default:
            break;


        }
    }
}
