package com.zl.vo_.utils;

import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.zl.vo_.main.Entity.LoginEntity;

/**
 * Created by Administrator on 2017/11/17.
 */

public class myEaseUserUtls {
    public static EaseUser Json2User(LoginEntity.MyFriends userJson) {
        EaseUser easeUser = new EaseUser(userJson.getUsername());
        easeUser.setNick(userJson.getNike());
        easeUser.setAvatar(userJson.getAvatar());
      // easeUser.setUserInfo(userJson.toJSONString());
        EaseCommonUtils.setUserInitialLetter(easeUser);
        return easeUser;
    }
}
