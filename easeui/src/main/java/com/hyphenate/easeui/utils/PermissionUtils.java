package com.hyphenate.easeui.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Administrator on 2017/12/25.
 */

public class PermissionUtils {
    public static boolean checkOrRequestPermissions(Context mContext, String[]permissions, int requestCode,
                                                    android.app.Activity aaa ){
        boolean isGranted= checkAllPermissions(mContext,permissions);
        if(isGranted){
            //所有权限已经被赋予
            return true;
        }else {
            //请求权限
            requestPermissions(aaa,permissions,requestCode);
            return false;
        }
    }
    /***
     * 请求权限
     */
    private static void requestPermissions(android.app.Activity aaa ,String[]permissions,int requestCode) {
        ActivityCompat.requestPermissions(aaa,permissions,requestCode);
    }
    /***
     * 检查是否所有的权限已经被赋予
     * @param mContext
     * @param permissions
     * @return
     */
    private static boolean checkAllPermissions(Context mContext,String[] permissions){
        for (String permission:permissions) {
            if(ContextCompat.checkSelfPermission(mContext,permission)!= PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return  true;
    }
}
