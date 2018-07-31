package com.zl.vo_.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.widget.EaseSwitchButton;
import com.zl.vo_.DemoApplication;
import com.zl.vo_.DemoModel;
import com.zl.vo_.R;
import com.zl.vo_.db.DemoDBManager;
import com.zl.vo_.main.Entity.CheckPwdEntivity;
import com.zl.vo_.main.Entity.PrivacyInfoData;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.main.views.HypCommDialog;
import com.zl.vo_.main.views.HypDialog01;
import com.zl.vo_.main.views.XTitleView;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/22.
 */

public class Setting_PrivacyActivityVo extends VoBaseActivity implements View.OnClickListener {
    @BindView(R.id.setting_life_note_pwd)
    public RelativeLayout setting_life_note_pwd;
    @BindView(R.id.privacy_re)
    public RelativeLayout privacy_re;
    @BindView(R.id.setting_pwd)
    public RelativeLayout setting_pwd;
    @BindView(R.id.switch_pwd_class)
    public EaseSwitchButton switch_pwd_class;
    public DemoModel demoModel;
    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;
    @BindView(R.id.allclear_re)
    public RelativeLayout allclear_re;
    //******************************
    @BindView(R.id.lifenote_more_iv)
    public ImageView lifenote_more_iv;
    @BindView(R.id.goodFriendPWD_more_iv)
    public ImageView goodFriendPWD_more_iv;
    @BindView(R.id.privacy_more_iv)
    public ImageView privacy_more_iv;
    @BindView(R.id.OneKeyAllClearPWD_more_iv)
    public ImageView OneKeyAllClearPWD_more_iv;
    @BindView(R.id.lifenote_more_infotrans_iv)
    public ImageView lifenote_more_infotrans_iv;
    //*************
    //通用的dialog
    private HypCommDialog dialog;
    //显示提示的对话框
    private HypDialog01 dialog01;
    public PrivacyInfoData.PrivacyInfo.PrivacyCell mCell;
    private String vip;
    @BindView(R.id.title)
    public RelativeLayout title;
    public DemoDBManager dbManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_set_privacy);
        dbManager=DemoDBManager.getInstance();
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        vip = myUtils.readUser(Setting_PrivacyActivityVo.this).getVip();
        //获取弹框提示内容
        getData();
        mInit();
    }

    /***
     * 获取解释说明
     */
    private void getData() {
        RequestParams paams = new RequestParams(Url.PrivacyInfosUrl);
        x.http().post(paams, new MyCommonCallback<Result<PrivacyInfoData>>() {
            @Override
            public void success(Result<PrivacyInfoData> data) {
                if ("0".endsWith(data.code)) {
                    PrivacyInfoData infoData = data.data;
                    PrivacyInfoData.PrivacyInfo privacyInfo = infoData.getInfo();
                    PrivacyInfoData.PrivacyInfo.PrivacyCell cell = privacyInfo.getPrivate_info();
                    mCell = cell;
                }
            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {
            }
        });
    }

    private void mInit() {
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        demoModel = new DemoModel(Setting_PrivacyActivityVo.this);
    }

    @OnClick({R.id.setting_life_note_pwd,
            R.id.privacy_re, R.id.setting_pwd, R.id.allclear_re, R.id.switch_pwd_class,
            R.id.lifenote_more_iv, R.id.goodFriendPWD_more_iv, R.id.privacy_more_iv,
            R.id.OneKeyAllClearPWD_more_iv, R.id.lifenote_more_infotrans_iv
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lifenote_more_infotrans_iv:
                //信息传输加密
                if (mCell != null) {
                    showHyp01Dialog("设置信息传输加密", mCell.getPrivate_5());
                }
                break;
            case R.id.lifenote_more_iv:
                //人生笔记密码更多
                if (mCell != null) {
                    showHyp01Dialog("设置人生笔记密码", mCell.getPrivate_6());
                }
                break;
            case R.id.goodFriendPWD_more_iv:
                //好友加密密码更多
                if (mCell != null) {
                    showHyp01Dialog("设置好友加密", mCell.getPrivate_7());
                }
                break;
            case R.id.privacy_more_iv:
                //隐私功能更多
                if (mCell != null) {
                    showHyp01Dialog("隐私功能设置", mCell.getPrivate_8());
                }
                break;
            case R.id.OneKeyAllClearPWD_more_iv:
                //一键请求加密密码更多
                if (mCell != null) {
                    showHyp01Dialog("一键清除设置", mCell.getPrivate_9());
                }
                break;
            case R.id.setting_life_note_pwd:
                //设置人生笔记密码
                if ("0".equals(vip)) {
                    Toast.makeText(this, "此功能需要开通vip", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(Setting_PrivacyActivityVo.this, LifeNotePasswordActivityVo.class));
                break;
            case R.id.privacy_re:
                //进入设置隐私功能设置
                if ("0".equals(vip)) {
                    Toast.makeText(this, "此功能需要开通vip", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(Setting_PrivacyActivityVo.this, SettingsPrivacyActivity2.class));
                break;
            case R.id.setting_pwd:
                //进入添加隐私密码的页面
                if ("0".equals(vip)) {
                    Toast.makeText(this, "此功能需要开通vip", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(Setting_PrivacyActivityVo.this, PrivacyPwdSettingActivityVo.class));
                break;
            case R.id.allclear_re:
                //一键清除
                if ("0".equals(vip)) {
                    Toast.makeText(this, "此功能需要开通vip", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog = new HypCommDialog.Builder(Setting_PrivacyActivityVo.this)
                        .setTitle("一键清除")
                        .setContent("确认清除加密好友，清除后将解除好友关系！")
                        .setCancelable(false)
                        .setCancelListener("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        })
                        .setConfirmListener("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                OneKeyAllClear();
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
                break;
            case R.id.switch_pwd_class:
                //加密等级
                if ("0".equals(vip)) {
                    Toast.makeText(this, "此功能需要开通vip", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (switch_pwd_class.isSwitchOpen()) {
                    switch_pwd_class.closeSwitch();
                } else {
                    switch_pwd_class.openSwitch();
                }
                break;
            default:
                break;
        }
    }

    /***
     * 点击三个点，显示提示说明
     *
     * @param dialogTitle
     * @param content
     */
    private void showHyp01Dialog(String dialogTitle, String content) {
        dialog01 = new HypDialog01.Builder(Setting_PrivacyActivityVo.this)
                .setTitle(dialogTitle)
                .setContent(content)
                .setCancelable(false)
                .setConfirmListener("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog01.dismiss();
                    }
                }).create();
        dialog01.show();
    }

    /****
     * 一键清除所有加密好友
     */
    private void OneKeyAllClear() {
        loading_view.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams(Url.OneKeyAllClearUrl);
        params.addParameter("userid", myUtils.readUser(Setting_PrivacyActivityVo.this).getUserid());
        x.http().post(params, new MyCommonCallback<Result>() {
            @Override
            public void success(Result data) {
                loading_view.setVisibility(View.GONE);
                if ("0".equals(data.code)) {
                    try {
                        //一键清空后，记录联系人是否隐藏状态的表应该清空
                        boolean clearOk= dbManager.clearAllEncryptionFriends();
                        Log.i("clearOk",clearOk+"");
                        Intent intent = new Intent("needRefresh");
                        sendBroadcast(intent);
                        finish();

                    } catch (Exception e) {
                        Log.i("OneKeyAllClear",e.getMessage());
                    }
                }else {
                    Toast.makeText(Setting_PrivacyActivityVo.this, data.info, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                loading_view.setVisibility(View.GONE);
            }
        });
    }
}
