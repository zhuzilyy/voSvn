package com.zl.vo_.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.zl.vo_.DemoApplication;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.Entity.adEntivity;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Administrator on 2018/1/18.
 */

public class SplashFatherActivityVo extends VoBaseActivity {
    protected String picUrl;
    protected String postTime;
    protected String linkUrl;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getGameParams();
      //  getSplashData();
    }

    /**
     * 获取游戏参数
     */
    private void getGameParams() {

    }

    private void getSplashData() {
        RequestParams params=new RequestParams(Url.AdURL);
        x.http().post(params, new MyCommonCallback<Result<adEntivity>>() {
            @Override
            public void success(Result<adEntivity> data) {
                if("0".equals(data.code)){
                    adEntivity dataAd= data.data;
                    adEntivity.adInfo adInfo=dataAd.getInfo();
                    if(adInfo!=null){
                        adEntivity.adInfo.adImage adImage= adInfo.getIndex_image();
                        linkUrl=adImage.getLink();
                        picUrl=adImage.getPicurl();
                        postTime=adImage.getTime();

                    }
                }
            }
            @Override
            public void error(Throwable ex, boolean isOnCallback) {
            }
        });
    }
}
