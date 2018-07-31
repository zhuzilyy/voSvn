package com.zl.vo_.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zl.vo_.DemoApplication;
import com.zl.vo_.main.Entity.LoginData;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.activities.CreateVoVIPAccountActivityVo;
import com.zl.vo_.main.activities.VipPayWayActivityVo;
import com.zl.vo_.main.activities.VoBaseActivity;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.utils.Url;

import net.sourceforge.simcpux.bean.Constants;

import org.xutils.http.RequestParams;
import org.xutils.x;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.pay_result);
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		api.handleIntent(getIntent(), this);
		VoBaseActivity.addActivity(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}
	@Override
	public void onReq(BaseReq req) {

	}
	@Override
	public void onResp(BaseResp resp) {
		if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
			//֧支付成功
		//	Toast.makeText(this, "xxx,chengggong", Toast.LENGTH_SHORT).show();
			final String vipBefore= myUtils.readUser(WXPayEntryActivity.this).getVip();
			RequestParams params=new RequestParams(Url.AccountInfoURL);
			params.addParameter("huanxin_account",myUtils.readUser(WXPayEntryActivity.this).getHuanxin_account());

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
								myUtils.clearSharedUser(WXPayEntryActivity.this);
								myUtils.saveUser(user,WXPayEntryActivity.this);
								Intent intent=new Intent("wxpayOk");
								intent.putExtra("state","success");
								if(vipBefore==user.getVip()){
									intent.putExtra("state02","2");
								}
								sendBroadcast(intent);
								VipPayWayActivityVo.instance.finish();
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




		}else{
			finish();
		}
	}


}