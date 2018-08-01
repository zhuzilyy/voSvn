package com.zl.vo_.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;

/**
 * Created by Administrator on 2018/6/6.
 */

public class WhiteBgBitmapUtil {
    public static Bitmap drawableBitmapOnWhiteBg(Context context, Bitmap bitmap){
        Bitmap newBitmap =null;
        //Bitmap newBitmap = Bitmap.createBitmap(120, 120, Bitmap.Config.ARGB_8888);
        String manufacturer = Build.MANUFACTURER;
        if (manufacturer.equalsIgnoreCase("meizu")){
            newBitmap=Bitmap.createBitmap(900, 900, Bitmap.Config.ARGB_8888);
        }else{
            newBitmap=Bitmap.createBitmap(1260, 1260, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(context.getResources().getColor(android.R.color.white));
        Paint paint=new Paint();
        if (manufacturer.equalsIgnoreCase("meizu")){
            canvas.drawBitmap(bitmap, 0, 0, paint); //将原图使用给定的画笔画到画布上
        }else{
            canvas.drawBitmap(bitmap, 10, 10, paint); //将原图使用给定的画笔画到画布上
        }
        //canvas.drawBitmap(bitmap, 1, 1, paint); //将原图使用给定的画笔画到画布上
        return newBitmap;
    }
}
