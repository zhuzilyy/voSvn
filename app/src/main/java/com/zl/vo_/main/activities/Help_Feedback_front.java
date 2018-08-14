package com.zl.vo_.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zl.vo_.R;
import com.zl.vo_.utils.Url;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/11/25.
 */

public class Help_Feedback_front extends VoBaseActivity implements View.OnClickListener {

    private String mmurl = "";
    private String messageID;


    @BindView(R.id.iv_back)
    public ImageView back;
    public String title_str;
    @BindView(R.id.web_title_)
    public TextView web_title_;
    private String mParam;
    @BindView(R.id.re_function)
    public RelativeLayout re_function;
    @BindView(R.id.re_feedback)
    public RelativeLayout re_feedback;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_helpback_front);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        title_str = getIntent().getStringExtra("title");
        mmurl = getIntent().getStringExtra("url");
        mParam = getIntent().getStringExtra("param");
        if (!TextUtils.isEmpty(title_str)) {
            web_title_.setText(title_str);
        }
        minit();
    }


    private void minit() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //*******************************


    }

    @OnClick({R.id.re_feedback, R.id.re_function})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_feedback:
                Intent intent1=new Intent(Help_Feedback_front.this,PlatformComplainsActivityVo.class);

                startActivity(intent1);
                break;
            case R.id.re_function:
                Intent intent=new Intent(Help_Feedback_front.this,Help_Feedback.class);
              //  intent.putExtra("url","http://47.95.115.55:8080/voadmin/home/api/page_list");
             //   intent.putExtra("url","http://api.ykhswl.net/voadmin/home/api/page_list");
                intent.putExtra("url", "http://api.ykhswl.net/vo_admin_system/list.php");
                intent.putExtra("param","");
                intent.putExtra("title","功能介绍");
                startActivity(intent);
                break;

            default:
                break;


        }

    }
}
