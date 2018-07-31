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

public class HypCommDialog extends Dialog {
    private TextView tv_title;
    private TextView tv_content;
    private Button btn_cancel;
    private Button btn_confirm;

    private String titleStr;
    private String contentStr;
    private String cancelStr;
    private String confirmStr;


    private View.OnClickListener defaultClickLister=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cancel();
        }
    };

    private View.OnClickListener cancelListener=defaultClickLister;
    private View.OnClickListener confirmListener=defaultClickLister;



    public HypCommDialog(Context context) {
        super(context,R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_commdialog);
        tv_title= findViewById(R.id.commDialog_title);
        tv_content= findViewById(R.id.commDialog_content);
        btn_cancel= findViewById(R.id.commDialog_cancel);
        btn_confirm= findViewById(R.id.commDialog_confirm);
    }

    @Override
    public void show() {
        super.show();
        show(this);
    }

    private void show(HypCommDialog hypCommDialog) {
        if(!TextUtils.isEmpty(titleStr)){
            hypCommDialog.tv_title.setText(titleStr);
        }
        if(!TextUtils.isEmpty(contentStr)){
            hypCommDialog.tv_content.setText(contentStr);
        }
        if(!TextUtils.isEmpty(cancelStr)){
            hypCommDialog.btn_cancel.setText(cancelStr);
            hypCommDialog.btn_cancel.setOnClickListener(cancelListener);
        }
        if(!TextUtils.isEmpty(confirmStr)){
            hypCommDialog.btn_confirm.setText(confirmStr);
            hypCommDialog.btn_confirm.setOnClickListener(confirmListener);
        }
    }

    public static class Builder{
        HypCommDialog dialog;
        public Builder(Context context) {
            dialog=new HypCommDialog(context);
        }

        public Builder setTitle(String titleStr){
            dialog.titleStr=titleStr;
            return this;
        }
        public Builder setContent(String contentStr){
            dialog.contentStr=contentStr;
            return this;
        }

        public Builder setCancelListener(String cancenStr,View.OnClickListener l){
            dialog.cancelStr=cancenStr;
            dialog.cancelListener=l;
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

        public HypCommDialog create(){
            return  dialog;
        }




    }



}
