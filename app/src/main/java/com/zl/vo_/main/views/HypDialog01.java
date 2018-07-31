package com.zl.vo_.main.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zl.vo_.R;

/**
 * Created by Administrator on 2018/1/26.
 */

public class HypDialog01 extends Dialog {
    private TextView tv_title;
    private TextView tv_content;
    private Button btn_confirm;

    private String titleStr;
    private String contentStr;
    private String confirmStr;


    private View.OnClickListener defaultClickLister=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cancel();
        }
    };

    private View.OnClickListener confirmListener=defaultClickLister;



    public HypDialog01(Context context) {
        super(context,R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_hypdialog01);
        tv_title= findViewById(R.id.more_title);
        tv_content= findViewById(R.id.more_content);
        btn_confirm= findViewById(R.id.more_confirm);
    }

    @Override
    public void show() {
        super.show();
        show(this);
    }

    private void show(HypDialog01 hypdialog01) {
        if(!TextUtils.isEmpty(titleStr)){
            hypdialog01.tv_title.setText(titleStr);
        }
        if(!TextUtils.isEmpty(contentStr)){
            hypdialog01.tv_content.setText(contentStr);
        }
        if(!TextUtils.isEmpty(confirmStr)){
            hypdialog01.btn_confirm.setText(confirmStr);
            hypdialog01.btn_confirm.setOnClickListener(confirmListener);
        }
    }

    public static class Builder{
        HypDialog01 dialog;
        public Builder(Context context) {
            dialog=new HypDialog01(context);
        }

        public Builder setTitle(String titleStr){
            dialog.titleStr=titleStr;
            return this;
        }
        public Builder setContent(String contentStr){
            dialog.contentStr=contentStr;
            return this;
        }

        public Builder setConfirmListener(String confirmStr,View.OnClickListener l){
            dialog.confirmStr=confirmStr;
            dialog.confirmListener=l;
            return this;
        }
        public Builder setCancelable(boolean isCancelable){
            dialog.setCancelable(isCancelable);
            return this;
        }
        public HypDialog01 create(){
            return  dialog;
        }




    }



}
