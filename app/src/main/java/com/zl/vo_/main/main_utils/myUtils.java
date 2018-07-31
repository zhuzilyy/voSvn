package com.zl.vo_.main.main_utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.hyphenate.easeui.domain.EaseUser;
import com.zl.vo_.DemoModel;
import com.zl.vo_.main.Entity.FriendDataFromMine;
import com.zl.vo_.main.Entity.LoginData;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.Entity.WXAccessTokenInfo;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.utils.Url;
import com.zl.vo_.widget.RoundImageView;

import org.apache.commons.codec.binary.Base64;
import org.xutils.http.RequestParams;
import org.xutils.http.app.ParamsBuilder;
import org.xutils.x;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2017/8/14.
 */

public class myUtils {
    public static String PHONE_REG="^1[34578]\\d{9}$";

    /***
     * 登录成功后，将user对象存储在share中
     * @param user
     * @param context
     */
    public static void saveUser(LoginData.LoginInfo.LoginAccountInfo user, Context context) {
        SharedPreferences preferences = context.getSharedPreferences("currentUser",MODE_PRIVATE);

        // 创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(user);
            // 将字节流编码成base64的字符窜
            String oAuth_Base64 = new String(Base64.encodeBase64(baos
                    .toByteArray()));
            Log.i("xxx","save的字符串=="+oAuth_Base64);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("uu", oAuth_Base64);


            editor.commit();

            Log.i("xxx","user存储成功");
        } catch (IOException e) {

            Log.i("xxx","user存储失败"+e);
        }

    }

    /****
     * 从share中读取User
     * @return
     */

    public static LoginData.LoginInfo.LoginAccountInfo readUser(Context context) {
        LoginData.LoginInfo.LoginAccountInfo user = null;
        Log.i("xxx","context@=="+context);
        SharedPreferences preferences =context.getSharedPreferences("currentUser",MODE_PRIVATE);

        String productBase64 = preferences.getString("uu", "");
        Log.i("xxx","productBase64=="+productBase64);

        //读取字节
        byte[] base64 = org.apache.commons.codec.binary.Base64.decodeBase64(productBase64.getBytes());

        //封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            //再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);
            try {
                //读取对象
                user = (LoginData.LoginInfo.LoginAccountInfo) bis.readObject();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (StreamCorruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i("xxx","有读取势必=="+e);
        }
        return user;
    }

    /****
     * 清空sharedPreference中保存的用户信息
     * @param context
     * @return
     */
    public static void  clearSharedUser(Context context){
        SharedPreferences preferences =context.getSharedPreferences("currentUser",MODE_PRIVATE);
        preferences.edit().clear().commit();
        Log.i("clear","789");



    }
//********************************************************************











//********************************************************************


    /**
     * 将图片裁剪到指定大小
     *
     * @param uri
     */
    public static  void CutPic(Uri uri, Activity activity) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);// 设置Intent中的view是可以裁剪的
        // 设置宽高比
       /* intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);*/
        // 设置裁剪图片的宽高
        intent.putExtra("outputX", 360);
        intent.putExtra("outputY", 360);
        intent.putExtra("outputFormat", "JPEG");// 图片格式
        // 设置是否返回数据
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为3
        activity.startActivityForResult(intent, 3);

    }

    /***
     * 图片的缩放方法
     *
     * @param bgimage   ：源图片资源
     * @param newWidth  ：缩放后宽度
     * @param newHeight ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    public static Bitmap getBitmap(RoundImageView touxiang){

        Drawable drawable=touxiang.getDrawable();
        if(drawable!=null){
            Bitmap bitmap=drawableToBitmap(drawable);
            return bitmap;
        }else {

            return  null;
        }


    }


    /***
     * drawable 转换为bitmap
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {



        Bitmap bitmap = Bitmap.createBitmap(

                drawable.getIntrinsicWidth(),

                drawable.getIntrinsicHeight(),

                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_4444

                        : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        //canvas.setBitmap(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        drawable.draw(canvas);

        return bitmap;

    }



    /**
     * 通过Base32将Bitmap转换成Base64字符串
     * @param bit
     * @return
     */
    public static String Bitmap2StrByBase64(Bitmap bit){
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 40, bos);//参数100表示不压缩
        byte[] bytes=bos.toByteArray();
        return android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT);
    }




    /**
     * 折叠通知栏
     *
     * @param context
     */
    public static void expandNotification(Context context) {
        Object service = context.getSystemService("statusbar");
        if (null == service)
            return;
        try {
            Class<?> clazz = Class.forName("android.app.StatusBarManager");
            int sdkVersion = android.os.Build.VERSION.SDK_INT;
            Method collapse = null;
            if (sdkVersion <= 16) {
                collapse = clazz.getMethod("expand");
            } else {
                collapse = clazz.getMethod("expandNotificationsPanel");
            }
            collapse.setAccessible(true);
            collapse.invoke(service);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //保存微信的accessToken
    public static boolean saveAccessInfotoLocation(WXAccessTokenInfo tokenInfo, Context context){
        SharedPreferences preferences = context.getSharedPreferences("tokenInfo",MODE_PRIVATE);
        // 创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(tokenInfo);
            // 将字节流编码成base64的字符窜
            String oAuth_Base64 = new String(Base64.encodeBase64(baos
                    .toByteArray()));
            Log.i("xxx","save的字符串=="+oAuth_Base64);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Info", oAuth_Base64);

            editor.commit();
            Log.i("xxx","tokenInfo存储成功");
            return true;
        } catch (IOException e) {

            Log.i("xxx","tokenInfo存储失败"+e);
            return false;
        }

    }

    /****
     * 从share中读取User
     * @return
     */

    public static WXAccessTokenInfo readAccessInfotoLocation(Context context) {
        WXAccessTokenInfo tokenInfo = null;
        Log.i("xxx","context@=="+context);
        SharedPreferences preferences =context.getSharedPreferences("tokenInfo",MODE_PRIVATE);

        String productBase64 = preferences.getString("Info", "");
        Log.i("xxx","productBase64=="+productBase64);

        //读取字节
        byte[] base64 = org.apache.commons.codec.binary.Base64.decodeBase64(productBase64.getBytes());

        //封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            //再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);
            try {
                //读取对象
                tokenInfo = (WXAccessTokenInfo) bis.readObject();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (StreamCorruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i("xxx","有读取势必=="+e);
        }
        return tokenInfo;
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
        移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
        联通：130、131、132、152、155、156、185、186
        电信：133、153、180、189、（1349卫通）
        总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
        */
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)){

            return false;
        }
        else {
            return mobiles.matches(telRegex);
        }
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    ///*******优化==开始********************************************************

    /***
     * 检测当前时间是否在设置的消息免打扰之间
     * @param demoModel
     * @return
     */
    public static boolean WithInDisturbTime(DemoModel demoModel ){
        String start_time_str=demoModel.getSettingDistrubStartTime();
        Log.i("miandarao","设定的开始时间：="+start_time_str);
        String end_time_str=demoModel.getSettingDistrubEndTime();
        Log.i("miandarao","设定的结束时间：="+end_time_str);
        Calendar cal = Calendar.getInstance();// 当前日期
        int hour = cal.get(Calendar.HOUR_OF_DAY);// 获取小时
        int minute = cal.get(Calendar.MINUTE);// 获取分钟
        String current_time_str="";
        if(hour<10&&minute<10){
            current_time_str="0"+hour+":"+"0"+minute;
        }else if(minute<10&&hour>10){
            current_time_str=hour+":"+"0"+minute;
        }else if(minute>10&&hour<10){
            current_time_str="0"+hour+":"+minute;
        }else {
            current_time_str=hour+":"+minute;
        }
        Log.i("miandarao","当前时间：="+current_time_str);
        return demoModel.adjustTime(start_time_str,end_time_str,current_time_str);
    }


}
