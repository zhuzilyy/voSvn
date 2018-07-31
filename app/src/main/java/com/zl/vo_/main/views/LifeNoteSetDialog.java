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

public class LifeNoteSetDialog extends Dialog {
    private RelativeLayout re_cancel;
    private LinearLayout ll_setPwd;
    private LinearLayout ll_fixPwd;
    private LinearLayout ll_cancelPwd;
    private LinearLayout ll_findPwd;

    private View.OnClickListener defaultClickLister=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cancel();
        }
    };

    private View.OnClickListener cancelListener=defaultClickLister;
    private View.OnClickListener setPwdListener=defaultClickLister;
    private View.OnClickListener fixPwdListener=defaultClickLister;
    private View.OnClickListener cancelPwdListener=defaultClickLister;
    private View.OnClickListener findPwdListener=defaultClickLister;

    public LifeNoteSetDialog(Context context) {
        super(context,R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_dia_lifenote);
        re_cancel= findViewById(R.id.re_cancel);
        ll_setPwd= findViewById(R.id.lifenote_setPWD);
        ll_fixPwd= findViewById(R.id.lifenote_fixPWD);
        ll_cancelPwd= findViewById(R.id.lifenote_cancelPWD);
        ll_findPwd= findViewById(R.id.lifenote_findPWD);

    }

    @Override
    public void show() {
        super.show();
        show(this);
    }

    private void show(LifeNoteSetDialog hypCommDialog) {

        if(re_cancel!=null){

            hypCommDialog.re_cancel.setOnClickListener(cancelListener);
        }
        if(ll_setPwd!=null){

            hypCommDialog.ll_setPwd.setOnClickListener(setPwdListener);
        }
        if(ll_fixPwd!=null){

            hypCommDialog.ll_fixPwd.setOnClickListener(fixPwdListener);
        }
        if(ll_cancelPwd!=null){

            hypCommDialog.ll_cancelPwd.setOnClickListener(cancelPwdListener);
        }
        if(ll_findPwd!=null){

            hypCommDialog.ll_findPwd.setOnClickListener(findPwdListener);
        }
    }

    public static class Builder{
        LifeNoteSetDialog dialog;
        public Builder(Context context) {
            dialog=new LifeNoteSetDialog(context);
        }



        public Builder setCancelListener(View.OnClickListener l){
            dialog.cancelListener=l;
            return this;
        }
        public Builder setPwdListener(View.OnClickListener l){

            dialog.setPwdListener=l;
            return this;
        }
        public Builder fixPwdListener(View.OnClickListener l){

            dialog.fixPwdListener=l;
            return this;
        }
        public Builder cancelPwdListener(View.OnClickListener l){

            dialog.cancelPwdListener=l;
            return this;
        }
        public Builder findPwdListener(View.OnClickListener l){

            dialog.findPwdListener=l;
            return this;
        }
        public Builder setCancelable(boolean isCancelable){
            dialog.setCancelable(isCancelable);
            return this;
        }
        public LifeNoteSetDialog create(){
            return  dialog;
        }




    }



}
