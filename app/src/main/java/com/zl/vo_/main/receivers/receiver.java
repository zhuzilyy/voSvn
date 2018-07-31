package com.zl.vo_.main.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zl.vo_.db.DemoDBManager;
import com.zl.vo_.main.main_fragments.MyContactsListFragment;

/**
 * Created by Administrator on 2017/12/19.
 */

public class receiver extends BroadcastReceiver {
    private DemoDBManager dbManager=DemoDBManager.getInstance();
    @Override
    public void onReceive(Context context, Intent intent) {

        String action=intent.getAction();
        if("hide".equals(action)){



        }
    }
}
