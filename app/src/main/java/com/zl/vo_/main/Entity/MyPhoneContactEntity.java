package com.zl.vo_.main.Entity;

/**
 * Created by Administrator on 2017/11/10.
 */

public class MyPhoneContactEntity {
    private String name;
    private String phone;
    private boolean isMyFriend;

    public MyPhoneContactEntity() {
    }

    public MyPhoneContactEntity(String name, String phone) {

        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isMyFriend() {
        return isMyFriend;
    }

    public void setMyFriend(boolean myFriend) {
        isMyFriend = myFriend;
    }
}
