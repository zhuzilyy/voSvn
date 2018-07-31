package com.zl.vo_.main.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zl.vo_.R;

/**
 * Created by Administrator on 2018/1/26.
 */

public class OpenVipDialog extends Dialog {
    private RelativeLayout btn_cancel;
    private Button btn_allClear;
    private LinearLayout btn_setPwdFriend;
    private LinearLayout btn_fixPwdFriend;

    private View.OnClickListener defaultClickLister=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cancel();
        }
    };

    private View.OnClickListener cancelListener = defaultClickLister;
    private View.OnClickListener allclearListener = defaultClickLister;
    private View.OnClickListener setpwdfriendListener=defaultClickLister;
    private View.OnClickListener fixpwdfriendListener=defaultClickLister;
    public OpenVipDialog(Context context) {
        super(context,R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_dia_pracy_);
        btn_cancel= findViewById(R.id.cancelBtn);
        btn_allClear= findViewById(R.id.allClearPwdFriendBtn);
        btn_setPwdFriend= findViewById(R.id.setPwdFriendBtn);
        btn_fixPwdFriend= findViewById(R.id.fixPwdFriendBtn);
    }

    @Override
    public void show() {
        super.show();
        show(this);
    }

    private void show(OpenVipDialog hypCommDialog) {
        if(btn_cancel!=null){
            hypCommDialog.btn_cancel.setOnClickListener(cancelListener);
        }
        if(btn_allClear!=null){
            hypCommDialog.btn_allClear.setOnClickListener(allclearListener);
        }
        if(btn_setPwdFriend!=null){
            hypCommDialog.btn_setPwdFriend.setOnClickListener(setpwdfriendListener);
        }
        if(btn_fixPwdFriend != null){
            hypCommDialog.btn_fixPwdFriend.setOnClickListener(fixpwdfriendListener);
        }
    }
    public static class Builder{
        OpenVipDialog dialog;
        public Builder(Context context) {
            dialog=new OpenVipDialog(context);
        }
        public Builder setCancelListener(View.OnClickListener l){
            dialog.cancelListener=l;
            return this;
        }
        public Builder setAllClearListener(View.OnClickListener l){
            dialog.allclearListener=l;
            return this;
        }
        public Builder setPwdFriendListener(View.OnClickListener l){
            dialog.setpwdfriendListener=l;
            return this;
        }
        public Builder setFixPwdFriendListener(View.OnClickListener l){
            dialog.fixpwdfriendListener=l;
            return this;
        }
        public Builder setCancelable( boolean isCancelable){
            dialog.setCancelable(isCancelable);
            return this;
        }
        public OpenVipDialog create(){
            return  dialog;
        }

    }





}
