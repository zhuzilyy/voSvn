package com.zl.vo_.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.zl.vo_.DemoApplication;
import com.zl.vo_.DemoModel;
import com.zl.vo_.R;
import com.zl.vo_.main.Entity.LoginData;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.Entity.WXAccessTokenInfo;
import com.zl.vo_.main.Entity.WXErrorInfo;
import com.zl.vo_.main.activities.BindPhoneActivityVo;
import com.zl.vo_.main.activities.LoginActivity;
import com.zl.vo_.main.activities.VoBaseActivity;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_fragments.MainActivity;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.utils.AppConst;
import com.zl.vo_.utils.Url;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	private static final int RETURN_MSG_TYPE_LOGIN = 1;
	private static final int RETURN_MSG_TYPE_SHARE = 2;
	private Gson gson=new Gson();
	public static Activity instance;
	private String userOpenId;
	private DemoModel demoModel=new DemoModel(WXEntryActivity.this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lay_wxentry);
		VoBaseActivity.addActivity(this);
		//如果没回调onResp，八成是这句没有写
		DemoApplication.mWxApi.handleIntent(this.getIntent(), this);
		instance=this;
	}

	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq req) {
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	//app发送消息给微信，处理返回消息的回调
	@Override
	public void onResp(BaseResp resp) {
		//Log.i("wx",resp.errStr);

	switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				if (RETURN_MSG_TYPE_SHARE == resp.getType()){
					Toast.makeText(this, "分享失败", Toast.LENGTH_SHORT).show();
					finish();
				}
				else Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show();
				break;
			case BaseResp.ErrCode.ERR_OK:
				Log.i("ee","000");

				switch (resp.getType()) {
					case RETURN_MSG_TYPE_LOGIN:
						//拿到了微信返回的code,立马再去请求access_token
						String code = ((SendAuth.Resp) resp).code;
						Log.i("wx","code = " + code);
						//就在这个地方，用网络库什么的或者自己封的网络api，发请求去咯，注意是get请求
						// 通过code获取授权口令access_token
						getAccessToken(code);
						break;
					case RETURN_MSG_TYPE_SHARE:
						Toast.makeText(this, "分享成功", Toast.LENGTH_SHORT).show();
						finish();
						break;
				}
				break;
		}
	}

	/***
	 * 获取AccessToken
	 * @param code
     */
	private void getAccessToken(String code) {
		String GetAccessTokenURL="https://api.weixin.qq.com/sns/oauth2/access_token?" +
				"appid=" + AppConst.APP_ID +
				"&secret=" + AppConst.APP_SECRET +
				"&code=" + code +
				"&grant_type=authorization_code";

		RequestParams params=new RequestParams(GetAccessTokenURL);
		x.http().get(params, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				// 判断是否获取成功，成功则去获取用户信息，否则提示失败
				processGetAccessTokenResult(result);
				Log.i("ee",result);
			}
			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				Log.i("err","错误=="+ex);
			}

			@Override
			public void onCancelled(CancelledException cex) {

			}

			@Override
			public void onFinished() {

			}
		});

	}


	/**
	 * 处理获取的授权信息结果
	 * @param response 授权信息结果
	 */
	private void processGetAccessTokenResult(String response) {
		// 验证获取授权口令返回的信息是否成功
		if (validateSuccess(response)) {
			// 使用Gson解析返回的授权口令信息
			WXAccessTokenInfo tokenInfo = gson.fromJson(response, WXAccessTokenInfo.class);
			// 保存信息到手机本地
			myUtils.saveAccessInfotoLocation(tokenInfo,WXEntryActivity.this);
			// 获取用户信息
			getUserInfo(tokenInfo.getAccess_token(), tokenInfo.getOpenid());
			} else {
			// 授权口令获取失败，解析返回错误信息
			WXErrorInfo wxErrorInfo = gson.fromJson(response, WXErrorInfo.class);
			// 提示错误信息
			Log.i("wxErrorInfo","错误信息: " + wxErrorInfo.getErrmsg());

			}
	}

	/**
	 * 验证是否成功
	 *
	 * @param response 返回消息
	 * @return 是否成功
	 */
	private boolean validateSuccess(String response) {
		String errFlag = "errmsg";
		return (errFlag.contains(response) && !"ok".equals(response))
				|| (!"errcode".contains(response) && !errFlag.contains(response));
	}

	/*****
	 * 获取用户信息
	 * @param access_token
	 * @param openid
     */
	private void getUserInfo(String access_token, String openid){
		String url="https://api.weixin.qq.com/sns/userinfo?" +
				"access_token=" + access_token +
				"&openid=" + openid;
		RequestParams params=new RequestParams(url);
		x.http().get(params, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				//Toast.makeText(WXEntryActivity.this, result+"", Toast.LENGTH_SHORT).show();
				try {
					JSONObject jsonObject=new JSONObject(result);
					String openid=jsonObject.getString("openid");
					userOpenId=openid;
					String unionid=jsonObject.getString("unionid");
					String nickname=jsonObject.getString("nickname");
					int sex=jsonObject.getInt("sex");
					String headimgurl=jsonObject.getString("headimgurl");
					Intent intent=new Intent();
					intent.setAction("com.action.startLogin");
					sendBroadcast(intent);
					//跳转到微信信息也
					Intent intent1=new Intent(WXEntryActivity.this,wxInfoActivityVo.class);
					intent1.putExtra("openid",openid);
					intent1.putExtra("unionid",unionid);
					intent1.putExtra("nickname",nickname);
					if (sex == 1) {
						intent1.putExtra("sex", "0");
					} else {
						intent1.putExtra("sex", "1");
					}

					intent1.putExtra("headimgurl",headimgurl);

					startActivity(intent1);
					finish();

					//WechatLogin(openid,unionid,nickname,sex,headimgurl);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				//在这个地方获取到用户的信息后，将信息上传到自己分服务器，通过判断 是否绑定openid 来决定去哪：
					//a:手机号注册 并绑定openid  直接登录操作，返回userInfo
					//b:手机号注册，但未绑定openid,弹出绑定手机号和输入验证码界面，提交到后台，进行openid和手机号的绑定
				    //c:手机号未注册，第一次直接微信登录，跳转到注册界面，完善信息并将手机号和openID绑定并登录
				//finish();
			}
			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				Log.i("wx",ex+"获取用户信息==错误");

			}

			@Override
			public void onCancelled(CancelledException cex) {

				Log.i("wx",cex+"获取用户信息==错误");
			}

			@Override
			public void onFinished() {

			}
		});
	}

	//微信登录
	private void WechatLogin(final String openId,final String unionId,final String nickname,final int sex,final String headimgurl){
		RequestParams params=new RequestParams(Url.WX_LOGIN);
		params.addParameter("openid",openId);
		params.addParameter("unionid",unionId);
		x.http().post(params, new MyCommonCallback<Result<LoginData>>() {
			@Override
			public void success(Result<LoginData> data) {
				Intent intentSuccess=new Intent();
				intentSuccess.setAction("com.action.successLogin");
				sendBroadcast(intentSuccess);
				String code = data.code;
				if (code.equals("-1111")) {

					Intent intent = new Intent(WXEntryActivity.this, BindPhoneActivityVo.class);
					intent.putExtra("openid", openId);
					intent.putExtra("unionid", unionId);
					intent.putExtra("nickname", nickname);
					intent.putExtra("headimgurl", headimgurl);
					if (sex == 1) {
						intent.putExtra("sex", "0");
					} else {
						intent.putExtra("sex", "1");
					}

					startActivity(intent);
					finish();

				}else if ("0".equals(code)) {
					LoginData loginData = data.data;
					if (loginData != null) {
						LoginData.LoginInfo loginInfo = loginData.getInfo();
						if (loginInfo != null) {
							LoginData.LoginInfo.LoginAccountInfo user=loginInfo.getAccount_info();
							if(user!=null){
								//登录环信
								LoginHX(user,user.getAccount());
							}
						}
					}
				}
			}
			@Override
			public void error(Throwable ex, boolean isOnCallback) {
				Intent intentFail=new Intent();
				intentFail.setAction("com.action.failLogin");
				sendBroadcast(intentFail);
			}
		});
	}

	/****
	 * 登录环信
	 *
	 */
	private void LoginHX(final LoginData.LoginInfo.LoginAccountInfo user, final String name) {
		// DemoDBManager.getInstance().closeDB();
		EMClient.getInstance().login(user.getHuanxin_account(),user.getHuanxin_account(),new EMCallBack() {
			@Override
			public void onSuccess() {
				Log.i("cspp", "onSuccess");
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// ** manually load all local groups and conversation
						EMClient.getInstance().groupManager().loadAllGroups();
						EMClient.getInstance().chatManager().loadAllConversations();

						demoModel.setCurrentUserName(user.getHuanxin_account());
//                        boolean updatenick = EMClient.getInstance().updateCurrentUserNick(
//                                user.getNickname());
//                        if (!updatenick) {
//                            Log.e("LoginActivity", "update current user nick fail");
//                        }
						//保存账号
//						String accont= DemoDBManager.getInstance().getSwitchAccount(name);
//						if(!name.equals(accont)){
//							DemoDBManager.getInstance().saveAccountSwitch(name);
//						}
						/****
						 * 保存当前登录用户的信息
						 */
						myUtils.saveUser(user,WXEntryActivity.this);
						startActivity(new Intent(WXEntryActivity.this, MainActivity.class));
						LoginActivity.instance.finish();

						LoginActivity.instance.finish();
						finish();
					}
				});

			}

			@Override
			public void onError(int i, final String s) {
				Log.i("cspp", "onError" + s);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(WXEntryActivity.this, s, Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onProgress(int i, String s) {
				Log.i("cspp", "onProgress" + s);
			}
		});


	}



}