package com.zl.vo_.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.zl.vo_.db.DemoDBManager;
import com.zl.vo_.main.activities.VoBaseActivity;
import com.zl.vo_.main.main_fragments.MainActivity;
import com.zl.vo_.main.services.ShakeService;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/11/9.
 */

public class BaseActivity extends EaseBaseActivity {
    private DemoDBManager dbManager=DemoDBManager.getInstance();
    private  Timer timer;
    private boolean isExit=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //开启摇一摇服务
        Intent intent = new Intent(BaseActivity.this,ShakeService.class);
        intent.putExtra("context", VoBaseActivity.class);
        this.startService(intent);
        mInit();

    }

    private void mInit() {

    }

   /* *//****
     * 更新密码表
     * @param
     *//*
    private boolean updateMyPwdTable(List<MyPwdList.MyPwdListInfo.pwdList> pwdLists) {
      return   dbManager.updateOrInsertPwdTable(pwdLists);


    }*/


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
           // moveTaskToBack(false);
            exitBy2Click();
            return true;
        }
        return true;
    }
    /**
     * 双击退出函数
     */
    private void exitBy2Click() {
        Timer tExit = null;
        if (getClass().getName().equals(MainActivity.class.getName())){
            if (isExit == false) {
                isExit = true; // 准备退出
                // Toast.makeText(this, "再按一次，退到后台", Toast.LENGTH_SHORT).show();
                tExit = new Timer();
                tExit.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        isExit = false; // 取消退出
                    }
                },2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
            } else {
                moveTaskToBack(false);
            }
        }else {
            finish();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
