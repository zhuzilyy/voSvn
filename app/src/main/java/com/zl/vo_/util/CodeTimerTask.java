package com.zl.vo_.util;

import android.os.AsyncTask;
import android.widget.TextView;


/**
 * Created by asus on 2017/2/11.
 */
public class CodeTimerTask extends AsyncTask<Void, Void, Void> {
    private int time = 60;
    private TextView textView;
    private static  boolean isNew;
    private boolean isRun;


    //单例模式
    private static CodeTimerTask task;
    //私有化构造
    private CodeTimerTask(){}
    //对外提供一个可以获取该类实例的方法
    public static CodeTimerTask getInstence(){
        if(task==null){
            task=new CodeTimerTask();
            isNew=true;
        }
        return  task;
    }

    public  void  starrTimer(TextView textView){
        this.textView=textView;
        if(isNew){
            execute();
        }
    }

    @Override
    protected void onPreExecute() {
        time = 60;
        isNew=false;
        isRun=true;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            for (; time >= 0; time--) {
                publishProgress();
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        if (textView != null) {
            textView.setText(String.format("%ds", time));
            if (textView.isEnabled()) {
                textView.setEnabled(false);
            }
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        end();

    }

    private void end(){
        if(textView!=null){
            textView.setEnabled(true);
            textView.setText("获取验证码");
        }

        cancle();
    }




    /**
     * 完成一次倒计时
     */
    public void cancle(){
        if(task!=null){
            isRun=false;
            super.cancel(true);
            task=null;
            isNew=true;
        }
    }

    public boolean isRun() {
        return isRun;
    }
}
