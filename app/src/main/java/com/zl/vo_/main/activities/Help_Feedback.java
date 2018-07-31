package com.zl.vo_.main.activities;

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

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2017/11/25.
 */

public class Help_Feedback extends VoBaseActivity {
    @BindView(R.id.webview)
    public WebView mWebView;
    private  String mmurl="";
    private String messageID;
    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;

    @BindView(R.id.iv_back)
    public ImageView back;
    public String title_str;
    @BindView(R.id.web_title_)
    public TextView web_title_;
    private String mParam;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_web);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        title_str=getIntent().getStringExtra("title");
        mmurl= getIntent().getStringExtra("url");
        mParam=getIntent().getStringExtra("param");
        if(!TextUtils.isEmpty(title_str)){
            web_title_.setText(title_str);
        }
        minit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(mWebView.canGoBack()){
                mWebView.goBack();
                return true;
            }else {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void minit() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(mWebView.canGoBack()){
                  mWebView.goBack();
              }else {
                  finish();
              }
            }
        });

        loading_view.setVisibility(View.VISIBLE);

        //*******************************

        if(mmurl!=null){
            if(!TextUtils.isEmpty(mParam)){
                mWebView.loadUrl(mmurl+"?cate_id="+mParam);
            }else {
                mWebView.loadUrl(mmurl);
            }
        }
        //启用支持javascript
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        //  settings.setUseWideViewPort(true); // 关键点
        //settings.setAllowFileAccess(true); // 允许访问文件
        // settings.setSupportZoom(true); // 支持缩放
        //优先使用缓存
        //mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //不使用缓存：
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        //判断页面加载过程
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub

                if (newProgress >95) {
                    loading_view.setVisibility(View.GONE);
                    // 网页加载完成
                    //  Toast.makeText(pb_webView.this, "newProgress"+newProgress, Toast.LENGTH_SHORT).show();




                } else {
                    // 加载中http://www.ykppyx.vip/online/download.html?roomid=123&gameid=321

                }

            }
        });


    }
}
