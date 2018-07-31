package com.zl.vo_.main.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zl.vo_.R;

/**
 * Created by Administrator on 2018/1/24.
 */

public class Hyp_dialog extends Dialog {

    private TextView tv_copy;
    private TextView tv_delete;
    private TextView tv_forword;

    private View.OnClickListener defaultOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cancel();
        }
    };

    private View.OnClickListener copyClickListener=defaultOnClickListener;
    private View.OnClickListener deleteClickListener=defaultOnClickListener;
    private View.OnClickListener forwordClicListener=deleteClickListener;



    public Hyp_dialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_hyp_dialog);
        tv_copy=findViewById(R.id.msg_copy);
        tv_delete=findViewById(R.id.msg_delete);
        tv_forword=findViewById(R.id.msg_forword);

    }

    @Override
    public void show() {
        super.show();
        show(this);
    }

    private void show(Hyp_dialog hyp_dialog) {

        hyp_dialog.tv_copy.setOnClickListener(copyClickListener);
        hyp_dialog.tv_delete.setOnClickListener(deleteClickListener);
        hyp_dialog.tv_forword.setOnClickListener(forwordClicListener);

    }

    public static class Builder{
       private Hyp_dialog dialog;

        public Builder(Context context) {
            dialog=new Hyp_dialog(context);
        }



        /***
         * 复制点击事件
         * @param l
         * @return
         */
        public Builder setCopyClickListener(View.OnClickListener l){
            dialog.copyClickListener=l;
            return this;
        }

        /***
         * 删除点击事件
         * @param l
         * @return
         */
        public Builder setDeleteClickListener(View.OnClickListener l){
            dialog.deleteClickListener=l;
            return this;
        }

        /***
         * 复制点击事件
         * @param l
         * @return
         */
        public Builder setForwordClickListener(View.OnClickListener l){
            dialog.forwordClicListener=l;
            return this;
        }
        public Hyp_dialog create(){
            return dialog;
        }




    }
}
