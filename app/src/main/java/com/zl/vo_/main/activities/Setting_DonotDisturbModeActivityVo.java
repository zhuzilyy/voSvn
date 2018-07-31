package com.zl.vo_.main.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.easeui.widget.EaseSwitchButton;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.zl.vo_.DemoModel;
import com.zl.vo_.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/22.
 */

public class Setting_DonotDisturbModeActivityVo extends VoBaseActivity implements View.OnClickListener,
        OnDateSetListener {
    @BindView(R.id.iv_back)
    public ImageView iv_back;
    @BindView(R.id.switch_disrupt)
    public EaseSwitchButton switch_disrupt;
    public DemoModel settingsModel;
    @BindView(R.id.re_disturb_starttime)
    public RelativeLayout starttime;
    @BindView(R.id.re_disturb_endtime)
    public RelativeLayout endtiem;

    @BindView(R.id.disturb_startTime_tv)
    public TextView disturb_startTime;
    @BindView(R.id.disturb_endTime_tv)
    public TextView disturb_endTime;
    private boolean isStart=false;
    private boolean isEnd=false;
    //======================
    private String disturb_startTime_str;
    private String disturb_edgTime_str;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_donotdisturb);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        mInit();
    }

    private void mInit() {
        settingsModel=new DemoModel(Setting_DonotDisturbModeActivityVo.this);
        disturb_startTime_str=settingsModel.getSettingDistrubStartTime();
        disturb_edgTime_str=settingsModel.getSettingDistrubEndTime();
        //新消息提醒
        if (settingsModel.getIsDisturb()) {
            switch_disrupt.openSwitch();
            disturb_startTime.setText(disturb_startTime_str);
            starttime.setVisibility(View.VISIBLE);
            endtiem.setVisibility(View.VISIBLE);
            disturb_startTime.setText(settingsModel.getSettingDistrubStartTime());
            disturb_endTime.setText(settingsModel.getSettingDistrubEndTime());
        } else {
            switch_disrupt.closeSwitch();
            disturb_endTime.setText(disturb_edgTime_str);
            starttime.setVisibility(View.GONE);
            endtiem.setVisibility(View.GONE);
            disturb_startTime.setText(settingsModel.getSettingDistrubStartTime());
            disturb_endTime.setText(settingsModel.getSettingDistrubEndTime());
        }



    }
    @OnClick({R.id.iv_back,R.id.switch_disrupt,
            R.id.re_disturb_starttime,R.id.re_disturb_endtime})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.switch_disrupt:
               //开启免打扰
                if (switch_disrupt.isSwitchOpen()) {
                    switch_disrupt.closeSwitch();
                    starttime.setVisibility(View.GONE);
                    endtiem.setVisibility(View.GONE);
                    settingsModel.setIsDisturb(false);
                    Toast.makeText(this, "关", Toast.LENGTH_SHORT).show();
                } else {
                    switch_disrupt.openSwitch();
                    starttime.setVisibility(View.VISIBLE);
                    endtiem.setVisibility(View.VISIBLE);
                    settingsModel.setIsDisturb(true);
                    Toast.makeText(this, "开", Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.re_disturb_starttime:
                isStart=true;
                TimePickerDialog dialogHourMins1 = new TimePickerDialog.Builder()
                        .setType(Type.HOURS_MINS)
                        .setCallBack(this)
                        .setTitleStringId("vo免打扰")
                        .setThemeColor(getResources().getColor(R.color.common_top_bar_blue))
                        .build();
                dialogHourMins1.show(getSupportFragmentManager(), "HOURS_MINS");
                break;
            case R.id.re_disturb_endtime:
                isEnd=true;
                TimePickerDialog dialogHourMins2 = new TimePickerDialog.Builder()
                        .setType(Type.HOURS_MINS)
                        .setCallBack(this)
                        .setTitleStringId("vo免打扰")
                        .setThemeColor(getResources().getColor(R.color.common_top_bar_blue))
                        .build();
                dialogHourMins2.show(getSupportFragmentManager(), "HOURS_MINS");
                break;
            default:
                break;
        }
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        if(isStart){
            isStart=false;
            Date date = new Date(millseconds);
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");//精确到分钟
            String time = format.format(date);
            disturb_startTime.setText(time);
            Log.i("time",time+"=kaishi");
            settingsModel.setSettingDistrubStartTime(time);

            Log.i("7890","保存的开始时间=="+settingsModel.getSettingDistrubStartTime());
        }else if(isEnd){
            isEnd=false;
            Date date = new Date(millseconds);
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");//精确到分钟
            String time = format.format(date);
            disturb_endTime.setText(time);
            Log.i("time",time+"=jieshu");
            settingsModel.setSettingDistrubEndTime(time);
            Log.i("7890","保存的结束时间=="+settingsModel.getSettingDistrubEndTime());
        }


    }
}
