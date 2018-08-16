package com.zl.vo_.main.main_fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zl.vo_.Constant;
import com.zl.vo_.DemoApplication;
import com.zl.vo_.R;
import com.zl.vo_.main.activities.AboutVoActivityVo;
import com.zl.vo_.main.activities.Find_GameActivityVo;
import com.zl.vo_.main.activities.Help_Feedback;
import com.zl.vo_.main.activities.Help_Feedback_front;
import com.zl.vo_.main.activities.addFriendActivity_ContactsVo;
import com.zl.vo_.main.activities.addFriendActivity_SearchVo;
import com.zl.vo_.main.views.DetailsTypePopupWindow;
import com.zl.vo_.ui.GroupsActivity;
import com.zl.vo_.ui.ScanCaptureActivity;
import com.zl.vo_.util.WhiteBgBitmapUtil;
import com.zl.vo_.utils.AppConst;
import com.zl.vo_.utils.Url;
import com.zl.vo_.widget.FXPopWindow;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main_FragmentFind extends Fragment implements View.OnClickListener {
    private View vv;
    @BindView(R.id.iv_add)
    public ImageView iv_add;
    @BindView(R.id.iv_search)
    public ImageView iv_search;
    public   FXPopWindow fxPopWindow;
    //---------------------------
    @BindView(R.id.re_qrcode)
    public RelativeLayout re_qcode;
    @BindView(R.id.re_game)
    public RelativeLayout re_game;
    @BindView(R.id.re_helpfeedback)
    public RelativeLayout re_helpfeedback;
    @BindView(R.id.re_aboutVO)
    public RelativeLayout re_aboutVO;
    @BindView(R.id.tv_update_flag)
    public TextView tv_update_flag;
    @BindView(R.id.ll_find)
    public LinearLayout ll_find;
    private PopupWindow pw_share;
    private View view_share;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vv=inflater.inflate(R.layout.fragment_find, container, false);
        ButterKnife.bind(this,vv);
        mInit();
        return vv;
    }

    private void mInit() {
        if(DemoApplication.hasNerVersion == 1){
            tv_update_flag.setVisibility(View.VISIBLE);
        }else if(DemoApplication.hasNerVersion == 0){
            tv_update_flag.setVisibility(View.INVISIBLE);
        }
        view_share=LayoutInflater.from(getActivity()).inflate(R.layout.pw_share,null);



    }
    @OnClick({R.id.iv_add,R.id.iv_search,R.id.re_qrcode,R.id.re_game,
            R.id.re_helpfeedback,R.id.re_aboutVO,R.id.re_share})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_add:
                DetailsTypePopupWindow typePopupWindow = new DetailsTypePopupWindow(getActivity(),iv_add,"","");
                typePopupWindow.setmItemsOnClick(new DetailsTypePopupWindow.ItemsOnClick() {
                    @Override
                    public void itemsOnClick(int position) {
                        switch (position) {
                            case 0:
                                startActivity(new Intent(getActivity(),GroupsActivity.class));
                                break;
                            //添加新的好友
                            case 1:
                                startActivity(new Intent(getActivity(),addFriendActivity_ContactsVo.class));
                                break;
                            //扫一扫
                            case 2:
                                startActivity(new Intent(getActivity(),ScanCaptureActivity.class));
                                break;
                            //帮助及反馈
                            case 3:
                                Intent intent=new Intent(getActivity(),Help_Feedback.class);
                                intent.putExtra("url", Url.NEW_HELPANDFEEDBACK);
                                intent.putExtra("param","");
                                intent.putExtra("title","功能介绍");
                                startActivity(intent);
                                break;
                        }
                    }
                });


                break;
            case R.id.iv_search:
                startActivity(new Intent(getActivity(), addFriendActivity_SearchVo.class));
                break;
            case R.id.re_qrcode:
                //二维码
                startActivity(new Intent(getActivity(),ScanCaptureActivity.class));
                break;
            case R.id.re_game:
                startActivity(new Intent(getActivity(), Find_GameActivityVo.class));
                //游戏
                break;
            case R.id.re_helpfeedback:
                //帮助与反馈
                Intent intent=new Intent(getActivity(),Help_Feedback_front.class);
              /*  intent.putExtra("url","http://47.95.115.55:8080/voadmin/home/api/page_list");
                intent.putExtra("param","16");
                intent.putExtra("title","帮助与反馈");*/
                getActivity().startActivity(intent);
                break;
            case R.id.re_aboutVO:
                //关于vo
                startActivity(new Intent(getActivity(), AboutVoActivityVo.class));
                break;
            //分享给好友
            case R.id.re_share:
                //分享微信链接
                WX_share();
                break;
        }
    }

    /***
     * 分享微信链接，邀请好友下载vo
     */
    private void WX_share() {
        pw_share = new PopupWindow(getActivity());
        pw_share.setContentView(view_share);
        pw_share.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        pw_share.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        pw_share.setTouchable(true);
        pw_share.setFocusable(true);
        pw_share.setBackgroundDrawable(new BitmapDrawable());
        pw_share.setAnimationStyle(R.style.AnimBottom);
        pw_share.showAtLocation(ll_find, Gravity.BOTTOM, 0, 0);
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
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getActivity().getWindow().setAttributes(lp);
    }


    //分享到微信好友
    private void shareFriends(String type) {
        IWXAPI mWxApi;
        mWxApi = WXAPIFactory.createWXAPI(getActivity(), AppConst.APP_ID, false);
        // 将该app注册到微信
        mWxApi.registerApp(AppConst.APP_ID);
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = Constant.DOWN_LOAD_URL;
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
