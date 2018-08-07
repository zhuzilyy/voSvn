package com.zl.vo_.main.main_fragments;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cpiz.android.bubbleview.BubblePopupWindow;
import com.cpiz.android.bubbleview.BubbleTextView;
import com.cpiz.android.bubbleview.RelativePos;
import com.hyphenate.easeui.widget.EaseSwitchButton;
import com.squareup.picasso.Picasso;
import com.zl.vo_.DemoApplication;
import com.zl.vo_.R;
import com.zl.vo_.db.DemoDBManager;
import com.zl.vo_.main.Entity.LoginData;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.activities.CreateVoVIPAccountActivityVo;
import com.zl.vo_.main.activities.CurrentUserInfoActivityVo;
import com.zl.vo_.main.activities.Help_Feedback;
import com.zl.vo_.main.activities.LifeNote;
import com.zl.vo_.main.activities.SettingsActivityVo;
import com.zl.vo_.main.activities.SettingsPrivacyActivity3;
import com.zl.vo_.main.activities.SettingsPrivacyActivity4;
import com.zl.vo_.main.activities.addFriendActivity_ContactsVo;
import com.zl.vo_.main.activities.addFriendActivity_SearchVo;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.main.newacitivity.CancelLifePwdActivity;
import com.zl.vo_.main.newacitivity.FindLifePwdActivity;
import com.zl.vo_.main.newacitivity.FixLifePwdActivity;
import com.zl.vo_.main.newacitivity.SettingLifePwdActivity;
import com.zl.vo_.main.views.DetailsTypePopupWindow;
import com.zl.vo_.main.views.HypCommDialog;
import com.zl.vo_.main.views.LifeNoteSetDialog;
import com.zl.vo_.main.views.PracyFriendDialog;
import com.zl.vo_.main.views.vipDialog;
import com.zl.vo_.ui.GroupsActivity;
import com.zl.vo_.ui.ScanCaptureActivity;
import com.zl.vo_.utils.SPUtils;
import com.zl.vo_.utils.Url;
import com.zl.vo_.widget.FXPopWindow;

import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cpiz.android.bubbleview.Utils.dp2px;


public class Main_FragmentProfile extends Fragment implements View.OnClickListener {
    private View vv;
    @BindView(R.id.iv_add)
    public ImageView iv_add;
    @BindView(R.id.iv_search)
    public ImageView iv_search;
    public FXPopWindow fxPopWindow;
    @BindView(R.id.re_myinfo)
    public RelativeLayout re_myinfo;
    @BindView(R.id.re_settings)
    public RelativeLayout re_settings;
    @BindView(R.id.re_lifenote)
    public RelativeLayout re_lifenote;
    //-----------------------
    @BindView(R.id.iv_avatar)
    public ImageView iv_avatar;
    @BindView(R.id.tv_name)
    public TextView tv_name;
    @BindView(R.id.tv_void)
    public TextView tv_void;
    @BindView(R.id.re_createVOVIPAccount)
    public RelativeLayout re_createVOVIPAccount;
    public MyReceiver myReceiver;
    @BindView(R.id.switch_infopwd)
    public com.hyphenate.easeui.widget.EaseSwitchButton switch_infopwd;
    @BindView(R.id.card_ClearAllFriend)
    public CardView card_ClearAllFriend;
    @BindView(R.id.card_SetPricyPwd)
    public CardView card_SetPricyPwd;
    @BindView(R.id.card_InfoTransform)
    public CardView card_InfoTransform;
    @BindView(R.id.card_SetLifeNote)
    public CardView card_SetLifeNote;
    PracyFriendDialog friendDialog = null;
    LifeNoteSetDialog lifeNoteSetDialog = null;
    //是否展示vip功能项
    public Boolean hideVip = true;
    @BindView(R.id.ll_vipFunction)
    public LinearLayout ll_vipFunction;
    private String vip;
    private HypCommDialog dialog;
    public DemoDBManager dbManager;
    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;
    //***************************
    @BindView(R.id.guid01)
    public ImageView guide01;
    @BindView(R.id.guid02)
    public ImageView guide02;
    @BindView(R.id.guid03)
    public ImageView guide03;
    @BindView(R.id.guid04)
    public ImageView guide04;
    @BindView(R.id.info_iv01)
    public ImageView info_iv01;
    @BindView(R.id.info_iv02)
    public ImageView info_iv02;
    @BindView(R.id.re_info)
    public RelativeLayout re_info;
    public Handler handler = new Handler();
    public vipDialog dialog_vip;
    //*****************************************
    @BindView(R.id.flag01)
    public ImageView flag01;
    @BindView(R.id.flag02)
    public ImageView flag02;
    @BindView(R.id.flag_03)
    public ImageView flag03;
    @BindView(R.id.flag04)
    public ImageView flag04;
    @BindView(R.id.textView15)
    public TextView textView15;
    @BindView(R.id.textView16)
    public TextView textView16;
    @BindView(R.id.textView17)
    public TextView textView17;
    @BindView(R.id.textView14)
    public TextView textView14;
    //薛金柱添加
    @BindView(R.id.rl_lifeNote)
    RelativeLayout rl_lifeNote;
    @BindView(R.id.rl_setFriend)
    RelativeLayout rl_setFriend;
    @BindView(R.id.rl_addAndReduceFriend)
    RelativeLayout rl_addAndReduceFriend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vv = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, vv);
        //---------------------------
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("FixPersonInfoOK");
        IntentFilter filter1 = new IntentFilter("openVipOK");
        getActivity().registerReceiver(myReceiver, filter);
        getActivity().registerReceiver(myReceiver, filter1);
        //-------------------------
        mInit();
        return vv;
    }
    private void mInit() {
        //初始化开关
        switch_infopwd.closeSwitch();
        Glide.with(getActivity()).asGif().load(R.drawable.info_iv01).into(info_iv01);
        Glide.with(getActivity()).asGif().load(R.drawable.info_iv02).into(info_iv02);
        //判断是否为vip状态，如果不是，某些条目为灰色
        juageVip();

        dbManager = DemoDBManager.getInstance();
        LoginData.LoginInfo.LoginAccountInfo currentUser = myUtils.readUser(getActivity());
        if (currentUser != null) {
            if (!TextUtils.isEmpty(currentUser.getAvatar())) {
                //默认头像
                Picasso.with(getActivity()).load(myUtils.readUser(getActivity()).getAvatar())
                        .placeholder(R.drawable.fx_default_useravatar).into(iv_avatar);
            }
            vip = currentUser.getVip();
            //昵称
            tv_name.setText(myUtils.readUser(getActivity()).getNickname());
            //void
            tv_void.setText("vo号：" + myUtils.readUser(getActivity()).getAccount());
        }
    }

    /***
     * 初始化时判断是否为vip,如果不是vip,某些条目显示成灰色
     */
    private void juageVip() {
        LoginData.LoginInfo.LoginAccountInfo user = myUtils.readUser(getActivity());
        if (user != null) {
             vip = user.getVip();
            if ("0".equals(vip)) {
                //非vip

                flag01.setImageResource(R.mipmap.vo_lifelock_gray);
                flag02.setImageResource(R.mipmap.vo_pracy_gray);
                flag03.setImageResource(R.mipmap.vo_infopwd_gray);
                flag04.setImageResource(R.mipmap.vo_allclear_gray);

                textView14.setTextColor(getResources().getColor(R.color.gray));
                textView15.setTextColor(getResources().getColor(R.color.gray));
                textView16.setTextColor(getResources().getColor(R.color.gray));
                textView17.setTextColor(getResources().getColor(R.color.gray));

//                card_SetLifeNote.setEnabled(false);
//                card_SetPricyPwd.setEnabled(false);
//                card_InfoTransform.setEnabled(false);
//                card_ClearAllFriend.setEnabled(false);

            } else if ("1".equals(vip)) {
                //vip
                switch_infopwd.setEnabled(true);
                flag01.setImageResource(R.mipmap.vo_lifelock);
                flag02.setImageResource(R.mipmap.vo_pracy);
                flag03.setImageResource(R.mipmap.vo_infopwd);
                flag04.setImageResource(R.mipmap.vo_allclear);

                textView14.setTextColor(getResources().getColor(R.color.black));
                textView15.setTextColor(getResources().getColor(R.color.black));
                textView16.setTextColor(getResources().getColor(R.color.black));
                textView17.setTextColor(getResources().getColor(R.color.black));

                card_SetLifeNote.setEnabled(true);
                card_SetPricyPwd.setEnabled(true);
                card_InfoTransform.setEnabled(true);
                card_ClearAllFriend.setEnabled(true);

//                guide01.setVisibility(View.INVISIBLE);
//                guide02.setVisibility(View.INVISIBLE);
//                guide03.setVisibility(View.INVISIBLE);
//                guide04.setVisibility(View.INVISIBLE);

            }
        }
    }

    @OnClick({R.id.iv_search, R.id.iv_add, R.id.re_myinfo, R.id.re_settings,
            R.id.re_lifenote, R.id.re_createVOVIPAccount, R.id.switch_infopwd,
            R.id.card_SetLifeNote, R.id.card_SetPricyPwd, R.id.card_ClearAllFriend,
            R.id.guid01, R.id.guid02, R.id.guid03, R.id.guid04,R.id.card_InfoTransform,
    R.id.iv_lifeNoteKnow,R.id.iv_setFriend,R.id.iv_addAndReduceFriend})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //隐私好友一键清除
            case R.id.card_ClearAllFriend:
                //一键清除
                if ("0".equals(vip)) {
                    showVip();
                } else {
                    mineAllClear();
                }
                break;
            case R.id.card_SetPricyPwd:
                //隐私好友设置
                if ("0".equals(vip)) {
                    showVip();
                } else {
                    rl_setFriend.setVisibility(View.VISIBLE);

                   /* boolean setFriend = (boolean) SPUtils.get(getActivity(), "setFriend", true);
                    if (setFriend){
                        rl_setFriend.setVisibility(View.VISIBLE);
                    }else{
                        mineSetPricyPwd();
                    }*/
                    //mineSetPricyPwd();
                }
                break;

            case R.id.card_SetLifeNote:
                //人生笔记密码设置
                if ("0".equals(vip)) {
                    showVip();
                } else {
                    rl_lifeNote.setVisibility(View.VISIBLE);
                   /* boolean firstLifeNote = (boolean) SPUtils.get(getActivity(), "firstLifeNote", true);
                    if (firstLifeNote){
                        rl_lifeNote.setVisibility(View.VISIBLE);
                    }else{
                        mineHideLifePwd();
                    }*/
                }
                break;
            case R.id.card_InfoTransform:
            case R.id.switch_infopwd:
                //信息传输加密

                if ("0".equals(vip)) {
                    showVip();
                } else {
                    mineInfoPwd();
                }

                break;
            case R.id.iv_search:
                startActivity(new Intent(getActivity(), addFriendActivity_SearchVo.class));
                break;
            case R.id.iv_add:
                DetailsTypePopupWindow typePopupWindow = new DetailsTypePopupWindow(getActivity(), iv_add, "", "");
                typePopupWindow.setmItemsOnClick(new DetailsTypePopupWindow.ItemsOnClick() {
                    @Override
                    public void itemsOnClick(int position) {
                        switch (position) {
                            case 0:
                                startActivity(new Intent(getActivity(), GroupsActivity.class));
                                break;
                            //添加新的好友
                            case 1:
                                startActivity(new Intent(getActivity(), addFriendActivity_ContactsVo.class));
                                break;
                            //扫一扫
                            case 2:
                                startActivity(new Intent(getActivity(), ScanCaptureActivity.class));
                                break;
                            //帮助及反馈
                            case 3:
                                Intent intent=new Intent(getActivity(),Help_Feedback.class);
                                intent.putExtra("url", Url.FunctionIntroduceUrl);
                                intent.putExtra("param","15");
                                intent.putExtra("title","功能介绍");
                                startActivity(intent);

                                break;
                        }
                    }
                });
                break;
            case R.id.re_myinfo:
                startActivity(new Intent(getActivity(), CurrentUserInfoActivityVo.class));
                break;
            case R.id.re_settings:
                startActivity(new Intent(getActivity(), SettingsActivityVo.class));
                break;
            case R.id.re_lifenote:
                //进入人生笔记
                LoginData.LoginInfo.LoginAccountInfo user = myUtils.readUser(getActivity());
                if (!TextUtils.isEmpty(myUtils.readUser(getContext()).getPersonpass())) {
                    //弹出输入密码框
                    showSettingLifePwd();
                } else {
                    startActivity(new Intent(getActivity(), LifeNote.class));
                }
                break;
            case R.id.re_createVOVIPAccount:
                startActivity(new Intent(getActivity(), CreateVoVIPAccountActivityVo.class));
                break;
            case R.id.guid01:
                showPop("一键开启专属人生笔记，您的私密空间为您永久保存", guide01);
                break;
            case R.id.guid02:
                showPop("一键隐藏好友，一摇隐藏好友，一键显示隐私好友", guide02);
                break;
            case R.id.guid03:
                showPop("一键信息传输加密，2048位密码保护您的信息不被第三方窃取", guide03);
                break;
            case R.id.guid04:
                showPop(" 一键解除加密隐私好友，不留任何信息", guide04);
                break;
            case R.id.iv_lifeNoteKnow:
                rl_lifeNote.setVisibility(View.GONE);
                mineHideLifePwd();
                SPUtils.put(getActivity(),"firstLifeNote",false);
                break;
            case R.id.iv_setFriend:
                rl_setFriend.setVisibility(View.GONE);
                rl_addAndReduceFriend.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_addAndReduceFriend:
                rl_addAndReduceFriend.setVisibility(View.GONE);
                mineSetPricyPwd();
                SPUtils.put(getActivity(),"setFriend",false);
                break;
            default:
                break;
        }
    }

    /****
     * 显示次对话框，表示暂时没有开通vip
     */
    private void showVip() {
        dialog_vip = new vipDialog.Builder(getActivity())
                .setOpenVipListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), CreateVoVIPAccountActivityVo.class);
                        startActivity(intent);
                        dialog_vip.dismiss();
                    }
                }).setVipFunctionListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLifeNoteFunctionDialog();
                        dialog_vip.dismiss();
                    }
                }).create();
        dialog_vip.show();
    }

    /***
     * 如果没开通vip,弹出vip后，点击功能详情弹出此对话框
     */
    private void showLifeNoteFunctionDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.lay_vip_introduce);
        dialog.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.re_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void mineInfoPwd() {
        if (switch_infopwd.isSwitchOpen()) {
            switch_infopwd.closeSwitch();
        } else {
            showInfoRe(switch_infopwd, re_info);
        }
    }

    private void mineHideLifePwd() {
        lifeNoteSetDialog = new LifeNoteSetDialog.Builder(getActivity())
                .setCancelable(false)
                .setCancelListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lifeNoteSetDialog.dismiss();
                    }
                }).setPwdListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //设置密码
                        lifeNoteSetDialog.dismiss();
                        Intent intent = new Intent(getActivity(), SettingLifePwdActivity.class);
                        startActivity(intent);

                    }
                }).fixPwdListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lifeNoteSetDialog.dismiss();
                        //修改密码
                        Intent intent = new Intent(getActivity(), FixLifePwdActivity.class);
                        startActivity(intent);

                    }
                }).cancelPwdListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lifeNoteSetDialog.dismiss();
                        //取消密码
                        Intent intent = new Intent(getActivity(), CancelLifePwdActivity.class);
                        startActivity(intent);


                    }
                }).findPwdListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lifeNoteSetDialog.dismiss();
                        //找回密码
                        Intent intent = new Intent(getActivity(), FindLifePwdActivity.class);
                        startActivity(intent);
                    }
                })
                .create();
        lifeNoteSetDialog.show();

    }
    /***
     * 弹出是否设置人生笔记密码
     */
    private void showSettingLifePwd() {
        final Dialog dialog = new Dialog(getActivity());
        View vv = LayoutInflater.from(getActivity()).inflate(R.layout.lay_setlife_pwd2, null);
        dialog.setContentView(vv);
        final EditText ed_pwd = vv.findViewById(R.id.ed_pwd);
        TextView tv_confirm = vv.findViewById(R.id.tv_confirm);
        TextView tv_setpwd = vv.findViewById(R.id.tv_setpwd);
        ImageView cancel_iv = vv.findViewById(R.id.cancel_iv);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwdStr = ed_pwd.getText().toString().trim();
                if (TextUtils.isEmpty(pwdStr)) {
                    return;
                }
                String pp = org.xutils.common.util.MD5.md5(pwdStr);
                pp = pp.toUpperCase();
                Log.i("mmd5", "==:" + pp);
                Log.i("mmd5", "==:" + myUtils.readUser(getActivity()).getPersonpass());
                if (pp.equals(myUtils.readUser(getContext()).getPersonpass())) {
                    startActivity(new Intent(getActivity(), LifeNote.class));
                    dialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tv_setpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingLifePwdActivity.class));
            }
        });

        cancel_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    private void mineSetPricyPwd() {
        friendDialog = new PracyFriendDialog.Builder(getActivity())
                .setCancelable(false)
                .setCancelListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        friendDialog.dismiss();
                    }
                }).setAllClearListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        friendDialog.dismiss();
                    }
                }).setPwdFriendListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //设置加密好友
                        DemoApplication.whichActivity = 0;
                        startActivity(new Intent(getActivity(), SettingsPrivacyActivity3.class));
                        friendDialog.dismiss();

                    }
                }).setFixPwdFriendListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //修改加密好友
                        DemoApplication.whichActivity = 1;
                        startActivity(new Intent(getActivity(), SettingsPrivacyActivity4.class));
                        friendDialog.dismiss();

                    }
                })
                .create();
        friendDialog.show();
    }

    /**
     * 一键清除
     */
    private void mineAllClear() {
        if ("0".equals(vip)) {
            Toast.makeText(getActivity(), "此功能需要开通vip", Toast.LENGTH_SHORT).show();
            return;
        }
        final Dialog dialog = new Dialog(getActivity());
        View vv = LayoutInflater.from(getActivity()).inflate(R.layout.lay_allclear2, null);
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
                confirmAgain();
                dialog.dismiss();
            }
        });
        dialog.show();


    }

    private void confirmAgain() {


        final Dialog dialog = new Dialog(getActivity());
        View vv = LayoutInflater.from(getActivity()).inflate(R.layout.lay_allclear3, null);
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
              //  Toast.makeText(getActivity(), "一键清除", Toast.LENGTH_SHORT).show();
               OneKeyAllClear();
                dialog.dismiss();
            }
        });
        dialog.show();


    }

    private void showPop(String tvStr, ImageView iv) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.simple_text_bubble, null);
        BubbleTextView mBubbleTextView = rootView.findViewById(R.id.popup_bubble);
        BubblePopupWindow mBubblePopupWindow = new BubblePopupWindow(rootView, mBubbleTextView);
        mBubblePopupWindow.setPadding(dp2px(50));
        mBubbleTextView.setText(tvStr);
        RelativePos mRelativePos = new RelativePos(RelativePos.CENTER_HORIZONTAL, RelativePos.CENTER_VERTICAL);
        mRelativePos.setHorizontalRelate(RelativePos.TO_LEFT_OF);
        showPopupBubble(mBubblePopupWindow, mRelativePos, 0, 0, iv);
    }

    private void showPopupBubble(BubblePopupWindow mBubblePopupWindow, RelativePos mRelativePos, int mMarginH, int mMarginV, ImageView iv) {
        if (getActivity().hasWindowFocus()) {
            mBubblePopupWindow.showArrowTo(iv, mRelativePos, mMarginH, mMarginV);
        }
    }

    /*****
     * 广播接收者
     */
    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("FixPersonInfoOK".equals(action)) {
                LoginData.LoginInfo.LoginAccountInfo currentUser = myUtils.readUser(getActivity());
                if (currentUser != null) {
                    if (!TextUtils.isEmpty(currentUser.getAvatar())) {
                        //默认头像
                        Picasso.with(getActivity()).load(myUtils.readUser(getActivity()).getAvatar())
                                .placeholder(R.drawable.fx_default_useravatar).into(iv_avatar);
                    }
                    //昵称
                    tv_name.setText(myUtils.readUser(getActivity()).getNickname());
                    //void
                    tv_void.setText("vo号：" + myUtils.readUser(getActivity()).getAccount());
                }
            }
            if("openVipOK".equals(action)){
                //开通vip成功
                juageVip();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myReceiver != null) {
            getActivity().unregisterReceiver(myReceiver);
        }
    }

    /****
     * 一键清除所有加密好友
     */
    private void OneKeyAllClear() {
        loading_view.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams(Url.OneKeyAllClearUrl);
        params.addParameter("userid", myUtils.readUser(getActivity()).getUserid());
        x.http().post(params, new MyCommonCallback<Result>() {
            @Override
            public void success(Result data) {
                loading_view.setVisibility(View.GONE);
                if ("0".equals(data.code)) {
                    try {
                        //一键清空后，记录联系人是否隐藏状态的表应该清空
                        boolean clearOk = dbManager.clearAllEncryptionFriends();
                        Log.i("clearOk", clearOk + "");
                        Intent intent = new Intent("needRefresh");
                        getActivity().sendBroadcast(intent);
                    } catch (Exception e) {
                        Log.i("OneKeyAllClear", e.getMessage());
                    }
                } else {
                    Toast.makeText(getActivity(), data.info, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                loading_view.setVisibility(View.GONE);
            }
        });
    }
    //*****************************************************
    /***
     * 信息传输加密
     */
    public void showInfoRe(final EaseSwitchButton button, final RelativeLayout re_info) {
        re_info.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                button.openSwitch();
                re_info.setVisibility(View.INVISIBLE);
            }
        }, 7930);
    }

}
