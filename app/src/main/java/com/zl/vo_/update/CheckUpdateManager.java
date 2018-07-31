package com.zl.vo_.update;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.zl.vo_.update.bean.Version;

/**
 * Created by haibin
 * on 2016/10/19.
 */

public class CheckUpdateManager {
    private ProgressDialog mWaitDialog;
    private Context mContext;
    private boolean mIsShowDialog;
    private RequestPermissions mCaller;

    public CheckUpdateManager(Context context, boolean showWaitingDialog) {
        this.mContext = context;
        mIsShowDialog = showWaitingDialog;
        if (mIsShowDialog) {
            mWaitDialog = new ProgressDialog(context);
            mWaitDialog.setMessage("正在检查中...");
            mWaitDialog.setCancelable(false);
            mWaitDialog.setCanceledOnTouchOutside(false);
        }
    }


    public void checkUpdate() {
        if (mIsShowDialog) {
            mWaitDialog.show();
        }
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
        builder.setMessage("下载安装V娱乐" )
                .setCancelable(false)
                .setPositiveButton("下载",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 转到手机设置界面，用户设置GPS
                                Version version=new Version();
                                version.setVersion("2");
                                version.setUrl("http://47.95.115.55:8080/voadmin/test.apk");
                                dialog.dismiss();
                                // 你的dialog点击了确定调用：
                                mCaller.call(version);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 你的dialog点击了取消调用：


            }
        }).show();
    }

    public void setCaller(RequestPermissions caller) {
        this.mCaller = caller;
    }

    public interface RequestPermissions {
        void call(Version version);
    }
}
