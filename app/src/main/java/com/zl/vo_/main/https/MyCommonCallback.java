package com.zl.vo_.main.https;


import android.util.Log;

import com.google.gson.Gson;

import org.xutils.common.Callback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Administrator on 2017/3/20.
 */

public abstract class MyCommonCallback<T> extends Object implements Callback.CommonCallback<String> {
    private Type type;

    public MyCommonCallback() {
        Type superClass=this.getClass().getGenericSuperclass();
        this.type=((ParameterizedType)superClass).getActualTypeArguments()[0];

    }

    @Override
    public void onSuccess(String result) {
        //进度条消失

        Log.i("hyp",result);



       Gson gson=new Gson();
        T data=gson.fromJson(result,type);
        if(data!=null){
            success(data);


        }


    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        //进度条消失

        error(ex,isOnCallback);
    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }

    public abstract void success(T data);

    public abstract  void error(Throwable ex, boolean isOnCallback);
}
