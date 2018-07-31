package com.zl.vo_.main.activities;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zl.vo_.R;
import com.zl.vo_.adapter.vipAdapter;
import com.zl.vo_.main.Entity.LoginData;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.Entity.VIPProductData;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/4.
 */

public class CreateVoVIPAccountActivityVo extends VoBaseActivity implements View.OnClickListener {
    //---------------------
    public static String total_fee;
    public static String out_trade_no;
    public static String notify_url;
    //---------------------
    @BindView(R.id.iv_back)
    public ImageView iv_back;
    @BindView(R.id.vipLv)
    public ListView vipLv;
    public vipAdapter adapter;
    public List<Boolean> isCheckList=new ArrayList<>();
    //---------------------
    public de.hdodenhof.circleimageview.CircleImageView vip_head;
    @BindView(R.id.vip_final_save)
    public TextView vip_final_save;
    @BindView(R.id.vip_final_price)
    public TextView vip_final_price;
    public TextView vip_name;
    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;

    public ImageView vip_state;
//    @BindView(R.id.vip_info_tv)
//    public TextView vip_info_tv;
//    @BindView(R.id.vip_info_iv)
//    public ImageView vip_info_iv;
    public ImageView back;

    public List<VIPProductData.VIPProductInfo.VIPProductCell> biglist=new ArrayList<>();
    @BindView(R.id.vip_btn_submit)
    public Button vip_btn_submit;
    public WebView vip_webview;
    public String pid;
    public MyReceiver myReceiver;
    public LinearLayout function_vip_ll;
    public LinearLayout function_four_ll;
    public ImageView function_arrow;
    private boolean functionShow=true;

    public TextView vip_function_pressfriend;
    public TextView vip_function_shakefriend;
    public TextView vip_function_showallfriend;
    public TextView vip_function_allclear;
    public TextView vip_function_lifenoteprivacy;
    public TextView vip_function_infortrans;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_create_vo_vip_account);
        ButterKnife.bind(this);
        //*********************
        myReceiver=new MyReceiver();
        IntentFilter filter=new IntentFilter("wxpayOk");
        registerReceiver(myReceiver,filter);
        getData();
        mInit();
    }
    private void getData() {
        loading_view.setVisibility(View.VISIBLE);
        RequestParams params=new RequestParams(Url.GetVIPProductUrl);
        params.addParameter("userid", myUtils.readUser(CreateVoVIPAccountActivityVo.this).getUserid());
        x.http().post(params, new MyCommonCallback<Result<VIPProductData>>() {
            @Override
            public void success(Result<VIPProductData> data) {
                loading_view.setVisibility(View.GONE);
                if("0".equals(data.code)){
                  VIPProductData productData=  data.data;
                   VIPProductData.VIPProductInfo productInfo= productData.getInfo();
                   List<VIPProductData.VIPProductInfo.VIPProductCell> cells= productInfo.getVipproduct_list();
                    biglist.clear();
                    biglist.addAll(cells);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                loading_view.setVisibility(View.GONE);
                Log.i("CreateVoVIP",ex.getMessage()+"");
            }
        });
    }
    private void mInit() {


        View headerview=LayoutInflater.from(CreateVoVIPAccountActivityVo.this).inflate(R.layout.lay_vipheader,null);
        final View footerview= LayoutInflater.from(CreateVoVIPAccountActivityVo.this).inflate(R.layout.lay_vipfooter,null);
        vipLv.addFooterView(footerview);
        vipLv.addHeaderView(headerview);
        vip_head=headerview.findViewById(R.id.vip_head);
        back=headerview.findViewById(R.id.back);
        vip_name=headerview.findViewById(R.id.vip_name);
        vip_state=headerview.findViewById(R.id.vip_state);
        vip_webview=footerview.findViewById(R.id.vip_webview);
        function_vip_ll=headerview.findViewById(R.id.function_vip_ll);
        function_four_ll=headerview.findViewById(R.id.function_four_ll);
        function_arrow=headerview.findViewById(R.id.vip_arrow);

        vip_function_pressfriend=headerview.findViewById(R.id.vip_function_pressfriend);
        vip_function_shakefriend=headerview.findViewById(R.id.vip_function_shakefriend);
        vip_function_showallfriend=headerview.findViewById(R.id.vip_function_showallfriend);
        vip_function_allclear=headerview.findViewById(R.id.vip_function_allclear);
        vip_function_lifenoteprivacy=headerview.findViewById(R.id.vip_function_lifenoteprivacy);
        vip_function_infortrans=headerview.findViewById(R.id.vip_function_infortrans);

        function_vip_ll.setOnClickListener(this);
        back.setOnClickListener(this);
        function_vip_ll.setOnClickListener(this);

        vip_function_pressfriend.setOnClickListener(this);
        vip_function_shakefriend.setOnClickListener(this);
        vip_function_showallfriend.setOnClickListener(this);
        vip_function_allclear.setOnClickListener(this);
        vip_function_lifenoteprivacy.setOnClickListener(this);
        vip_function_infortrans.setOnClickListener(this);

        if(vip_webview!=null){
            setVIPWebView(vip_webview);
        }
        LoginData.LoginInfo.LoginAccountInfo user= myUtils.readUser(CreateVoVIPAccountActivityVo.this);
        if(user!=null){
            Glide.with(CreateVoVIPAccountActivityVo.this).load(user.getAvatar()).into(vip_head);
            vip_name.setText(user.getNickname());
            if("1".equals(user.getVip())){
                vip_state.setVisibility(View.VISIBLE);
            }else {
                vip_state.setVisibility(View.GONE);
            }
        }
        adapter=new vipAdapter(CreateVoVIPAccountActivityVo.this,biglist);
        vipLv.setAdapter(adapter);
        vipLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              if(0==i){
                  return;
              }
                adapter.setSelectedPosition(i-1);
                adapter.notifyDataSetInvalidated();
                VIPProductData.VIPProductInfo.VIPProductCell cell=biglist.get(i-1);
                if(cell!=null){
                    vip_final_price.setText(cell.getDiscount_price());
                    Double disPrice=(Double.parseDouble(cell.getPrice()))-(Double.parseDouble(cell.getDiscount_price()));
                    java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
                    vip_final_save.setText("已省"+df.format(disPrice)+"元");

                    pid=biglist.get(i-1).getProduct_id();
                }
            }
        });




    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);


    }

    /***
     *
     * @param mWebView
     */
    private void setVIPWebView(WebView mWebView) {
        mWebView.loadUrl(Url.VIPWebView);
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
                    // 加载中
                }
            }
        });
    }
    @OnClick({R.id.iv_back,R.id.vip_btn_submit})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.vip_btn_submit:
                if(TextUtils.isEmpty(pid)){
                    Toast.makeText(this, "请选择您要购买的产品", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent=new Intent(CreateVoVIPAccountActivityVo.this,VipPayWayActivityVo.class);
                intent.putExtra("pid",pid);
                startActivityForResult(intent,100);
                break;
            case R.id.function_vip_ll:
                //动画显示隐藏
                if(functionShow){
                    ObjectAnimator animator = ObjectAnimator.ofFloat(function_arrow, "rotation", 0f, 180f);
                    animator.setDuration(100);
                    animator.start();

                }else {
                    ObjectAnimator animator = ObjectAnimator.ofFloat(function_arrow, "rotation", 180f, 0f);
                    animator.setDuration(100);
                    animator.start();

                }

                break;
            case R.id.vip_function_pressfriend:
            case R.id.vip_function_shakefriend:
            case R.id.vip_function_showallfriend:
            case R.id.vip_function_allclear:
            case R.id.vip_function_lifenoteprivacy:
            case R.id.vip_function_infortrans:

                //显示popuwindow
                showVipFunctionDetails();
                break;
            default:
                break;
        }
    }

    /****
     * 显示vip6大功能的详情介绍
     */
    private void showVipFunctionDetails() {
        final PopupWindow popupWindow =  new PopupWindow(CreateVoVIPAccountActivityVo.this);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        View vv = LayoutInflater.from(CreateVoVIPAccountActivityVo.this).inflate(R.layout.lay_vip_function_details,null);
        popupWindow.setContentView(vv);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
       // popupWindow.setTouchable(false);

        popupWindow.showAsDropDown(function_vip_ll);
        vv.findViewById(R.id.pp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode){
            case 200:

                String state=data.getStringExtra("state");
                String state02=data.getStringExtra("state02");

                LoginData.LoginInfo.LoginAccountInfo user= myUtils.readUser(CreateVoVIPAccountActivityVo.this);
                if(user!=null){
                    Glide.with(CreateVoVIPAccountActivityVo.this).load(user.getAvatar()).into(vip_head);
                    vip_name.setText(user.getNickname());
                    if("1".equals(user.getVip())){
                        vip_state.setVisibility(View.VISIBLE);
                    }else {
                        vip_state.setVisibility(View.GONE);
                    }
                }

                if("success".equals(state)){
                    final Dialog dialog=new Dialog(CreateVoVIPAccountActivityVo.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.lay_vip_pay_success);
                    TextView stateTv=dialog.findViewById(R.id.state_tv);
                    if("1".equals(state02)){
                        stateTv.setText("会员开通成功");
                        sendBroadcast(new Intent("openVipOK"));
                    }else {
                        stateTv.setText("支付成功");
                    }

                    dialog.findViewById(R.id.vip_success_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            break;

            default:
            break;


        }


    }

    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
           String action= intent.getAction();
            if("wxpayOk".equals(action)){
                //***********微信支付成功后的操作**************************************************

                String state=intent.getStringExtra("state");
                String state02=intent.getStringExtra("state02");

                LoginData.LoginInfo.LoginAccountInfo user2= myUtils.readUser(CreateVoVIPAccountActivityVo.this);
                if(user2!=null){
                    Glide.with(CreateVoVIPAccountActivityVo.this).load(user2.getAvatar()).into(vip_head);
                    vip_name.setText(user2.getNickname());
                    if("1".equals(user2.getVip())){
                        vip_state.setVisibility(View.VISIBLE);
                    }else {
                        vip_state.setVisibility(View.GONE);
                    }
                }

                if("success".equals(state)){
                    final Dialog dialog=new Dialog(CreateVoVIPAccountActivityVo.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.lay_vip_pay_success);
                    TextView stateTv=dialog.findViewById(R.id.state_tv);
                    if("1".equals(state02)){
                        stateTv.setText("会员开通成功");
                    }else {
                        stateTv.setText("支付成功");
                    }

                    dialog.findViewById(R.id.vip_success_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                    sendBroadcast(new Intent("openVipOK"));
                }


            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(myReceiver!=null){
            unregisterReceiver(myReceiver);
        }
    }
}