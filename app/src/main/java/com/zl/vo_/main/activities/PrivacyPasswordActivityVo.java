package com.zl.vo_.main.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.easeui.widget.EaseSwitchButton;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.zl.vo_.DemoApplication;
import com.zl.vo_.R;
import com.zl.vo_.adapter.PrivacyPwd_ContactAdapter;
import com.zl.vo_.db.DemoDBManager;
import com.zl.vo_.main.Entity.CheckPwdEntivity;
import com.zl.vo_.main.Entity.MyPwdEntity;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/25.
 */

public class PrivacyPasswordActivityVo extends VoBaseActivity implements View.OnClickListener,OnDateSetListener{
    @BindView(R.id.lv)
    public ListView lv;
    public PrivacyPwd_ContactAdapter adapter;
    public TextView hideStratTime_tv;

    public boolean isStart=false;
    @BindView(R.id.iv_back)
    public ImageView ivback;
    public RelativeLayout privacyhide_re_starttime;
    //----------------- ----------

    public EaseSwitchButton switch_shake;
    public EaseSwitchButton switch_time;
    public List<MyFrindEntivity> pwdEntivityList=new ArrayList<>();
    DemoDBManager dbManager=DemoDBManager.getInstance();
    @BindView(R.id.submit)
    public TextView submit;

    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;
    //****************************************
    private String currentPassId;
    private String currentIsSetTime;
    private String currentIsSetShake;
    private String currentStartTime;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_privacy_pwd);
        //pwdInfo=DemoApplication.pwdInfo;
        //获取加密密码
        currentPassId=getIntent().getStringExtra("passId");
        //获取是否设置了隐藏时间
        currentIsSetTime=getIntent().getStringExtra("isSetTime");
        //获取是否设置了摇一摇
        currentIsSetShake=getIntent().getStringExtra("isSetShake");
        //获取是否设置了开始时间
        currentStartTime=getIntent().getStringExtra("startTime");
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        mIint();
    }

    private void mIint() {
        //---------------------------------------
        View headview= LayoutInflater.from(PrivacyPasswordActivityVo.this).inflate(R.layout.lay_privacypwd_head,null);
        RelativeLayout hide_re_start=headview.findViewById(R.id.privacyhide_re_starttime);
        hideStratTime_tv= headview.findViewById(R.id.privacyhide_startTime_tv);
        switch_shake=headview.findViewById(R.id.switch_shake);
        switch_time=headview.findViewById(R.id.switch_time);
        privacyhide_re_starttime=headview.findViewById(R.id.privacyhide_re_starttime);
        switch_time.setOnClickListener(this);
        switch_shake.setOnClickListener(this);
        hide_re_start.setOnClickListener(this);
        adapter=new PrivacyPwd_ContactAdapter(PrivacyPasswordActivityVo.this,pwdEntivityList,currentPassId);
        lv.setAdapter(adapter);
        lv.addHeaderView(headview);
        ////----------------------------------------------------
        //是否设置了时间
        if("0".equals(currentIsSetTime)){
            switch_time.closeSwitch();
            privacyhide_re_starttime.setVisibility(View.GONE);
            currentIsSetTime="0";
        }else {
            switch_time.openSwitch();
            privacyhide_re_starttime.setVisibility(View.VISIBLE);
            currentIsSetTime="1";
        }
        //是否开启了摇一摇
        if("0".equals(currentIsSetShake)){
            switch_shake.closeSwitch();
            currentIsSetShake="0";
        }else {
            switch_shake.openSwitch();
            currentIsSetShake="1";
        }
        //查询数据库 ，是本passid 和没有passid的

        List<MyFrindEntivity> allFriends=dbManager.getAllFriends();
        if(allFriends.size()>0){

            for (int i = 0; i <allFriends.size() ; i++) {
                if(allFriends.get(i).getPassid().equals(currentPassId)|| TextUtils.isEmpty(allFriends.get(i).getPassid())){
                    pwdEntivityList.add(allFriends.get(i));
                }
            }
            for (int i = 0; i <pwdEntivityList.size() ; i++) {
                if(pwdEntivityList.get(i).getPassid().equals(currentPassId)){
                    pwdEntivityList.get(i).setMychecked(true);
                }else {
                    pwdEntivityList.get(i).setMychecked(false);
                }
            }
            int checkedNum=getCheckNum(pwdEntivityList);
            if(checkedNum>0){
                submit.setText("确定("+checkedNum+")");
            }else {
                submit.setText("确定");
            }
        }
        adapter.notifyDataSetChanged();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(pwdEntivityList.get(i-1).getMychecked()){
                    pwdEntivityList.get(i-1).setMychecked(false);
                }else {
                    pwdEntivityList.get(i-1).setMychecked(true);
                }
                int CheckedNumber=getCheckNum(pwdEntivityList);
                if(CheckedNumber>0){
                    submit.setText("确定("+CheckedNumber+")");
                }else {
                    submit.setText("确定");
                }
                adapter.notifyDataSetChanged();
            }
        });


    }

    /***
     * 获取有多少人被选中
     * @param pwdEntivityList
     * @return
     */
    private int getCheckNum(List<MyFrindEntivity> pwdEntivityList) {
        List<MyFrindEntivity> entivities=new ArrayList<>();
        for (int i = 0; i <pwdEntivityList.size() ; i++) {
            if( pwdEntivityList.get(i).getMychecked()){
                entivities.add(pwdEntivityList.get(i));
            }
        }
        return entivities.size();
    }
    @OnClick({R.id.iv_back,R.id.submit})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.privacyhide_re_starttime:
                isStart=true;
                TimePickerDialog dialogHourMins1 = new TimePickerDialog.Builder()
                        .setType(Type.HOURS_MINS)
                        .setCallBack(this)
                        .setTitleStringId("隐藏时间设置")
                        .setThemeColor(getResources().getColor(R.color.common_top_bar_blue))
                        .build();
                dialogHourMins1.show(getSupportFragmentManager(), "HOURS_MINS");
                break;
            case R.id.switch_shake:
                //隐藏密码
                if(switch_shake.isSwitchOpen()){
                    switch_shake.closeSwitch();
                    currentIsSetShake="0";
                }else {
                    switch_shake.openSwitch();
                    currentIsSetShake="1";
                }
                break;
            case R.id.switch_time:
                //隐藏时间
                if(switch_time.isSwitchOpen()){
                    //关闭
                    switch_time.closeSwitch();
                    currentIsSetTime="0";
                    privacyhide_re_starttime.setVisibility(View.GONE);
                }else {
                    //开启
                    switch_time.openSwitch();
                    currentIsSetTime="1";
                    privacyhide_re_starttime.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.submit:
                ///提交保存
                String userID= getCheckedFriend();
                String time_=hideStratTime_tv.getText().toString().trim();
                submitPrivacyMembers(currentPassId,userID,currentIsSetTime,currentIsSetShake,time_);
                break;

            default:
                break;
        }
    }
    /*****
     * 获取加密的好友的userid
     * @return
     */
    private String getCheckedFriend() {
        String friendUserId="";
        for (int i = 0; i <pwdEntivityList.size() ; i++) {
           if(pwdEntivityList.get(i).getMychecked()){
               friendUserId+=pwdEntivityList.get(i).getUserID()+",";
           }
        }
        return friendUserId;
    }
    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        if(isStart){
            isStart=false;
            Date date = new Date(millseconds);
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");//精确到分钟
            String time = format.format(date);
            hideStratTime_tv.setText(time);
            Log.i("time",time+"=kaishi");
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    public void submitPrivacyMembers(String passid, final String frienduserid, String isSet, String isMove, String time){
        loading_view.setVisibility(View.VISIBLE);
        RequestParams params=new RequestParams(Url.AppendToPrivacy_More_Url);
        params.addParameter("passid",passid);
        params.addParameter("userid", myUtils.readUser(PrivacyPasswordActivityVo.this).getUserid());
        params.addParameter("friend_userid_str",frienduserid);
        params.addParameter("isset",isSet);
        params.addParameter("ismove",isMove);
        params.addParameter("starttime",time);
        x.http().post(params, new MyCommonCallback<Result<MyPwdEntity>>() {
            @Override
            public void success(Result<MyPwdEntity> data) {
                loading_view.setVisibility(View.GONE);
                if("0".equals(data.code)){
                    MyPwdEntity entity= data.data;
                    MyPwdEntity.MyPwdInfo pwdInfo=entity.getInfo();
                    MyPwdEntity.MyPwdInfo.MyPwdCell cell=pwdInfo.getPrivacypassinfo();
                    if(cell!=null){
                        boolean b= UpdateMyPrivacyPwd(cell);
                        if(b){
                            sendBroadcast(new Intent("needRefresh"));
                            Intent intent=new Intent(PrivacyPasswordActivityVo.this,SettingsPrivacyActivity5_over.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(PrivacyPasswordActivityVo.this, "隐藏失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                loading_view.setVisibility(View.GONE);
            }
        });
    }
    /****
     * 将设置的隐私密码保存在数据库中
     * @param cell
     * @return
     */
    private boolean UpdateMyPrivacyPwd(MyPwdEntity.MyPwdInfo.MyPwdCell cell) {
        return dbManager.UpdateMyPwd(cell,"0");
    }
}
