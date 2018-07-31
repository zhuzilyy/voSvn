package com.ybao.pullrefreshview.support.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;

/**
 * Created by Ybao on 16/7/24.
 */
public class Utils {
    public static int getScreenWidth(Context context) {
        Display mDisplay = ((Activity) context).getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        mDisplay.getMetrics(metrics);
        return metrics.widthPixels;
    }

    /**
     * 屏幕分辨率高
     **/
    public static int getScreenHeight(Context context) {
        Display mDisplay = ((Activity) context).getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        mDisplay.getMetrics(metrics);
        return metrics.heightPixels;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
