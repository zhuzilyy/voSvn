package com.zl.vo_.main.activities;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
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
import android.widget.ScrollView;
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
import com.zl.vo_.util.Utils;
import com.zl.vo_.utils.Url;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
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
    @BindView(R.id.back)
    public ImageView back;
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
    private LinearLayout ll_vipnofree;
    private TextView tv_vipnofree;
    private TextView vip_endtime;
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
        LoginData.LoginInfo.LoginAccountInfo user  = myUtils.readUser(CreateVoVIPAccountActivityVo.this);
    }

    /***
     * 开通试用
     * @param userId
     */
    private void getVipTest(final String userId) {
        final Dialog dialog = new Dialog(CreateVoVIPAccountActivityVo.this);
        View vv = LayoutInflater.from(CreateVoVIPAccountActivityVo.this).inflate(R.layout.lay_create_vip_yesno, null);
        dialog.setContentView(vv);
        ImageView cancel = vv.findViewById(R.id.cancel_iv);
        TextView confirm = vv.findViewById(R.id.tv_confrim);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params= new RequestParams(Url.TestVip);
                params.addParameter("userid",userId);
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(final String result) {
                        Log.i("aa",""+result);
                        //开通试用，将vip == 1,sy==0
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObject =  new JSONObject(result);
                                    if(jsonObject!=null){
                                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                        if(jsonObject1!=null){
                                            JSONObject jsonObject2 = jsonObject1.getJSONObject("info");
                                            if(jsonObject2!=null){
                                                JSONObject jsonObject3 = jsonObject2.getJSONObject("vip");
                                                if(jsonObject3!=null){
                                                String endTime = jsonObject3.getString("new_endtime");
                                                    //开通成功，更新数据
                                                    LoginData.LoginInfo.LoginAccountInfo user = myUtils.readUser(CreateVoVIPAccountActivityVo.this);
                                                    getUserLastInfo(user.getHuanxin_account());
                                                }
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.i("aa",""+ex.getMessage());
                    }
                    @Override
                    public void onCancelled(CancelledException cex) {
                    }
                    @Override
                    public void onFinished() {
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /***
     * 获取用户的信息（开通试用）
     * @param huanxin_account
     */
    private void getUserLastInfo(String huanxin_account) {
        RequestParams params=new RequestParams(Url.AccountInfoURL);
        params.addParameter("huanxin_account",myUtils.readUser(CreateVoVIPAccountActivityVo.this).getHuanxin_account());
        x.http().post(params, new MyCommonCallback<Result<LoginData>>() {
            @Override
            public void success(Result<LoginData> data) {
                // loadingview.setVisibility(View.GONE);
                LoginData loginData=data.data;
                LoginData.LoginInfo loginInfo=loginData.getInfo();
                if("0".equals(data.code)){
                    if(loginInfo!=null){
                        LoginData.LoginInfo.LoginAccountInfo user=loginInfo.getAccount_info();
                        if(user!=null){
                            //清除本地保存的用户信息
                            myUtils.clearSharedUser(CreateVoVIPAccountActivityVo.this);
                            myUtils.saveUser(user,CreateVoVIPAccountActivityVo.this);
                            //免费试用消失
                            ll_vipnofree.setVisibility(View.GONE);
                            //显示到期时间，显示vip图样
                            if(!TextUtils.isEmpty(user.getVip_enddate())){
                                vip_endtime.setText(user.getVip_enddate()+"到期");
                                vip_endtime.setVisibility(View.VISIBLE);
                            }
                            vip_state.setVisibility(View.VISIBLE);

                            Intent intent=new Intent();
                            intent.setAction("wxpayOk");
                            intent.putExtra("state","success");
                            intent.putExtra("state02","1");
                            sendBroadcast(intent);

                            //


                        }
                    }
                }
            }
            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                Log.i("io","log");
            }
        });
    }

    private void getData() {
        loading_view.setVisibility(View.VISIBLE);
        RequestParams params=new RequestParams(Url.GetVIPProductUrl);
        params.addParameter("userid", myUtils.readUser(CreateVoVIPAccountActivityVo.this).getUserid());
        x.http().post(params, new MyCommonCallback<Result<VIPProductData>>() {
            @Override
            public void success(Result<VIPProductData> data) {
                Log.i("vip",data+"");
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
        vip_endtime= headerview.findViewById(R.id.vip_endtime);


        ll_vipnofree=headerview.findViewById(R.id.ll_vipnofree);
        tv_vipnofree = headerview.findViewById(R.id.tv_vipnofree);
        final LoginData.LoginInfo.LoginAccountInfo user2  = myUtils.readUser(CreateVoVIPAccountActivityVo.this);
        if(user2!=null){
            if("0".equals(user2.getSy())){
               ll_vipnofree.setVisibility(View.VISIBLE);
            }else if("1".equals(user2.getSy())) {
                ll_vipnofree.setVisibility(View.GONE);
            }
        }

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
        function_four_ll.setOnClickListener(this);

        if(vip_webview!=null){
            setVIPWebView(vip_webview);
        }
        LoginData.LoginInfo.LoginAccountInfo user= myUtils.readUser(CreateVoVIPAccountActivityVo.this);
        if(user!=null){
            Glide.with(CreateVoVIPAccountActivityVo.this).load(user.getAvatar()).into(vip_head);

            if("1".equals(user.getVip())){
                vip_state.setVisibility(View.VISIBLE);
                vip_name.setText(user.getNickname()+"(蓝钻会员)");
            }else {
                vip_state.setVisibility(View.GONE);
                vip_name.setText(user.getNickname());
            }

            //VIP到期时间
            if(!TextUtils.isEmpty(user.getVip_enddate())){
                vip_endtime.setVisibility(View.VISIBLE);
                vip_endtime.setText(user.getVip_enddate()+"到期");
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

        //开通vip试用
        ll_vipnofree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user2!=null){
                    getVipTest(user2.getUserid());
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
        mWebView.loadUrl(Url.NEW_VIPWebView);
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
                //view.loadUrl(url);
                if (url.equals("http://api.ykhswl.net/vo_admin_system/view.php?id=11")) {
                    Intent intent = new Intent(CreateVoVIPAccountActivityVo.this, Help_Feedback.class);
                    intent.putExtra("title","会员服务协议");
                    intent.putExtra("url","http://api.ykhswl.net/vo_admin_system/view.php?id=11");
                    startActivity(intent);
                    return true;
                } else {
                    return false;
                }
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
        //http://api.ykhswl.net/vo_admin_system/view.php?id=11
    }
    @OnClick({R.id.iv_back,R.id.vip_btn_submit,R.id.back})
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
            case R.id.function_four_ll:
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
        LinearLayout ll_content=vv.findViewById(R.id.ll_content);
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
        ll_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow!=null && popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
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

                    vip_name.setText(user.getNickname()+"(蓝钻会员)");
                    vip_endtime.setVisibility(View.VISIBLE);
                    vip_endtime.setText(user.getVip_enddate());
                    ll_vipnofree.setVisibility(View.GONE);
                    if("1".equals(state02)){
                        stateTv.setText("会员开通成功");
                        sendBroadcast(new Intent("openVipOK"));
                    }else {
                        stateTv.setText("支付成功");
                        sendBroadcast(new Intent("openVipOK"));
                    }

                    dialog.findViewById(R.id.vip_success_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                //支付成功之后请求接口

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
                String state02 =intent.getStringExtra("state02");

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

                    vip_name.setText(user2.getNickname()+"(蓝钻会员)");
                    vip_endtime.setVisibility(View.VISIBLE);
                    vip_endtime.setText(user2.getVip_enddate());
                    ll_vipnofree.setVisibility(View.GONE);

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
