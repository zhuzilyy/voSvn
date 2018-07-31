package com.zl.vo_.main.Entity;

/**
 * Created by Administrator on 2017/12/6.
 */

public class FriendDataFromMine  {
    public FriendDataInfoFromMine getInfo() {
        return Info;
    }

    public void setInfo(FriendDataInfoFromMine info) {
        Info = info;
    }

    public FriendDataInfoFromMine Info;
    public class FriendDataInfoFromMine{
        public FriendDataInfoDetailsFromMine getFriend_info() {
            return friend_info;
        }

        public void setFriend_info(FriendDataInfoDetailsFromMine friend_info) {
            this.friend_info = friend_info;
        }

        public FriendDataInfoDetailsFromMine friend_info;
        public class FriendDataInfoDetailsFromMine{
            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }

            public String getHuanxin_account() {
                return huanxin_account;
            }

            public void setHuanxin_account(String huanxin_account) {
                this.huanxin_account = huanxin_account;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
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

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getFriend_check() {
                return friend_check;
            }

            public void setFriend_check(String friend_check) {
                this.friend_check = friend_check;
            }

            private String id;
            private String userid;
            private String huanxin_account;
            private String avatar;
            private String sex;
            private String address;
            private String nickname;
            private String account;
            private String mobile;
            private String remark;
            private String friend_check;


        }
    }
}
