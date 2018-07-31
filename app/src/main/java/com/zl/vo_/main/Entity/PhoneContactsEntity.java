package com.zl.vo_.main.Entity;

import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/16.
 */

public class PhoneContactsEntity extends BaseIndexPinyinBean implements Serializable {
    public PhoneContactsEntity() {
    }

    public PhoneContactsEntity(String nickname) {
        this.nickname = nickname;
    }

    public String getHuanxin_account() {
        return huanxin_account;
    }

    public void setHuanxin_account(String huanxin_account) {
        this.huanxin_account = huanxin_account;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    private String userid;
    private String nickname;
    private String account;
    private String mobile;
    private String tag;
    private String avatar;
    private String huanxin_account;

    public boolean isTop() {
        return isTop;
    }
    public void setTop(boolean top) {
        isTop = top;
    }
    private boolean isTop;//是否是最上面的 不需要被转化成拼音的


    @Override
    public String getTarget() {
        return nickname;
    }
    @Override
    public boolean isNeedToPinyin() {
        return !isTop;
    }


    @Override
    public boolean isShowSuspension() {
        return !isTop;
    }



 }


