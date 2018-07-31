package com.zl.vo_.main.main_utils;

import android.content.Context;

/**
 * ============================================================
 * <p>
 * 版 权 ： 沈阳夜鱼科技有限公司
 * <p>
 * 作 者 ： Ywp
 * <p>
 * 版 本 ： 2.0
 * <p>
 * 创建日期 ：2017/7/3
 * <p>
 * 描 述 ：
 *      常用单位转换的辅助类
 *
 * 修订历史 ：
 * <p>
 * ============================================================
 **/
public class DensityUtils {

    private DensityUtils() {
		/* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        // return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
        // dpVal, context.getResources().getDisplayMetrics());
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpVal * scale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param context
     * @param spVal
     * @return
     */
    public static int sp2px(Context context, float spVal) {
        // return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
        // spVal, context.getResources().getDisplayMetrics());
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spVal * fontScale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static int px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxVal / scale + 0.5f);
    }


    /**
     * px转sp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static int px2sp(Context context, float pxVal) {
        // return (pxVal /
        // context.getResources().getDisplayMetrics().scaledDensity);
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxVal / fontScale + 0.5f);
    }
}
