package com.zl.vo_.httpUtils;

import android.content.Context;

import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/15.
 */

public class HttpsManager {

    private static final String TAG = "HttpsManager";


    private static final String CONFIG_TABLE_NAME = "hxuser";
    private static final String CONFIG_USERNAME = "username";
    private static final String CONFIG_NICK = "nickname";
    private static final String CONFIG_AVATAR = "avatar";
    private static final String parseServer = "http://parse.easemob.com/parse/";

    private static HttpsManager instance = new HttpsManager();
    private HttpsManager() {
    }
    public static HttpsManager getInstance() {
        return instance;
    }

    public void onInit(Context context) {
        Context appContext = context.getApplicationContext();

    }

    /***
     * 更新昵称
     * @param nickname
     * @return
     */
    public boolean updateParseNickName(final String nickname) {
        String username = EMClient.getInstance().getCurrentUser();

        return false;
    }

    /****
     * 获取联系人信息
     * @param usernames
     * @param callback
     */
    public void getContactInfos(List<String> usernames, final EMValueCallBack<List<EaseUser>> callback) {

    }

    /****
     * 异步获取当前用户的信息
     * @param callback
     */
    public void asyncGetCurrentUserInfo(final EMValueCallBack<EaseUser> callback){
        final String username = EMClient.getInstance().getCurrentUser();

    }

    /***
     * 异步获取用户的信息
     * @param username
     * @param callback
     */
    public void asyncGetUserInfo(final String username,final EMValueCallBack<EaseUser> callback){

    }

    /****
     * 上传头像
     * @param data
     * @return
     */
    public String uploadParseAvatar(byte[] data) {
        String username = EMClient.getInstance().getCurrentUser();

        return null;
    }




}
