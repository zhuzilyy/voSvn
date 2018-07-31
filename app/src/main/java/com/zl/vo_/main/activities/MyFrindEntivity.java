package com.zl.vo_.main.activities;

import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/8.
 */

public class MyFrindEntivity extends BaseIndexPinyinBean implements Serializable{
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNike() {
        return nike;
    }

    public void setNike(String nike) {
        this.nike = nike;
    }

    public String getHuanxinID() {
        return huanxinID;
    }

    public void setHuanxinID(String huanxinID) {
        this.huanxinID = huanxinID;
    }

    public String getGrooupID() {
        return grooupID;
    }

    public void setGrooupID(String grooupID) {
        this.grooupID = grooupID;
    }

    private String account;
    private String avatar;
    private String nike;
    private String huanxinID;
    private String grooupID;
    private String userID;
    private String phone;
    private String friend_check;
    private String passid;
    private String groupname;
    private String remark;
    private String address;
    private String sex;
    private String vip;

    public boolean isTop() {
        return isTop;
    }

    public MyFrindEntivity setTop(boolean top) {
        isTop = top;
        return this;
    }

    private boolean isTop;//是否是最上面的 不需要被转化成拼音的
    //空构造
    public MyFrindEntivity() {
    }
    //带有备注的构造
    public MyFrindEntivity(String remark) {
        this.remark = remark;
    }

    public boolean getMychecked() {
        return mychecked;
    }

    public void setMychecked(boolean mychecked) {
        this.mychecked = mychecked;
    }

    private boolean mychecked;



    public String getFriend_check() {
        return friend_check;
    }

    public void setFriend_check(String friend_check) {
        this.friend_check = friend_check;
    }

    public String getPassid() {
        return passid;
    }

    public void setPassid(String passid) {
        this.passid = passid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String getTarget() {
        return remark;
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
