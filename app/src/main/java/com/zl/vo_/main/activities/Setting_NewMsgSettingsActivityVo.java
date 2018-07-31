package com.zl.vo_.main.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hyphenate.easeui.widget.EaseSwitchButton;
import com.zl.vo_.DemoHelper;
import com.zl.vo_.DemoModel;
import com.zl.vo_.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/22.
 */

public class Setting_NewMsgSettingsActivityVo extends VoBaseActivity implements View.OnClickListener {
    //是否接收新消息
    @BindView(R.id.switch_notification)
    public EaseSwitchButton notifySwitch;
    //是否显示消息详情
    @BindView(R.id.notifySwitch_details)
    public EaseSwitchButton notifySwitch_details;
    //声音
    @BindView(R.id.notifySwitch_voice)
    public EaseSwitchButton soundSwitch;
    //震动
    @BindView(R.id.notifySwitch_vibrate)
    public EaseSwitchButton vibrateSwitch;




    //--------------------------
    @BindView(R.id.re_voice)
    public RelativeLayout re_voice;
    @BindView(R.id.re_Vibration)
    public RelativeLayout re_Vibration;
    @BindView(R.id.re_newmsg_voice)
    public RelativeLayout re_newmsg_voice;
    @BindView(R.id.re_msgdetails)
    public LinearLayout re_msgdetails;
    //------------------
    private DemoModel settingsModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_new_msgsettings);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        mInit();
    }

    private void mInit() {
        settingsModel = DemoHelper.getInstance().getModel();
        //新消息提醒
        if (settingsModel.getSettingMsgNotification()) {
            notifySwitch.openSwitch();

        } else {
            notifySwitch.closeSwitch();
            notifySwitch_details.closeSwitch();
            soundSwitch.closeSwitch();
            vibrateSwitch.openSwitch();

            re_newmsg_voice.setVisibility(View.GONE);
            re_voice.setVisibility(View.GONE);
            re_Vibration.setVisibility(View.GONE);
            re_msgdetails.setVisibility(View.GONE);




        }
        //声音
        if (settingsModel.getSettingMsgSound()) {
            soundSwitch.openSwitch();
        } else {
            soundSwitch.closeSwitch();
        }
        // 震动
        if (settingsModel.getSettingMsgVibrate()) {
            vibrateSwitch.openSwitch();
        } else {
            vibrateSwitch.closeSwitch();
        }
        //是否显示消息详情
        if (settingsModel.getSettingMsgDetails()) {
            notifySwitch_details.openSwitch();
        } else {
            notifySwitch_details.closeSwitch();
        }


    }
    @OnClick({R.id.iv_back,R.id.switch_notification,R.id.notifySwitch_details,R.id.notifySwitch_voice,
            R.id.notifySwitch_vibrate})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.switch_notification:
                //是否接收新消息
                if (notifySwitch.isSwitchOpen()) {
                    notifySwitch.closeSwitch();
                    settingsModel.setSettingMsgNotification(false);
                    re_newmsg_voice.setVisibility(View.GONE);
                    re_voice.setVisibility(View.GONE);
                    re_Vibration.setVisibility(View.GONE);
                    re_msgdetails.setVisibility(View.GONE);
                    //设置声音关闭
                    settingsModel.setSettingMsgSound(false);
                    soundSwitch.closeSwitch();
                    //设置震动关闭
                    settingsModel.setSettingMsgVibrate(false);
                    vibrateSwitch.closeSwitch();
                } else {
                    notifySwitch.openSwitch();
                    settingsModel.setSettingMsgNotification(true);
                    re_newmsg_voice.setVisibility(View.VISIBLE);
                    re_voice.setVisibility(View.VISIBLE);
                    re_Vibration.setVisibility(View.VISIBLE);
                    re_msgdetails.setVisibility(View.VISIBLE);
                    //设置声音关闭
                    settingsModel.setSettingMsgSound(true);
                    soundSwitch.openSwitch();
                    //设置震动关闭
                    settingsModel.setSettingMsgVibrate(true);
                    vibrateSwitch.openSwitch();
                }
                break;
            case R.id.notifySwitch_details:
                //是否显示消息详情
                if (notifySwitch_details.isSwitchOpen()) {
                    notifySwitch_details.closeSwitch();
                    settingsModel.setSettingMsgDetails(false);
                } else {
                    notifySwitch_details.openSwitch();
                    settingsModel.setSettingMsgDetails(true);
                }
                break;
            case R.id.notifySwitch_voice:
                //声音
                if (soundSwitch.isSwitchOpen()) {
                    soundSwitch.closeSwitch();
                    settingsModel.setSettingMsgSound(false);
                } else {
                    soundSwitch.openSwitch();
                    settingsModel.setSettingMsgSound(true);
                }
                break;
            case R.id.notifySwitch_vibrate:
                //震动
                if (vibrateSwitch.isSwitchOpen()) {
                    vibrateSwitch.closeSwitch();
                    settingsModel.setSettingMsgVibrate(false);
                   boolean b= settingsModel.getSettingMsgVibrate();
                    Log.i("ii",b+"");
                } else {
                    vibrateSwitch.openSwitch();
                    settingsModel.setSettingMsgVibrate(true);
                }
                break;
            default:
                break;
        }

    }
}
