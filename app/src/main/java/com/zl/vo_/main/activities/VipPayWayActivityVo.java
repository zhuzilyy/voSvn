package com.zl.vo_.main.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.SyncStateContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.zl.vo_.Constant;
import com.zl.vo_.R;
import com.zl.vo_.main.Entity.LoginData;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.Entity.WxPayData;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.utils.Url;

import net.sourceforge.simcpux.bean.Constants;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.x;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/12/25.
 */

public class VipPayWayActivityVo extends VoBaseActivity implements View.OnClickListener{
    public static String total_fee;
    public static String out_trade_no;
    public String pid;
    public RelativeLayout way_zfb_re;
    @BindView(R.id.way_wx_re)
    public RelativeLayout way_wx_re;
    @BindView(R.id.way_submit_btn)
    public Button way_submit_btn;
    @BindView(R.id.ra_zfb)
    public RadioButton ra_zfb;
    @BindView(R.id.ra_wx)
    public RadioButton ra_wx;
    private int currentPayType;
    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;
    //支付宝
    public static String APPID,totalFee,orderNumber ;
    /** 商户私钥，pkcs8格式 */
    public static  String RSA_PRIVATE;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private String mOrderstr;
    public static VipPayWayActivityVo instance;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

         //在这里做支付后的处理
        };
    };


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.lay_vip_pay_way);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        pid=getIntent().getStringExtra("pid");
        if(!TextUtils.isEmpty(pid)){
            getZFBInfo(pid);
        }
        out_trade_no="333";
        mInit();
    }

    /****
     * 从自己服务器上获取支付信息
     * @param pid
     */
    private void getZFBInfo(String pid) {
        RequestParams params=new RequestParams(Url.ZFBUrl);
        params.addParameter("product_id",pid);
        params.addParameter("userid", myUtils.readUser(VipPayWayActivityVo.this).getUserid());
        x.http().post(params, new MyCommonCallback<Result<ZFBData>>() {
            @Override
            public void success(Result<ZFBData> data) {
                Log.i("ss",data+"");
                if("0".equals(data.code)){
                    ZFBData zfbData = data.data;
                    ZFBData.ZFBInfo zfbInfo=zfbData.getInfo();
                    String orderstr = zfbInfo.getOrderstr();
                    mOrderstr=orderstr;
                }
            }
            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                Log.i("sss",ex.getMessage());
            }
        });
    }
    private void mInit() {
        instance=this;
    }
    @OnClick({R.id.way_wx_re,R.id.way_zfb_re,R.id.way_submit_btn})
    @Override
    public void onClick(View v) {
       switch(v.getId()){
           case R.id.way_wx_re:
               //微信支付  1
               ra_wx.setChecked(true);
               ra_zfb.setChecked(false);
               currentPayType=1;
               break;
           case R.id.way_zfb_re:
               //支付宝支付  2
               ra_wx.setChecked(false);
               ra_zfb.setChecked(true);
               currentPayType=2;
               break;
           case R.id.way_submit_btn:
               if((!ra_wx.isChecked())&&(!ra_zfb.isChecked())){
                   Toast.makeText(this, "请选择支付方式", Toast.LENGTH_SHORT).show();
                   return;
               }
                if(currentPayType==1){
                    //微信支付
                    wxPay();
                }else {
                    //支付宝支付
                    alipay();
                    break;
                }}
    }

    /****
     * 微信支付
     */
    private void wxPay() {
        loading_view.setVisibility(View.VISIBLE);
        RequestParams params=new RequestParams(Url.WxPayUrl);
        params.addParameter("product_id",pid);
        params.addParameter("userid",myUtils.readUser(VipPayWayActivityVo.this).getUserid());
        x.http().post(params, new MyCommonCallback<Result<WxPayData>>() {
            @Override
            public void success(Result<WxPayData> data) {
                loading_view.setVisibility(View.GONE);
                if("0".equals(data.code)){
                    WxPayData payData=data.data;
                    WxPayData.WxPayInfo payInfo=payData.getInfo();
                    Constants.APP_ID=payInfo.getAppid();
                    Constants.MCH_ID=payInfo.getMch_id();
                    Constants.NOTIFY_URL=payInfo.getNotify_url();
                    double totalfeeDouble=Double.parseDouble(payInfo.getTotal_fee())*100;
                    int totalfeeInt= (int) totalfeeDouble;
                    Constants.TOTAL_FEE=totalfeeInt+"";
                    Constants.OUT_TRADE_NO=payInfo.getOut_trade_no();
                    if(!TextUtils.isEmpty(Constants.TOTAL_FEE)&&!TextUtils.isEmpty(Constants.OUT_TRADE_NO)){
                        WxPayActivity payActivity=new WxPayActivity(VipPayWayActivityVo.this);
                    }
                }
            }
            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                loading_view.setVisibility(View.GONE);
                Log.i("ui",ex.getMessage());
            }
        });
    }
    private void alipay() {

        boolean isInstall = isAppInstalled(VipPayWayActivityVo.this,"com.eg.android.AlipayGphone");
        if(!isInstall){
            Toast.makeText(instance, "请先下载安装支付宝app", Toast.LENGTH_SHORT).show();
            return;
        }


        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(VipPayWayActivityVo.this);
                if(alipay!=null){
                    if(!TextUtils.isEmpty(mOrderstr)){
                        final Map<String,String> result = alipay.payV2(mOrderstr,true);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String result_=result.get("result");
                                Log.i("","");
                                if(!TextUtils.isEmpty(result_)){
                                    try {
                                        JSONObject jsonObject=new JSONObject(result_);
                                        JSONObject jsonObject1=jsonObject.getJSONObject("alipay_trade_app_pay_response");
                                       String code_= jsonObject1.getString("code");
                                        if("10000".equals(code_)){
                                            //支付成功
                                           final String vipBefore= myUtils.readUser(VipPayWayActivityVo.this).getVip();
                                            RequestParams params=new RequestParams(Url.AccountInfoURL);
                                            params.addParameter("huanxin_account",myUtils.readUser(VipPayWayActivityVo.this).getHuanxin_account());
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
                                                                myUtils.clearSharedUser(VipPayWayActivityVo.this);
                                                                myUtils.saveUser(user,VipPayWayActivityVo.this);
                                                                Intent intent=new Intent();
                                                                intent.putExtra("state","success");
                                                                if(vipBefore==user.getVip()){
                                                                    intent.putExtra("state02","2");
                                                                }
                                                                setResult(200,intent);
                                                                finish();
                                                            }
                                                        }
                                                    }
                                                }
                                                @Override
                                                public void error(Throwable ex, boolean isOnCallback) {
                                                    Log.i("io","log");
                                                }
                                            });
                                        }else {
                                            //支付失败
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                }
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public boolean isAppInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<String>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);
    }

}
