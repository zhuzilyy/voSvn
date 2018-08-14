package com.zl.vo_.main.activities;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;
import com.superrtc.util.BitmapUtil;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zl.vo_.Constant;
import com.zl.vo_.DemoApplication;
import com.zl.vo_.DemoModel;
import com.zl.vo_.R;
import com.zl.vo_.main.Entity.PhoneContactsEntity;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.Entity.friendHintEntivity;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.myindexbar.CityAdapter_contacts;
import com.zl.vo_.myindexbar.DividerItemDecoration;
import com.zl.vo_.util.WhiteBgBitmapUtil;
import com.zl.vo_.utils.AppConst;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/21.
 */

public class addFriendActivity_ContactsVo extends VoBaseActivity implements View.OnClickListener {
//    @BindView(R.id.newFriend_lv)
//    public ListView newFriend_lv;
  // private static final String INDEX_STRING_TOP = "↑";
    @BindView(R.id.ll_phoneContacts)
    public LinearLayout ll_phoneContacts;

    @BindView(R.id.tv_search_friend)
    public TextView tv_search_friend;
    @BindView(R.id.tv_addFriend)
    public TextView tv_addFriend;
    private View view_share;

    public DemoModel demoModel=new DemoModel(addFriendActivity_ContactsVo.this);
    //******************************
    @BindView(R.id.rv)
    public RecyclerView mRv;
    @BindView(R.id.header)
    public com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader header;

    public CityAdapter_contacts mAdapter;
    public LinearLayoutManager mManager;
    public List<PhoneContactsEntity> mDatas = new ArrayList<>();
    public SuspensionDecoration mDecoration;

    /**
     * 右侧边栏导航区域
     */
    @BindView(R.id.indexBar)
    public IndexBar mIndexBar;

    /**
     * 显示指示器DialogText
     */
    @BindView(R.id.tvSideBarHint)
    public TextView mTvSideBarHint;
    @BindView(R.id.wx_share_ll)
    public LinearLayout wx_share_ll;
    private PopupWindow pw_share;
    @BindView(R.id.container)
    public RelativeLayout container;


   // public List<friendHintEntivity.friendHintInfo.friend_hintArr> bigList=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_addfriend);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        mRv.setLayoutManager(mManager = new LinearLayoutManager(addFriendActivity_ContactsVo.this));
        header.attachTo(mRv);
        //获取是否显示通讯录
        boolean b=demoModel.getSettingRecommedFriend();
        if(!b){
            ll_phoneContacts.setVisibility(View.GONE);
        }

        //上传通讯录字符串，返回是否是我的朋友
        getData(DemoApplication.phoneStrRes);
        mInit();
    }

    /****
     * 上传通讯录字符串，返回是否是我的好友
     */
    private void getData(String contactSTR) {

        RequestParams params=new RequestParams(Url.Friend_hintURL);

        params.addParameter("userid", myUtils.readUser(addFriendActivity_ContactsVo.this).getUserid());
        params.addParameter("address_book",contactSTR);
        params.setConnectTimeout(300000);
        DemoApplication.nn=201;
        x.http().post(params, new MyCommonCallback<Result<friendHintEntivity>>() {
            @Override
            public void success(Result<friendHintEntivity> data) {
                friendHintEntivity entivity=data.data;
                friendHintEntivity.friendHintInfo hintInfo=entivity.getInfo();
                if(hintInfo!=null){
                   List<friendHintEntivity.friendHintInfo.friend_hintArr> hintArrs=hintInfo.getFriend_hint_arr();
                    FormmDatas(hintArrs);
                   // mAdapter.notifyDataSetChanged();
                 //   mRv.setLayoutManager(mManager = new LinearLayoutManager(addFriendActivity_ContactsVo.this));
                    mAdapter = new CityAdapter_contacts(addFriendActivity_ContactsVo.this,mDatas);
                    mRv.setAdapter(mAdapter);
                    mRv.addItemDecoration(mDecoration = new SuspensionDecoration(addFriendActivity_ContactsVo.this,mDatas));
                    //如果add两个，那么按照先后顺序，依次渲染。
                 //   mRv.addItemDecoration(new DividerItemDecoration(addFriendActivity_ContactsVo.this, DividerItemDecoration.VERTICAL_LIST));
                    //indexbar初始化
                    mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                            .setNeedRealIndex(true)//设置需要真实的索引
                            .setmLayoutManager(mManager);//设置RecyclerView的LayoutManager
                    mIndexBar.setmSourceDatas(mDatas)//设置数据
                            .invalidate();
                    mDecoration.setmDatas(mDatas);
                    mAdapter.setMyContactClickListener(new CityAdapter_contacts.myContactsClickListener() {
                        @Override
                        public void onMyContactItemClick(int positon) {
                            if("2".equals(mDatas.get(positon).getTag())){
                                //发送短信
                                sendSMSwithLink(mDatas.get(positon).getMobile());
                            }else {
                                // Toast.makeText(context, "进入个人详情界面", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(addFriendActivity_ContactsVo.this, UserDetailsActivityVo.class);
                                intent.putExtra("HXid",mDatas.get(positon).getHuanxin_account());
                                intent.putExtra("way","apply_mobile");
                                startActivity(intent);
                            }
                        }
                        @Override
                        public void onMyContactItemLongClick(int position) {
                        }
                    });
                }
            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                Log.i("xx","钢铁侠=="+ex.getMessage());

                int n=9/5;
                Log.i("uu",n+""+ex.getMessage());

            }
        });


    }

    /****
     * 发送短信邀请
     * @param mobile
     */
    private void sendSMSwithLink(final String mobile) {
        final Dialog dialog=new Dialog(addFriendActivity_ContactsVo.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.lay_dia_sms);
        dialog.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    SmsManager smsManager = SmsManager.getDefault();
                    PendingIntent sentIntent = PendingIntent.getBroadcast(addFriendActivity_ContactsVo.this, 0, new Intent(), 0);
                    smsManager.sendTextMessage(mobile, null, "快来下载VO，和好友一起畅享VO娱乐"+ Constant.DOWN_LOAD_URL, sentIntent, null);
                    Toast.makeText(addFriendActivity_ContactsVo.this, "短信已发送", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(addFriendActivity_ContactsVo.this, "短信发送失败", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
        dialog.show();







    }
    /***
     *
     * @param hintArrs
     */
    private void FormmDatas(List<friendHintEntivity.friendHintInfo.friend_hintArr> hintArrs) {

        for (int i = 0; i < hintArrs.size(); i++) {
            friendHintEntivity.friendHintInfo.friend_hintArr hintArr=hintArrs.get(i);
            PhoneContactsEntity en=new PhoneContactsEntity();
            en.setAccount(hintArr.getAccount());
            en.setAvatar(hintArr.getAvatar());
            en.setHuanxin_account(hintArr.getHuanxin_account());
            en.setMobile(hintArr.getMobile());
            en.setNickname(hintArr.getNickname());
            en.setTag(hintArr.getTag());
            en.setUserid(hintArr.getUserid());
            mDatas.add(en);

        }

    }

    private void mInit() {
        view_share=LayoutInflater.from(addFriendActivity_ContactsVo.this).inflate(R.layout.pw_share,null);

//        mRv.setLayoutManager(mManager = new LinearLayoutManager(addFriendActivity_ContactsVo.this));
//        mAdapter = new CityAdapter_contacts(addFriendActivity_ContactsVo.this,mDatas);
//        mRv.setAdapter(mAdapter);
//        mRv.addItemDecoration(mDecoration = new SuspensionDecoration(addFriendActivity_ContactsVo.this,mDatas));
//        //如果add两个，那么按照先后顺序，依次渲染。
//        mRv.addItemDecoration(new DividerItemDecoration(addFriendActivity_ContactsVo.this, DividerItemDecoration.VERTICAL_LIST));
//        //indexbar初始化
//        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
//                .setNeedRealIndex(true)//设置需要真实的索引
//                .setmLayoutManager(mManager);//设置RecyclerView的LayoutManager
//        mIndexBar.setmSourceDatas(mDatas)//设置数据
//                .invalidate();
//        mDecoration.setmDatas(mDatas);





    }
    @OnClick({R.id.tv_addFriend,R.id.tv_search_friend,R.id.wx_share_ll})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tv_addFriend:
                //右上角‘添加好友’
                startActivity(new Intent(addFriendActivity_ContactsVo.this,addFriendActivity_SearchVo.class));
                break;
            case R.id.tv_search_friend:
                //搜索框
                startActivity(new Intent(addFriendActivity_ContactsVo.this,addFriendActivity_SearchVo.class));
                break;
            case R.id.wx_share_ll:
                //分享微信链接
                WX_share();
                break;
            default:
                break;
        }
    }

    /***
     * 分享微信链接，邀请好友下载vo
     */
    private void WX_share() {
        pw_share = new PopupWindow(addFriendActivity_ContactsVo.this);
        pw_share.setContentView(view_share);
        pw_share.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        pw_share.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        pw_share.setTouchable(true);
        pw_share.setFocusable(true);
        pw_share.setBackgroundDrawable(new BitmapDrawable());
        pw_share.setAnimationStyle(R.style.AnimBottom);
        pw_share.showAtLocation(container, Gravity.BOTTOM, 0, 0);
        // 设置pw弹出时候的背景颜色的变化
        backgroundAlpha(0.5f);
        pw_share.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

        view_share.findViewById(R.id.ll_wechat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //微信好友
                shareFriends("0");
                pw_share.dismiss();
            }
        });

        view_share.findViewById(R.id.ll_friendcircle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //朋友圈
                shareFriends("1");
                pw_share.dismiss();
            }
        });
    }
    /**
     * 设置添加屏幕的背景透明度
     *
     * @param
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }


    //分享到微信好友
    private void shareFriends(String type) {
        IWXAPI mWxApi;
        mWxApi = WXAPIFactory.createWXAPI(addFriendActivity_ContactsVo.this, AppConst.APP_ID, false);
        // 将该app注册到微信
        mWxApi.registerApp(AppConst.APP_ID);
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl =Constant.DOWN_LOAD_URL;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title ="人生知己，我在VO等你！";
        msg.description ="手机摇一摇即可隐藏好友,聊天办公,高端人群都在用" ;
        Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.mipmap.logo);
        Bitmap bitmap = WhiteBgBitmapUtil.changeColor(bmp);
        msg.setThumbImage(bitmap);

        //bitmap.recycle();
        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
        req.message = msg;
        /**
         * 判断是否是朋友圈
         */
        if("1".equals(type)){
            req.scene =  SendMessageToWX.Req.WXSceneTimeline;
        }else if("0".equals(type)){
            req.scene = SendMessageToWX.Req.WXSceneSession;
        }
        // 调用api接口发送数据到微信
        mWxApi.sendReq(req);
    }
    public static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
