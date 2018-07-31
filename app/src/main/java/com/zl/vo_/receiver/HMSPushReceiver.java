/*
 *  * EaseMob CONFIDENTIAL
 * __________________
 * Copyright (C) 2017 EaseMob Technologies. All rights reserved.
 *
 * NOTICE: All information contained herein is, and remains
 * the property of EaseMob Technologies.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from EaseMob Technologies.
 */
package com.zl.vo_.receiver;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.huawei.hms.support.api.push.PushReceiver;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.EMLog;

public class HMSPushReceiver extends PushReceiver{
    private static final String TAG = HMSPushReceiver.class.getSimpleName();

    @Override
    public void onToken(Context context, String token, Bundle extras){
        //没有失败回调，假定token失败时token为null
        if(token != null && !token.equals("")){
            EMLog.d("HWHMSPush()()(", "register huawei hms push token success token:" + token);
            EMClient.getInstance().sendHMSPushTokenToServer("100321893", token);
            Log.i("tag",token+"=======token=======");
            Toast.makeText(context, token+"=======token====", Toast.LENGTH_SHORT).show();

        }else{
            EMLog.e("HWHMSPush", "register huawei hms push token fail!");
            Log.i("tag","=======2222222222222222222=======");
            Toast.makeText(context, "2222222222222222222",Toast.LENGTH_SHORT).show();
        }
    }

}
